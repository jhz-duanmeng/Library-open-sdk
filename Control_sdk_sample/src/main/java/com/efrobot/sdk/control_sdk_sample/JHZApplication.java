package com.efrobot.sdk.control_sdk_sample;

import android.util.Log;

import com.efrobot.library.OnInitCompleteListener;
import com.efrobot.library.RobotManager;
import com.efrobot.sdk.control_sdk_sample.ui.base.BaseApplication;
import com.efrobot.sdk.control_sdk_sample.ui.base.TitileBarConfig;


/**
 * Created by 鑫宇 on 2016/11/18.
 */
public class JHZApplication extends BaseApplication implements  OnInitCompleteListener {
    public static boolean initSuccess;
    /**
     * 请求授权参数
     */
    private String accessKey = "98838811";
    private String accessSecret = "mR07n5fmEC5DDY7yTn55PmdVYJ9bcF9Z";
    private String devId = "568968789";

    @Override
    public void onCreate() {
        super.onCreate();
        TitileBarConfig.setTitleBarRootLayout(R.layout.title_bar);
        RobotManager.getInstance(this).connect();
        RobotManager.getInstance(this).init(accessKey, accessSecret, devId, this);

    }

    @Override
    public void onInitComplete(RobotManager.ResultCode resultCode) {
        Log.e("TAG", resultCode.name() + ": " + resultCode.ordinal());
        if (resultCode == RobotManager.ResultCode.INIT_SUCCESS) {
            initSuccess = true;
            Log.e("JHZApplication","initSuccess: "+initSuccess+"resultCode"+resultCode);
        }
    }
}
