package com.efrobot.library.model;

/**
 * 安全监测model
 */
public class SafetyCheckModel {

    public int electricity;        //电量百分比（低于5%）；0正常 1异常
    public int wholeElectricity;   //整机电流；0正常 1异常
    public int projector;          //投影仪；0正常 1异常
    public int pad;
    public int headWingMotor;      //头部、翅膀、风机；0正常 1异常
    public int mainboard;          //主板；0正常 1异常
    public int batteryPerformance; //电池性能；0正常 1异常
    public int doubleWeel;         //电池电量（双轮电机）；0正常 1异常
    public static volatile SafetyCheckModel instance;

    public static synchronized SafetyCheckModel getInstance() {
        if (instance == null) {
            synchronized (SafetyCheckModel.class) {
                if (instance == null) {
                    instance = new SafetyCheckModel();
                }
            }
        }
        return instance;
    }

    private SafetyCheckModel(){

    }
    public int getElectricity() {
        return electricity;
    }

    public void setElectricity(int electricity) {
        this.electricity = electricity;
    }

    public int getWholeElectricity() {
        return wholeElectricity;
    }

    public void setWholeElectricity(int wholeElectricity) {
        this.wholeElectricity = wholeElectricity;
    }

    public int getProjector() {
        return projector;
    }

    public void setProjector(int projector) {
        this.projector = projector;
    }

    public int getPad() {
        return pad;
    }

    public void setPad(int pad) {
        this.pad = pad;
    }

    public int getHeadWingMotor() {
        return headWingMotor;
    }

    public void setHeadWingMotor(int headWingMotor) {
        this.headWingMotor = headWingMotor;
    }

    public int getMainboard() {
        return mainboard;
    }

    public void setMainboard(int mainboard) {
        this.mainboard = mainboard;
    }

    public int getBatteryPerformance() {
        return batteryPerformance;
    }

    public void setBatteryPerformance(int batteryPerformance) {
        this.batteryPerformance = batteryPerformance;
    }

    public int getDoubleWeel() {
        return doubleWeel;
    }

    public void setDoubleWeel(int doubleWeel) {
        this.doubleWeel = doubleWeel;
    }
}
