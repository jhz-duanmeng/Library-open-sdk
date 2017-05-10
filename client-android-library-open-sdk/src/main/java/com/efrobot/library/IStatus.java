package com.efrobot.library;


import com.efrobot.library.client.Local;
import com.efrobot.library.client.StatusClientFactory;
import com.efrobot.library.model.AppInfo;
import com.efrobot.library.model.RobotFace;
import com.efrobot.library.model.RobotIntonation;
import com.efrobot.library.model.RobotLanguage;
import com.efrobot.library.model.RobotVersion;
import com.efrobot.library.model.SafetyCheckModel;

import java.util.List;


public interface IStatus {


    /**
     * 获取所有表情
     *
     * @return 表情列表
     */

    @Local
    List<RobotFace> readRobotFaceListFromDB();

    /**
     * 新增一个表情
     *
     * @param face 表情
     */
    @Local
    void writeRobotFaceToDB(RobotFace face);

    /**
     * 获取当前使用的表情
     *
     * @return 表情
     */
    @Local
    RobotFace readRobotCurrentFaceFromDB();

    /**
     * 设置当前表情
     *
     * @param id 表情ID
     */
    @Local
    void writeRobotCurrentFaceToDB(String id);

    /**
     * 获取所有方言
     *
     * @return 方言列表
     */
    @Local
    List<RobotLanguage> readRobotLanguageFromDB();

    /**
     * 新增一条方言
     *
     * @param robotLanguage 新增方言
     */
    @Local
    void writeRobotLanguageToDB(RobotLanguage robotLanguage);

    /**
     * 获取当前使用的方言
     *
     * @return 当前方言
     */
    @Local
    RobotLanguage readRobotCurrentLanguageFromDB();

    /**
     * 设置当前使用的方言
     *
     * @param id 方言ID
     */
    @Local
    void writeRobotCurrentLanguageToDB(String id);

    /**
     * 获取设置信息
     *
     * @return 面罩状态开关 -1:未获取到
     */
    @Local
    int readRobotSettingFromDB();

    /**
     * 插入/更新 设置信息
     *
     * @param maskStateSwitch 面罩状态开关
     */
    @Local
    void writeRobotSettingToDB(int maskStateSwitch);

    /**
     * 获取当前使用的语调
     *
     * @return 当前语调
     */
    @Local
    RobotIntonation readRobotCurrentIntonationFromDB();

    /**
     * 设置当前使用的语调
     *
     * @param id 语调ID
     */
    @Local
    void writeRobotCurrentIntonationToDB(String id);

    /**
     * 获取所有语调
     *
     * @return 语调集合
     */
    @Local
    List<RobotIntonation> readRobotIntonationFromDB();

    /**
     * 插入 语调
     *
     * @param intonation 语调
     */
    @Local
    void writeRobotIntonationToDB(RobotIntonation intonation);

    @Local
    String readRobotNumberFormDB();

    @Local
    int readRobotMemberStateFormDB();

    @Local
    void writeRobotNameFormDB(String nickName);

    @Local
    long readCountDownTimeFormDB();

    @Local
    void writeCountDownTimeFormDB(long countDownTime);

    @Local
    String readRobotNameFormDB();

    /**
     * 获取机器人的版本
     */
    @Local
    int getRobotVersion();

    /**
     * 获取机器人号码
     *
     * @return 机器人号码，无号码返回null
     */
    @Local
    String getRobotId();


    /**
     * 获取机器人的本地昵称
     *
     * @return 机器人本地昵称，无昵称返回小胖
     */
    @Local
    String getRobotName();

    /**
     * 获取机器人会员状态
     *
     * @return 获取机器人会员状态
     */
    @Local
    int getRobotMemberState();

    /**
     * 读取数据，重新初始化MemberState
     */
    @Local
    void reRobotMemberState();

    /**
     * 读取数据，重新初始化robotName
     */
    @Local
    void reReadRobotName();


