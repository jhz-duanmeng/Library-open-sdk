package com.efrobot.library.task;


import android.util.Log;

import com.efrobot.library.RobotManager;

import org.json.JSONException;
import org.json.JSONObject;

public class WingManager extends BaseTask implements ITask {
    //双翅膀运动
    public final static int MOVE_WING = 0;
    //左翅膀运动
    public final static int MOVE_LEFT_WING = 1;
    //右翅膀运动
    public final static int MOVE_RIGHT_WING = 2;

    private final static int ANGLE_MAX = 120;
    private final static int ANGLE_MIN = 0;

    private static WingManager mInstance;

    protected WingManager(RobotManager mRobotManager) {
        super(mRobotManager);
    }

    public synchronized static WingManager getInstance(RobotManager mRobotManager) {
        if (mInstance == null)
            mInstance = new WingManager(mRobotManager);
        return mInstance;
    }

    /**
     * 翅膀以默认速度向上运动
     *
     * @param movePart 运动部位{@link #MOVE_WING},{@link #MOVE_LEFT_WING},{@link #MOVE_RIGHT_WING}
     */
    public void moveUp(int movePart) {
        executeTask(movePart, "move", ANGLE_MAX, null);
    }

    /**
     * 翅膀以指定的速度向上运动
     */
    public void moveUp(int movePart, int speed) {
        executeTask(movePart, "move", ANGLE_MAX, String.valueOf(speed));
    }

    /**
     * 翅膀以默认速度向下运动（运动到最小角度 0度）
     */
    public void moveDown(int movePart) {
        executeTask(movePart, "move", ANGLE_MIN, null);
    }

    /**
     * 翅膀以指定的速度向下运动（运动到最小角度 0度）
     */
    public void moveDown(int movePart, int speed) {
        executeTask(movePart, "move", ANGLE_MIN, String.valueOf(speed));
    }


    /**
     * 翅膀以默认速度向指定角度运动
     */
    public void moveToByAngle(int movePart, int angle) {
        executeTask(movePart, "move", angle, null);
    }

    /**
     * 翅膀以指定的速度向指定角度运动
     */
    public void moveToByAngle(int movePart, int angle, int speed) {
        executeTask(movePart, "move", angle, String.valueOf(speed));
    }

    /**
     * 翅膀停止运动
     */
    public void stop(int movePart) {
        executeTask(movePart, "stop", 0, null);
    }

    public void query() {
        executeTask(MOVE_WING, "query", 0, null);
    }

    private void executeTask(int movePart, String direction, int angle, String speed) {
        try {
            String moveStr = getMoveStr(movePart);
            if (moveStr == null) {
                Log.e("WingTask", "movePart 参数非法");
            }
            JSONObject wingJson = new JSONObject();
            wingJson.put("direction", direction);
            if (!"stop".equals(direction) || !"query".equals(direction)) {
                if (speed != null)
                    wingJson.put("speed", speed);
                wingJson.put("angle", angle);
            }

            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put(moveStr, wingJson);

            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 双翅以指定速度 角度运动
     *
     * @param leftWingAngle  左翅角度
     * @param leftWingSpeed  左翅速度
     * @param rightWingAngle 右翅角度
     * @param rightWingSpeed 右翅速度
     */
    public void setDoubleWingMoveWay(int leftWingAngle, String leftWingSpeed, int rightWingAngle, String rightWingSpeed) {
        Log.e("WingTask","Double Wing Task Params :"+leftWingAngle+" "+leftWingSpeed+" "+rightWingAngle+" "+rightWingSpeed);
        executeDoubleWingTask("move", leftWingAngle, leftWingSpeed, rightWingAngle, rightWingSpeed);
    }

    /**
     * 双翅任务
     * @param direction
     * @param leftWingAngle
     * @param leftWingSpeed
     * @param rightWingAngle
     * @param rightWingSpeed
     */
    private void executeDoubleWingTask(String direction, int leftWingAngle, String leftWingSpeed, int rightWingAngle, String rightWingSpeed) {
        String moveStr = getMoveStr(MOVE_WING);
        if (moveStr == null) {
            Log.e("WingTask", "movePart 双翅运动非法");
        }
        try {
            JSONObject wingJson = new JSONObject();
            wingJson.put("direction", direction);
            if (!"stop".equals(direction) || !"query".equals(direction)) {
                if (leftWingSpeed != null)
                    if (!leftWingSpeed.equals("")) {
                        Log.e("WingTask","WingTask---->put leftWing data");
                        wingJson.put("left_speed", leftWingSpeed);
                        wingJson.put("left_angle",leftWingAngle);
                    }
                if (rightWingSpeed != null)
                    if (!rightWingSpeed.equals("")) {
                        wingJson.put("right_speed", rightWingSpeed);
                        wingJson.put("right_angle",rightWingAngle);
                    }
            }
            JSONObject mJSONObject = new JSONObject();
            mJSONObject.put(moveStr, wingJson);
            executeTask(mJSONObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getMoveStr(int movePart) {
        switch (movePart) {
            case MOVE_WING:
                return "wing";
            case MOVE_LEFT_WING:
                return "left_wing";
            case MOVE_RIGHT_WING:
                return "right_wing";
        }
        return null;
    }


    @Override
    public int getTaskType() {
        return TASK_TYPE_TASK;
    }
}
