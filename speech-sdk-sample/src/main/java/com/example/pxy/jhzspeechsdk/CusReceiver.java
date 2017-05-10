package com.example.pxy.jhzspeechsdk;

import android.util.Log;

import com.efrobot.library.speechsdk.SpeechSdkReceiver;


/**
 * Created by 鑫宇 on 2016/12/16.
 */
public class CusReceiver extends SpeechSdkReceiver {
    @Override
    public void onCusReceive(Boolean aBoolean) {
        Log.i("CusReceiver","当前语音识别的开关状态是： "+aBoolean);
    }
}
