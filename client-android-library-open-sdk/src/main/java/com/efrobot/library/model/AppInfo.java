package com.efrobot.library.model;

/**
 * app信息
 * Created by superking on 2016/12/6.
 */
public class AppInfo {
    private String appPackage;
    private String appName;
    private int status;
    private int type;

    public AppInfo() {
    }

    public AppInfo(String appPackage, String appName, int status, int type) {
        this.appPackage = appPackage;
        this.appName = appName;
        this.status = status;
        this.type = type;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
