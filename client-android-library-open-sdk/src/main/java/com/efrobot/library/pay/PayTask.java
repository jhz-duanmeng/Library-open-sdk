package com.efrobot.library.pay;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.efrobot.library.OnInitUnLinkListener;
import com.efrobot.library.RobotManager;
import com.efrobot.paylibrary.ExecuteServiceAIDL;
import com.efrobot.paylibrary.ICallBack;
import com.efrobot.paylibrary.OrderInfo;
import com.efrobot.paylibrary.PayResult;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * 发送下单信息唤起商城支付，唤起小胖商城支付类.
 * <p>
 * Created by zhangyun on 2016/9/2.
 */
public class PayTask implements ServiceConnection {

    /**
     * 支付订单信息.
     */
    private OrderInfo mOrderInfo;

    /**
     * mContext被用来唤起aidl服务.
     */
    private Context mContext;

    /**
     * 访问服务端的aidl接口,封装了getServerPayResult(in String info,ICallBack icallback)
     * 方法与服务端该方法对应,服务端是指商城钱包app.
     *
     * @see ExecuteServiceAIDL
     */
    private ExecuteServiceAIDL mExecuteService;

    /**
     * 支付结果的接口回调,服务端通知后由该接口返回给第三方app支付结果.
     *
     * @see IPayResultCallBack
     */
    private IPayResultCallBack mIPayResultCallBack;

    /**
     * 服务端services对应的action.
     */
    private static final String BIND_ACTION = "com.efrobot.library.sdk.pay";

    /**
     * 签名算法.
     */
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";


    private boolean isBind = false;

    private static final int SUCCESS=0;//SUCCESS	支付成功
    private static final int NETWORK_FAIL=-1000;//NETWORK_FAIL	服务器网络请求失败
    private static final int SIGN_FAIL=-1001;// SIGN_FAIL	验证签名失败，签名有误
    private static final int ORDER_INVALID=-1002;// ORDER_INVALID	订单无效
    private static final int PWD_ERROR=-1003;//PWD_ERROR	支付密码错误
    private static final int PARAM_FORMAT_ERROR=-1004;//PARAM_FORMAT_ERROR	下单请求参数错误
    private static final int NONETWORK=-1005;//  NONETWORK	无网络连接，请检查网络
    private static final int CANCEL=-1006;//CANCEL	取消支付
    private static final int SYSTEM_ERROR=-1007;//SYSTEM_ERROR	客户端请求失败，网络请求发送异常，稍后重试，请检查商城客户端是否安装
    private static final int ROBOT_PROCUCT_NOT_EXIST=-1008;//机器人本体商品不存在
    private static final int ROBOT_PRICE_NOT_EXIST=-1009;//机器人本体定价不存在

    private static final int REQUEST_ERROR=-1011;//请求错误
    private static final int GOLD_ACCOUNT_NOT_AVAILABLE=-1012;//金币账户不可用
    private static final int Gold_NOT_ENOUGH=-1013;//金币不足
    private static final int VERIFY_ROBOT_FAIL=-1014;//机器人数据无效
    private static final int GOLD_ACCOUNT_NOT_EXIST=-1015;//金币账户不存在
    private static final int ROBOTID_INEXISTENCE=-1016;//robotId无效，请输入有效数据



    private static final int APPSTORE_CONNECTION_ERROR=-1010;//商城应用连接错误
    private int MAX_CONNECTION_COUNT=3;//最大连接数，当商城进程挂了的话会重新连接
    /**
     * 默认构造器，实例化PayTask对象.
     *
     * @param context Context对象
     * @see Context
     */
    public PayTask(Context context) {
        this.mContext = context;
    }

    /**
     * 唤起商城客户端进行支付.
     *
     * @param orderInfo          订单相关信息的参数对象
     * @param iPayResultCallBack 支付结果回调接口
     * @see OrderInfo
     * @see IPayResultCallBack
     */
    public void pay(OrderInfo orderInfo, IPayResultCallBack iPayResultCallBack) {
        if(!RobotManager.getInstance(mContext).getResultCode()){
            throw new ExceptionInInitializerError();
        }
        if (orderInfo == null || iPayResultCallBack == null) {
            throw new NullPointerException();
        }
        if ((!orderInfo.checkOrderInfoIegality() )) {
            throw new IllegalArgumentException();
        }
        //先解除绑定，然后再绑定服务，防止当正在尝试连接商城时，又点击了支付
        onRemovePayTask();
        mOrderInfo = orderInfo;
        mIPayResultCallBack = iPayResultCallBack;
        Intent intent = new Intent();
        intent.setAction(BIND_ACTION);
        isBind=mContext.bindService(intent, this, BIND_AUTO_CREATE);
    }


