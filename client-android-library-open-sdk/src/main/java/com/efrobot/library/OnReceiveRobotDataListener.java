package com.efrobot.library;


public interface OnReceiveRobotDataListener {
    void onReceiverData(int robotStateIndex, byte[] data);
}
