package com.efrobot.speechlibrary;

/**
 * Created by lhy on 2016/10/10
 *
 * @Link
 * @description
 */
 interface IManager {
    boolean registListener(String packgeName);
    boolean unregistListener(String packgeName);
    void initData(String key,String str);
    void deleteIns(String packageName,String ins);
    boolean openSpeechDiscern(String packageName);
    boolean closeSpeechDiscern(String packageName);
    void removeSpeechState(int type);
}