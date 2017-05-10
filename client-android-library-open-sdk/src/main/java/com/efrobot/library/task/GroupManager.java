package com.efrobot.library.task;

import android.content.Context;

import com.efrobot.library.RobotManager;

import java.util.LinkedList;
import java.util.List;

/**
 * 脚本管理任务
 */
public class GroupManager extends BaseTask {

    private static GroupManager mInstance;

    private OnGroupTaskExecuteListener mNewOnGroupTaskExecuteListener;

    private List<OnGroupTaskExecuteListener> mOldOnGroupTaskExecuteListenerList = new LinkedList<OnGroupTaskExecuteListener>();

    private static Context mContext;
    protected GroupManager(RobotManager mRobotManager) {
        super(mRobotManager);
    }

    public static synchronized GroupManager getInstance(RobotManager mRobotManager, Context context) {
        mContext=context;
        if (mInstance == null)
            mInstance = new GroupManager(mRobotManager);
        return mInstance;
    }

    /**
     * 开始执行脚本
     * @param tasksContent 脚本内容
     */
    public void execute(String tasksContent) {
        execute(tasksContent, null);
    }

    /**
     * 开始执行脚本
     * @param tasksContent 脚本内容
     */
    public void execute(String tasksContent, OnGroupTaskExecuteListener mOnGroupTaskExecuteListener) {
        for (int i = 0; i < mOldOnGroupTaskExecuteListenerList.size(); i++) {
            mOldOnGroupTaskExecuteListenerList.get(i).onStop();
        }
        mOldOnGroupTaskExecuteListenerList.clear();

        if (mNewOnGroupTaskExecuteListener != null)
            mOldOnGroupTaskExecuteListenerList.add(mNewOnGroupTaskExecuteListener);

        this.mNewOnGroupTaskExecuteListener = mOnGroupTaskExecuteListener;
        executeTask(tasksContent);
    }

    /**
     * 停止脚本
     */
    public void stop() {
        executeTask("stop");
    }

    /**
     * 重置机器人（头部、双翅、灯带状态还原）
     */
    public void reset() {
        executeTask("reset");
    }

    @Override
    public int getTaskType() {
        return TASK_TYPE_TASK_GROUP;
    }

    public void dispatchListenerData(byte[] listenerData, String from) {
        byte groupListenerType = listenerData[0];
        if ((groupListenerType & 0xf) == 0) {
            String selfPackName = mContext.getPackageName();
            if (from.equals(selfPackName)) {

                for (int i = 0; i < mOldOnGroupTaskExecuteListenerList.size(); i++) {
                    mOldOnGroupTaskExecuteListenerList.get(i).onStop();
                }

                mOldOnGroupTaskExecuteListenerList.clear();

                if (mNewOnGroupTaskExecuteListener != null) {
                    mNewOnGroupTaskExecuteListener.onStart();
                    mOldOnGroupTaskExecuteListenerList.add(mNewOnGroupTaskExecuteListener);
                    mNewOnGroupTaskExecuteListener = null;
                }

            } else {

                for (int i = 0; i < mOldOnGroupTaskExecuteListenerList.size(); i++) {
                    mOldOnGroupTaskExecuteListenerList.get(i).onStop();
                }

                mOldOnGroupTaskExecuteListenerList.clear();
            }
        } else if ((groupListenerType & 0xf) == 1) {

            for (int i = 0; i < mOldOnGroupTaskExecuteListenerList.size(); i++) {
                mOldOnGroupTaskExecuteListenerList.get(i).onStop();
            }

            mOldOnGroupTaskExecuteListenerList.clear();
        }
    }

    public interface OnGroupTaskExecuteListener {
        void onStart();
        void onStop();
    }
}
