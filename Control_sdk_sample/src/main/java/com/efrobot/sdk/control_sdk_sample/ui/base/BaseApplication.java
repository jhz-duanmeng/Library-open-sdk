package com.efrobot.sdk.control_sdk_sample.ui.base;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/5/11.
 * 
 * 共性的初始化操作
 * 共用的数据
 */
public class BaseApplication extends Application{
    private static Executor mExecutor;
    public static  Executor getExecutor(){
        if (mExecutor==null){
            synchronized (BaseApplication.class){
                if (mExecutor==null){
                    mExecutor= Executors.newFixedThreadPool(5);
                }
            }
        }
      
        return mExecutor;
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
    }
    

}
