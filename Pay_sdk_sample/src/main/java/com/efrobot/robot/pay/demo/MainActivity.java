package com.efrobot.robot.pay.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.efrobot.library.OnInitUnLinkListener;
import com.efrobot.library.RobotManager;
import com.efrobot.library.pay.PayTask;
import com.efrobot.paylibrary.OrderInfo;
import com.efrobot.paylibrary.PayResult;

public class MainActivity extends Activity implements View.OnClickListener, PayTask.IPayResultCallBack {

    private Button mBtPay;
    private Context mContext;
    private PayTask mPayTask;
    private OrderInfo mOrderinfo;
    //测试专用，第三方需在开放平台申请
    private static final String privatekey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMGPHhgeYTq9GcqCAl/KCyWDXlr0GrPAUfeBS2wrQxX2qb7qbq3wbHL0dQDmEUbDQ/zSv76ufIhoWoN1kzmXdaKeOqgCfdQwZ/oZyI4wo0OgsD6R6O40DjXoDyqYptIl37I0LZXuOr0ojOuK8uPWDbk7SUQPwNpoMjj6xKO3YHFBAgMBAAECgYAuvPFGX6eYuGrO5H/QQ4hXgZY6HjGIcAsa2cStlPC5D/vDFvas155IT9Ek62kGjkYPeZBFafq2MYSRs8VeJb2IoGv9b6SzbS1ooHeIwtugVKaZC/WwvLWacMkJBKZETMq2LQXO0i/ca+jmEtNNppGj9Sowk8dAA3ssHkbLIPBYBQJBAOEk4pYRe55+a06jCiazGHVrSOareIJgCsz4IB7sEcejf3nt0CVC11yOLcKM7G0teD7U7j4Zdn0BXos94eZXq1cCQQDcFhSySgiX2hiGppqhtDKbHiwYY95ifR4TvOwh9wsve85vUVwFM0HUxiN1lR1QddVdOPQxEXdBJu1W14F8NgEnAkBhvWSMp4xWEyW8nrUSdd+hYfDlCv2nA9DMiM9Q/UT+uTDnGuGou2rJKIQfGJSvFPVFuaSv2tgAjq6fnj7Kg7bDAkAzHXm/Esy+H+w0Ubt2NPjP6AkLvR4oN7DlmqopFUxmwdVnKzk5B/eRKFeR9ojxZ/yfAxxLzSeqXRUgZzruk0hfAkEAj3YoySkJkvIx9v7I1jnij1P8+gX/DKTr1Vl6S5Dc+bzesnrBqNq3Jr0JIgFp5RTmABDBCwO9MeKALRfMc0t/rQ==";

    private String DevId="568968789";

    private String AccessKey="98838811";

    private String AccessSecret="mR07n5fmEC5DDY7yTn55PmdVYJ9bcF9Z";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        InitViews();
    }

    void InitViews() {
        mPayTask = new PayTask(mContext);
        mOrderinfo = new OrderInfo();
        mBtPay = (Button) findViewById(R.id.pay);
        mBtPay.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay:

                //商户在开放平台申请的应用ID，123456为测试账号需换成开发者自己申请的账号
                mOrderinfo.setAppId("83");

                //商户在开放平台申请的账户ID，123456为测试账号需换成开发者自己申请的账号
                mOrderinfo.setDevId(DevId);

                //商户自己订单系统的交易号
                mOrderinfo.setOutTradeNo("123456");

                //交易发生的时间戳
                mOrderinfo.setTimestamp("2016-11-10 11:31:36");

                //交易金额
                mOrderinfo.setTotalAmount("1");

                //商品详情
                mOrderinfo.setBody("小胖之声");

                //商品标题
                mOrderinfo.setSubject("英语学习");

                //销售产品码（签约用户申请销售appId下对应产品码）
                mOrderinfo.setProductCode("123456");

                //机器人主动通知开发者服务器订单状态的地址
                mOrderinfo.setNotifyUrl("http:api.xxx.com/pay");

                //私钥，开放平台申请
                mOrderinfo.setPrivateKey(privatekey);


                mPayTask.init(AccessKey, AccessSecret, DevId, new OnInitUnLinkListener() {
                    @Override
                    public void onInitComplete(RobotManager.ResultCode resultCode) {
                        if(resultCode== RobotManager.ResultCode.INIT_SUCCESS){
                            try {
                                mPayTask.pay(mOrderinfo, MainActivity.this);
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }else{
                            Log.d("初始化结果","初始化失败");
                        }
                    }
                });



                break;
        }
    }

    @Override
    public void getPayResult(final  PayResult payResult) {

        Log.d("支付结果", payResult.toString());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","onDestroy");
        if (mPayTask != null) {
            mPayTask.onRemovePayTask();
        }

    }
}
