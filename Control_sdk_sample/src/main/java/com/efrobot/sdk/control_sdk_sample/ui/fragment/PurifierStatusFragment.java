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
public class PurifierStatusFragment extends BaseFragment implements View.OnClickListener, RobotManager.OnPurifierStatusChangedListener {
    private TextView mTvPurifierState;
    private Button mBtRegisterPurifierStatusChangedListener;
    private Button mBtUnRegisterPurifierStatusChangedListener;

    private RobotManager mRobotManager;
    public static final String TAG = "PurifierStatusFragment";
    public static final int PURIFIER_STATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PURIFIER_STATE:
                    RobotStatus.PurifierStatus purifierStatus = (RobotStatus.PurifierStatus) msg.obj;
                    mTvPurifierState.setText("purifierStatus: " + purifierStatus.name() + "：：" + purifierStatus.ordinal());
                    break;
                case 2:

                    break;
            }
        }
    };

    @Override
    public int setRootView() {
        return R.layout.fragment_purifier_status;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mTvPurifierState = (TextView) mRootView.findViewById(R.id.tv_PurifierState);
        mBtRegisterPurifierStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_registerPurifierStatusChangedListener);
        mBtUnRegisterPurifierStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_unRegisterPurifierStatusChangedListener);

    }

    @Override
    public void initData() {
        mBtRegisterPurifierStatusChangedListener.setOnClickListener(this);
        mBtUnRegisterPurifierStatusChangedListener.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_registerPurifierStatusChangedListener:
                mRobotManager.registerPurifierStatusChangedListener(this);
                break;
            case R.id.bt_unRegisterPurifierStatusChangedListener:
                mRobotManager.unRegisterPurifierStatusChangedListener(this);
                break;
        }
    }

    @Override
    public void onPurifierStatusChanged(RobotStatus.PurifierStatus purifierStatus) {
        Log.e(TAG, "purifierStatus: " + purifierStatus.name() + "：：" + purifierStatus.ordinal());
        Message message = new Message();
        message.what = PURIFIER_STATE;
        message.obj = purifierStatus;
        mHandler.sendMessage(message);
    }
}
