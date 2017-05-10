package com.efrobot.sdk.control_sdk_sample.ui.fragment;


import android.support.v4.app.Fragment;
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
public class IDFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTvFragId;
    private Button btGetRobotIdFragId;
    private RobotManager mRobotManager;
    private TextView mTvFragSpeak;
    private Button mBtSpeak;




    @Override
    public int setRootView() {
        return R.layout.fragment_id;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mTvFragId = (TextView) mRootView.findViewById(R.id.tv_frag_id);
        btGetRobotIdFragId = (Button) mRootView.findViewById(R.id.bt_getRobotId_frag_id);
        mTvFragSpeak = (TextView) mRootView.findViewById(R.id.tv_frag_speak);
        mBtSpeak = (Button) mRootView.findViewById(R.id.bt_speak);
        mBtSpeak.setOnClickListener(this);
    }

    @Override
    public void initData() {
        btGetRobotIdFragId.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_getRobotId_frag_id:
                String robotId = RobotStatus.getInstance(mActivity).getRobotId();
                mTvFragId.setText("robotId: " + robotId);
                break;
            case R.id.bt_speak:
                String text="我是进化者机器人公司制造的小胖";
                mRobotManager.executeSpeak(text);

        }
    }
}