    /**
     * 获取机器人的会员倒计时时间
     *
     * @return 会员倒计时时间 （单位：毫秒）,负数为过期
     */
    @Local
    long getRobotMemberCountDownTime();

    /**
     * 设置机器人的会员倒计时时间
     *
     * @return 设置会员倒计时时间 （单位：毫秒）,负数为过期
     */
    @Local
    void setRobotMemberCountDownTime(long countDownTime);

    /**
     * 读取数据，重新初始化countDownTime
     */
    @Local
    void reRobotMemberCountDownTime();


    /**
     * 得到左轮发电机电量
     *
     * @return
     */
    int getLeftWheelMotorElectricity();

    void setLeftWheelMotorElectricity(int leftWheelMotorElectricity);

    /**
     * 得到右轮发电机电量
     *
     * @return
     */
    int getRightWheelMotorElectricity();

    void setRightWheelMotorElectricity(int rightWheelMotorElectricity);

    int getHeadMotorElectricity();

    void setHeadMotorElectricity(int headMotorElectricity);

    long getProjectorHotCloseTime();

    void setProjectorHotCloseTime(long projectorHotCloseTime);

    int getProjectorPrepareComplete();

    void setProjectorPrepareComplete(int projectorPrepareComplete);

    int getWingMotorElectricity();

    void setWingMotorElectricity(int wingMotorElectricity);

    int getPowerKeyState();

    void setPowerKeyState(int powerKeyState);

    int getSosKeyState();

    void setSosKeyState(int sosKeyState);


    int getProjectorTemperatureState();

    void setProjectorTemperatureState(int projectorTemperatureState);

    void setBatteryLevel(int batteryLevel);

    /**
     * 获取机器人的电池电量
     */
    int getBatteryLevel();


    int getBatteryState();

    void setBatteryState(int batteryState);

    /**
     * 获取机器人的电池电流
     */
    int getElectric();


    void setElectric(int electric);

    /**
     * 获取投影电压
     *
     * @return 投影电压（单位：mv）
     */
    int getProjectorPower();


    void setProjectorPower(int projectorPower);

    /**
     * 获取pad电压
     *
     * @return pad电压（单位：mv）
     */
    int getPadPower();

    void setPadPower(int padPower);

    /**
     * 获取电机（头部翅膀风机）电压
     *
     * @return 电机（头部翅膀风机）电压（单位：mv）
     */
    int getMotorPower();

    void setMotorPower(int motorPower);

    /**
     * 获取主板电压
     *
     * @return 主板电压（单位：mv）
     */
    int getMainBoardPower();

    void setMainBoardPower(int mainBoardPower);


    /**
     * 获取电池电压
     *
     * @return 电池电压（单位：mv）
     */
    int getBatteryVoltage();

    void setBatteryVoltage(int batteryVoltage);

    /**
     * 获取机器人Pm2.5传感器测量值
     *
     * @return Pm2.5传感器测量值
     */
    int getPm2_5();


    void setPm2_5(int pm2_5);


    /**
     * 获取机器人检测的温度值
     */
    int getTemperature();

    void setTemperature(int temperature);

    /**
     * 获取机器人检测的湿度
     */
    int getHumidity();


    void setHumidity(int humidity);


    /**
     * 获取机器人检测的异味
     */
    int getPeculiarSmell();


    void setPeculiarSmell(int peculiarSmell);


    int getHeadKeyState();

    void setHeadKeyState(int headKeyState);

    /**
     * 机器人地图状态
     */
    int getMapState();

    void setMapState(int mapState);

    /**
     * 机器人是否有地图
     *
     * @return hasMap 是否有地图
     */
    boolean hasMap();

    /**
     * 获取机器人头部的角度
     *
     * @return 头部的角度
     */
    int getHeadAngle();

    void setHeadAngle(int headAngle);

    int getLightBeltBrightness();

    void setLightBeltBrightness(int lightBeltBrightness);

