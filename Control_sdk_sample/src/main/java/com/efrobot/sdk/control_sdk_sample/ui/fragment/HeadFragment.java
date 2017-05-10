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
public class HeadFragment extends BaseFragment implements View.OnClickListener, RobotManager.OnHeadTouchStatusChangedListener {

    private TextView mTvHeadState;
    private Button mBtRegisterOnHeadTouchStatus;
    private Button mBtUnregisterOnHeadTouchStatus;
    private Button mBtHeadMoveLeft;
    private Button mBtHeadMoveLeftBySpeed;
    private Button mBtHeadMoveRight;
    private Button mBtHeadMoveRightBySpeed;
    private Button mBtHeadMoveToByAngle;
    private Button mBtHeadMoveToByAngleAndSpeed;
    private Button mBtStopHeadMove;
    private RobotManager mRobotManager;
    public static final String TAG = "HeadFragment";
    public static final int HEAD_STATE = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HEAD_STATE:
                    RobotStatus.HeadTouchStatus headTouchStatus = (RobotStatus.HeadTouchStatus) msg.obj;
                    mTvHeadState.setText("headTouchStatus: " + headTouchStatus.name() + "：：" + headTouchStatus.ordinal());
                    break;
            }
        }
    };

    @Override
    public int setRootView() {
        return R.layout.fragment_head;
    }

    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mTvHeadState = (TextView) mRootView.findViewById(R.id.tv_headState);
        mBtRegisterOnHeadTouchStatus = (Button) mRootView.findViewById(R.id.bt_registerOnHeadTouchStatus);
        mBtUnregisterOnHeadTouchStatus = (Button) mRootView.findViewById(R.id.bt_unregisterOnHeadTouchStatus);
        mBtHeadMoveLeft = (Button) mRootView.findViewById(R.id.bt_headMoveLeft);
        mBtHeadMoveLeftBySpeed = (Button) mRootView.findViewById(R.id.bt_headMoveLeftBySpeed);
        mBtHeadMoveRight = (Button) mRootView.findViewById(R.id.bt_headMoveRight);
        mBtHeadMoveRightBySpeed = (Button) mRootView.findViewById(R.id.bt_headMoveRightBySpeed);
        mBtHeadMoveToByAngle = (Button) mRootView.findViewById(R.id.bt_headMoveToByAngle);
        mBtHeadMoveToByAngleAndSpeed = (Button) mRootView.findViewById(R.id.bt_headMoveToByAngleAndSpeed);
        mBtStopHeadMove = (Button) mRootView.findViewById(R.id.bt_stopHeadMove);
    }

    @Override
    public void initData() {
        mBtRegisterOnHeadTouchStatus.setOnClickListener(this);
        mBtUnregisterOnHeadTouchStatus.setOnClickListener(this);
        mBtHeadMoveLeft.setOnClickListener(this);
        mBtHeadMoveLeftBySpeed.setOnClickListener(this);
        mBtHeadMoveRight.setOnClickListener(this);
        mBtHeadMoveRightBySpeed.setOnClickListener(this);
        mBtHeadMoveToByAngle.setOnClickListener(this);
        mBtHeadMoveToByAngleAndSpeed.setOnClickListener(this);
        mBtStopHeadMove.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_registerOnHeadTouchStatus:
                mRobotManager.registerOnHeadTouchStatusChangedListener(this);
                break;
            case R.id.bt_unregisterOnHeadTouchStatus:
                mRobotManager.unRegisterOnHeadTouchStatusChangedListener(this);
                break;
            case R.id.bt_headMoveLeft:
                try {
                    mRobotManager.getHeadInstance().moveLeft();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_headMoveLeftBySpeed:
                try {
                    mRobotManager.getHeadInstance().moveLeft(25);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_headMoveRight:
                try {
                    mRobotManager.getHeadInstance().moveRight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_headMoveRightBySpeed:
                try {
                    mRobotManager.getHeadInstance().moveRight(25);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_headMoveToByAngle:
                try {
                    mRobotManager.getHeadInstance().moveToByAngle(39);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_headMoveToByAngleAndSpeed:
                try {
                    mRobotManager.getHeadInstance().moveToByAngle(25,39);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.bt_stopHeadMove:
                try {
                    mRobotManager.getHeadInstance().stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onHeadTouchStatusChanged(RobotStatus.HeadTouchStatus headTouchStatus) {
        Log.e(TAG, "headTouchStatus: " + headTouchStatus.name() + "：：" + headTouchStatus.ordinal());
        Message message = new Message();
        message.what = HEAD_STATE;
        message.obj = headTouchStatus;
        mHandler.sendMessage(message);
    }
}
