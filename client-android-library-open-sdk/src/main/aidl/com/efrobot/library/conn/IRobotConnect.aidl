// IRobotConnect.aidl
package com.efrobot.library.conn;

import com.efrobot.library.conn.IRobotMessageListener;
import com.efrobot.library.conn.RobotMessage;

interface IRobotConnect {

    void executeTask(in String from, in RobotMessage robotMessage);

    RobotMessage invokeGetStatusMethod(in String from, in String targetClass, in String targetMethod);

    void addRobotMessageListener(in String from, in int index, in IRobotMessageListener listener);

    void removeRobotMessageListener(in String from, in int index);
}
