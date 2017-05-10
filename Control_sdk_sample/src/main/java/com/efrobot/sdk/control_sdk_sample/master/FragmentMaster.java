package com.efrobot.sdk.control_sdk_sample.master;


import com.efrobot.sdk.control_sdk_sample.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2016/3/17.
 */
public class FragmentMaster {
    private  List<BaseFragment> mBaseFragments = new ArrayList<>();
    private static FragmentMaster mFragmentMaster;
    
    private FragmentMaster() {
    }

    /**
     *
     * @return返回全部fragment
     */
    public List<BaseFragment> getBaseFragments() {
        return mBaseFragments;
    }

    public static FragmentMaster getInstance() {
        if (mFragmentMaster == null) {
            synchronized (FragmentMaster.class) {
                if (mFragmentMaster == null) {
                    mFragmentMaster = new FragmentMaster();
                }
            }
        }
        return mFragmentMaster;
    }
    
    //添加一个Frag进入管理
    public void addFragToMaster(BaseFragment baseFragment) {
        mBaseFragments.add(baseFragment);
    }
    
    //删除一个Frag
    public void delFragFromMaster(BaseFragment baseFragment) {
        mBaseFragments.remove(baseFragment);
    }
    
    //获取指定的某一个Frag
    public BaseFragment getFrag(Class BaseFragmentClass) {
        for (BaseFragment baseFragment : mBaseFragments) {
            if (baseFragment.getClass() == BaseFragmentClass) {
                return baseFragment;
            }
        }
        throw new RuntimeException("NO Fragment found!!");
    }
    
    //销毁全部的Frag
    public void killAll() {
        Iterator<BaseFragment> iterator = mBaseFragments.iterator();
        while (iterator.hasNext()) {
            iterator.next().killSelf();
        }
    }
    
    //销毁某一个的Frag
    public void killOne(Class BaseFragmentClass) {
        Iterator<BaseFragment> iterator = mBaseFragments.iterator();
        while (iterator.hasNext()) {
            BaseFragment baseFragment = iterator.next();
            if (baseFragment.getClass() == BaseFragmentClass) {
                baseFragment.killSelf();
            }
            
        }
    }
}
