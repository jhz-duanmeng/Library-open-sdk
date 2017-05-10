package com.efrobot.sdk.control_sdk_sample.master;


import com.efrobot.sdk.control_sdk_sample.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 * 统一管理全部的Activity
 */
public class ActivityMaster {
    private List<BaseActivity> mAllActivities = new ArrayList<>();
    private static ActivityMaster mActivityMaster;
    
    private ActivityMaster() {
    }
    
    public static ActivityMaster getInstance() {
        if (mActivityMaster == null) {
            synchronized (ActivityMaster.class) {
                if (mActivityMaster == null) {
                    mActivityMaster = new ActivityMaster();
                }
            }
        }
        return mActivityMaster;
    }
    
    //添加一个Activity进入管理
    public void addActivityToMaster(BaseActivity baseActivity) {
        mAllActivities.add(baseActivity);
    }
    
    //删除一个Activity
    public void delActivityFromMaster(BaseActivity baseActivity) {
        mAllActivities.remove(baseActivity);
    }
    
    //获取指定的某一个Activity
    public BaseActivity getActivity(Class baseActivityClass) {
        for (BaseActivity baseActivity : mAllActivities) {
            if (baseActivity.getClass() == baseActivityClass) {
                return baseActivity;
            }
        }
        throw new RuntimeException("NO Activiy found!!");
    }
    
    //销毁全部的Activity
    public void killAll() {
        Iterator<BaseActivity> iterator = mAllActivities.iterator();
        while (iterator.hasNext()) {
            iterator.next().killSelf();
        }
    }
    
    //销毁某一个的Activity
    public void killOne(Class baseActivityClass) {
        Iterator<BaseActivity> iterator = mAllActivities.iterator();
        while (iterator.hasNext()) {
            BaseActivity baseActivity = iterator.next();
            if (baseActivity.getClass() == baseActivityClass) {
                baseActivity.killSelf();
            }
            
        }
    }
}
