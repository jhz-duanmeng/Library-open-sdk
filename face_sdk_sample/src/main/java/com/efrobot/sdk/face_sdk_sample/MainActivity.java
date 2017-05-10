package com.efrobot.sdk.face_sdk_sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.efrobot.library.OnInitUnLinkListener;
import com.efrobot.library.RobotManager;
import com.efrobot.library.utils.FaceType;

public class MainActivity extends AppCompatActivity {

    /**
     * 请求授权参数
     */
    private String accessKey = "98838811";
    private String accessSecret = "mR07n5fmEC5DDY7yTn55PmdVYJ9bcF9Z";
    private String devId = "568968789";
    public static boolean initSuccess;
    public static final int STOP = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP:
                    RobotManager.getInstance(MainActivity.this).stopPlayFace();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RobotManager.getInstance(this).init(accessKey, accessSecret, devId, new OnInitUnLinkListener() {

            @Override
            public void onInitComplete(RobotManager.ResultCode resultCode) {
                if (resultCode == RobotManager.ResultCode.INIT_SUCCESS) {
                    initSuccess = true;
//                    Log.e("JHZApplication","initSuccess: "+initSuccess+"resultCode"+resultCode);
                }
            }
        });
    }

    public void go1(View view) {
        if (initSuccess) {
            RobotManager.getInstance(this).startPlayFace(FaceType.ARROGANT);
        }
    }


    public void go2(View view) {
        if (initSuccess) {
            RobotManager.getInstance(this).startPlayFace(FaceType.ARROGANT, 0);
            mHandler.sendEmptyMessageDelayed(STOP, 10000);
        }
    }


    public void go3(View view) {
        if (initSuccess) {
            RobotManager.getInstance(this).startPlayFace(FaceType.ARROGANT, 1);
            mHandler.sendEmptyMessageDelayed(STOP, 10000);
        }
    }

}
