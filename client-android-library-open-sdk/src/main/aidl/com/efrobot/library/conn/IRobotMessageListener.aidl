// IRobotDataListener.aidl
package com.efrobot.library.conn;

import com.efrobot.library.conn.RobotMessage;

// 机器人的消息
interface IRobotMessageListener {

    void onReceiveRobotMessage(in RobotMessage robotMessage);
}