    void onTranslateResultCode(PayResult param){
        switch (param.getResultCode()){
            case "SUCCESS":
                param.setResultCode(String.valueOf(SUCCESS));
                break;
            case "NETWORK_FAIL":
                param.setResultCode(String.valueOf(NETWORK_FAIL));//NETWORK_FAIL	服务器网络请求失败
                break;
            case "SIGN_FAIL":
                param.setResultCode(String.valueOf(SIGN_FAIL));// SIGN_FAIL	验证签名失败，签名有误
                break;
            case "ORDER_INVALID":
                param.setResultCode(String.valueOf(ORDER_INVALID));// ORDER_INVALID	订单无效
                break;
            case "PWD_ERROR":
                param.setResultCode(String.valueOf(PWD_ERROR));//PWD_ERROR	支付密码错误
                break;
            case "PARAM_FORMAT_ERROR":
                param.setResultCode(String.valueOf(PARAM_FORMAT_ERROR));//PARAM_FORMAT_ERROR	下单请求参数错误
                break;
            case "NONETWORK":
                param.setResultCode(String.valueOf(NONETWORK));//  NONETWORK	无网络连接，请检查网络
                break;
            case "CANCEL":
                param.setResultCode(String.valueOf(CANCEL));//CANCEL	取消支付
                break;
            case "SYSTEM_ERROR":
                param.setResultCode(String.valueOf(SYSTEM_ERROR));//SYSTEM_ERROR	客户端
                break;
            case "ROBOT_PROCUCT_NOT_EXIST":
                param.setResultCode(String.valueOf(ROBOT_PROCUCT_NOT_EXIST));//机器人本体商品不存在
                break;
            case "ROBOT_PRICE_NOT_EXIST":
                param.setResultCode(String.valueOf(ROBOT_PRICE_NOT_EXIST));//机器人本体定价不存在
                break;
            case "APPSTORE_CONNECTION_ERROR":
                param.setResultCode(String.valueOf(APPSTORE_CONNECTION_ERROR));//商城连接错误
                break;

            case "REQUEST_ERROR":
                param.setResultCode(String.valueOf(REQUEST_ERROR));//商城连接错误
                break;
            case "GOLD_ACCOUNT_NOT_AVAILABLE":
                param.setResultCode(String.valueOf(GOLD_ACCOUNT_NOT_AVAILABLE));//商城连接错误
                break;
            case "Gold_NOT_ENOUGH":
                param.setResultCode(String.valueOf(Gold_NOT_ENOUGH));//商城连接错误
                break;
            case "VERIFY_ROBOT_FAIL":
                param.setResultCode(String.valueOf(VERIFY_ROBOT_FAIL));//商城连接错误
                break;
            case "GOLD_ACCOUNT_NOT_EXIST":
                param.setResultCode(String.valueOf(GOLD_ACCOUNT_NOT_EXIST));//商城连接错误
                break;
            case "ROBOTID_INEXISTENCE":
                param.setResultCode(String.valueOf(ROBOTID_INEXISTENCE));//商城连接错误
                break;
            default:
                param.setResultCode(String.valueOf(SYSTEM_ERROR));//商城连接错误
                break;



        }
        onRemovePayTask();
    }
    /**
     * 提供给钱包app返回支付结果时的回调.
     */
    private ICallBack.Stub mCallBack = new ICallBack.Stub() {
        @Override
        public void handleByServer(PayResult param) throws RemoteException {
            onTranslateResultCode(param);
            mIPayResultCallBack.getPayResult(param);
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mExecuteService == null)
                return;
            mExecuteService.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mExecuteService = null;
            //这里重新绑定远程Service
            if(MAX_CONNECTION_COUNT>0){
                Intent intent = new Intent();
                intent.setAction(BIND_ACTION);
                mContext.startService(intent);
                isBind=mContext.bindService(intent, PayTask.this, BIND_AUTO_CREATE);
                --MAX_CONNECTION_COUNT;
                Log.d("PayTask重新连接"+MAX_CONNECTION_COUNT, "binder died. tname:" + Thread.currentThread().getName());
            }else{
                //回调，次数重新初始化，服务断开
                PayResult payResult = new PayResult();
                payResult.setData("商城应用内部异常");
                payResult.setResultCode("APPSTORE_CONNECTION_ERROR");
                onTranslateResultCode(payResult);
                MAX_CONNECTION_COUNT=3;
                mIPayResultCallBack.getPayResult(payResult);
            }

        }
    };
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        try {
            mExecuteService = ExecuteServiceAIDL.Stub.asInterface(service);
            if (mExecuteService != null) {
                mExecuteService.getServerPayResult(mOrderInfo, mCallBack);
                //设置死亡代理,当商城应用的Binder挂了，这边收到通知重新连接远程服务
                mExecuteService.asBinder().linkToDeath(mDeathRecipient, 0);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            //当未连接上的时候返回一个空的PayResult
            PayResult payResult = new PayResult();
            payResult.setData("连接商城应用异常");
            payResult.setResultCode("SYSTEM_ERROR");
            onTranslateResultCode(payResult);
            mIPayResultCallBack.getPayResult(payResult);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        //当未连接上的时候返回一个空的PayResult
        mExecuteService=null;
        PayResult payResult = new PayResult();
        payResult.setData("未连接上服务,请检查商城应用版本!");
        payResult.setResultCode("SYSTEM_ERROR");
        onTranslateResultCode(payResult);
        mIPayResultCallBack.getPayResult(payResult);

    }
    public  void init(String accessKey, String accessSecret, String devId, OnInitUnLinkListener mOnInitUnLinkListener){
        RobotManager.getInstance(mContext).init(accessKey,accessSecret,devId,mOnInitUnLinkListener);
    }
    /**
     * 支付结果回调.
     */
    public interface IPayResultCallBack {
        void getPayResult(PayResult payResult);
    }

    /**
     * 解除服务的绑定，需在onDestroy调用
     */
    public void onRemovePayTask(){
        if(mContext!=null&&isBind){
            mContext.unbindService(this);
            isBind=false;
        }
    }
}

