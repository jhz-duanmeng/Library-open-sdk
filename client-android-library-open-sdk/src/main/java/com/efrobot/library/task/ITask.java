package com.efrobot.library.task;


public interface ITask {
    int TASK_TYPE_TASK = 0;
    int TASK_TYPE_TASK_GROUP = 1;
    int TASK_TYPE_TASK_GROUP_DANCE = 2;
    int TASK_TYPE_TASK_CONTROL = 3;
    int TASK_TYPE_PROJECTOR = 4;
    int TASK_TYPE_NAVIGATION = 5;
    int TASK_TYPE_CHARGINGPILE = 6;
    int TASK_TYPE_CONTROL_DWA = 7;
    int TASK_TYPE_CUSTOM = 100;
    int TASK_TYPE_NOTIFY_ROBOT_STATE = 101;
    int TASK_TYPE_INFRARED = 102;//红外阈
    int TASK_TYPE_SPEECH=103;//语音任务

    int getTaskType();
}
