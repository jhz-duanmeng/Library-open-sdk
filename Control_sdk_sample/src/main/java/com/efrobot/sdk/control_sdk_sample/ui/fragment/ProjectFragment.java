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
public class ProjectFragment extends BaseFragment implements View.OnClickListener, RobotManager.OnProjectStatusChangedListener {

    private TextView mTvBatteryState;
    private Button mBtRegisterProjectorStatusChangedListener;
    private Button mBtUnRegisterProjectorStatusChangedListener;

    private RobotManager mRobotManager;
    public static final String TAG = "ProjectFragment";
    public static final int PROJECT_STATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROJECT_STATE:
                    RobotStatus.ProjectStatus projectStatus = (RobotStatus.ProjectStatus) msg.obj;
                    mTvBatteryState.setText("projectStatus: " + projectStatus.name() + "：：" + projectStatus.ordinal());
                    break;
                case 2:

                    break;
            }
        }
    };


    @Override
    public int setRootView() {
        return R.layout.fragment_project;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mTvBatteryState = (TextView) mRootView.findViewById(R.id.tv_BatteryState);
        mBtRegisterProjectorStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_registerProjectorStatusChangedListener);
        mBtUnRegisterProjectorStatusChangedListener = (Button) mRootView.findViewById(R.id.bt_unRegisterProjectorStatusChangedListener);
    }

    @Override
    public void initData() {
        mBtRegisterProjectorStatusChangedListener.setOnClickListener(this);
        mBtUnRegisterProjectorStatusChangedListener.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_registerProjectorStatusChangedListener:
                mRobotManager.registerProjectorStatusChangedListener(this);
                break;
            case R.id.bt_unRegisterProjectorStatusChangedListener:
                mRobotManager.unRegisterProjectorStatusChangedListener(this);
                break;
        }

    }

    @Override
    public void onProjectStatusChanged(RobotStatus.ProjectStatus projectStatus) {
        Log.e(TAG, "batteryStatus: " + projectStatus.name() + "：：" + projectStatus.ordinal());
        Message message = new Message();
        message.what = PROJECT_STATE;
        message.obj = projectStatus;
        mHandler.sendMessage(message);
    }
}
