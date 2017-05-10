package com.example.pxy.jhzspeechsdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.efrobot.library.speechsdk.SpeechManager;


public class MainActivity extends Activity {
  private boolean register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void go1(View view) {
        if(MyApplication.initSpeechSuccess) {
            SpeechManager.getInstance().registerSpeechListener(new SpeechManager.ISpeech() {
                @Override
                public void onRegisterSuccess() {
                    Log.i("aidlSpeech", "第三方收到：注册成功");
                    Toast.makeText(MainActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                     //设置数据
                    register=true;
                }
                @Override
                public void onRegisterFail() {
                    Log.i("aidlSpeech", "第三方收到：注册失败");
                    Toast.makeText(MainActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUnRegisterSuccess() {
                    Log.i("aidlSpeech", "第三方收到：反注册成功");
                    Toast.makeText(MainActivity.this,"反注册成功",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onUnRegisterFail() {
                    Log.i("aidlSpeech", "第三方收到：反注册失败");
                    Toast.makeText(MainActivity.this,"反注册失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void go2(View view) {
        try {
            SpeechManager.getInstance().unRegisterSpeechListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void go3(View view) {
        String str = "[    {        \"code\": \"10001\",        \"content\": \"\",        \"ins\": \"关闭语音\",        \"ip\": \"\",        \"packageName\": \"com.example.pxy.jhzspeechsdk\",        \"port\": \"\",        \"reply\": \"好的\",        \"type\": \"0\",        \"url\": \"http://www.baidu.com\"    },    {        \"code\": \"10002\",        \"content\": \"\",        \"ins\": \"打开语音\",        \"ip\": \"\",        \"packageName\": \"com.example.pxy.jhzspeechsdk\",        \"port\": \"\",        \"reply\": \"好的\",        \"type\": \"0\",        \"url\": \"\"    }]";
        if(register){
            try {
                SpeechManager.getInstance().setData(str);
            }  catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
