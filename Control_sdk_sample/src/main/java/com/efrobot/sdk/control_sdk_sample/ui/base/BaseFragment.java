package com.efrobot.sdk.control_sdk_sample.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efrobot.sdk.control_sdk_sample.R;
import com.efrobot.sdk.control_sdk_sample.master.FragmentMaster;


/**
 * Created by Administrator on 2016/5/11.
 */
public abstract class BaseFragment extends Fragment {
    public String tag = "xx";
    public BaseActivity mActivity;
    public LayoutInflater mLayoutInflater;
    public View mRootView;
    public FragmentManager mFragmentManager;
    public FragmentMaster mFragmentMaster;
    private View mTitleBar, mTitleLeft, mTitleCenter, mTitleRight;
    private TextView mTvTitleLeft, mTvTitleCenter, mTvTitleRight;

    public BaseFragment() {

        tag = this.getClass().getSimpleName();
    }
    public View getRootView(int resLayout,ViewGroup container) {
        View view=mLayoutInflater.inflate(resLayout,container,false);
        return view;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mActivity = (BaseActivity) this.getActivity();
        mLayoutInflater = mActivity.getLayoutInflater();
        mFragmentManager = this.getFragmentManager();
        if (TitileBarConfig.useTitleBar) {
            mRootView = initTileBar(setRootView());

        } else {
            mRootView = mLayoutInflater.inflate(setRootView(), container, false);
        }
        //创建就自动被添加到fragmentmaster中
        mFragmentMaster = FragmentMaster.getInstance();
        mFragmentMaster.addFragToMaster(this);
        initViews();
        initData();
        return mRootView;
    }

    //设置布局
    public abstract int setRootView();

    //初始化布局View
    public abstract void initViews();

    //初始化布局数据
    public abstract void initData();

    //打吐司
    public void showToast(String text) {
        showToast(text, Toast.LENGTH_SHORT);
    }

    public void showToast(String text, int length) {
        Toast.makeText(mActivity, text, length).show();
    }

    //自杀
    public void killSelf() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.remove(this);
        fragmentTransaction.commit();
    }

    //跳转到其他Activity
    public void startActvity(Class activity, Bundle extra) {
        Intent intent = new Intent(mActivity, activity);
        intent.putExtra("data", extra);
        mActivity.startActivity(intent);
    }

    //启动服务
    public void startService(Class service, Bundle extra) {
        Intent intent = new Intent(mActivity, service);
        intent.putExtra("data", extra);
        mActivity.startService(intent);
    }

    //发送广播
    public void sendBroadcast(Class receiver, Bundle extra) {
        Intent intent = new Intent(mActivity, receiver);
        intent.putExtra("data", extra);
        mActivity.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁的时候自动从fragmentmaster中移除
        mFragmentMaster.delFragFromMaster(this);
    }

    /**
     * 初始化titlebar
     *
     * @param layoutResID
     * @return
     */
    private View initTileBar(int layoutResID) {
        LinearLayout relativeLayout = new LinearLayout(mActivity);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);
        relativeLayout.setOrientation(LinearLayout.VERTICAL);
        mTitleBar = mLayoutInflater.inflate(TitileBarConfig.getTitleBarRootLayout(), relativeLayout, false);
        mTitleBar.setVisibility(View.GONE);
        try {
            mTitleLeft = mTitleBar.findViewById(R.id.title_left);
        } catch (Exception e) {
            Log.d(tag,"NO LEFT ADDED");
        }

        try {
            mTvTitleLeft = (TextView) mTitleBar.findViewById(R.id.tv_title_left);
        } catch (Exception e) {
            Log.d(tag,"NO LEFT TV ADDED");
        }

        try {
            mTitleCenter = mTitleBar.findViewById(R.id.title_center);

        } catch (Exception e) {
            Log.d(tag,"NO CENTER ADDED");
        }

        try {
            mTvTitleCenter = (TextView) mTitleBar.findViewById(R.id.tv_title_center);
        } catch (Exception e) {
            Log.d(tag,"NO CENTER TV ADDED");
        }
        try {
            mTitleRight = mTitleBar.findViewById(R.id.title_right);

        } catch (Exception e) {
            Log.d(tag,"NO RIGHT ADDED");
        }
        try {
            mTvTitleRight = (TextView) mTitleBar.findViewById(R.id.tv_title_right);
        } catch (Exception e) {
            Log.d(tag,"NO RIGHT TV ADDED");
        }

        View rootView = mLayoutInflater.inflate(layoutResID, relativeLayout, false);
        relativeLayout.addView(mTitleBar);
        relativeLayout.addView(rootView);
        mTitleBar.requestFocus();
        return relativeLayout;
    }

    public void setTitleLeft(String text, View.OnClickListener onClickListener) {
        mTitleLeft.setVisibility(View.VISIBLE);
        if (onClickListener != null && mTitleLeft != null) {
            mTitleLeft.setOnClickListener(onClickListener);
        }
        if (text != null && mTvTitleLeft != null) {
            mTvTitleLeft.setText(text);
            if (onClickListener != null && mTitleLeft == null) {
                mTvTitleLeft.setOnClickListener(onClickListener);
            }
        }

    }

    public void setTitleCenter(String text, View.OnClickListener onClickListener) {
        mTitleBar.setVisibility(View.VISIBLE);

        if (onClickListener != null && mTitleCenter != null) {
            mTitleCenter.setOnClickListener(onClickListener);
        }
        if (text != null && mTvTitleCenter != null) {
            mTvTitleCenter.setText(text);
            if (onClickListener != null && mTitleCenter == null) {
                mTvTitleCenter.setOnClickListener(onClickListener);
            }
        }
    }

    public void setTitleRight(String text, View.OnClickListener onClickListener) {
        mTitleRight.setVisibility(View.VISIBLE);

        if (onClickListener != null && mTitleRight != null) {
            mTitleRight.setOnClickListener(onClickListener);
        }
        if (text != null && mTvTitleRight != null) {
            mTvTitleRight.setText(text);
            if (onClickListener != null && mTitleRight == null) {
                mTvTitleRight.setOnClickListener(onClickListener);
            }
        }
    }
    //封装3部分标题栏----------


    //对于fragment跳转的操作-------------------
    public void addFragment(int des, BaseFragment desFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_in, R.anim.start_out);//add frag 的进场动画
        fragmentTransaction.add(des, desFragment, desFragment.tag);
        fragmentTransaction.addToBackStack(desFragment.tag);
        fragmentTransaction.commit();
    }

    public void removeFragment(BaseFragment desFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.back_in, R.anim.back_out);//remove frag 的出场动画
        fragmentTransaction.remove(desFragment);
        fragmentTransaction.addToBackStack(desFragment.tag);
        fragmentTransaction.commit();
    }

    public void replaceFragment(int des, BaseFragment desFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_in, R.anim.start_out);
        fragmentTransaction.replace(des, desFragment);
        fragmentTransaction.addToBackStack(desFragment.tag);
        fragmentTransaction.commit();
    }

    public void showFragment(BaseFragment desFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_in, R.anim.start_out);
        fragmentTransaction.show(desFragment);
        fragmentTransaction.commit();
    }

    public void hideFragment(BaseFragment desFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.back_in, R.anim.back_out);
        fragmentTransaction.hide(desFragment);
        fragmentTransaction.commit();
    }
    //对于fragment操作结束


}
