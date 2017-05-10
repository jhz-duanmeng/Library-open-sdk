package com.efrobot.library.model;

/**
 * 语调
 * Created by superking on 2016/11/17.
 */
public class RobotIntonation {
    private String id;
    private String name;
    private String speed; //语速
    private String pitch; //音高
    private String soundEffect; //效果

    public RobotIntonation() {
    }

    public RobotIntonation(String id, String name, String speed, String pitch, String soundEffect) {
        this.id = id;
        this.name = name;
        this.speed = speed;
        this.pitch = pitch;
        this.soundEffect = soundEffect;
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

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getPitch() {
        return pitch;
    }

    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    public String getSoundEffect() {
        return soundEffect;
    }

    public void setSoundEffect(String soundEffect) {
        this.soundEffect = soundEffect;
    }
}
