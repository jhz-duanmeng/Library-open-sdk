package com.efrobot.library.task;

import com.efrobot.library.RobotManager;

import org.json.JSONException;
import org.json.JSONObject;


public class ControlManager extends BaseTask {

    private static ControlManager mInstance;

    private int taskType = TASK_TYPE_TASK_CONTROL;
    protected ControlManager(RobotManager mRobotManager) {
        super(mRobotManager);
    }


    public static synchronized ControlManager getInstance(RobotManager mRobotManager) {
        if(mInstance == null)
            mInstance = new ControlManager(mRobotManager);
        return mInstance;
    }

    /**
     * 设置灯带亮度
     * @param brightness 灯带亮度（最小值为 0，最大值为 255）
     */
    public void setLightBeltBrightness(int brightness) {
        try {
            JSONObject lightBeltJson = new JSONObject();
            lightBeltJson.put("brightness", brightness);
            JSONObject mJsonObject = new JSONObject();
            mJsonObject.put("light_belt",lightBeltJson);
            executeTask(mJsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public int getTaskType() {
        return taskType;
    }
}
