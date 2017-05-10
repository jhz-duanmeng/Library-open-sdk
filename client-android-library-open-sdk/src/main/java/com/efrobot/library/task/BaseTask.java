package com.efrobot.library.task;


import com.efrobot.library.RobotManager;

public abstract class BaseTask implements ITask{

    private final static String TAG = "BaseTask";

    protected RobotManager mRobotManager;

    protected BaseTask(RobotManager mRobotManager){
        this.mRobotManager = mRobotManager;
    }

    protected boolean check(String orderContent){
        if(mRobotManager == null) {
            return false;
        }
        if(orderContent == null || orderContent.trim().length() == 0) {
            return false;
        }
        return true;
    }

    protected void executeTask (String generateTaskJson) {
        if(check( generateTaskJson)) {
            mRobotManager.execute(getTaskType(), generateTaskJson);
        }
    }

}
