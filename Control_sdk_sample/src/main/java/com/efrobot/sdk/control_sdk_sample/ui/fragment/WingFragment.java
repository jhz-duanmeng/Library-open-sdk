package com.efrobot.sdk.control_sdk_sample.ui.fragment;


import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.efrobot.library.RobotManager;
import com.efrobot.library.task.WingManager;
import com.efrobot.sdk.control_sdk_sample.R;
import com.efrobot.sdk.control_sdk_sample.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WingFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTvWingState;
    private Button mBtWingMoveUp;
    private Button mBtWingMoveUpBySpeed;
    private Button mBtWingMoveDown;
    private Button mBtWingMoveDownBySpeed;
    private Button mBtWingMoveToByAngle;
    private Button mBtWingMoveToByAngleAndSpeed;
    private Button mBtStopWingMove;

    private RobotManager mRobotManager;
    public static final String TAG = "WingFragment";


    @Override
    public int setRootView() {
        return R.layout.fragment_wing;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mTvWingState = (TextView) mRootView.findViewById(R.id.tv_wingState);
        mBtWingMoveUp = (Button) mRootView.findViewById(R.id.bt_wingMoveUp);
        mBtWingMoveUpBySpeed = (Button) mRootView.findViewById(R.id.bt_wingMoveUpBySpeed);
        mBtWingMoveDown = (Button) mRootView.findViewById(R.id.bt_wingMoveDown);
        mBtWingMoveDownBySpeed = (Button) mRootView.findViewById(R.id.bt_wingMoveDownBySpeed);
        mBtWingMoveToByAngle = (Button) mRootView.findViewById(R.id.bt_wingMoveToByAngle);
        mBtWingMoveToByAngleAndSpeed = (Button) mRootView.findViewById(R.id.bt_wingMoveToByAngleAndSpeed);
        mBtStopWingMove = (Button) mRootView.findViewById(R.id.bt_stopWingMove);
    }

    @Override
    public void initData() {
        mBtWingMoveUp.setOnClickListener(this);
        mBtWingMoveUpBySpeed.setOnClickListener(this);
        mBtWingMoveDown.setOnClickListener(this);
        mBtWingMoveDownBySpeed.setOnClickListener(this);
        mBtWingMoveToByAngle.setOnClickListener(this);
        mBtWingMoveToByAngleAndSpeed.setOnClickListener(this);
        mBtStopWingMove.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_wingMoveUp:
                try {
                    mRobotManager.getWingInstance().moveUp(WingManager.MOVE_WING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_wingMoveUpBySpeed:
                try {
                    mRobotManager.getWingInstance().moveUp(WingManager.MOVE_WING,24);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_wingMoveDown:
                try {
                    mRobotManager.getWingInstance().moveDown(WingManager.MOVE_WING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_wingMoveDownBySpeed:
                try {
                    mRobotManager.getWingInstance().moveDown(WingManager.MOVE_WING,24);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_wingMoveToByAngle:
                try {
                    mRobotManager.getWingInstance().moveToByAngle(WingManager.MOVE_WING,45);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_wingMoveToByAngleAndSpeed:
                try {
                    mRobotManager.getWingInstance().moveToByAngle(WingManager.MOVE_WING,45,24);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_stopWingMove:
                try {
                    mRobotManager.getWingInstance().stop(WingManager.MOVE_WING);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
