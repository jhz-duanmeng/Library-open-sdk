package com.efrobot.sdk.control_sdk_sample.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efrobot.sdk.control_sdk_sample.R;
import com.efrobot.sdk.control_sdk_sample.master.ActivityMaster;


/**
 * Created by Administrator on 2016/5/11.
 */
public abstract class BaseActivity extends FragmentActivity {
    //封装自己，经常作为上下文用
    public BaseActivity mBaseActivity;
    public FragmentManager mFragmentManager;
    public LayoutInflater mLayoutInflater;
    public ActivityMaster mActivityMaster;
    private View mTitleBar, mTitleLeft, mTitleCenter, mTitleRight;
    private TextView mTvTitleLeft, mTvTitleCenter, mTvTitleRight;
    private long mTimeBegin;
    private boolean mSafeExit;
    private String mSafeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayoutInflater = this.getLayoutInflater();
        mBaseActivity = this;
        mFragmentManager = this.getSupportFragmentManager();
        mActivityMaster = ActivityMaster.getInstance();
        mActivityMaster.addActivityToMaster(this);
        setContentView(setRootView());
        initViews();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivityMaster.delActivityFromMaster(this);
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
        Toast.makeText(mBaseActivity, text, length).show();
    }

    //Activity自杀
    public void killSelf() {
        finish();
    }

    //跳转到其他Activity
    public void startActvity(Class activity, Bundle extra) {
        Intent intent = new Intent(mBaseActivity, activity);
        intent.putExtra("data", extra);
        startActivity(intent);
    }

    //跳转activity +进出场动画
    public void startActivityWithAnim(Class activity, Bundle extra) {
        Intent intent = new Intent(mBaseActivity, activity);
        intent.putExtra("data", extra);
        startActivity(intent);
        overridePendingTransition(R.anim.start_in, R.anim.start_out);//进出场动画
    }

    //获取数据
    public Bundle getData() {
        return this.getIntent().getBundleExtra("data");
    }

    //启动服务
    public void startService(Class service, Bundle extra) {
        Intent intent = new Intent(mBaseActivity, service);
        intent.putExtra("data", extra);
        startService(intent);
    }

    //发送广播
    public void sendBroadcast(Class receiver, Bundle extra) {
        Intent intent = new Intent(mBaseActivity, receiver);
        intent.putExtra("data", extra);
        sendBroadcast(intent);
    }

    //对于Fragment的操作---------
    public void addFragment(int des, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_in, R.anim.start_out);
        fragmentTransaction.add(des, fragment, fragment.tag);
        fragmentTransaction.commit();
    }

    public void removeFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_in, R.anim.start_out);
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public void replaceFragment(int des, BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_in, R.anim.start_out);
        fragmentTransaction.replace(des, fragment, fragment.tag);
        fragmentTransaction.commit();
    }

    public void hideFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.back_in, R.anim.back_out);
        fragmentTransaction.hide(fragment);
        fragmentTransaction.commit();
    }

    public void showFragment(BaseFragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.start_in, R.anim.start_out);
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();
    }

    //对于Fragment的操作---------


    //封装3部分标题栏----------
    @Override
    public void setContentView(int layoutResID) {
        //如果config中的布尔值为ture，就初始化标题栏
        if (TitileBarConfig.useTitleBar) {
            initTileBar(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    /**
     * 封装titlebar中间的部分
     *
     * @param layoutResID
     */
    private void initTileBar(int layoutResID) {
        //去标题
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置根布局rootView
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(linearLayout);
        //设置布局宽高
        ViewGroup.LayoutParams layoutParams = linearLayout.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        linearLayout.setLayoutParams(layoutParams);

        //我们自己set的titlebar的布局
        mTitleBar = mLayoutInflater.inflate(TitileBarConfig.getTitleBarRootLayout(), linearLayout, false);
        mTitleBar.setVisibility(View.GONE);

        try {
            mTitleLeft = mTitleBar.findViewById(R.id.title_left);
        } catch (Exception e) {
//            LogUtil.d("NO LEFT ADDED");
        }

        try {
            mTvTitleLeft = (TextView) mTitleBar.findViewById(R.id.tv_title_left);
        } catch (Exception e) {
//            LogUtil.d("NO LEFT TV ADDED");
        }

        try {
            mTitleCenter = mTitleBar.findViewById(R.id.title_center);

        } catch (Exception e) {
//            LogUtil.d("NO CENTER ADDED");
        }

        try {
            mTvTitleCenter = (TextView) mTitleBar.findViewById(R.id.tv_title_center);
        } catch (Exception e) {
//            LogUtil.d("NO CENTER TV ADDED");
        }
        try {
            mTitleRight = mTitleBar.findViewById(R.id.title_right);

        } catch (Exception e) {
//            LogUtil.d("NO RIGHT ADDED");
        }
        try {
            mTvTitleRight = (TextView) mTitleBar.findViewById(R.id.tv_title_right);
        } catch (Exception e) {
//            LogUtil.d("NO RIGHT TV ADDED");
        }

        //rootView就是xml中的布局
        View rootView = mLayoutInflater.inflate(layoutResID, linearLayout, false);
        //先添加标题栏，竖直线性布局，titlebar在上面
        linearLayout.addView(mTitleBar);
        linearLayout.addView(rootView);
        //焦点自动在标题栏上
        mTitleBar.requestFocus();
    }

    /**
     * 封装titlebar左边的部分
     *
     * @param text
     * @param onClickListener
     */
    public void setTitleLeft(String text, View.OnClickListener onClickListener) {
        mTitleBar.setVisibility(View.VISIBLE);
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

    /**
     * 封装titlebar右边的部分
     *
     * @param text
     * @param onClickListener
     */
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
        mTitleBar.setVisibility(View.VISIBLE);

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

    @Override
    public void onBackPressed() {
        //如果fragment stack中有2个以上fragment的时候是否先移除fragment
        if (mFragmentManager.getBackStackEntryCount() > 1) {
            mFragmentManager.popBackStack();
            return;
        }
        if (mSafeExit) {
            safeExit();
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.back_in, R.anim.back_out);//出场动画
    }

    public void setSafeExit(boolean useSafe, String text) {
        mSafeExit = useSafe;
        mSafeText = text;
    }

    private void safeExit() {
        long timeNow = System.currentTimeMillis();
        long cha = timeNow - mTimeBegin;
        if (cha >= 2000) {
            mTimeBegin = timeNow;
            Toast.makeText(mBaseActivity, mSafeText, Toast.LENGTH_SHORT).show();
            return;
        }
        mActivityMaster.killAll();
    }
}