    int getLeftWingAngle();

    void setLeftWingAngle(int leftWingAngle);

    int getRightWingAngle();

    void setRightWingAngle(int rightWingAngle);

    /**
     * 获取机器人头部面罩的开关状态
     *
     * @return 头部面罩的开关状态
     */
    int getMaskState();

    void setMaskState(int maskState);

    int getTrayState();

    void setTrayState(int trayState);


    /**
     * 获取机器人投影仪开关状态
     */
    int getProjectorState();


    void setProjectorState(int projectorState);

    /**
     * 获取机器人净化器开关状态
     */
    int getPurifierState();

    void setPurifierState(int purifierState);

    void setPurifierWorkTime(int purifierWorkTime);

    /**
     * 获取净化器的工作时长
     *
     * @return 净化器的工作时长（单位：小时），
     * 注意此数据只有调用重置方法才会被重置
     */
    int getPurifierWorkTime();

    int getHeadState();

    void setHeadState(int state);

    int getLeftWingState();

    void setLeftWingState(int state);

    int getRightWingState();

    void setRightWingState(int state);

    int getLeftWheelState();

    void setLeftWheelState(int state);

    int getRightWheelState();

    void setRightWheelState(int state);

    /**
     * 获取机器人导航状态
     */
    int getNavigationState();


    void setNavigationState(int navigationState);

    int getPurifierFanSpeed();

    void setPurifierFanSpeed(int purifierFanSpeed);

    int getLeftWheelSuspendState();

    void setLeftWheelSuspendState(int leftWheelSuspendState);

    int getRightWheelSuspendState();

    void setRightWheelSuspendState(int rightWheelSuspendState);

    int getBindChargingPile();

    void setBindChargingPile(int bindChargingPile);

    int getRobotLocation();

    void setRobotLocation(int robotLocation);

    int getHasConnectedPile();

    void setHasConnectedPile(int hasConnectedPile);

    /*  ============================     机器人APP状态       ===========================   */


    void setVideoService(int videoService);

    int getVideoService();

    long getLastVideoServiceCheck();

    void setLastVideoServiceCheck(long lastVideoServiceCheck);

    int getSpeechModel();

    void setSpeechModel(int speechModel);

    long getLastSpeechModelCheck();

    void setLastSpeechModelCheck(long lastSpeechModelCheck);

    int getSpeechModelTiming();

    void setSpeechModelTiming(int speechModelTiming);

    long getLastSpeechModelTimingCheck();

    void setLastSpeechModelTimingCheck(long lastSpeechModelTimingCheck);


    long getLastDanceStateCheck();

    void setLastDanceStateCheck(long lastDanceStateCheck);

    int getDanceState();

    void setDanceState(int danceState);

    void setLoseWayState(int loseWayState);

    /**
     * 获取地图构建状态
     *
     * @return
     */
    int getLoseWayState();

    long getLastLoseWayStateCheck();

    void setLastLoseWayStateCheck(long lastLoseWayStateCheck);

    int getBuildMapState();

    void setBuildMapState(int buildMapState);

    long getLastBuildMapStateCheck();

    void setLastBuildMapStateCheck(long lastBuildMapStateCheck);


    int getNavigationVoicePrompt();

    void setNavigationVoicePrompt(int navigationVoicePrompt);

    long getLastNavigationVoicePromptStateCheck();

    void setLastNavigationVoicePromptStateCheck(long lastNavigationVoicePromptCheck);

    String getArmVersion();

    String getUltSN();

    void setUltSN(String ultSN);

    String getMotorDriverSN();

    void setMotorDriverSN(String motorDriverSN);

    void setArmVersion(String armVersion);

    String getArmSN();

    void setArmSN(String armSN);

    String getChargingPileVersion();

    void setChargingPileVersion(String chargingPileVersion);

    String getUltVersion();

    void setUltVersion(String ultVersion);

    String getMotorDriverVersion();

