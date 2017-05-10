package com.example.pxy.jhzspeechsdk;

import android.app.Application;
import android.util.Log;

import com.efrobot.library.OnInitCompleteListener;
import com.efrobot.library.OnInitUnLinkListener;
import com.efrobot.library.RobotManager;
import com.efrobot.library.speechsdk.SpeechManager;


/**
 * Created by 鑫宇 on 2017/1/5.
 */
public class MyApplication extends Application implements OnInitCompleteListener, OnInitUnLinkListener {
    /**
     * 请求授权参数
     */
    private String accessKey = "98838811";
    private String accessSecret = "mR07n5fmEC5DDY7yTn55PmdVYJ9bcF9Z";
    private String devId = "568968789";
    public static boolean initSpeechSuccess = false;

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechManager.getInstance().init(this,accessKey, accessSecret, devId, this);

    }

    @Override
    public void onInitComplete(RobotManager.ResultCode resultCode) {
        if (resultCode == RobotManager.ResultCode.INIT_SUCCESS) {
            //认证通过
            Log.e("TAG", "" + resultCode.ordinal());
            initSpeechSuccess=true;

        }
    }
}


