package com.efrobot.library.model;

/**
 * 表情
 * Created by superking on 2016/11/17.
 */
public class RobotFace {
    private String id;
    private String name; //名称
    private String path; //路径

    public RobotFace() {
    }

    public RobotFace(String id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
