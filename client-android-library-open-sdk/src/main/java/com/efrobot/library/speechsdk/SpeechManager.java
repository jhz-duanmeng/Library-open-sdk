package com.efrobot.library.speechsdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.efrobot.library.OnInitUnLinkListener;
import com.efrobot.library.RobotManager;
import com.efrobot.speechlibrary.IManager;

import java.util.List;

/**
 * Created by lhy on 2016/10/13
 *
 * @Link
 * @description
 */
public class SpeechManager {
    public static final String TAG = "SpeechManager";
    private static SpeechManager mSpeechManager;
    private static String PACKAGE_NAME = "";
    private Context mContext;
    private ISpeech mISpeech;

    private SpeechManager() {
    }

    public static SpeechManager getInstance() {
        if (mSpeechManager == null) {
            synchronized (SpeechManager.class) {
                if (mSpeechManager == null) {
                    mSpeechManager = new SpeechManager();
                }
            }
        }
        return mSpeechManager;
    }

    public  void init(Context context,String accessKey, String accessSecret, String devId, OnInitUnLinkListener mOnInitUnLinkListener){
        PACKAGE_NAME = context.getPackageName();
        mContext = context;
        RobotManager.getInstance(mContext).init(accessKey,accessSecret,devId,mOnInitUnLinkListener);
    }
    //注册
    public void registerSpeechListener( ISpeech mISpeech) {
        if(PACKAGE_NAME == null || mContext==null){
            Log.e(TAG,"请先进行SDK初始化");
            throw new NullPointerException();
        }
        if (!RobotManager.getInstance(mContext).getResultCode()) {
            throw new ExceptionInInitializerError();
        }

        this.mISpeech = mISpeech;

        Intent intent = new Intent();
        intent.setAction("com.efrobot.speechlibrary.remoteservice");
//      intent.setPackage("com.efrobot.speechlibrary");
        mContext.bindService(intent, mConn, Context.BIND_AUTO_CREATE);
    }

    //反注册
    public void unRegisterSpeechListener() throws Exception {
        if (manager == null) {
            throw new NullPointerException();
        }
        boolean bool = manager.unregistListener(PACKAGE_NAME);
        if (bool) {
            //接触绑定
            mContext.unbindService(mConn);
            //通知第三方app解绑
            if (mISpeech != null) {
                mISpeech.onUnRegisterSuccess();
            }
        } else {
            if (mISpeech != null) {
                mISpeech.onUnRegisterFail();
            }
        }
    }

    /**
     * 设置数据
     *
     * @param data
     */
    public void setData(String data) throws RemoteException {
        if (manager == null) {
            throw new NullPointerException();
        }
        manager.initData(PACKAGE_NAME, data);
    }

    /**
     * 删除指令
     *
     * @param packageName
     * @param ins
     * @throws RemoteException
     */
    private void deleteIns(String packageName, String ins) throws RemoteException {
        manager.deleteIns(packageName, ins);
    }

    IManager manager;
    ServiceConnection mConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            manager = IManager.Stub.asInterface(service);
            Log.i("aidlSpeech", PACKAGE_NAME + "绑定成功");
            registerListener();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("aidlSpeech", PACKAGE_NAME + "断开连接");
            //服务被杀，会走这个方法
        }
    };

    private void registerListener() {
        try {
            boolean bool = manager.registListener(PACKAGE_NAME);
            //通知第三方app绑定成功
            if (mISpeech != null) {
                if (bool) {
                    mISpeech.onRegisterSuccess();
                } else {
                    mISpeech.onRegisterFail();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  removeSpeechState(int type) throws RemoteException {
        if (manager == null) {
            throw new NullPointerException();
        }
         manager.removeSpeechState(type);
    }
    /**
     * 开启语音识别
     *
     * @param packageName
     * @return
     * @throws RemoteException
     */
    public boolean openSpeechDiscern(String packageName) throws RemoteException {
        if (manager == null) {
            throw new NullPointerException();
        }
        return manager.openSpeechDiscern(packageName);
    }

    /**
     * 关闭语音识别
     *
     * @param packageName
     * @return
     * @throws RemoteException
     */
    public boolean closeSpeechDiscern(String packageName) throws RemoteException {
        if (manager == null) {
            throw new NullPointerException();
        }
        return manager.closeSpeechDiscern(packageName);
    }

    /**
     * 隐式启动Service转换成显式跳转
     *
     * @param context
     * @param implicitIntent 隐式Intent
     * @return
     */
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        // Retrieve all services that can match the given intent
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size() != 1) {
            return null;
        }
        // Get component info and create ComponentName
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        // Create a new intent. Use the old one for extras and such reuse
        Intent explicitIntent = new Intent(implicitIntent);
        // Set the component to be explicit
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    /**
     * @return 当前compileSdkVersion
     */
    private int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (Exception e) {
            version = android.os.Build.VERSION.SDK_INT;// api 17(4.2)
        }
        return version;
    }


    public interface ISpeech {
        void onRegisterSuccess();

        void onRegisterFail();

        void onUnRegisterSuccess();

        void onUnRegisterFail();

    }
}
