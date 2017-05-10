package com.efrobot.library.model;

/**
 * Created by Lzhen on 2016/11/30.
 */
public class RobotVersion {
    private String id;
    private String mark; //名称
    private String channel; //路径

    public RobotVersion() {
    }

    public RobotVersion(String id, String mark, String channel) {
        this.id = id;
        this.mark = mark;
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
