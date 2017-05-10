package com.efrobot.library;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import com.efrobot.library.client.StatusClientFactory;
import com.efrobot.library.conn.IRobotConnect;
import com.efrobot.library.conn.IRobotMessageListener;
import com.efrobot.library.conn.RobotMessage;
import com.efrobot.library.net.UrlConstants;
import com.efrobot.library.task.ControlManager;
import com.efrobot.library.task.GroupManager;
import com.efrobot.library.task.HeadManager;
import com.efrobot.library.task.WheelManager;
import com.efrobot.library.task.WingManager;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鑫宇 on 2016/9/2.
 */
public class RobotManager {
    /* 电池状态事件*/
    private RobotStatus.BatteryStatus mBatteryStatus = RobotStatus.BatteryStatus.UNKNOWN;
    /* 头部事件*/
    private RobotStatus.HeadTouchStatus mHeadTouchStatus = RobotStatus.HeadTouchStatus.UNKNOWN;
    /* 投影仪事件*/
    private RobotStatus.ProjectStatus mProjectStatus = RobotStatus.ProjectStatus.UNKNOWN;
    /*托盘事件  */
    public RobotStatus.SalverStatus mSalverStatus = RobotStatus.SalverStatus.UNKNOWN;
    /* 净化器事件*/
    private RobotStatus.PurifierStatus mPurifierStatus = RobotStatus.PurifierStatus.UNKNOWN;
    /* 面罩事件*/
    private RobotStatus.MaskStatus mMaskStatus = RobotStatus.MaskStatus.UNKNOWN;

    public final static String TAG = "RobotManager";
    private final static String ACTION_NAME_ROS_MASTER_START = "com.efrobot.service.START_ROS_MASTER";
    private Context mContext;
    /* 获取机器人管理实例 单例对象 */
    private volatile static RobotManager mInstance;
    private String packageName;
    /* 机器人状态对象 */
    private IStatus mRobotStatus;
    private RosMasterBroadcast mBroadcastReceiver;


    /* TTS语音广播 */
    private static final String ACTION_VOICE = "com.efrobot.speech.voice.ACTION_TTS";
    /* 语音识别广播 */
    public static final String ACTION_SPEECH_START = "com.efrobot.speech.action.SPEECH_START";

    public static final String ACTION_FACE = "com.efrobot.robot.action.SDK_PLAY_FACE";

    private List<String> mPathList = new ArrayList<String>();
    private String prefixPath;
    public static VideoActivity mVideoActivity;

    //双翅膀运动
    public final static int MOVE_WING = 0;
    //左翅膀运动
    public final static int MOVE_LEFT_WING = 1;
    //右翅膀运动
    public final static int MOVE_RIGHT_WING = 2;

    /* 脚本任务管理 */
    private GroupManager groupManager;
    /* 头部任务管理 */
    private HeadManager headManager;
    /* 双翅任务管理 */
    private WingManager wingManager;
    /* 双轮任务管理 */
    private WheelManager wheelManager;
    /* 行为任务管理 */
    private ControlManager controlManager;

    //-----------------------------------新增-------------------------------------
    private StatusClientFactory factory;
    private BaseHandler mHandler;
    private RobotMessageListenerImp robotMessageListenerImp;
    /**
     * 存储所有所有OnRobotStateChange的回调
     */
    private SparseArray<List<OnRobotStateChangeListener>> mRobotStateChangeListenerGroup = new SparseArray<List<OnRobotStateChangeListener>>();

    //------------------------------------------------------------------------

