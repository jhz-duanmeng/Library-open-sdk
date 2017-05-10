package com.efrobot.library.task;

import com.efrobot.library.RobotManager;

import org.json.JSONException;
import org.json.JSONObject;

public class HeadManager extends BaseTask {


    private static HeadManager mInstance;

    protected HeadManager(RobotManager mRobotManager) {
        super(mRobotManager);
    }


    public synchronized static HeadManager getInstance(RobotManager mRobotManager) {
        if(mInstance == null)
            mInstance = new HeadManager(mRobotManager);
        return mInstance;
    }


    /**
     * 以默认速度移动到头部的最左端
     */
    public void moveLeft() {
        executeTask("left", null);
    }

    /**
     * 以指定的速度移动到头部的最左端
     * @param speed 移动速度（最大值为100，最小为0）
     */
    public void moveLeft(int speed) {
        executeTask("left", String.valueOf(speed));
    }

    /**
     * 以默认速度移动到头部的最右端
     */
    public void moveRight() {
        executeTask("right", null);
    }

    /**
     * 以指定的速度移动到头部的最右端
     * @param speed 移动速度（最大值为100，最小为0）
     */
    public void moveRight(int speed) {
        executeTask("right", String.valueOf(speed));
    }

    /**
     * 以指默认速度移动到头部指定的角度
     * @param angle 移动角度（机器人头部向左的最大角度为0度，中间为120度，最右边为240度）
     */
    public void moveToByAngle(int angle) {
        executePositionTask(angle, null);
    }

    /**
     * 以指指定的速度移动到头部指定的角度
     *  @param angle 移动角度（机器人头部向左的最大角度为0度，中间为120度，最右边为240度）
     *  @param speed 移动速度（最大值为100，最小为0）
     */
    public void moveToByAngle(int angle, int speed) {
        executePositionTask(angle, String.valueOf(speed));
    }


    /**
     *  停止头部运动
     */
    public void stop() {
        executeTask("stop", null);
    }

    /**
     * 查询头部角度
     */
    public void query() {
        executeTask("query", null);
    }

    private void executeTask(String direction, String speed) {
        try {
            JSONObject headJson = new JSONObject();
            headJson.put("direction", direction);
            if (speed != null)
                headJson.put("speed", speed);
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("head", headJson);
            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void executePositionTask(int angle,  String speed) {
        try {
            JSONObject headJson = new JSONObject();
            headJson.put("direction", "move");
            headJson.put("angle", angle);
            if (speed != null)
                headJson.put("speed", speed);
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put("head", headJson);

            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getTaskType() {
        return TASK_TYPE_TASK;
    }
}
