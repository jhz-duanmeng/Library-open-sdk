package com.efrobot.library.model;

/**
 * 方言
 * Created by superking on 2016/11/24.
 */
public class RobotLanguage {
    private String id;
    private String name;
    private String language;

    public RobotLanguage() {
    }

    public RobotLanguage(String id, String name, String language) {
        this.id = id;
        this.name = name;
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
}