    private RobotManager(Context context) {
        this.mContext = context.getApplicationContext();
        prefixPath = "android.resource://" + mContext.getPackageName() + "/";
        mVideoActivity = new VideoActivity();
        packageName = context.getPackageName();
        String processName = getProcessName(mContext, Process.myPid());
        if (packageName != null && processName != null && !packageName.equals(processName)) {
            Log.w(TAG, "Child Process: " + processName);
            return;
        }
        //创建代理工厂
        factory = new StatusClientFactory();
        //设置来源报名
        factory.setFrom(packageName);
        //设置代理可用
        mRobotStatus = RobotStatus.getProxyInstance(getContext(), factory);
        mHandler = new BaseHandler(this);
        robotMessageListenerImp = new RobotMessageListenerImp();
        //注册Master开启广播
        mBroadcastReceiver = new RosMasterBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_NAME_ROS_MASTER_START);
         mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        connect();

    }
    public Context getContext() {
        return mContext;
    }
    /**
     * @return null may be returned if the specified process not found
     */
    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    private static class BaseHandler extends Handler {
        private final WeakReference<RobotManager> mObjects;

        public BaseHandler(RobotManager mObject) {
            mObjects = new WeakReference<RobotManager>(mObject);
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                RobotManager mObject = mObjects.get();
                if (mObject != null)
                    mObject.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public class RobotMessageListenerImp extends IRobotMessageListener.Stub {

        @Override
        public void onReceiveRobotMessage(RobotMessage robotMessage) throws RemoteException {
            parseReceiveRobotMessage(robotMessage);
        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    }

    private void parseReceiveRobotMessage(RobotMessage robotMessage) {
        notifyReceiveRobotMessageListener(robotMessage.getIndex(), robotMessage.getData());
    }
    /**
     * 发布消息
     */
    private void notifyReceiveRobotMessageListener(int robotStateIndex, Object data) {
        Message message = Message.obtain();
        message.what = robotStateIndex;
        message.obj = data;
        mHandler.sendMessage(message);
    }
    /**
     * 监听事件
     *
     * @param msg 处理消息
     */
    private void handleMessage(Message msg) {
        switch (msg.what) {
            default:
                List<OnRobotStateChangeListener> onRobotStateChangeListeners = mRobotStateChangeListenerGroup.get(msg.what);
                if (onRobotStateChangeListeners != null && onRobotStateChangeListeners.size() != 0) {
                    for (int i = 0; i < onRobotStateChangeListeners.size(); i++) {
                        OnRobotStateChangeListener mOnRobotStateChangeListener = onRobotStateChangeListeners.get(i);
                       //强转不要骂我，原来的框架都是这样的，为了不改文档所以只能这样强转
                        switch (msg.what) {
                            //电池状态事件的监听
                            case RobotStatus.ROBOT_STATE_INDEX_BATTERY_STATUS_CHARGING:
                                (( OnBatteryStatusChangedListener) mOnRobotStateChangeListener).onBatteryStatusChanged(getBatteryStatus());
                                break;
                            //电池电量事件的监听
                            case RobotStatus.ROBOT_STATE_INDEX_BATTERY_STATUS_LEVEL:
                                (( OnBatteryLevelChangedListener) mOnRobotStateChangeListener).onBatteryLevelChanged(getBatteryLevel());
                                break;
                            //头部事件的监听
                            case RobotStatus.ROBOT_STATE_INDEX_HEAD_KEY:
                                ((OnHeadTouchStatusChangedListener) mOnRobotStateChangeListener).onHeadTouchStatusChanged(getHeadTouchStatus());
                                break;
                            //投影仪事件的监听
                            case RobotStatus.ROBOT_STATE_INDEX_PROJECTOR_STATE:
                                ((OnProjectStatusChangedListener)mOnRobotStateChangeListener).onProjectStatusChanged(getProjectorStatus());
                                break;
                            //净化器的监听
                            case RobotStatus.ROBOT_STATE_INDEX_PURIFIER_STATE:
                                ((OnPurifierStatusChangedListener)mOnRobotStateChangeListener).onPurifierStatusChanged(getPurifierStatus());
                                break;
                            //面罩的监听
                            case RobotStatus.ROBOT_STATE_INDEX_MASK:
                                ((OnMaskStatusChangedListener)mOnRobotStateChangeListener).onMaskStatusChanged(getMaskStatus());
                                break;
                            //托盘的监听
                            case RobotStatus.ROBOT_STATE_INDEX_TRAY:
                                ((OnSalverStatusChangedListener)mOnRobotStateChangeListener).onSalverStatusChanged(getSalverStatus());
                                break;
                        }

                    }
                }
                break;
        }
    }

    IRobotConnect robotConnect;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            robotConnect = IRobotConnect.Stub.asInterface(service);
            factory.updateRobotConnect(robotConnect);
            /*  连接成功后更新所有监听   */
            updateListener();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
            robotConnect = null;
            factory.updateRobotConnect(null);
        }
    };
    /**
     * 连接成功后更新所有监听（比如server挂掉重启后，所有的监听都会清空，所以需要sdk自动注册）
     */
    private void updateListener() {
        for (int i = 0; i < mRobotStateChangeListenerGroup.size(); i++) {
            int robotStateIndex = mRobotStateChangeListenerGroup.keyAt(i);
            List<OnRobotStateChangeListener> onRobotStateChangeListeners = mRobotStateChangeListenerGroup.get(robotStateIndex);
            if (onRobotStateChangeListeners != null && onRobotStateChangeListeners.size() != 0 && robotConnect != null) {
                try {
                    robotConnect.addRobotMessageListener(packageName, robotStateIndex, robotMessageListenerImp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 连接下位机通讯程序
     */
    public void connect(){

        Intent intent = new Intent("com.efrobot.action.RobotControlService");
//        intent.addCategory("android.intent.category.DEFAULT");
        boolean flag= mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        Log.e("connect()","flag"+flag);
    }

    /**
     * 获取机器人管理实例
     *
     * @return 机器人管理实例
     */
    public static synchronized RobotManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (RobotManager.class) {
                if (mInstance == null) {
                    mInstance = new RobotManager(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取机器人控制实例（设置灯带亮度、关机、设置机器人号码）
     *
     * @return 机器人控制实例
     */
    public ControlManager getControlInstance() throws Exception{

        if (getInitResult()) {
            if (controlManager == null) {
                controlManager = ControlManager.getInstance(mInstance);
            }
            return controlManager;
        }
        Log.e(TAG,"请认证");
        return null;
    }

    /**
     * 获取机器人头部管理实例
     *
     * @return 机器人头部管理实例
     */
    public HeadManager getHeadInstance() throws Exception{
        if (getInitResult()) {
            if (headManager == null) {
                headManager = HeadManager.getInstance(mInstance);
            }
            return headManager;
        }
        Log.e(TAG,"请认证");
        return null;
    }
//    /**
//     * 获取机器人脚本管理实例
//     *
//     * @return 机器人脚本管理实例
//     */
//    public GroupManager getGroupInstance() {
//        if (groupManager == null) {
//            groupManager = GroupManager.getInstance(mInstance,mContext);
//        }
//        return groupManager;
//    }

    /**
     * 获取机器人翅膀管理实例
     *
     * @return 机器人翅膀管理实例
     */
    public WingManager getWingInstance() throws Exception{
        if (getInitResult()) {
            if (wingManager == null) {
                wingManager = WingManager.getInstance(mInstance);
            }
            return wingManager;
        }
        Log.e(TAG,"请认证");
        return null;
    }

    /**
     * 获取机器人底部双轮管理实例
     *
     * @return 机器人底部双轮管理实例
     */
    public WheelManager getWheelInstance() throws Exception{
        if (getInitResult()) {
            if (wheelManager == null) {
                wheelManager = WheelManager.getInstance(mInstance);
            }
            return wheelManager;
        }
        return null;
    }

    /**
     * 机器人SDK初始化的结果
     */
    private   boolean resultCode;

    public  boolean getResultCode(){
        return resultCode;
    }

    /**
     * 初始化SDK
     *
     * @param accessKey
     * @param accessSecret
     * @param onInitCompleteListener 初始化完成回调
     */
    public void init(String accessKey, String accessSecret, String devId, OnInitCompleteListener onInitCompleteListener) {
        if(onInitCompleteListener instanceof OnInitUnLinkListener && resultCode){
            mOnInitCompleteListener.onInitComplete(ResultCode.INIT_SUCCESS);
        }

        Log.e(TAG, "正在授权，请稍后");
        registerOnInitCompleteListener(onInitCompleteListener);
        if ("".equals(accessKey.trim()) || accessKey == null) {
            Log.e(TAG, "accessKey:" + accessKey + ",授权失败");
            mOnInitCompleteListener.onInitComplete(ResultCode.INIT_FAILURE);
            return;
        }
        if ("".equals(accessSecret.trim()) || accessSecret == null) {
            Log.e(TAG, "accessSecret:" + accessSecret + ",授权失败");
            mOnInitCompleteListener.onInitComplete(ResultCode.INIT_FAILURE);
            return;
        }
        if ("".equals(devId.trim()) || devId == null) {
            Log.e(TAG, "accessSecret:" + devId + ",授权失败");
            mOnInitCompleteListener.onInitComplete(ResultCode.INIT_FAILURE);
            return;
        }
        try {
            OkHttpClient client = new OkHttpClient();
            JSONObject appSdkAuth = new JSONObject();
            appSdkAuth.put("accessKey", accessKey);
            appSdkAuth.put("accessSecret", accessSecret);
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//            Log.e(TAG, "   " + appSdkAuth.toString());
            RequestBody body = RequestBody.create(JSON, appSdkAuth.toString());
            Request request = new Request.Builder().url(UrlConstants.SDK_AUTH_URL+"?devId="+devId).post(body).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    Log.e("RobotManager", "--------------------------初始化请求失败:   " + e.toString() + "  " + request.body().toString());
                    resultCode = false;
                    mOnInitCompleteListener.onInitComplete(ResultCode.INIT_FAILURE);
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    Log.e("RobotManager", "--------------------------初始化请求成功");
                    String responseStr = response.body().string();
                    Log.e(TAG, "得到的是: " + responseStr.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);
//                        Log.e(TAG, "得到的是: " + jsonObject.toString());
//                        String msg = jsonObject.getString("msg");
                        int responseCode = jsonObject.optInt("resultCode", 0);
                        if (responseCode == 1) {
//                          Log.e("RobotManager", "--------------------------" + msg);
                            resultCode = true;
                            if(mOnInitCompleteListener instanceof OnInitUnLinkListener){
                                mOnInitCompleteListener.onInitComplete(ResultCode.INIT_SUCCESS);
                            }else
                            notifyOnInitCompleteListener();
                        } else if (responseCode == 0) {
//                          Log.e("RobotManager", "--------------------------" + msg);
                            resultCode = false;
                            mOnInitCompleteListener.onInitComplete(ResultCode.INIT_FAILURE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * init 结果
     */
    public enum ResultCode {
        INIT_FAILURE, INIT_SUCCESS
    }
    /**
     * 初始化完成回调
     */
    private OnInitCompleteListener mOnInitCompleteListener;

    public void registerOnInitCompleteListener(OnInitCompleteListener mOnInitCompleteListener) {
        this.mOnInitCompleteListener = mOnInitCompleteListener;
    }

    private void registerOnRobotStateChangeListener(int robotStateIndex, OnRobotStateChangeListener mChangeListener) {
        if (mChangeListener == null) return;
        List<OnRobotStateChangeListener> onRobotStateChangeListeners = mRobotStateChangeListenerGroup.get(robotStateIndex);
        if (onRobotStateChangeListeners == null) {
            onRobotStateChangeListeners = new ArrayList<OnRobotStateChangeListener>();
            mRobotStateChangeListenerGroup.append(robotStateIndex, onRobotStateChangeListeners);
        }
        if (onRobotStateChangeListeners.size() == 0 && robotConnect != null) {
            try {
                robotConnect.addRobotMessageListener(packageName, robotStateIndex, robotMessageListenerImp);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        onRobotStateChangeListeners.add(mChangeListener);
    }

    private void unRegisterOnRobotStateChangeListener(int robotStateIndex, OnRobotStateChangeListener mChangeListener) {
        List<OnRobotStateChangeListener> onRobotStateChangeListeners = mRobotStateChangeListenerGroup.get(robotStateIndex);
        if (onRobotStateChangeListeners != null && onRobotStateChangeListeners.size() > 0) {
            onRobotStateChangeListeners.remove(mChangeListener);
            if (onRobotStateChangeListeners.size() == 0)
                mRobotStateChangeListenerGroup.remove(robotStateIndex);
        }
        onRobotStateChangeListeners = mRobotStateChangeListenerGroup.get(robotStateIndex);
        if ((onRobotStateChangeListeners == null || onRobotStateChangeListeners.size() == 0) && robotConnect != null) {
            try {
                robotConnect.removeRobotMessageListener(packageName, robotStateIndex);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取投影仪状态
     *
     * @return ProjectStatus 投影仪状态
     */
    public RobotStatus.ProjectStatus getProjectorStatus() {
        if (!getInitResult()) return RobotStatus.ProjectStatus.FAILURE;

         switch ( mRobotStatus.getProjectorState()){
             case 0:
                 setProjectorStatus(0);//投影仪关闭
                 break;
             case 1:
                 setProjectorStatus(1);
                 break;//投影仪打开
             case -10000:
                 setProjectorStatus(2);
                 break;//投影仪打开
         }
        return mProjectStatus;
    }

    /**
     * 注册投影仪状态变化监听
     *
     * @param listener 投影仪状态变化监听
     */
    public void registerProjectorStatusChangedListener(OnProjectStatusChangedListener listener) {
        registerOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_PROJECTOR_STATE, listener);

    }
    /**
     * 取消注册投影仪状态变化监听
     *
     * @param listener 投影仪状态变化监听
     */
    public void unRegisterProjectorStatusChangedListener(OnProjectStatusChangedListener listener) {
        unRegisterOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_PROJECTOR_STATE, listener);
    }
    /**
     * 获取投影仪状态
     *
     * @return ProjectStatus 投影仪状态
     */
    private void setProjectorStatus(RobotStatus.ProjectStatus projectorStatus) {
        mProjectStatus = projectorStatus;
    }

    private void setProjectorStatus(int projectorStatus) {
        setProjectorStatus(RobotStatus.ProjectStatus.getProjectStatus(projectorStatus));
    }
    /**
     * 投影仪状态回调
     */
    public interface OnProjectStatusChangedListener extends OnRobotStateChangeListener{
        void onProjectStatusChanged(RobotStatus.ProjectStatus projectStatus);
    }

    /**
     * 注册机器人电量变化监听
     *
     * @param listener 电池电量变化监听
     */
    public void registerBatteryLevelChangedListener(OnBatteryLevelChangedListener listener) {
        registerOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_BATTERY_STATUS_LEVEL, listener);
    }

    /**
     * 取消注册机器人电量变化监听
     *
     * @param listener 电池电量变化监听
     */
    public void unRegisterBatteryLevelChangedListener(OnBatteryLevelChangedListener listener) {
        unRegisterOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_BATTERY_STATUS_LEVEL, listener);
    }

    /**
     * 电量状态变化监听
     */
      interface   OnBatteryLevelChangedListener extends OnRobotStateChangeListener {
         void onBatteryLevelChanged(int batteryLevel);
    }

    /**
     * 获取机器人的电池电量
     *
     * @return 电池电量(百分比) 未获取到返回-10000
     */
    public int getBatteryLevel() {
        return mRobotStatus.getBatteryLevel();
    }

    /**
     * 电池状态变化监听
     */
    public interface OnBatteryStatusChangedListener  extends OnRobotStateChangeListener{
        void onBatteryStatusChanged(RobotStatus.BatteryStatus batteryStatus);
    }

    /**
     * 设置电池状态
     *
     * @param batteryStatus 电池状态
     */
    private void setBatteryStatus(RobotStatus.BatteryStatus batteryStatus) {
        mBatteryStatus = batteryStatus;
    }

    private void setBatteryStatus(int batteryStatus) {
        setBatteryStatus(RobotStatus.BatteryStatus.getBatteryStatus(batteryStatus));
    }

    /**
     * 获取电池状态
     *
     * @return BatteryStatus 电池状态
     */
    public RobotStatus.BatteryStatus getBatteryStatus() {
        if (!getInitResult()) return RobotStatus.BatteryStatus.FAILURE;

        switch (mRobotStatus.getBatteryState()){
            case 0:
                setBatteryStatus(0);
                break;
            case -10000:
                setBatteryStatus(2);
                break;
            case 1:
            case 2:
            case 3:
            case 4:
                setBatteryStatus(1);
                break;
        }
        return mBatteryStatus;

    }

    /**
     * 注册机器人电池状态变化监听
     *
     * @PARAM LISTENER 电池状态变化监听
     */
    public void registerBatteryStatusChangeListener(OnBatteryStatusChangedListener listener) {
        registerOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_BATTERY_STATUS_CHARGING, listener);
    }

    /**
     * 取消注册机器人电池状态变化监听
     *
     * @param listener 电池状态变化监听
     */
    public void unRegisterBatteryStatusChangeListener(OnBatteryStatusChangedListener listener) {
        unRegisterOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_BATTERY_STATUS_CHARGING, listener);
    }

    /**
     * 头顶触摸状态变化监听
     */
    public interface OnHeadTouchStatusChangedListener extends OnRobotStateChangeListener{
        void onHeadTouchStatusChanged(RobotStatus.HeadTouchStatus headTouchStatus);
    }

    /**
     * 获取头顶触摸状态
     *
     * @return HeadTouchStatus 头顶触摸状态
     */
    public RobotStatus.HeadTouchStatus getHeadTouchStatus() {
        if (!getInitResult()) return RobotStatus.HeadTouchStatus.FAILURE;
        switch (mRobotStatus.getHeadKeyState()){
            case 0:
                setHeadTouchStatus(0);
                break;
            case -10000:
                setHeadTouchStatus(2);
                break;
            case 1:
            case 2:
                setHeadTouchStatus(1);
                break;
        }
        return mHeadTouchStatus;
    }

    /**
     * 设置头顶触摸状态
     *
     * @param headTouchStatus 头顶触摸状态
     */
    private void setHeadTouchStatus(RobotStatus.HeadTouchStatus headTouchStatus) {
        mHeadTouchStatus = headTouchStatus;
    }

    private void setHeadTouchStatus(int headTouchStatus) {
        setHeadTouchStatus(RobotStatus.HeadTouchStatus.getHeadTouchStatus(headTouchStatus));
    }

    /**
     * 注册头顶触摸状态变化监听
     *
     * @param listener 头顶触摸状态变化监听
     */
    public void registerOnHeadTouchStatusChangedListener(OnHeadTouchStatusChangedListener listener) {
        registerOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_HEAD_KEY, listener);
    }

    /**
     * 取消注册头顶触摸状态变化监听
     *
     * @param listener 头顶触摸状态变化监听
     */
    public void unRegisterOnHeadTouchStatusChangedListener(OnHeadTouchStatusChangedListener listener) {
        unRegisterOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_HEAD_KEY, listener);
    }

    /**
     * 净化器状态变化监听
     */
    public interface OnPurifierStatusChangedListener extends OnRobotStateChangeListener{
        void onPurifierStatusChanged(RobotStatus.PurifierStatus purifierStatus);
    }


    /**
     * 获取机器人净化器状态
     *
     * @return 净化器状态，未获取到返回- 10000
     */
    public RobotStatus.PurifierStatus getPurifierStatus() {
        if (!getInitResult()) return RobotStatus.PurifierStatus.FAILURE;
        switch (mRobotStatus.getPurifierState()){
            case -10000:
                setPurifierStatus(4);
                break;
            case 0:
                setPurifierStatus(0);
                break;
            case 1:
                setPurifierStatus(1);
                break;
            case 2:
                setPurifierStatus(2);
                break;
            case 100:
                setPurifierStatus(3);
                break;
        }
        return mPurifierStatus;
    }

    /**
     * 设置净化器状态
     *
     * @param purifierStatus 净化器状态
     */
    private void setPurifierStatus(RobotStatus.PurifierStatus purifierStatus) {
        mPurifierStatus = purifierStatus;
    }

    private void setPurifierStatus(int purifierStatus) {
        setPurifierStatus(RobotStatus.PurifierStatus.getPurifierStatus(purifierStatus));
    }

    /**
     * 注册净化器状态变化监听
     *
     * @param listener 净化器状态变化监听
     */
    public void registerPurifierStatusChangedListener(OnPurifierStatusChangedListener listener) {
        registerOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_PURIFIER_STATE, listener);
    }


    /**
     * 取消注册净化器状态变化监听
     *
     * @param listener 净化器状态变化监听
     */
    public void unRegisterPurifierStatusChangedListener(OnPurifierStatusChangedListener listener) {
        unRegisterOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_PURIFIER_STATE, listener);
    }

    /**
     * 获取机器人面罩的开关状态
     *
     * @return MaskStatus 面罩状态
     */
    public RobotStatus.MaskStatus getMaskStatus() {
        if (!getInitResult()) return RobotStatus.MaskStatus.FAILURE;
        switch (mRobotStatus.getMaskState()){
            case -10000:
                setMaskStatus(3);
            case 0:
                setMaskStatus(0);
                break;
            case 1:
                setMaskStatus(1);
                break;
            case 2:
                setMaskStatus(2);
                break;
        }
        return mMaskStatus;
    }

    /**
     * 设置面罩状态
     *
     * @param maskStatus 面罩状态
     */
    private void setMaskStatus(RobotStatus.MaskStatus maskStatus) {
        this.mMaskStatus = maskStatus;
    }

    private void setMaskStatus(int maskStatus) {
        setMaskStatus(RobotStatus.MaskStatus.getMaskStatus(maskStatus));
    }

    /**
     * 面罩状态变化监听
     */
    public interface OnMaskStatusChangedListener extends OnRobotStateChangeListener{
        void onMaskStatusChanged(RobotStatus.MaskStatus maskStatus);
    }

    /**
     * 注册面罩状态变化监听
     *
     * @param listener 面罩状态变化监听
     */
    public void registerMaskStatusChangedListener(OnMaskStatusChangedListener listener) {
        registerOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_MASK, listener);
    }

    /**
     * 取消注册面罩状态变化监听
     *
     * @param listener 面罩状态变化监听
     */
    public void unRegisterMaskStatusChangedListener(OnMaskStatusChangedListener listener) {
        unRegisterOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_MASK, listener);
    }

    /**
     * 托盘状态变化监听
     */
    public interface OnSalverStatusChangedListener extends OnRobotStateChangeListener{
        void onSalverStatusChanged(RobotStatus.SalverStatus salverStatus);
    }

    /**
     * 获取机器人托盘的状态
     *
     * @return SalverStatus 托盘状态
     */
    public RobotStatus.SalverStatus getSalverStatus() {
        if (!getInitResult()) return RobotStatus.SalverStatus.FAILURE;
        switch (mRobotStatus.getTrayState()){
            case -10000:
                setSalverStatus(2);
            case 0:
                setSalverStatus(0);
                break;
            case 1:
                setSalverStatus(1);
                break;
        }
        return mSalverStatus;
    }

    /**
     * 设置托盘状态
     *
     * @return SalverStatus 托盘状态
     */
    private void setSalverStatus(RobotStatus.SalverStatus salverStatus) {
        mSalverStatus = salverStatus;
    }

    private void setSalverStatus(int salverStatus) {
        setSalverStatus(RobotStatus.SalverStatus.getSalverStatus(salverStatus));
    }

    /**
     * 注册托盘状态变化监听
     *
     * @param listener 托盘状态变化监听
     */
    public void registerSalverStatusChangedListener(OnSalverStatusChangedListener listener) {
        registerOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_TRAY, listener);
    }

    /**
     * 取消注册托盘状态变化监听
     *
     * @param listener 托盘状态变化监听
     */
    public void unRegisterSalverStatusChangedListener(OnSalverStatusChangedListener listener) {
        unRegisterOnRobotStateChangeListener(RobotStatus.ROBOT_STATE_INDEX_TRAY, listener);
    }

    public void execute(int type, String orderContent) {
        execute(type, orderContent.getBytes());
    }

    public void execute(int type, byte[] orderData) {
        try {
            if (robotConnect != null) {
                RobotMessage message = new RobotMessage();
                message.setIndex(type);
                message.setData(orderData);
                message.setIdentifier(packageName);
                message.setSendTimeStamp(System.currentTimeMillis());
                robotConnect.executeTask(packageName, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置机器人TTS.
     *
     * @param content 机器人说话内容 TTS
     */
    public void executeSpeak(String content) {
        if(!getInitResult()){
            Log.e(TAG," 请认证!");
            return;
        }
        Intent mIntent = new Intent(ACTION_VOICE);
        try {
            JSONObject object = new JSONObject();
            object.put("content", content);
            mIntent.putExtra("data", object.toString());
            mContext.sendBroadcast(mIntent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//    /**
//     * 播放表情
//     *
//     * @param face 表情
//     * @param type 播放类型
//     */
//    public void startPlayFace(int face, int type) {
//        String path = mPathList.get(face);
//        Log.e(TAG, path);
//        Intent intent = new Intent(mContext, VideoActivity.class);
//        intent.putExtra("path", path);
//        intent.putExtra("type", type);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//    }
//    /**
//     * 停止播放表情
//     */
//    public void stopPlayFace() {
//        mVideoActivity.stop();
//    }

    /**
     * 播放表情
     *
     * @param face 表情
     * @param type 播放类型
     */
    public void startPlayFace(int face, int type) {
        if(!getResultCode()){
            Log.e(TAG," 请认证!");
            return;
        }
        Intent mIntent = new Intent(ACTION_FACE);
        JSONObject json = new JSONObject();
        try {
            json.put("ID", "[" + face + "]");
            if (type == 0)
                json.put("SDK_FACE", 0);
            else if (type == 1)
                json.put("SDK_FACE", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIntent.putExtra("data", json.toString());
        mContext.sendBroadcast(mIntent);
    }

    /**
     * 播放表情
     *
     * @param face 表情
     */
    public void startPlayFace(int face) {
        if(!getResultCode()){
            Log.e(TAG," 请认证!");
            return;
        }
        Intent mIntent = new Intent(ACTION_FACE);
        JSONObject json = new JSONObject();
        try {
            json.put("ID", "[" + face + "]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIntent.putExtra("data", json.toString());
        mContext.sendBroadcast(mIntent);
    }

    /**
     * 停止播放表情
     */
    public void stopPlayFace() {
        if(!getResultCode()){
            Log.e(TAG," 请认证!");
            return;
        }
        Intent mIntent = new Intent(ACTION_FACE);
        mIntent.putExtra("type", 0);
        mContext.sendBroadcast(mIntent);
    }

    /**
     * @return RobotManager初始化和Ros连接是否成功
     */
    public void notifyOnInitCompleteListener() {
        if(Constants.DEBUG){
        Log.e(TAG,"mRobotControlNode.hasConnect(): "+hasConnect());
        Log.e(TAG,"resultCode: "+resultCode);
        Log.e(TAG,"mOnInitCompleteListener: "+mOnInitCompleteListener.toString());
        }
        if (hasConnect() && resultCode && mOnInitCompleteListener != null) {
            mOnInitCompleteListener.onInitComplete(ResultCode.INIT_SUCCESS);
        }
    }

    /**
     * @return boolean SDK和ROS初始化结果
     */
    public boolean getInitResult() {
        return hasConnect() && resultCode;
    }
    public boolean hasConnect() {
        return mServiceConnection != null;
    }
    public static String format(int n) {
        String str = Integer.toHexString(n);
        int l = str.length();
        if (l == 1) return "0" + str;
        else
            str.substring(l - 2, l);
        return str;
    }

    private class RosMasterBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, action);
            if (ACTION_NAME_ROS_MASTER_START.equals(action)) {
                connect();
            }
        }
    }

}
