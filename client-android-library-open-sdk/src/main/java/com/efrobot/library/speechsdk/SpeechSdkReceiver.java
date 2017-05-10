package com.efrobot.library.speechsdk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by lhy on 2016/12/8
 *
 * @Link
 * @description
 */
public abstract class SpeechSdkReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle=intent.getExtras();
       boolean bool= bundle.getBoolean("SpeechDiscernState");
        onCusReceive(bool);
//         Log.i("LHY","client1 SpeechDiscernState="+bool);
    }
    public abstract void onCusReceive(Boolean discernState);
}
