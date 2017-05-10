package com.efrobot.library.task;

import android.util.SparseArray;

import com.efrobot.library.RobotManager;

import org.json.JSONException;
import org.json.JSONObject;

public class WheelManager extends BaseTask {

    private final int defaultSpeed = 200;

    private static WheelManager mInstance;


    private WheelFeedBackCallBack mFeedBackCallBack;
    private SparseArray<WheelFeedBackCallBack> mAllFeedBackListeners = new SparseArray<WheelFeedBackCallBack>();//负责管理所有的双轮里程数反馈的监听

    protected WheelManager(RobotManager mRobotManager) {
        super(mRobotManager);
    }

    public static synchronized WheelManager getInstance(RobotManager mRobotManager) {
        if (mInstance == null)
            mInstance = new WheelManager(mRobotManager);
        return mInstance;
    }

    /**
     * 以默认速度向前运动
     */
    public void moveFront() {
        executeTask("front", null);
    }

    /**
     * 以指定的速度向前运动
     *
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void moveFront(int speed) {
        executeTask("front", String.valueOf(speed));
    }

    /**
     * 以默认速度向前运动指定的距离
     *
     * @param distance 运动的距离（单位：米）
     */
    public void moveToFrontByDistance(int distance) {
        executeDistanceTask("front_distance", null, distance);
    }

    /**
     * 以指定的速度向前运动指定的距离
     *
     * @param distance 运动的距离（单位：米）
     * @param speed    运动速度（最小为0， 最大为400）
     */
    public void moveToFrontByDistance(int distance, int speed) {
        executeDistanceTask("front_distance", String.valueOf(speed), distance);
    }

    /**
     * 以默认速度向后运动
     */
    public void moveBack() {
        executeTask("back", null);
    }

    /**
     * 以指定的速度向后运动
     *
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void moveBack(int speed) {
        executeTask("back", String.valueOf(speed));
    }

    /**
     * 以默认速度向后运动指定的距离
     *
     * @param distance 运动的距离（单位：米）
     */
    public void moveToBackByDistance(int distance) {
        executeDistanceTask("back_distance", null, distance);
    }

    /**
     * 以指定的速度向后运动指定的距离
     *
     * @param distance 运动的距离（单位：米）
     * @param speed    运动速度（最小为0， 最大为400）
     */
    public void moveToBackByDistance(int distance, int speed) {
        executeDistanceTask("back_distance", String.valueOf(speed), distance);
    }

    /**
     * 以默认速度向左运动
     */
    public void moveLeft() {
        executeTask("left", null);
    }

    /**
     * 以指定的速度向左运动
     *
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void moveLeft(int speed) {
        executeTask("left", String.valueOf(speed));
    }

    /**
     * 以默认速度向左运动到指定的角度
     *
     * @param angle 运动的角度
     */
    public void moveToLeftByAngle(int angle) {
        executeAngleTask("left_angle", null, angle);
    }

    /**
     * 以指定的速度向左运动到指定的角度
     *
     * @param angle 运动的角度
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void moveToLeftByAngle(int speed, int angle) {
        executeAngleTask("left_angle", String.valueOf(speed), angle);
    }


    /**
     * 以默认速度向右运动
     */
    public void moveRight() {
        executeTask("right", null);
    }

    /**
     * 以默认速度向右运动到指定的角度
     *
     * @param angle 运动的角度
     */
    public void moveToRightByAngle(int angle) {
        executeAngleTask("right_angle", null, angle);
    }

    /**
     * 以指定的速度向右运动到指定的角度
     *
     * @param angle 运动的角度
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void moveToRightByAngle(int speed, int angle) {
        executeAngleTask("right_angle", String.valueOf(speed), angle);
    }


    /**
     * 以指定的速度向右运动
     *
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void moveRight(int speed) {
        executeTask("right", String.valueOf(speed));
    }

    /**
     * 双轮停止运动
     */
    public void stop() {
        executeTask("stop", null);
    }

    /**
     * 自定义右轮运动
     * @param direction 向前（2） 向后(0) 停止(1)
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void customRightWheel(int direction,int speed){
        executeSingleWheelTask("right_wheel", String.valueOf(direction), String.valueOf(speed));
    }

    /**
     * 自定义左轮运动
     * @param direction 向前（2） 向后(0) 停止(1)
     * @param speed 运动速度（最小为0， 最大为400）
     */
    public void customLeftWheel(int direction,int speed){
        executeSingleWheelTask("left_wheel",String.valueOf(direction),String.valueOf(speed));
    }



    public void getWheelFeedBackCodes(WheelFeedBackCallBack callBack) {
        if (callBack != null)
            mAllFeedBackListeners.append(mAllFeedBackListeners.size(), callBack);
    }

    private void executeDistanceTask(String direction, String speed, int distance) {
        try {
            JSONObject wheelJson = new JSONObject();
            wheelJson.put("direction", direction);
            wheelJson.put("distance", distance);
            if (speed != null)
                wheelJson.put("speed", speed);
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("wheel", wheelJson);

            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void executeAngleTask(String direction, String speed, int angle) {
        try {
            JSONObject wheelJson = new JSONObject();
            wheelJson.put("angle", angle);
            wheelJson.put("direction", direction);
            if (speed != null)
                wheelJson.put("speed", speed);
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("wheel", wheelJson);

            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executeSingleWheelTask(String wheel,String direction, String speed) {
        try {
            JSONObject wheelJson = new JSONObject();
            wheelJson.put("direction", direction);
            if (speed != null)
                wheelJson.put("speed", speed);
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put(wheel, wheelJson);
            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executeTask(String direction, String speed) {
        try {
            JSONObject wheelJson = new JSONObject();
            wheelJson.put("direction", direction);
            if (speed != null)
                wheelJson.put("speed", speed);
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("wheel", wheelJson);
            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    public void dispatchFeedBack(byte[] data) {
        if (mAllFeedBackListeners != null && mAllFeedBackListeners.size() > 0) {
            for (int i = 0; i < mAllFeedBackListeners.size(); i++) {
                //OnInfraredThresholdFeedBackCallBack feedBackListener = clone.valueAt(i);
                final WheelFeedBackCallBack feedBackListener = mAllFeedBackListeners.get(i);
                if (feedBackListener != null) {
                    feedBackListener.wheelFeedBack(data);
                }
            }
        }
    }

    public void unRegisterWheelFeedBack() {
        mAllFeedBackListeners.clear();
    }

    public interface WheelFeedBackCallBack {
        public void wheelFeedBack(byte[] data);
    }

    @Override
    public int getTaskType() {
        return TASK_TYPE_TASK;
    }
}
