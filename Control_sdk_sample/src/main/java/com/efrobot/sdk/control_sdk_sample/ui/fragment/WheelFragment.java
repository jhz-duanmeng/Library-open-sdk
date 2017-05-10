package com.efrobot.sdk.control_sdk_sample.ui.fragment;


import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.efrobot.library.RobotManager;
import com.efrobot.library.RobotStatus;
import com.efrobot.sdk.control_sdk_sample.R;
import com.efrobot.sdk.control_sdk_sample.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class WheelFragment extends BaseFragment implements View.OnClickListener, RobotManager.OnHeadTouchStatusChangedListener {

    private Button mBtWheelMoveFront;
    private Button mBtWheelMoveFrontBySpeed;
    private Button mBtWheelMoveBack;
    private Button mBtWheelMoveBackBySpeed;
    private Button mBtWheelMoveLeft;
    private Button mBtWheelMoveLeftBySpeed;
    private Button mBtWheelMoveRight;
    private Button mBtWheelMoveRightAllBySpeed;
    private Button mBtStopWheelMove;

    public static final int WHEEL_MOVE_FRONT = 0;
    public static final int WHEEL_MOVE_FRONT_BYSPEED = 1;
    public static final int WHEEL_MOVE_BACK = 2;
    public static final int WHEEL_MOVE_BACK_BYSPEED = 3;
    public static final int WHEEL_MOVE_LEFT = 4;
    public static final int WHEEL_MOVE_LEFT_BYSPEED = 5;
    public static final int WHEEL_MOVE_RIGHT = 6;
    public static final int WHEEL_MOVE_RIGHT_ALL_BYSPEED = 7;


    private RobotManager mRobotManager;
    public static final String TAG = "WingFragment";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHEEL_MOVE_FRONT:
                    try {
                        mRobotManager.getWheelInstance().moveFront();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_FRONT, 1000);
                    break;
                case WHEEL_MOVE_FRONT_BYSPEED:
                    try {
                        mRobotManager.getWheelInstance().moveFront(39);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_FRONT_BYSPEED, 1000);
                    break;
                case WHEEL_MOVE_BACK:
                    try {
                        mRobotManager.getWheelInstance().moveBack();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_BACK, 1000);
                    break;
                case WHEEL_MOVE_BACK_BYSPEED:
                    try {
                        mRobotManager.getWheelInstance().moveBack(39);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_BACK_BYSPEED, 1000);
                    break;
                case WHEEL_MOVE_LEFT:
                    try {
                        mRobotManager.getWheelInstance().moveLeft();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_LEFT, 1000);
                    break;
                case WHEEL_MOVE_LEFT_BYSPEED:
                    try {
                        mRobotManager.getWheelInstance().moveLeft(39);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_LEFT_BYSPEED, 1000);
                    break;
                case WHEEL_MOVE_RIGHT:
                    try {
                        mRobotManager.getWheelInstance().moveRight();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_RIGHT, 1000);
                    break;
                case WHEEL_MOVE_RIGHT_ALL_BYSPEED:
                    try {
                        mRobotManager.getWheelInstance().moveRight(39);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_RIGHT_ALL_BYSPEED, 1000);
                    break;
            }
        }
    };

    @Override
    public int setRootView() {
        return R.layout.fragment_wheelfragment;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }


    @Override
    public void initViews() {
        mRobotManager= RobotManager.getInstance(mActivity);
        mBtWheelMoveFront = (Button) mRootView.findViewById(R.id.bt_wheelMoveFront);
        mBtWheelMoveFrontBySpeed = (Button) mRootView.findViewById(R.id.bt_wheelMoveFrontBySpeed);
        mBtWheelMoveBack = (Button) mRootView.findViewById(R.id.bt_wheelMoveBack);
        mBtWheelMoveBackBySpeed = (Button) mRootView.findViewById(R.id.bt_wheelMoveBackBySpeed);
        mBtWheelMoveLeft = (Button) mRootView.findViewById(R.id.bt_wheelMoveLeft);
        mBtWheelMoveLeftBySpeed = (Button) mRootView.findViewById(R.id.bt_wheelMoveLeftBySpeed);
        mBtWheelMoveRight = (Button) mRootView.findViewById(R.id.bt_wheelMoveRight);
        mBtWheelMoveRightAllBySpeed = (Button) mRootView.findViewById(R.id.bt_wheelMoveRightAllBySpeed);
        mBtStopWheelMove = (Button) mRootView.findViewById(R.id.bt_stopWheelMove);
        mRobotManager.registerOnHeadTouchStatusChangedListener(this);
    }

    @Override
    public void initData() {
        mBtWheelMoveFront.setOnClickListener(this);
        mBtWheelMoveFrontBySpeed.setOnClickListener(this);
        mBtWheelMoveBack.setOnClickListener(this);
        mBtWheelMoveBackBySpeed.setOnClickListener(this);
        mBtWheelMoveLeft.setOnClickListener(this);
        mBtWheelMoveLeftBySpeed.setOnClickListener(this);
        mBtWheelMoveRight.setOnClickListener(this);
        mBtWheelMoveRightAllBySpeed.setOnClickListener(this);
        mBtStopWheelMove.setOnClickListener(this);

    }

    private void clearHandler() {
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View view) {
        clearHandler();
        switch (view.getId()) {
            case R.id.bt_wheelMoveFront:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_FRONT, 1000);
                break;
            case R.id.bt_wheelMoveFrontBySpeed:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_FRONT_BYSPEED, 1000);
                break;
            case R.id.bt_wheelMoveBack:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_BACK, 1000);
                break;
            case R.id.bt_wheelMoveBackBySpeed:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_BACK_BYSPEED, 1000);
                break;
            case R.id.bt_wheelMoveLeft:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_LEFT, 1000);
                break;
            case R.id.bt_wheelMoveLeftBySpeed:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_LEFT_BYSPEED, 1000);
                break;

            case R.id.bt_wheelMoveRight:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_RIGHT, 1000);
                break;

            case R.id.bt_wheelMoveRightAllBySpeed:
                mHandler.sendEmptyMessageDelayed(WHEEL_MOVE_RIGHT_ALL_BYSPEED, 1000);
                break;
            case R.id.bt_stopWheelMove:
                try {
                    mRobotManager.getWheelInstance().stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onHeadTouchStatusChanged(RobotStatus.HeadTouchStatus headTouchStatus) {
        Log.e(TAG, "当前头部状态是" + headTouchStatus.name() + ": " + headTouchStatus.ordinal());
        if (headTouchStatus.ordinal() == 1 || headTouchStatus.ordinal() == 2) {
            Log.e(TAG, "我这执行了");
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
