package com.efrobot.sdk.control_sdk_sample.ui.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.efrobot.library.RobotManager;
import com.efrobot.sdk.control_sdk_sample.R;
import com.efrobot.sdk.control_sdk_sample.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class LightBeltFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtRegisterSalverStatusChangedListener;
    private Button mBtUnRegisterSalverStatusChangedListener;

    private RobotManager mRobotManager;
    public static final String TAG = "LightBeltFragment";


    @Override
    public int setRootView() {
        return R.layout.fragment_light_belt;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mBtRegisterSalverStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_setLightBeltBrightness250);
        mBtUnRegisterSalverStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_setLightBeltBrightness0);
    }

    @Override
    public void initData() {
        mBtRegisterSalverStatusChangedListener.setOnClickListener(this);
        mBtUnRegisterSalverStatusChangedListener.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_setLightBeltBrightness250:
                try {
                    mRobotManager.getControlInstance().setLightBeltBrightness(250);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_setLightBeltBrightness0:
                try {
                    mRobotManager.getControlInstance().setLightBeltBrightness(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
