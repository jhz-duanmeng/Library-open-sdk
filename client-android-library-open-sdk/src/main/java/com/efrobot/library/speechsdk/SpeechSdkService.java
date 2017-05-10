package com.efrobot.library.speechsdk;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.efrobot.speechsdk.ISDKSpeech;

/**
 * Created by lhy on 2016/10/13
 *
 * @Link
 * @description
 */
public abstract class SpeechSdkService extends Service {
    public final static String TAG = "SpeechSdkService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "SpeechSdkService onCreate");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG, "SpeechSdkService onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "SpeechSdkService onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "SpeechSdkService onBind");
        return mBinder;
    }

    private Binder mBinder = new ISDKSpeech.Stub() {

        @Override
        public void receive(String code) throws RemoteException {
            Log.i(TAG, "SDKService receive=" + code);
            onReceiveMessage(code);
        }
    };

    public abstract void onReceiveMessage(String code);
}
