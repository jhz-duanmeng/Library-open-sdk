package com.efrobot.sdk.control_sdk_sample.ui.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.efrobot.library.RobotManager;
import com.efrobot.library.RobotStatus;
import com.efrobot.sdk.control_sdk_sample.R;
import com.efrobot.sdk.control_sdk_sample.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalverStatusFragment extends BaseFragment implements View.OnClickListener, RobotManager.OnSalverStatusChangedListener {

    private TextView mTvSalverState;
    private Button mBtRegisterSalverStatusChangedListener;
    private Button mBtUnRegisterSalverStatusChangedListener;
    private RobotManager mRobotManager;
    public static final String TAG = "SalverStatusFragment";
    public static final int SALVER_STATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SALVER_STATE:
                    RobotStatus.SalverStatus salverStatus = (RobotStatus.SalverStatus) msg.obj;
                    mTvSalverState.setText("salverStatus: " + salverStatus.name() + "：：" + salverStatus.ordinal());
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    public int setRootView() {
        return R.layout.fragment_salver_status;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mTvSalverState = (TextView) mRootView.findViewById(R.id.tv_SalverState);
        mBtRegisterSalverStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_registerSalverStatusChangedListener);
        mBtUnRegisterSalverStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_unRegisterSalverStatusChangedListener);
    }

    @Override
    public void initData() {
        mBtRegisterSalverStatusChangedListener.setOnClickListener(this);
        mBtUnRegisterSalverStatusChangedListener.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_registerSalverStatusChangedListener:
                mRobotManager.registerSalverStatusChangedListener(this);
                break;
            case R.id.bt_unRegisterSalverStatusChangedListener:
                mRobotManager.unRegisterSalverStatusChangedListener(this);
                break;
        }
    }

    @Override
    public void onSalverStatusChanged(RobotStatus.SalverStatus salverStatus) {
        Log.e(TAG, "salverStatus: " + salverStatus.name() + "：：" + salverStatus.ordinal());
        Message message = new Message();
        message.what = SALVER_STATE;
        message.obj = salverStatus;
        mHandler.sendMessage(message);
    }
}
