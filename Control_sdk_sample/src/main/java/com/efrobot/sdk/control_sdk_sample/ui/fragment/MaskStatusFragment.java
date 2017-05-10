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
public class MaskStatusFragment extends BaseFragment implements View.OnClickListener, RobotManager.OnMaskStatusChangedListener {
    private TextView mTvHeadState;
    private Button mBtRegisterMaskStatusChangedListener;
    private Button mBtUnRegisterMaskStatusChangedListener;
    private RobotManager mRobotManager;
    public static final String TAG = "MaskStatusFragment";
    public static final int MASK_STATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MASK_STATE:
                    RobotStatus.MaskStatus maskStatus = (RobotStatus.MaskStatus) msg.obj;
                    mTvHeadState.setText("maskStatus: " + maskStatus.name() + "：：" + maskStatus.ordinal());
                    break;
                case 2:

                    break;
            }
        }
    };
    @Override
    public int setRootView() {
        return R.layout.fragment_mask_status;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mTvHeadState = (TextView) mRootView.findViewById(R.id.tv_headState);
        mBtRegisterMaskStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_registerMaskStatusChangedListener);
        mBtUnRegisterMaskStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_unRegisterMaskStatusChangedListener);
    }

    @Override
    public void initData() {
        mBtRegisterMaskStatusChangedListener.setOnClickListener(this);
        mBtUnRegisterMaskStatusChangedListener.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_registerMaskStatusChangedListener:
                mRobotManager.registerMaskStatusChangedListener(this);
                break;
            case R.id.bt_unRegisterMaskStatusChangedListener:
                mRobotManager.unRegisterMaskStatusChangedListener(this);
                break;
        }
    }

    @Override
    public void onMaskStatusChanged(RobotStatus.MaskStatus maskStatus) {
        Log.e(TAG, "maskStatus: " + maskStatus.name() + "：：" + maskStatus.ordinal());
        Message message = new Message();
        message.what = MASK_STATE;
        message.obj = maskStatus;
        mHandler.sendMessage(message);
    }
}