    void setMotorDriverVersion(String motorDriverVersion);

    int getOldChargingPileVersion();

    void setOldChargingPileVersion(int oldChargingPileVersion);

    String getChargingPileSN();

    void setChargingPileSN(String chargingPileSN);

    int getChargingPileMonth();

    void setChargingPileMonth(int chargingPileMonth);

    int getChargingPileDate();

    void setChargingPileDate(int chargingPileDate);

    int getChargingPileNumber();

    void setChargingPileNumber(int chargingPileNumber);

    String getChargingPileId();

    void setChargingPileId(String chargingPileId);

    int getChargingPileFrequency();

    void setChargingPileFrequency(int chargingPileFrequency);

    int getChargingPileLimit();

    void setChargingPileLimit(int chargingPileLimit);

    int getChargingPileVoltage();

    void setChargingPileVoltage(int chargingPileVoltage);

    int getChargingPileElectric();

    void setChargingPileElectric(int chargingPileElectric);

    int getRobotStateWheelElectricity();

    void setRobotStateWheelElectricity(int robotStateWheelElectricity);

    void setNavigationScene(int scene);

    int getNavigationScene();

    void setLastNavigationSceneCheck(long lastNavigationSceneCheck);

    long getLastNavigationSceneCheck();

    String getLinuxSaveNumber();

    void setLinuxSaveNumber(String linuxSaveNumber);

    String getRobotDiagnosisCmd();

    void setRobotDiagnosisCmd(String robotDiagnosisCmd);

    int getRobotDiagnosisCmdOnOff();

    void setRobotDiagnosisCmdOnOff(int robotDiagnosisCmdOnOff);

    int getInfraredState();

    void setInfraredState(int infraredState);

    SafetyCheckModel getSafeModel();

    int getBootUpReason();

    void setBootUpReason(int bootUpReason);

    int getHeadActivePassiveState();

    void setHeadActivePassiveState(int state);

    int getLeftWingActivePassiveState();

    void setLeftWingActivePassiveState(int state);

    int getRightWingActivePassiveState();

    void setRightWingActivePassiveState(int state);

    int getLeftWheelActivePassiveState();

    void setLeftWheelActivePassiveState(int state);

    int getRightWheelActivePassiveState();

    void setRightWheelActivePassiveState(int state);

    int getProjectorState3dModel();

    void setProjectorState3dModel(int projectorState3dModel);

    int getProjectorGreenPlus();

    void setProjectorGreenPlus(int projectorGreenPlus);

    int getProjectorBluePlus();

    void setProjectorBluePlus(int projectorBluePlus);

    int getProjectorRedPlus();

    void setProjectorRedPlus(int projectorRedPlus);

    int getUpdateCurrentState();

    @Local
    boolean writeSettingWheelsFormDB(boolean isSwitch);

    @Local
    boolean readSettingWheelsFormDB();

    /**
     * 插入 app信息
     *
     * @param appInfo app信息
     * @return 是否成功
     */
    @Local
    boolean writeAppInfoToDB(AppInfo appInfo);
    /**
     * 通过包名删除app信息
     *
     * @param appPackage 包名
     * @return 是否成功
     */

    @Local
    boolean delAppInfoByPackage(String appPackage);
    void setUpdateCurrentState(int updateCurrentState);
    @Local
    boolean writeVersionLegalityFormDB(int state);
    @Local
    int readVersionLegalityFormDB();

    int getProjectorFactoryInfoAndVersion();

    void setProjectorFactoryInfoAndVersion(int projectorFactoryInfoAndVersion);

    @Local
    void enableProxy(StatusClientFactory factory);

    /**
     * 获取当前版本和渠道号
     *
     * @return 当前版本和渠道号
     */
    @Local
    RobotVersion readRobotVersionFromDB();

    @Local
    boolean updateAppInfoByPackage(AppInfo appInfo);

    @Local
    List<AppInfo> readAppInfoFromDB();

}
