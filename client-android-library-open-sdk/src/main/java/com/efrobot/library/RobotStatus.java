package com.efrobot.library;


import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import com.efrobot.library.client.StatusClientFactory;
import com.efrobot.library.model.AppInfo;
import com.efrobot.library.model.RobotFace;
import com.efrobot.library.model.RobotIntonation;
import com.efrobot.library.model.RobotLanguage;
import com.efrobot.library.model.RobotVersion;
import com.efrobot.library.model.SafetyCheckModel;
import com.efrobot.library.utils.PreferencesUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RobotStatus implements IStatus{
    //services contentProvider authority
    public final static String SERVICES_PROVIDER_AUTHORITY = "com.efrobot.services.common";
    /*========================================获取机器人版本号==========================================*/

    /**
     * 家庭版
     */
    public final static int ROBOT_VERSION_FAMILY = 0;

    /**
     * 商务版
     */
    public final static int ROBOT_VERSION_BUSINESS = 1;

    /* ====================================== 净化器、投影仪状态 ======================================*/
    /**
     * 未知
     */
    public final static int UNKNOWN = -10000;


    /**
     * 错误
     */
    public final static int ERROR = -100000;


    /**
     * 打开状态
     */
    public final static int STATE_OPEN = 0;

    /**
     * 关闭状态
     */
    public final static int STATE_CLOSE = 1;

    /**
     * 投影机亮度调节
     */
    public final static int STATE_MONITOR_LIGHT = 4;

    /**
     * 不存在
     */
    public final static int STATE_INEXISTENCE = 2;

    /**
     * 故障
     */
    public final static int STATE_STOPPAGE = 3;

    /**
     * 净化器滤网不存在
     */
    public final static int STATE_FILTER_GAUZE_INEXISTENCE = 100;

    /* ====================================== 净化器、投影仪状态 ======================================*/



    /* TODO ====================================== 售后诊断软件状态 ======================================*/

    /**
     * 运动参数类型
     */
    public final static int ROBOT_DIAGNOSIS_CMD_STATE_INDEX_KEY = 127;

    public final static int ROBOT_DIAGNOSIS_CMD_ON_OFF_STATE_INDEX_KEY = 128;//开关

    /* ====================================== 各状态通知索引 ======================================*/
    /**
     * 头顶按钮
     */
    public final static int ROBOT_STATE_INDEX_HEAD_KEY = 0;

    /**
     * 头部面罩
     */
    public final static int ROBOT_STATE_INDEX_MASK = 1;

    /**
     * 托盘
     */
    public final static int ROBOT_STATE_INDEX_TRAY = 2;

    /**
     * 整机电流
     */

    public final static int ROBOT_STATE_INDEX_BATTERY_STATUS_ELECTRIC = 3;

    /**
     * 电池电量
     */
    public final static int ROBOT_STATE_INDEX_BATTERY_STATUS_LEVEL = 4;

    /**
     * 电池充放电
     */
    public final static int ROBOT_STATE_INDEX_BATTERY_STATUS_CHARGING = 5;

    /**
     * 传感器温度
     */
    public final static int ROBOT_STATE_INDEX_AIR_QUALITY_TEMPERATURE = 6;

    /**
     * 传感器湿度
     */
    public final static int ROBOT_STATE_INDEX_AIR_QUALITY_HUMIDITY = 7;

    /**
     * 传感器PM2.5
     */
    public final static int ROBOT_STATE_INDEX_AIR_QUALITY_PM2_5 = 8;

    /**
     * 传感器异味
     */
    public final static int ROBOT_STATE_INDEX_AIR_QUALITY_SMELL = 9;

    /**
     * 头部角度
     */
    public final static int ROBOT_STATE_INDEX_HEAD_ANGLE = 10;

    /**
     * 投影仪状态
     */
    public final static int ROBOT_STATE_INDEX_PROJECTOR_STATE = 11;

    /**
     * 净化器状态
     */
    public final static int ROBOT_STATE_INDEX_PURIFIER_STATE = 12;

    /**
     * 左翅膀角度
     */
    public final static int ROBOT_STATE_INDEX_LEFT_WING_ANGLE = 13;

    /**
     * 右翅膀角度
     */
    public final static int ROBOT_STATE_INDEX_RIGHT_WING_ANGLE = 14;

    /**
     * 头部状态（停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_HEAD = 15;

    /**
     * 左翅膀状态（停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_LEFT_WING = 16;

    /**
     * 右翅膀状态（停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_RIGHT_WING = 17;

    /**
     * 左轮（停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_LEFT_WHEEL = 18;

    /**
     * 右轮（停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_RIGHT_WHEEL = 19;

    /**
     * 自检
     */
    public final static int ROBOT_STATE_AUTO_CHECK = 20;

    /**
     * 投影仪电压
     */
    public final static int ROBOT_STATE_PROJECTOR_POWER = 21;

    /**
     * PAD电压
     */
    public final static int ROBOT_STATE_PAD_POWER = 22;

    /**
     * 头部、翅膀、风机电压
     */
    public final static int ROBOT_STATE_MOTOR_POWER = 23;

    /**
     * 主板电压
     */
    public final static int ROBOT_STATE_MAIN_BOARD_POWER = 24;

    /**
     * 电池电压
     */
    public final static int ROBOT_STATE_BATTERY_VOLTAGE = 25;

    /**
     * 左轮（主动/被动 停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_LEFT_WHEEL_ACTIVE_PASSIVE = 26;

    /**
     * 右轮（主动/被动 停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_RIGHT_WHEEL_ACTIVE_PASSIVE = 27;

    /**
     * 头部状态（主动/被动 停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_HEAD_ACTIVE_PASSIVE = 28;

    /**
     * 左翅膀状态（主动/被动 停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_LEFT_WING_ACTIVE_PASSIVE = 29;

    /**
     * 右翅膀状态（主动/被动 停止/运动）
     */
    public final static int ROBOT_STATE_INDEX_RIGHT_WING_ACTIVE_PASSIVE = 30;

    /**
     * SOS按钮
     */
    public final static int ROBOT_STATE_INDEX_ROBOT_SOS = 100;

    /**
     * 净化器工作时长
     */
    public final static int ROBOT_STATE_INDEX_PURIFIER_WORK_TIME = 101;

    /**
     * 动作组合状态监听
     */
    public final static int ROBOT_STATE_INDEX_GROUP_LISTENER = 102;

    /**
     * 遇到障碍物
     */
    public final static int ROBOT_STATE_INDEX_HAS_OBSTACLE = 104;

    /**
     * 净化器风机速度
     */
    public final static int ROBOT_STATE_INDEX_PURIFIER_FAN_SPEED = 105;

    /**
     * 左轮是否悬空状态
     */
    public final static int ROBOT_STATE_INDEX_LEFT_WHEEL_SUSPEND = 106;

    /**
     * 右轮是否悬空状态
     */
    public final static int ROBOT_STATE_INDEX_RIGHT_WHEEL_SUSPEND = 107;

    /**
     * 从下位机获取摄像头数据
     */
    public final static int ROBOT_DATA_INDEX_GET_CAMERA_PARAMS = 108;

    /**
     * 超声波
     */
    public final static int ROBOT_DATA_INDEX_GET_ULTRASONIC = 109;

    /**
     * 是否执行过接驳充电桩
     */
    public final static int ROBOT_STATE_INDEX_HAD_CONNECTED_PILE = 111;

    /**
     * 获取双轮参数
     */
    public final static int ROBOT_STATE_INDEX_GET_WHEEL_PARAMS = 112;

    /**
     * 获取双目参数
     */
    public final static int ROBOT_STATE_INDEX_GET_CAMERA_PARAMS = 113;

    /**
     * 获取红外参数
     */
    public final static int ROBOT_STATE_INDEX_GET_INFRARED_PARAMS = 114;

    /**
     * 获取机器人Id
     */
    public final static int ROBOT_STATE_INDEX_GET_ROBOT_ID = 150;

    public final static int ROBOT_STATE_INDEX_LEFT_WHEEL_MOTOR_ELECTRICITY = 115;
    public final static int ROBOT_STATE_INDEX_RIGHT_WHEEL_MOTOR_ELECTRICITY = 116;
    public final static int ROBOT_STATE_INDEX_HEAD_MOTOR_ELECTRICITY = 117;
    public final static int ROBOT_STATE_INDEX_WING_MOTOR_ELECTRICITY = 118;
    public final static int ROBOT_STATE_INDEX_WHEEL_FEED_BACK = 158;//双轮里程数的反馈
    /**
     * 获取绑定的充电桩
     */
    public final static int ROBOT_STATE_INDEX_GET_BIND_CHARGING_PILE = 119;

    /**
     * 电源按钮
     */
    public final static int ROBOT_STATE_INDEX_ROBOT_POWER_KEY = 120;


    public final static int ROBOT_STATE_INDEX_PURIFIER_SHUT_DOWN = 121;

    public final static int PURIFIER_SHUT_DOWN_TYPE_HEAD_KEY = 0;//摸头
    public final static int PURIFIER_SHUT_DOWN_TYPE_SOS_KEY = 1;//SOS按钮
    public final static int PURIFIER_SHUT_DOWN_TYPE_POWER_KEY = 2;//电源按钮
    public final static int PURIFIER_SHUT_DOWN_TYPE_CHARGING = 3;//充电
    public final static int PURIFIER_SHUT_DOWN_TYPE_SLEEP = 4;//休眠

    /*============================投影仪功能=========================================*/
    /**
     * 投影仪高温
     */
    public final static int PROJECTOR_HOT_STATE_INDEX_KEY = 122;

    public final static int PROJECTOR_STATE_TEMP_HOT = 1;//过温报警
    public final static int PROJECTOR_STATE_TEMP_NORMAL = 0;//常温状态
    public final static String PROJECTOR_HOT_CLOSE_PREFERENCES_KEY = "projector_hot_close_preferences_key";//投影仪过热关闭preferences_key
    public final static String PROJECTOR_CLOSE_PREFERENCES_KEY = "projector_close_preferences_key";//投影仪关闭preferences_key

    /**
     * 投影仪状态保存
     */
    public final static int PROJECTOR_PREPARE_COMPLETE_STATE_INDEX_KEY = 126;

    public final static int PROJECTOR_PREPARE_COMPLETE_STATE = 0;
    public final static int PROJECTOR_UNPREPARE_COMPLETE_STATE = 1;


    /**
     * 3D投影返回值
     */
    public final static int PROJECTOR_STATE_3D_MODEL_INDEX_KEY = 141;

    /**
     * 绿色增益返回值
     */
    public final static int PROJECTOR_STATE_GREEN_PLUS_INDEX_KEY = 142;

    /**
     * 蓝色增益返回值
     */
    public final static int PROJECTOR_STATE_BLUE_PLUS_INDEX_KEY = 143;

    /**
     * 红色增益返回值
     */
    public final static int PROJECTOR_STATE_RED_PLUS_INDEX_KEY = 144;

    /**
     * 接收厂家和版本信息
     */
    public final static int PROJECTOR_FACTORY_INFO_AND_VERSION = 145;


/* ====================================== 投影仪功能 ======================================*/


    /*=========================================================================*/
    /**
     * 版本更新当前状态
     */
    public final static int ROBOT_STATE_INDEX_VERSION_UPDATE_KEY = 130;


    /**
     * 数据传输中
     */
    public final static int UPDATE_STATE_DATA_TRANSMISSION = 1;


    /*============================红外参数=========================================*/
    public final static int ROBOT_DATA_INDEX_GET_INFRARED = 110;//获取红外初始化
    public final static int ROBOT_STATE_INDEX_INFRARED_GET_INFRAREDTHRESHOLD = 320;//获取红外阈值
    public final static int ROBOT_STATE_INDEX_INFRARED_GET_LABEL = 301;//获取红外初始化数据
    public final static int ROBOT_STATE_INDEX_INFRARED_INIT_DATA = 322;//红外初始化数据获取
    public final static int ROBOT_INFRARED_GET_BARRIER_FEEDBACK_PARAMS = 323;//获取红外壁障数据反馈

    public final static int ROBOT_STATE_INIT_INDEX_START = 500;
    public final static int ROBOT_STATE_INIT_INDEX_END = 501;


    /* ======================================安全监测索引 ======================================*/
    public final static int ROBOT_STATE_CHECK_SAFE_BATTERY = 600;//电池电量
    public final static int ROBOT_STATE_CHECK_SAFE_WHOLEMOTOR = 601;//整机电流
    public final static int ROBOT_STATE_CHECK_SAFE_PROJECTOR = 602;//投影仪
    public final static int ROBOT_STATE_CHECK_SAFE_PAD = 603;//PAD
    public final static int ROBOT_STATE_CHECK_SAFE_HEED_WHEEL_SYSTEM = 604;//头 翅膀 风机
    public final static int ROBOT_STATE_CHECK_SAFE_MAINBOARD = 605;//主板
    public final static int ROBOT_STATE_CHECK_SAFE_BATTERY_PERFORMANCE = 606;//电池性能
    public final static int ROBOT_STATE_CHECK_SAFE_DOUBLE_WHEEL_MOTOR = 607;//双轮电机

      /*   ===========================超声波板 电机驱动板===================================*/
    /**
     * 超声波 电机驱动板
     */

    public final static String ROBOT_STRING_UNKNOWN = "UNKNOWN";//获取为0则再重新获取
    public final static int ROBOT_STATE_INDEX_ARM_ID = 220;//ARM板Id
    public final static int ROBOT_STATE_INDEX_ARM_SN = 221;//ARM板SN
    public final static int ROBOT_STATE_INDEX_ULTRASONIC_VERSION = 222;//超声波板硬件版本号
    public final static int ROBOT_STATE_INDEX_ULTRASONIC_SN = 123;//超声波板SN号
    public final static int ROBOT_STATE_INDEX_MOTOR_DRIVER_VERSION = 124;//电机驱动板版本号
    public final static int ROBOT_STATE_INDEX_MOTOR_DRIVER_SN = 125;//电机驱动板SN号



    /* ====================================== 导航部分索引 ======================================*/

    /**
     * 导航模块数据
     */
    public final static int ROBOT_DATA_INDEX_NAVIGATION = 200;

    /**
     * 充电桩模块数据
     */
    public final static int ROBOT_DATA_INDEX_CHARGE_PILE = 201;

    /**
     * 导航状态(NAVIGATION_STATE_IDLE, NAVIGATION_STATE_NORMAL, NAVIGATION_STATE_CHARGING)
     */
    public final static int ROBOT_NAVIGATION_STATE_INDEX = 202;

    /**
     * 导航过程
     */
    public final static int ROBOT_NAVIGATION_CHANGE_INDEX = 203;

    /**
     * 地图状态索引
     */
    public final static int ROBOT_MAP_STATE_INDEX = 204;

    /**
     * 导航失败
     */
    public final static int ROBOT_NAVIGATION_FAIL_INDEX = 205;

    /**
     * 机器人定位（目前只用来判断是否在充电桩）
     */
    public final static int ROBOT_STATE_INDEX_LOCATION = 206;

    /* ====================================== 导航部分索引 ======================================*/

    /**
     * 在充电桩上
     */
    public final static int ROBOT_LOCATION_CHARGING_PILE = 0;

    /**
     * 正在出充电桩
     */
    public final static int ROBOT_LOCATION_OUT_CHARGING_PILE_DOING = 1;

    /**
     * 完成出充电桩
     */
    public final static int ROBOT_LOCATION_OUT_CHARGING_PILE_DONE = 2;

    /* ===================================充电桩==================================== */

    public final static int ROBOT_INDEX_CHARGING_PILE_VERSION = 131;//充电桩硬件版本号
    public final static int ROBOT_INDEX_OLD_CHARGING_PILE_VERSION = 139;//旧充电桩硬件版本号
    public final static int ROBOT_INDEX_CHARGING_PILE_SN = 132;//充电桩SN号
    public final static int ROBOT_INDEX_CHARGING_PILE_MONTH = 232;//充电桩月份
    public final static int ROBOT_INDEX_CHARGING_PILE_DATE = 133;//充电桩日期
    public final static int ROBOT_INDEX_CHARGING_PILE_NUMBER = 134;//充电桩编号
    public final static int ROBOT_INDEX_CHARGING_PILE_ID = 135;//充电桩Id号

    public final static int ROBOT_INDEX_CHARGING_PILE_LIMIT = 136;//限位开关状态
    public final static int ROBOT_INDEX_CHARGING_PILE_VOLTAGE = 137;//电压
    public final static int ROBOT_INDEX_CHARGING_PILE_ELECTRIC = 138;//电流


    /* ============================ 是否接触过充电桩 ============================ */

    public final static int ROBOT_STATE_HAD_NOT_CONNECTED_PILE = 0;
    public final static int ROBOT_STATE_HAD_CONNECTED_PILE = 1;

    /* ============================ 是否接触过充电桩 ============================ */

    /* ============================ APP视频部分索引 ============================ */

    /**
     * 视频服务（0 空闲，1 远程遥控，2 语音聊天，3 视频聊天）
     */
    public final static int ROBOT_STATE_INDEX_VIDEO_SERVICE = 300;

    public final static int VIDEO_SERVICE_STATE_IDLE = 0;
    public final static int VIDEO_SERVICE_STATE_REMOTE_CONTROL = 1;
    public final static int VIDEO_SERVICE_STATE_AUDIO = 2;
    public final static int VIDEO_SERVICE_STATE_VIDEO = 3;
    public final static int VIDEO_SERVICE_STATE_REMOTE_CONTROL_SOS = 4;


    /* ============================ APP视频部分索引 ============================ */


    /* ============================ APP语音模式部分索引 ============================ */

    public final static int ROBOT_STATE_INDEX_SPEECH_MODEL = 301;

    //唤醒模式
    public final static int ROBOT_SPEECH_MODEL_AWAKE = -4;
    //深睡眠模式
    public final static int ROBOT_SPEECH_MODEL_DEEP_SLEEP = -3;
    //浅睡眠模式
    public final static int ROBOT_SPEECH_MODEL_SHALLOW_SLEEP = -2;
    //语音空闲模式
    public final static int ROBOT_SPEECH_MODEL_DEFAULT = -1;
    //普通模式
    public final static int ROBOT_SPEECH_MODEL_NORMAL = 0;
    //睡眠模式(自主问答)
    public final static int ROBOT_SPEECH_MODEL_SLEEP = 1;
    //音乐模式(自主问答)
    public final static int ROBOT_SPEECH_MODEL_MUSIC = 2;
    //问答(自主问答)
    public final static int ROBOT_SPEECH_MODEL_QUESTION = 3;
    //相声(自主问答)
    public final static int ROBOT_SPEECH_MODEL_CROSS = 4;
    //小游戏(自主问答)
    public final static int ROBOT_SPEECH_MODEL_TURN = 5;
    //心理测试(自主问答)
    public final static int ROBOT_SPEECH_MODEL_PSYC = 6;
    //童话故事(自主问答)
    public final static int ROBOT_SPEECH_MODEL_FAIRY = 7;
    //笑话模式(自主问答)
    public final static int ROBOT_SPEECH_MODEL_SMILE = 8;
    //新闻模式(自主问答)
    public final static int ROBOT_SPEECH_MODEL_NEWS = 9;
    //音乐模式
    public final static int ROBOT_SPEECH_MODEL_AT_MUSIC = 10;
    //	笑话模式
    public final static int ROBOT_SPEECH_MODEL_AT_JOKE = 11;
    //	视频模式
    public final static int ROBOT_SPEECH_MODEL_AT_VIDEO = 12;
    //	童话模式
    public final static int ROBOT_SPEECH_MODEL_AT_FAIRY = 13;
    //	小游戏模式
    public final static int ROBOT_SPEECH_MODEL_AT_GAME = 14;
    //	百科全书模式
    public final static int ROBOT_SPEECH_MODEL_AT_ENCYCLOPAEDIA = 15;
    //	翻译模式
    public final static int ROBOT_SPEECH_MODEL_AT_TRANSLATION = 16;
    //	跳舞模式
    public final static int ROBOT_SPEECH_MODEL_AT_DANCE = 17;
    //	新闻模式
    public final static int ROBOT_SPEECH_MODEL_AT_NEWS = 18;
    //	相声模式
    public final static int ROBOT_SPEECH_MODEL_AT_CROSSTALK = 19;
    //	照相
    public final static int ROBOT_SPEECH_MODEL_AT_TAKE_PICTURE = 20;
    //	提醒模式
    public final static int ROBOT_SPEECH_MODEL_AT_SCHEDULE = 21;
    //	猜丁壳（拍头退出）（已屏蔽）//
    public final static int ROBOT_SPEECH_MODEL_AT_GUESS_ROSHAMBO = 22;
    //	导航模式
    public final static int ROBOT_SPEECH_MODEL_AT_NAVIGATION = 23;
    //	家庭影院模式
    public final static int ROBOT_SPEECH_MODEL_AT_FAMILYCINEMA = 24;
    //	Pm值
    public final static int ROBOT_SPEECH_MODEL_AT_PM = 27;
    //	异味值
    public final static int ROBOT_SPEECH_MODEL_AT_SMELL = 28;
    //	学习单词模式（已屏蔽）
    public final static int ROBOT_SPEECH_MODEL_AT_LEARN_WORD = 29;
    //	录像模式
    public final static int ROBOT_SPEECH_MODEL_AT_TAKE_VIDEO = 30;
    //	英语学习跟读
    public final static int ROBOT_SPEECH_MODEL_AT_FOLLOW = 31;
    //	小学英语233
    public final static int ROBOT_SPEECH_ROBOT_SPEECH_MODEL_233_TEACHING = 32;
    //	智能家眷导航模式
    public final static int ROBOT_SPEECH_MODEL_AT_HOUSE_NAVIGATION = 33;
    //  帮助模式
    public final static int ROBOT_SPEECH_MODEL_AT_HELP = 37;
    //超声波模式
    public final static int ROBOT_SPEECH_MODEL_AT_ULTRASONIC = 40;

    /* ============================ APP语音模式部分索引 ============================ */


    /* ============================ APP定时找你模式部分索引 ============================ */

    public final static int ROBOT_STATE_INDEX_SPEECH_MODEL_TIMING = 302;
    public final static int ROBOT_STATE_MODEL_TIMING_CLOSE = 0;
    public final static int ROBOT_STATE_MODEL_TIMING_OPEN = 1;

    /* ============================ APP定时找你模式部分索引 ============================ */


    /* ============================ APP跳舞部分索引 ============================ */
    public final static int ROBOT_STATE_INDEX_DANCE = 303;

    public final static int ROBOT_STATE_DANCE_STOP = 0;
    public final static int ROBOT_STATE_DANCE_DOING = 1;

    /* ============================ APP跳舞部分索引 ============================ */


    /* ============================ APP迷路部分索引 ============================ */
    public final static int ROBOT_STATE_INDEX_NAVIGATION_LOSE_WAY = 304;

    public final static int ROBOT_STATE_UN_NAVIGATION_LOSE_WAY = 0;
    public final static int ROBOT_STATE_NAVIGATION_LOSE_WAY = 1;

    /* ============================ APP迷路部分索引 ============================ */


    /* ============================ APP构建地图部分索引 ============================ */
    public final static int ROBOT_STATE_INDEX_BUILD_MAP = 310;

    public final static int ROBOT_STATE_UN_BUILD_MAP = 0;//未进入构建地图
    public final static int ROBOT_STATE_PRE_BUILD_MAP = 1;//进入地图但未构建地图
    public final static int ROBOT_STATE_BUILDING_MAP = 2;//进入地图并构建地图

    /* ============================ APP构建地图部分索引 ============================ */


    /* ====================================== 地图模块索引及状态 ======================================*/
    /**
     * 地图状态 未构建
     */
    public final static int MAP_STATE_UN_BUILD = 0;

    /**
     * 地图状态 构建中
     */
    public final static int MAP_STATE_BUILDING = 1;

    /**
     * 地图状态 构建完成
     */
    public final static int MAP_STATE_BUILD = 2;

    /* ====================================== 地图模块索引及状态 ======================================*/


    /* ============================ APP导航失败友好提示 ============================ */
    public final static int ROBOT_STATE_INDEX_NAVIGATION_VOICE_PROMPT = 307;


    /* ============================ APP导航失败友好提示 ============================ */


    /* ====================================== 头部触摸 ======================================*/
    /**
     * 抬起
     */
    public final static int HEADKEY_STATE_UP = 0;

    /**
     * 唤醒
     */
    public final static int HEADKEY_STATE_AWAKEN = 1;

    /**
     * 急停
     */
    public final static int HEADKEY_STATE_STOP = 2;

    /* ====================================== 头部触摸 ======================================*/

    /* ====================================== 电源按钮状态 ======================================*/
    /**
     * 按下
     */
    public final static int POWERKEY_STATE_DOWN = 0;
    /**
     * 抬起
     */
    public final static int POWERKEY_STATE_UP = 1;

    /* ====================================== 电源按钮状态 ======================================*/

    /* ====================================== SOS按钮状态 ======================================*/
    /**
     * 按下
     */
    public final static int SOSKEY_STATE_DOWN = 0;
    /**
     * 抬起
     */
    public final static int SOSKEY_STATE_UP = 1;

     /* ====================================== SOS按钮状态 ======================================*/


    /* ====================================== 面罩状态 ======================================*/
    /**
     * 面罩关闭
     */
    public final static int MASK_STATE_CLOSE = 0;

    /**
     * 面罩半开
     */
    public final static int MASK_STATE_MIDDLE = 1;

    /**
     * 面罩打开
     */
    public final static int MASK_STATE_OPEN = 2;

    /* ====================================== 面罩状态 ======================================*/


    /* ====================================== 托盘状态 ======================================*/

    /**
     * 托盘关闭
     */
    public final static int TRAY_STATE_CLOSE = 0;

    /**
     * 托盘打开
     */
    public final static int TRAY_STATE_OPEN = 1;

    /* ====================================== 托盘状态 ======================================*/

    /* ====================================== 投影仪状态 ======================================*/

    /**
     * 投影仪关闭
     */
    public final static int PROJECTOR_STATE_CLOSE = 0;

    /**
     * 投影仪打开
     */
    public final static int PROJECTOR_STATE_OPEN = 1;


    /* ====================================== 投影仪状态 ======================================*/

    /* ====================================== 双轮电流状态 ======================================*/

    public final static int ROBOT_STATE_WHEEL_INDEX_ELECTRICITY = 140;

    //未过流
    public final static int ROBOT_WHEEL_ELECTRICITY_NORMAL = 0;

    //代表过流
    public final static int ROBOT_WHEEL_ELECTRICITY_OVERCURRENT = 1;

    //清空过流状态
    public final static int ROBOT_WHEEL_ELECTRICITY_CLEAR = 2;

    /* ====================================== 双轮电流状态 ======================================*/

     /* ====================================== 电池状态 ======================================*/
    /**
     * 未充电
     */
    public final static int BATTERY_STATE_UN_CHARGING = 0;

    /**
     * 充电(线充)
     */
    public final static int BATTERY_STATE_CHARGING_LINE = 1;

    /**
     * 涓流充电(充电桩)
     */
    public final static int BATTERY_STATE_CHARGING_TRICKLE = 2;

    /**
     * 正常充电(充电桩)
     */
    public final static int BATTERY_STATE_CHARGING_NORMAL = 3;

    /**
     * 浮充(充电桩)
     */
    public final static int BATTERY_STATE_CHARGING_FLOATING = 4;


    /**
     * 充电完成(充电桩)
     */
    public final static int BATTERY_STATE_CHARGING_DONE = 5;

    /* ====================================== 电池状态 ======================================*/




    /* ====================================== 机器人电机状态 ======================================*/

    /**
     * 机器人电机状态（运动）
     */
    public final static int ROBOT_MOTOR_STATE_MOVING = 1;

    /**
     * 机器人电机状态（停止）
     */
    public final static int ROBOT_MOTOR_STATE_STOP = 0;

    /* ====================================== 机器人电机状态 ======================================*/


    /* ====================================== 导航状态 ======================================*/

    /**
     * 导航状态（闲置）
     */
    public static final int NAVIGATION_STATE_IDLE = 0;

    /**
     * 导航状态（普通目的地）
     */
    public static final int NAVIGATION_STATE_NORMAL = 1;

    /**
     * 导航状态（充电桩）
     */
    public static final int NAVIGATION_STATE_CHARGING = 2;

    /**
     * 导航状态（接驳）
     */
    public static final int NAVIGATION_STATE_CONNECT_CHARGING_PILE = 3;

    /**
     * 导航状态（出充电桩）
     */
    public static final int NAVIGATION_STATE_OUT_CHARGING_PILE = 4;

    /* ====================================== 导航状态 ======================================*/


    /* ====================================== 导航场景模块索引及状态 ======================================*/

    public final static int ROBOT_NAVIGATION_SCENE_INDEX = 309;
    /**
     * 自主巡航
     */
    public final static int ROBOT_NAVIGATION_SCENE_AUTO_CRUISE = 1;


    /* ====================================== 导航流程回调 ======================================*/

    /**
     * 开始导航
     */
    public final static int NAVIGATION_RESULT_START = 0;
    /**
     * 暂停导航
     */
    public final static int NAVIGATION_RESULT_PAUSE = 1;

    /**
     * 继续导航
     */
    public final static int NAVIGATION_RESULT_CONTINUE = 2;
    /**
     * 停止导航
     */
    public final static int NAVIGATION_RESULT_STOP = 3;

    /**
     * 导航成功
     */
    public final static int NAVIGATION_RESULT_SUCCESS = 4;

    /**
     * 导航失败
     */
    public final static int NAVIGATION_RESULT_FAIL = 5;

    /* ====================================== 导航流程回调 ======================================*/

    /* ====================================== 休眠结束上电原因 ======================================*/
    /**
     * 上位机上电原因
     */
    public final static int ROBOT_STATE_INDEX_PADPOWER_ON_REASON = 129;
    public final static int PADPOWER_ON_REASON_SLEEPOVERNORMAL = 1;//休眠正常结束
    public final static int PADPOWER_ON_REASON_POWERBUTTON = 2;//按电源键启动
    public final static int PADPOWER_ON_REASON_SOSBUTTON = 3;//按SOS键启动

    /* ====================================== 导航失败原因 ======================================*/

    //导航规划 起点不合法
    public final static int NAVIGATION_FAIL_REASON_START_POSITION_ILLEGAL = -1;
    //终点不合法
    public final static int NAVIGATION_FAIL_REASON_DESTINATION_POINT_ILLEGAL = -2;
    //规划路径
    public final static int NAVIGATION_FAIL_REASON_PLANNING_PATH = -3;
    //全局蔽障失败
    public final static int NAVIGATION_FAIL_REASON_OBSTACLE_AVOIDANCE = -4;
    //地图不存在
    public final static int NAVIGATION_FAIL_REASON_HAS_NOT_MAP = -5;
    //充电桩电极未抬起
    public final static int NAVIGATION_FAIL_REASON_ELECTRODE_NOT_RAISED = -15;
    //托盘打开，需要过门，拒绝导航
    public final static int NAVIGATION_FAIL_REASON_CAN_NOT_THROUGH_DOOR_BY_OPEN_TRY = -18;
    //托盘打开，规划路径失败
    public final static int NAVIGATION_FAIL_REASON_CAN_NOT_PLAN_PATH_BY_OPEN_TRY = -200;
    //检测原点，获取充电桩状态失败
    public final static int NAVIGATION_FAIL_REASON_CAN_NOT_GET_CHARGING_PILE_STATE = -201;
    //超声波标定失败
    public final static int NAVIGATION_FAIL_REASON_ULTRASONIC_CALIBRATION = -202;
    //试图到达可行区域失败
    public final static int NAVIGATION_FAIL_REASON_TRY_TO_FEASIBLE_REGION = -203;
    //尚未接驳不允许依赖地图导航
    public final static int NAVIGATION_FAIL_REASON_NOT_CONNECTED = -204;
    //检测原点，设置充电桩状态失败
    public final static int NAVIGATION_FAIL_REASON_SET_CHARGING_PILE_STATE = -205;
    //距离充电桩太近，不宜导航
    public final static int NAVIGATION_FAIL_REASON_DISTANCE_CHARGING_PILE_TOO_CLOSE = -206;


    /* ====================================== 导航流程回调 ======================================*/


    private static Context mContext;

    private static int robotVersion;
    private static String robotNumber;
    private static String robotName;
    private static long countDownTime;
    private static int memberState;


    private int batteryLevel;
    private int batteryState;
    private int electric;
    private int projectorPower;
    private int padPower;
    private int motorPower;
    private int mainBoardPower;
    private int batteryVoltage;


    private int pm2_5;
    private int temperature;
    private int humidity;
    private int peculiarSmell;

    private int headKeyState;
    private int maskState;
    private int trayState;
    private int projectorState;
    private int purifierState;
    private int navigationState;
    private int purifierFanSpeed;
    private int leftWheelSuspendState;
    private int rightWheelSuspendState;


    private int headAngle;
    private int leftWingAngle;
    private int rightWingAngle;
    private int lightBeltBrightness;
    private int purifierWorkTime;

    private int updateCurrentState;

    private int isUpdating;

    private int headState = ROBOT_MOTOR_STATE_STOP;
    private int leftWingState = ROBOT_MOTOR_STATE_STOP;
    private int rightWingState = ROBOT_MOTOR_STATE_STOP;
    private int leftWheelState = ROBOT_MOTOR_STATE_STOP;
    private int rightWheelState = ROBOT_MOTOR_STATE_STOP;

    private int headActivePassiveState = ROBOT_MOTOR_STATE_STOP;
    private int leftWingActivePassiveState = ROBOT_MOTOR_STATE_STOP;
    private int rightWingActivePassiveState = ROBOT_MOTOR_STATE_STOP;
    private int leftWheelActivePassiveState = ROBOT_MOTOR_STATE_STOP;
    private int rightWheelActivePassiveState = ROBOT_MOTOR_STATE_STOP;

    private int mapState = MAP_STATE_UN_BUILD;

    private int bindChargingPile;

    private int robotLocation;

    private int hasConnectedPile;


    private int videoService;
    private long lastVideoServiceCheck;

    private int speechModel;
    private long lastSpeechModelCheck;

    private int speechModelTiming;
    private long lastSpeechModelTimingCheck;


    private int danceState;
    private long lastDanceStateCheck;

    private int loseWayState;
    private long lastLoseWayStateCheck;

    private int buildMapState;
    private long lastBuildMapStateCheck;


    private int leftWheelMotorElectricity;
    private int rightWheelMotorElectricity;
    private int headMotorElectricity;
    private int wingMotorElectricity;


    //双轮电流保护
    private int robotStateWheelElectricity;
    //机器人ID
    private String linuxSaveNumber;
    //软件诊断
    private String robotDiagnosisCmd;//运动参数
    private int robotDiagnosisCmdOnOff;//开关

    //------------------------------超声波板 ARM  数据---------------------------------------
    private String armVersion;//ARM板Version
    private String armSN;//ARM板SN
    private String ultVersion;//超声波板硬件版本号
    private String ultSN;//超声波板SN号
    private String motorDriverVersion;//电动机驱动板版本号
    private String motorDriverSN;//电机驱动板SN号

    private String chargingPileVersion;//充电桩硬件版本号
    private int oldChargingPileVersion;//旧充电桩硬件版本号
    private String chargingPileSN;//充电桩SN号
    private int chargingPileMonth;//充电桩月份
    private int chargingPileDate;//充电桩日期
    private int chargingPileNumber;//充电桩编号
    private String chargingPileId;//充电桩id
    private int chargingPileFrequency;//充电桩频率


    private int chargingPileVoltage;//充电桩电压
    private int chargingPileElectric;//充电桩电流
    private int chargingPileLimit;//充电桩限位
    private int infraredState;//红外初始化的状态

    private int navigationVoicePrompt;

    private long lastNavigationVoicePromptCheck;
    private SafetyCheckModel safeModel;//安全检查模块


    private int sosKeyState;//sos按钮状态
    private int powerKeyState;//电源按钮状态

    private int projectorTemperatureState;//投影仪温度状态
    private long projectorHotCloseTime;//投影仪关闭时间

    private int projectorPrepareComplete;//投影仪状态

    private int projectorState3dModel;//3D返回值
    private int projectorGreenPlus;//绿色增益返回值
    private int projectorBluePlus;//蓝色增益返回值
    private int projectorRedPlus;//红色增益返回值
    private int projectorFactoryInfoAndVersion;//投影厂家信息和版本号

    public long getProjectorCloseTime() {
        return projectorCloseTime;
    }

    public void setProjectorCloseTime(long projectorCloseTime) {
        this.projectorCloseTime = projectorCloseTime;
    }

    private long projectorCloseTime;//保存投影仪关闭状态的变量

    /**
     * 会员倒计时状态未知，数据库未查询到
     */
    public static final long COUNTDOWN_UNKNOWN = Long.MIN_VALUE;
    /**
     * 会员金币不足，无倒计时
     */
    public static final long LACK_OF_GOLD = Long.MIN_VALUE + 1;


    /**
     * 会员状态过期
     */
    public static final int MEMBERSTATE_EXPIRE = 2;
    /**
     * 是会员
     */
    public static final int MEMBERSTATE_TRUE = 1;

    /**
     * 未开通会员
     */
    public static final int MEMBERSTATE_FALSE = 0;
    /**
     * 会员状态未知
     */
    public static final int MEMBERSTATE_UNKNOWN = -1;

    private int bootUpReason;

    private int navigationScene;
    private long lastNavigationSceneCheck;
    private static IStatus instance;
    private static IStatus proxyInstance;
    public int getUpdateCurrentState() {
        return updateCurrentState;
    }

    public void setUpdateCurrentState(int updateCurrentState) {
        this.updateCurrentState = updateCurrentState;
    }

    private RobotStatus() {
        this(null);
    }
    private RobotStatus(Context mContext) {
        this.mContext = mContext;
        setProjectorHotCloseTime(PreferencesUtils.getLong(mContext, PROJECTOR_HOT_CLOSE_PREFERENCES_KEY));
        setProjectorCloseTime(PreferencesUtils.getLong(mContext, PROJECTOR_CLOSE_PREFERENCES_KEY));
        try {
            String version = SystemProperties.get("ro.product.display", "unknow");
            if (!version.equals("unknow")) {
                String versionNumber = version.substring(3, 4);
                if (versionNumber.equals("0")) {
                    robotVersion = ROBOT_VERSION_FAMILY;
                } else if (versionNumber.equals("1")) {
                    robotVersion = ROBOT_VERSION_BUSINESS;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-6, "主人我没看到门啊，是不是在逗" + getRobotName() + " 玩啊。");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-16, "" + getRobotName() + " 是不是走迷糊了，好像不认识路了，我找找看");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-18, "主人，托盘打开," + getRobotName() + " 不能过门哟");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-201, "主人，" + getRobotName() + " 获取充电桩状态失败，请重新检测原点");
        robotNumber = readRobotNumberFormDB();
        robotName = readRobotNameFormDB();
        countDownTime = readCountDownTimeFormDB();
        memberState = readRobotMemberStateFormDB();
        batteryLevel = UNKNOWN;
        batteryState = UNKNOWN;
        electric = UNKNOWN;
        projectorPower = UNKNOWN;
        padPower = UNKNOWN;
        motorPower = UNKNOWN;
        mainBoardPower = UNKNOWN;
        batteryVoltage = UNKNOWN;

        pm2_5 = UNKNOWN;
        temperature = UNKNOWN;
        humidity = UNKNOWN;
        peculiarSmell = UNKNOWN;

        headKeyState = UNKNOWN;
        maskState = UNKNOWN;
        trayState = UNKNOWN;
        projectorState = UNKNOWN;
        purifierState = UNKNOWN;
        navigationState = NAVIGATION_STATE_IDLE;

        headAngle = UNKNOWN;
        leftWingAngle = UNKNOWN;
        rightWingAngle = UNKNOWN;
        lightBeltBrightness = UNKNOWN;
        purifierWorkTime = UNKNOWN;
        purifierFanSpeed = UNKNOWN;
        leftWheelSuspendState = UNKNOWN;
        rightWheelSuspendState = UNKNOWN;

        headState = ROBOT_MOTOR_STATE_STOP;
        leftWingState = ROBOT_MOTOR_STATE_STOP;
        leftWingState = ROBOT_MOTOR_STATE_STOP;
        rightWingState = ROBOT_MOTOR_STATE_STOP;
        leftWheelState = ROBOT_MOTOR_STATE_STOP;
        rightWheelState = ROBOT_MOTOR_STATE_STOP;

        bindChargingPile = UNKNOWN;
        robotLocation = UNKNOWN;
        hasConnectedPile = UNKNOWN;

        videoService = VIDEO_SERVICE_STATE_IDLE;
        speechModel = ROBOT_SPEECH_MODEL_DEFAULT;
        speechModelTiming = ROBOT_STATE_MODEL_TIMING_CLOSE;
        danceState = ROBOT_STATE_DANCE_STOP;
        loseWayState = ROBOT_STATE_UN_NAVIGATION_LOSE_WAY;
        buildMapState = ROBOT_STATE_UN_BUILD_MAP;
        robotStateWheelElectricity = ROBOT_WHEEL_ELECTRICITY_CLEAR;


        leftWheelMotorElectricity = UNKNOWN;
        rightWheelMotorElectricity = UNKNOWN;
        headMotorElectricity = UNKNOWN;
        wingMotorElectricity = UNKNOWN;

        armVersion = ROBOT_STRING_UNKNOWN;
        armSN = ROBOT_STRING_UNKNOWN;
        ultVersion = ROBOT_STRING_UNKNOWN;
        ultSN = ROBOT_STRING_UNKNOWN;
        motorDriverVersion = ROBOT_STRING_UNKNOWN;
        motorDriverSN = ROBOT_STRING_UNKNOWN;
        chargingPileVersion = ROBOT_STRING_UNKNOWN;
        chargingPileSN = ROBOT_STRING_UNKNOWN;
        chargingPileDate = UNKNOWN;
        chargingPileMonth = UNKNOWN;
        chargingPileId = ROBOT_STRING_UNKNOWN;
        chargingPileNumber = UNKNOWN;
        chargingPileFrequency = UNKNOWN;
        linuxSaveNumber = ROBOT_STRING_UNKNOWN;

        oldChargingPileVersion = UNKNOWN;

        navigationVoicePrompt = 0;
        lastNavigationVoicePromptCheck = 0;
        //实例化SafetyCheckModel
        safeModel = SafetyCheckModel.getInstance();


        sosKeyState = UNKNOWN;
        powerKeyState = UNKNOWN;
        projectorPrepareComplete = UNKNOWN;
        robotDiagnosisCmd = ROBOT_STRING_UNKNOWN;
        navigationScene = UNKNOWN;
        lastNavigationSceneCheck = UNKNOWN;
        robotDiagnosisCmdOnOff = UNKNOWN;
        projectorState3dModel = UNKNOWN;
        projectorGreenPlus = UNKNOWN;
        projectorBluePlus = UNKNOWN;
        projectorRedPlus = UNKNOWN;
        projectorFactoryInfoAndVersion = UNKNOWN;

        bootUpReason = UNKNOWN;
        updateCurrentState = UNKNOWN;
        isUpdating = UNKNOWN;

    }




    public void enableProxy(StatusClientFactory factory) {
        synchronized (RobotStatus.class) {
            factory.setProxyObject(this);
            instance = (IStatus) factory.newInstance(getClass().getClassLoader(), getClass().getInterfaces());
            Log.d(this.getClass().getSimpleName(), "enableProxy()");
        }
    }

    public static final SparseArray<String> NAVIGATION_RESULT_FAIL_REASON_ARRAY = new SparseArray<String>();


    public static final SparseArray<String> NAVIGATION_RESULT_FAIL_VOICE_PROMPT = new SparseArray<String>();

    static {
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-1, "虽然我刚刚有点迷糊，但路我还是勉强能走滴");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-2, "好吧，你非要让我去我就试试吧");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-3, "好哒，反正我现在迷糊着，走错了您可别怪我");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-4, "那我只好尝试去一下啦啦啦");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-5, "好哒，你这么想让我去，我就运用我的智慧尝试走一下");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-7, "好吧，我再试试");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-8, "我去试试，我不信我找不到我的家");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-9, "我去试试，我不信我到不了哪儿");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-10, "一直让我去，我还是去吧，不准可不怨我");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-202, "虽然刚刚迷糊，但我还是试试吧");
        NAVIGATION_RESULT_FAIL_VOICE_PROMPT.put(-203, "那我只好尝试去一下了，错了别说我笨哦");


        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-1, "起点不合法");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-2, "终点不合法");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-3, "规划路径失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-4, "全局蔽障失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-5, "地图不存在");

        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-7, "距离充电桩有点远，请主人遥控我到充电桩1米附近。");
        // NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-7, "超声波检测前方障碍");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-8, "距离充电桩太远,需要您遥控我到充电桩1米附近");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-9, "没有看到充电桩，回充电桩失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-10, "内部通信异常，建议重启机器人");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-11, "内部出现异常，建议重启机器人或充电桩");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-12, "未与充电桩电极接触，回充电桩失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-13, "距离充电桩太近,需要您遥控我到充电桩1米附近");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-14, "回充电桩超声波定位失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-15, "主人，请检查充电桩接驳处的电极是否弹出。");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-17, "回充电桩超声波检测到障碍物");
        /**
         *  0x05----接驳回电桩
         */
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-19, "回充电桩点亮指示灯失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-20, "获取充电桩指示灯数据失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-21, "获取充电桩接驳状态失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-22, "设置充电桩上电失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-23, "获取充电桩上电状态失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-24, "接驳充电桩成功");


        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-100, "出充电桩失败,获取充电桩状态失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-101, "出充电桩失败,充电桩断电失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-102, "出充电桩失败,直行受阻");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-103, "出充电桩失败,点亮充电桩指示灯失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-104, "出充电桩失败,获取摄像头数据失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-105, "视觉矫正位置失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-105, "出充电桩前检查到前方障碍物");

        /**
         * 0x01
         */
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-200, "托盘打开，规划路径失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-202, "超声波标定失败");
        NAVIGATION_RESULT_FAIL_REASON_ARRAY.put(-203, "试图到达可行区域失败");
    }

    /**
     * 获取所有表情
     *
     * @return 表情列表
     */
    public List<RobotFace> readRobotFaceListFromDB() {
        List<RobotFace> faceList = new ArrayList<RobotFace>();
        RobotFace face;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_face");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                face = new RobotFace();
                face.setId(cursor.getString(cursor.getColumnIndex("_id")));
                face.setName(cursor.getString(cursor.getColumnIndex("name")));
                face.setPath(cursor.getString(cursor.getColumnIndex("path")));
                faceList.add(face);
            }
            cursor.close();
        }
        return faceList;
    }

    /**
     * 新增一个表情
     *
     * @param face 表情
     */
    public void writeRobotFaceToDB(RobotFace face) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_face");
        ContentValues values = new ContentValues();
        values.put("_id", face.getId());
        values.put("name", face.getName());
        values.put("path", face.getPath());
        values.put("isSelect", 0);
        contentResolver.insert(contentUri, values);
    }

    /**
     * 获取当前使用的表情
     *
     * @return 表情
     */
    public RobotFace readRobotCurrentFaceFromDB() {
        RobotFace face = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_face");
        Cursor cursor = contentResolver.query(contentUri, null, "isSelect = 1", null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                face = new RobotFace();
                face.setId(cursor.getString(cursor.getColumnIndex("_id")));
                face.setName(cursor.getString(cursor.getColumnIndex("name")));
                face.setPath(cursor.getString(cursor.getColumnIndex("path")));
            }
            cursor.close();
        }
        return face;
    }

    /**
     * 设置当前表情
     *
     * @param id 表情ID
     */
    public void writeRobotCurrentFaceToDB(String id) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_face");
        Cursor cursor = contentResolver.query(contentUri, null, "isSelect = 1", null, null);
        ContentValues updateValues = new ContentValues();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (cursor != null) {
            updateValues.put("isSelect", 0);
            if (cursor.moveToFirst()) {
                String updateId = cursor.getString(cursor.getColumnIndex("_id"));
                if (!id.equals(updateId)) {
                    ops.add(ContentProviderOperation.newUpdate(contentUri).withValues(updateValues).withSelection("_id = ?", new String[]{updateId}).build());
                }
            }
            cursor.close();
        }
        updateValues.put("isSelect", 1);
        ops.add(ContentProviderOperation.newUpdate(contentUri).withValues(updateValues).withSelection("_id = ?", new String[]{id}).build());
        try {
            contentResolver.applyBatch(SERVICES_PROVIDER_AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清空当前表情
     *
     * @return 是否成功
     */
    public boolean cleanRobotCurrentFaceToDB() {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_face");
        Cursor cursor = contentResolver.query(contentUri, null, "isSelect = 1", null, null);
        ContentValues updateValues = new ContentValues();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (cursor != null) {
            updateValues.put("isSelect", 0);
            if (cursor.moveToFirst()) {
                String updateId = cursor.getString(cursor.getColumnIndex("_id"));
                return contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{updateId}) > 0;
            }
            cursor.close();
        }
        return false;
    }

    /**
     * 获取所有方言
     *
     * @return 方言列表
     */
    public List<RobotLanguage> readRobotLanguageFromDB() {
        List<RobotLanguage> languageList = new ArrayList<RobotLanguage>();
        RobotLanguage robotLanguage;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_language");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                robotLanguage = new RobotLanguage();
                robotLanguage.setId(cursor.getString(cursor.getColumnIndex("_id")));
                robotLanguage.setName(cursor.getString(cursor.getColumnIndex("name")));
                robotLanguage.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
                languageList.add(robotLanguage);
            }
            cursor.close();
        }
        return languageList;
    }

    /**
     * 新增一条方言
     *
     * @param robotLanguage 新增方言
     */
    public void writeRobotLanguageToDB(RobotLanguage robotLanguage) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_language");
        ContentValues updateValues = new ContentValues();
        updateValues.put("_id", robotLanguage.getId());
        updateValues.put("name", robotLanguage.getName());
        updateValues.put("language", robotLanguage.getLanguage());
        contentResolver.insert(contentUri, updateValues);
    }

    /**
     * 获取当前使用的方言
     *
     * @return 当前方言
     */
    public RobotLanguage readRobotCurrentLanguageFromDB() {
        RobotLanguage language = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_language");
        Cursor cursor = contentResolver.query(contentUri, null, "isSelect = 1", null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                language = new RobotLanguage();
                language.setId(cursor.getString(cursor.getColumnIndex("_id")));
                language.setName(cursor.getString(cursor.getColumnIndex("name")));
                language.setLanguage(cursor.getString(cursor.getColumnIndex("language")));
            }
            cursor.close();
        }
        return language;
    }

    /**
     * 设置当前使用的方言
     * @param id 方言ID
     */
    public void writeRobotCurrentLanguageToDB(String id) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_language");
        Cursor cursor = contentResolver.query(contentUri, null, "isSelect = 1", null, null);
        ContentValues updateValues = new ContentValues();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (cursor != null) {
            updateValues.put("isSelect", 0);
            if (cursor.moveToFirst()) {
                String updateId = cursor.getString(cursor.getColumnIndex("_id"));
                if (!id.equals(updateId)) {
                    ops.add(ContentProviderOperation.newUpdate(contentUri).withValues(updateValues).withSelection("_id = ?", new String[]{updateId}).build());
                }
            }
            cursor.close();
        }
        updateValues.put("isSelect", 1);
        ops.add(ContentProviderOperation.newUpdate(contentUri).withValues(updateValues).withSelection("_id = ?", new String[]{id}).build());
        try {
            contentResolver.applyBatch(SERVICES_PROVIDER_AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取设置信息
     *
     * @return 面罩状态开关 -1:未获取到
     */
    public int readRobotSettingFromDB() {
        int maskStateSwitch = -1;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_setting");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                int maskStateSwitchIndex = cursor.getColumnIndex("MaskStateSwitch");
                if (maskStateSwitchIndex > 0) {
                    maskStateSwitch = cursor.getInt(maskStateSwitchIndex);
                }
            }
            cursor.close();
        }
        return maskStateSwitch;
    }

    /**
     * 插入/更新 设置信息
     *
     * @param maskStateSwitch 面罩状态开关
     */
    public void writeRobotSettingToDB(int maskStateSwitch) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_setting");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            ContentValues updateValues = new ContentValues();
            updateValues.put("MaskStateSwitch", maskStateSwitch);
            if (cursor.moveToFirst()) {
                contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{"1"});
            } else {
                contentResolver.insert(contentUri, updateValues);
            }
            cursor.close();
        }
    }

    /**
     * 获取当前使用的语调
     * @return 当前语调
     */
    public RobotIntonation readRobotCurrentIntonationFromDB() {
        RobotIntonation intonation = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_intonation");
        Cursor cursor = contentResolver.query(contentUri, null, "isSelect = 1", null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                intonation = new RobotIntonation();
                intonation.setId(cursor.getString(cursor.getColumnIndex("_id")));
                intonation.setName(cursor.getString(cursor.getColumnIndex("name")));
                intonation.setSpeed(cursor.getString(cursor.getColumnIndex("speed")));
                intonation.setPitch(cursor.getString(cursor.getColumnIndex("pitch")));
                intonation.setSoundEffect(cursor.getString(cursor.getColumnIndex("sound_effect")));
            }
            cursor.close();
        }
        return intonation;
    }

    /**
     * 获取当前版本和渠道号
     *
     * @return 当前版本和渠道号
     */
    public RobotVersion readRobotVersionFromDB() {
        RobotVersion versionObject = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_version");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                versionObject = new RobotVersion();
                versionObject.setId(cursor.getString(cursor.getColumnIndex("_id")));
                versionObject.setMark(cursor.getString(cursor.getColumnIndex("mark")));
                versionObject.setChannel(cursor.getString(cursor.getColumnIndex("channel")));
            }
            cursor.close();
        }
        return versionObject;
    }

    /**
     * 设置当前使用的语调
     *
     * @param id 语调ID
     */
    public void writeRobotCurrentIntonationToDB(String id) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_intonation");
        Cursor cursor = contentResolver.query(contentUri, null, "isSelect = 1", null, null);
        ContentValues updateValues = new ContentValues();
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        if (cursor != null) {
            updateValues.put("isSelect", 0);
            if (cursor.moveToFirst()) {
                String updateId = cursor.getString(cursor.getColumnIndex("_id"));
                if (!id.equals(updateId)) {
                    ops.add(ContentProviderOperation.newUpdate(contentUri).withValues(updateValues).withSelection("_id = ?", new String[]{updateId}).build());
                }
            }
            cursor.close();
        }
        updateValues.put("isSelect", 1);
        ops.add(ContentProviderOperation.newUpdate(contentUri).withValues(updateValues).withSelection("_id = ?", new String[]{id}).build());
        try {
            contentResolver.applyBatch(SERVICES_PROVIDER_AUTHORITY, ops);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取所有语调
     *
     * @return 语调集合
     */
    public List<RobotIntonation> readRobotIntonationFromDB() {
        List<RobotIntonation> intonationList = new ArrayList<RobotIntonation>();
        RobotIntonation intonation;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_intonation");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                intonation = new RobotIntonation();
                intonation.setId(cursor.getString(cursor.getColumnIndex("_id")));
                intonation.setName(cursor.getString(cursor.getColumnIndex("name")));
                intonation.setSpeed(cursor.getString(cursor.getColumnIndex("speed")));
                intonation.setPitch(cursor.getString(cursor.getColumnIndex("pitch")));
                intonation.setSoundEffect(cursor.getString(cursor.getColumnIndex("sound_effect")));
                intonationList.add(intonation);
            }
            cursor.close();
        }
        return intonationList;
    }

    /**
     * 插入 语调
     *
     * @param intonation 语调
     */
    public void writeRobotIntonationToDB(RobotIntonation intonation) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_intonation");
        ContentValues updateValues = new ContentValues();
        updateValues.put("_id", intonation.getId());
        updateValues.put("name", intonation.getName());
        updateValues.put("speed", intonation.getSpeed());
        updateValues.put("pitch", intonation.getPitch());
        updateValues.put("sound_effect", intonation.getSoundEffect());
        updateValues.put("isSelect", 0);
        contentResolver.insert(contentUri, updateValues);
    }

    /**
     * 插入 app信息
     *
     * @param appInfo app信息
     * @return 是否成功
     */
    public boolean writeAppInfoToDB(AppInfo appInfo) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/app_info");
        ContentValues updateValues = new ContentValues();
        updateValues.put("apppackage", appInfo.getAppPackage());
        updateValues.put("appname", appInfo.getAppName());
        updateValues.put("status", appInfo.getStatus());
        updateValues.put("type", appInfo.getType());
        return contentResolver.insert(contentUri, updateValues) != null;
    }

    /**
     * 获取所有App信息
     *
     * @return 信息集合
     */
    public List<AppInfo> readAppInfoFromDB() {
        List<AppInfo> appInfoList = new ArrayList<AppInfo>();
        AppInfo appInfo;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/app_info");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                appInfo = new AppInfo();
                appInfo.setAppPackage(cursor.getString(cursor.getColumnIndex("apppackage")));
                appInfo.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                appInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                appInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
                appInfoList.add(appInfo);
            }
            cursor.close();
        }
        return appInfoList;
    }

    /**
     * 通过类型获取App信息
     *
     * @return 信息集合
     */
    public List<AppInfo> readAppInfoFromDBByType(int type) {
        List<AppInfo> appInfoList = new ArrayList<AppInfo>();
        AppInfo appInfo;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/app_info");
        Cursor cursor = contentResolver.query(contentUri, null, "type = ?", new String[]{type + ""}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                appInfo = new AppInfo();
                appInfo.setAppPackage(cursor.getString(cursor.getColumnIndex("apppackage")));
                appInfo.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                appInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                appInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
                appInfoList.add(appInfo);
            }
            cursor.close();
        }
        return appInfoList;
    }

    /**
     * 通过名称获取App信息
     *
     * @return app信息
     */
    public AppInfo readAppInfoFromDBByName(String appName) {
        AppInfo appInfo = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/app_info");
        Cursor cursor = contentResolver.query(contentUri, null, "appname = ?", new String[]{appName}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                appInfo = new AppInfo();
                appInfo.setAppPackage(cursor.getString(cursor.getColumnIndex("apppackage")));
                appInfo.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                appInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                appInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
            }
            cursor.close();
        }
        return appInfo;
    }

    /**
     * 通过包名获取App信息
     *
     * @return app信息
     */
    public AppInfo readAppInfoFromDBByPackage(String appPackage) {
        AppInfo appInfo = null;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/app_info");
        Cursor cursor = contentResolver.query(contentUri, null, "apppackage = ?", new String[]{appPackage}, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                appInfo = new AppInfo();
                appInfo.setAppPackage(cursor.getString(cursor.getColumnIndex("apppackage")));
                appInfo.setAppName(cursor.getString(cursor.getColumnIndex("appname")));
                appInfo.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                appInfo.setType(cursor.getInt(cursor.getColumnIndex("type")));
            }
            cursor.close();
        }
        return appInfo;
    }

    /**
     * 通过包名删除app信息
     *
     * @param appPackage 包名
     * @return 是否成功
     */
    public boolean delAppInfoByPackage(String appPackage) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/app_info");
        return contentResolver.delete(contentUri, "apppackage = ?", new String[]{appPackage}) > 0;
    }

    /**
     * 通过包名更新app信息
     *
     * @param appInfo app信息
     * @return 是否成功
     */
    public boolean updateAppInfoByPackage(AppInfo appInfo) {
        if (TextUtils.isEmpty(appInfo.getAppPackage())) {
            return false;
        }
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/app_info");
        ContentValues updateValues = new ContentValues();
        if (!TextUtils.isEmpty(appInfo.getAppName())) {
            updateValues.put("appname", appInfo.getAppName());
        }
        updateValues.put("status", appInfo.getStatus());
        updateValues.put("type", appInfo.getType());
        return contentResolver.update(contentUri, updateValues, "apppackage = ?", new String[]{appInfo.getAppPackage()}) > 0;
    }

    public String readRobotNumberFormDB() {
        String number = null;

        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_info");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                number = cursor.getString(cursor.getColumnIndex("robotNumber"));
                cursor.close();
            }
        }
        return number;
    }

    public int readRobotMemberStateFormDB() {
        int number = MEMBERSTATE_UNKNOWN;//-1代表未知状态
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/member_info");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                number = cursor.getInt(cursor.getColumnIndex("memberState"));
                cursor.close();
            }
        }
        return number;
    }


    public void writeRobotNameFormDB(String nickName) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/member_info");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ContentValues updateValues = new ContentValues();
                updateValues.put("nickName", nickName);
                contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{"1"});
                cursor.close();
            } else {
                ContentValues updateValues = new ContentValues();
                updateValues.put("nickName", nickName);
                updateValues.put("countDownTime", Long.MIN_VALUE);
                updateValues.put("updateTime", System.currentTimeMillis());
                updateValues.put("expireTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(countDownTime + System.currentTimeMillis())));
                updateValues.put("memberState", -1);
                contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{"1"});
                cursor.close();
            }
        }
    }

    public boolean writeSettingWheelsFormDB(boolean isSwitch) {
        boolean result = false;
        int state = isSwitch ? 0 : 1;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_setting");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            ContentValues updateValues = new ContentValues();
            updateValues.put("WheelsMoveSwitch", state);
            if (cursor.moveToFirst()) {
                result = contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{"1"})>0;
            } else {
                result = contentResolver.insert(contentUri, updateValues)!=null;
            }
            cursor.close();
        }
        return result;
    }

    public boolean readSettingWheelsFormDB() {
        int state = 0;

        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_setting");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                state = cursor.getInt(cursor.getColumnIndex("WheelsMoveSwitch"));
                cursor.close();
            }
        }

        return state == 0;
    }


    public boolean writeVersionLegalityFormDB(int state) {
        boolean result = false;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_version");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            ContentValues updateValues = new ContentValues();
            updateValues.put("legality", state);
            if (cursor.moveToFirst()) {
                result = contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{"1"})>0;
            } else {
                result = contentResolver.insert(contentUri, updateValues)!=null;
            }
            cursor.close();
        }
        return result;
    }

    public int readVersionLegalityFormDB() {
        int state = 0;

        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/robot_version");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int legality = cursor.getColumnIndex("legality");
                if(legality != -1) {
                    state = cursor.getInt(cursor.getColumnIndex("legality"));
                }
                cursor.close();
            }
        }
        return state;
    }


    public long readCountDownTimeFormDB() {
        long countDownTime = 0l;
        long updateTime = 0l;
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/member_info");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                countDownTime = cursor.getLong(cursor.getColumnIndex("countDownTime"));
                updateTime = cursor.getLong(cursor.getColumnIndex("updateTime"));
                cursor.close();
            }
            if (countDownTime == LACK_OF_GOLD) {
                return LACK_OF_GOLD;
            }
            return countDownTime + updateTime;
        }
        return COUNTDOWN_UNKNOWN;
    }

    public void writeCountDownTimeFormDB(long countDownTime) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/member_info");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                ContentValues updateValues = new ContentValues();
                updateValues.put("countDownTime", countDownTime);
                updateValues.put("updateTime", System.currentTimeMillis());
                updateValues.put("expireTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis() + countDownTime)));
                if (countDownTime == RobotStatus.LACK_OF_GOLD) {
                    updateValues.put("memberState", 0);
                }
                if (countDownTime > 0) {
                    updateValues.put("memberState", 1);
                } else {
                    updateValues.put("memberState", 2);
                }

                contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{"1"});
                cursor.close();
            } else {
                ContentValues updateValues = new ContentValues();
                updateValues.put("countDownTime", countDownTime);
                updateValues.put("updateTime", System.currentTimeMillis());
                updateValues.put("expireTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(System.currentTimeMillis() + countDownTime)));
                if (countDownTime == RobotStatus.LACK_OF_GOLD) {
                    updateValues.put("memberState", 0);
                }
                if (countDownTime > 0) {
                    updateValues.put("memberState", 1);
                } else {
                    updateValues.put("memberState", 2);
                }

                contentResolver.update(contentUri, updateValues, "_id = ?", new String[]{"1"});
                cursor.close();
            }
        }
    }


    public String readRobotNameFormDB() {
        String nickName = null;

        ContentResolver contentResolver = mContext.getContentResolver();
        Uri contentUri = Uri.parse("content://com.efrobot.services.common/member_info");
        Cursor cursor = contentResolver.query(contentUri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                nickName = cursor.getString(cursor.getColumnIndex("nickName"));
                cursor.close();
            }
        }
        return nickName;
    }

    public static IStatus getInstance(Context mContext) {
        if (instance == null) {
            synchronized (RobotStatus.class) {
                if (instance == null) {
                    instance = new RobotStatus(mContext.getApplicationContext());
                }
            }
        }
        return instance;
    }


    public static IStatus getProxyInstance(Context context, StatusClientFactory factory) {
        synchronized (RobotStatus.class) {
            mContext = context;
            RobotStatus robotState = new RobotStatus(context);
            factory.setProxyObject(robotState);
            instance = (IStatus) factory.newInstance(robotState.getClass().getClassLoader(), robotState.getClass().getInterfaces());
            Log.d("RobotState", "getProxyInstance()");
        }
        return instance;
    }

    /**
     * 获取机器人的版本
     *
     * @return 机器人的版本 {@link #UNKNOWN}
     */
    public int getRobotVersion() {
        return robotVersion;
    }

    /**
     * 获取机器人号码
     *
     * @return 机器人号码，无号码返回null
     */
    public String getRobotId() {
        if (robotNumber == null || robotNumber.trim().length() == 0) {
            robotNumber = readRobotNumberFormDB();
        }
        return robotNumber;
    }


    /**
     * 获取机器人的本地昵称
     *
     * @return 机器人本地昵称，无昵称返回小胖
     */
    public String getRobotName() {
        if (robotName == null || robotName.trim().length() == 0) {
            robotName = readRobotNameFormDB();
        }
        if (robotName == null || robotName.trim().length() == 0) {
            return "小胖";
        }

        return robotName;
    }

    /**
     * 获取机器人会员状态
     *
     * @return 获取机器人会员状态
     */
    public int getRobotMemberState() {
        if (memberState == -1) {
            memberState = readRobotMemberStateFormDB();
        }
        return memberState;
    }

    /**
     * 读取数据，重新初始化MemberState
     */
    public void reRobotMemberState() {
        memberState = readRobotMemberStateFormDB();
    }

    /**
     * 读取数据，重新初始化robotName
     */
    public void reReadRobotName() {
        robotName = readRobotNameFormDB();
        if (robotName == null || robotName.trim().length() == 0) {
            robotName = "小胖";
        }
    }


    /**
     * 获取机器人的会员倒计时时间
     *
     * @return 会员倒计时时间 （单位：毫秒）,负数为过期
     */
    public long getRobotMemberCountDownTime() {
        if (countDownTime == COUNTDOWN_UNKNOWN) {
            countDownTime = readCountDownTimeFormDB();
        }
        if (countDownTime == LACK_OF_GOLD) {
            return LACK_OF_GOLD;
        }
        if (countDownTime == COUNTDOWN_UNKNOWN) {
            return COUNTDOWN_UNKNOWN;
        }
        return countDownTime - System.currentTimeMillis();
    }

    /**
     * 设置机器人的会员倒计时时间
     *
     * @return 设置会员倒计时时间 （单位：毫秒）,负数为过期
     */
    public void setRobotMemberCountDownTime(long countdowntime) {
        writeCountDownTimeFormDB(countdowntime);
    }

    /**
     * 读取数据，重新初始化countDownTime
     */
    public void reRobotMemberCountDownTime() {
        countDownTime = readCountDownTimeFormDB();
    }


    /**
     * 得到左轮发电机电量
     *
     * @return
     */
    public int getLeftWheelMotorElectricity() {
        return leftWheelMotorElectricity;
    }

    public void setLeftWheelMotorElectricity(int leftWheelMotorElectricity) {
        this.leftWheelMotorElectricity = leftWheelMotorElectricity;
    }

    /**
     * 得到右轮发电机电量
     *
     * @return
     */
    public int getRightWheelMotorElectricity() {
        return rightWheelMotorElectricity;
    }

    public void setRightWheelMotorElectricity(int rightWheelMotorElectricity) {
        this.rightWheelMotorElectricity = rightWheelMotorElectricity;
    }

    public int getHeadMotorElectricity() {
        return headMotorElectricity;
    }

    public void setHeadMotorElectricity(int headMotorElectricity) {
        this.headMotorElectricity = headMotorElectricity;
    }

    public long getProjectorHotCloseTime() {
        return projectorHotCloseTime;
    }

    public void setProjectorHotCloseTime(long projectorHotCloseTime) {
        this.projectorHotCloseTime = projectorHotCloseTime;
    }

    public int getProjectorPrepareComplete() {
        return projectorPrepareComplete;
    }

    public void setProjectorPrepareComplete(int projectorPrepareComplete) {
        this.projectorPrepareComplete = projectorPrepareComplete;
    }

    public int getWingMotorElectricity() {
        return wingMotorElectricity;
    }

    public void setWingMotorElectricity(int wingMotorElectricity) {
        this.wingMotorElectricity = wingMotorElectricity;
    }

    public int getPowerKeyState() {
        return powerKeyState;
    }

    public void setPowerKeyState(int powerKeyState) {
        this.powerKeyState = powerKeyState;
    }

    public int getSosKeyState() {
        return sosKeyState;
    }

    public void setSosKeyState(int sosKeyState) {
        this.sosKeyState = sosKeyState;
    }


    public int getProjectorTemperatureState() {
        return projectorTemperatureState;
    }

    public void setProjectorTemperatureState(int projectorTemperatureState) {
        this.projectorTemperatureState = projectorTemperatureState;
    }

    public void setBatteryLevel(int batteryLevel) {
        this.batteryLevel = batteryLevel;
    }

    /**
     * 获取机器人的电池电量
     *
     * @return 电池电量，未获取到返回 {@link #UNKNOWN}
     */
    public int getBatteryLevel() {
        return batteryLevel;
    }


    public int getBatteryState() {
        return batteryState;
    }

    public void setBatteryState(int batteryState) {
        this.batteryState = batteryState;
    }

    /**
     * 获取机器人的电池电流
     *
     * @return 电池电流，未获取到返回 {@link #UNKNOWN}
     */
    public int getElectric() {
        return electric;
    }


    public void setElectric(int electric) {
        this.electric = electric;
    }

    /**
     * 获取投影电压
     *
     * @return 投影电压（单位：mv）
     */
    public int getProjectorPower() {
        return projectorPower;
    }


    public void setProjectorPower(int projectorPower) {
        this.projectorPower = projectorPower;
    }

    /**
     * 获取pad电压
     *
     * @return pad电压（单位：mv）
     */
    public int getPadPower() {
        return padPower;
    }

    public void setPadPower(int padPower) {
        this.padPower = padPower;
    }

    /**
     * 获取电机（头部翅膀风机）电压
     *
     * @return 电机（头部翅膀风机）电压（单位：mv）
     */
    public int getMotorPower() {
        return motorPower;
    }

    public void setMotorPower(int motorPower) {
        this.motorPower = motorPower;
    }

    /**
     * 获取主板电压
     *
     * @return 主板电压（单位：mv）
     */
    public int getMainBoardPower() {
        return mainBoardPower;
    }

    public void setMainBoardPower(int mainBoardPower) {
        this.mainBoardPower = mainBoardPower;
    }


    /**
     * 获取电池电压
     *
     * @return 电池电压（单位：mv）
     */
    public int getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(int batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    /**
     * 获取机器人Pm2.5传感器测量值
     *
     * @return Pm2.5传感器测量值
     * 未获取到  返回{@link #UNKNOWN}  其他 {@link #ROBOT_VERSION_FAMILY}  {@link #ROBOT_VERSION_BUSINESS}
     */
    public int getPm2_5() {
        return pm2_5;
    }


    public void setPm2_5(int pm2_5) {
        this.pm2_5 = pm2_5;
    }


    /**
     * 获取机器人检测的温度值
     *
     * @return 温度值，未获取到返回 {@link #UNKNOWN}
     */
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    /**
     * 获取机器人检测的湿度
     *
     * @return 湿度，未获取到返回 {@link #UNKNOWN}
     */
    public int getHumidity() {
        return humidity;
    }


    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }


    /**
     * 获取机器人检测的异味
     *
     * @return 异味值，未获取到返回 {@link #UNKNOWN}
     */
    public int getPeculiarSmell() {
        return peculiarSmell;
    }


    public void setPeculiarSmell(int peculiarSmell) {
        this.peculiarSmell = peculiarSmell;
    }


    public int getHeadKeyState() {
        return headKeyState;
    }

    public void setHeadKeyState(int headKeyState) {
        this.headKeyState = headKeyState;
    }

    /**
     * 机器人地图状态
     *
     * @return mapState 地图状态   未构建{@link #MAP_STATE_UN_BUILD}， 构建中{@link #MAP_STATE_BUILDING}，已构建{@link #MAP_STATE_BUILD}
     */
    public int getMapState() {
        return mapState;
    }

    public void setMapState(int mapState) {
        this.mapState = mapState;
    }

    /**
     * 机器人是否有地图
     *
     * @return hasMap 是否有地图
     */
    public boolean hasMap() {
        return mapState == MAP_STATE_BUILD;
    }

    /**
     * 获取机器人头部的角度
     *
     * @return 头部的角度
     * 未获取到返回 {@link #UNKNOWN} 范围[0-240]
     */
    public int getHeadAngle() {
        return headAngle;
    }

    public void setHeadAngle(int headAngle) {
        this.headAngle = headAngle;
    }

    public int getLightBeltBrightness() {
        return lightBeltBrightness;
    }

    public void setLightBeltBrightness(int lightBeltBrightness) {
        this.lightBeltBrightness = lightBeltBrightness;
    }


    public int getLeftWingAngle() {
        return leftWingAngle;
    }

    public void setLeftWingAngle(int leftWingAngle) {
        this.leftWingAngle = leftWingAngle;
    }

    public int getRightWingAngle() {
        return rightWingAngle;
    }

    public void setRightWingAngle(int rightWingAngle) {
        this.rightWingAngle = rightWingAngle;
    }

    /**
     * 获取机器人头部面罩的开关状态
     *
     * @return 头部面罩的开关状态
     * 未获取到返回 {@link #UNKNOWN}， 其他{@link #UNKNOWN} {@link #STATE_OPEN} {@link #STATE_CLOSE} {@link #STATE_STOPPAGE}
     */
    public int getMaskState() {
        return maskState;
    }

    public void setMaskState(int maskState) {
        this.maskState = maskState;
    }

    public int getTrayState() {
        return trayState;
    }

    public void setTrayState(int trayState) {
        this.trayState = trayState;
    }


    /**
     * 获取机器人投影仪开关状态
     * 未获取到返回 {@link #UNKNOWN}， 其他{@link #UNKNOWN} {@link #PROJECTOR_STATE_OPEN} {@link #PROJECTOR_STATE_CLOSE} {@link #STATE_INEXISTENCE}  {@link #STATE_STOPPAGE}
     */
    public int getProjectorState() {
        return projectorState;
    }


    public void setProjectorState(int projectorState) {
        this.projectorState = projectorState;
    }

    /**
     * 获取机器人净化器开关状态
     */
    public int getPurifierState() {
        return purifierState;
    }

    public void setPurifierState(int purifierState) {
        this.purifierState = purifierState;
    }

    public void setPurifierWorkTime(int purifierWorkTime) {
        this.purifierWorkTime = purifierWorkTime;
    }

    /**
     * 获取净化器的工作时长
     *
     * @return 净化器的工作时长（单位：小时），
     * 注意此数据只有调用重置方法才会被重置
     */
    public int getPurifierWorkTime() {
        return purifierWorkTime;
    }

    public int getHeadState() {
        return headState;
    }

    public void setHeadState(int state) {
        this.headState = state;
    }

    public int getLeftWingState() {
        return leftWingState;
    }

    public void setLeftWingState(int state) {
        this.leftWingState = state;
    }

    public int getRightWingState() {
        return rightWingState;
    }

    public void setRightWingState(int state) {
        this.rightWingState = state;
    }

    public int getLeftWheelState() {
        return leftWheelState;
    }

    public void setLeftWheelState(int state) {
        leftWheelState = state;
    }

    public int getRightWheelState() {
        return rightWheelState;
    }

    public void setRightWheelState(int state) {
        rightWheelState = state;
    }

    public int getHeadActivePassiveState() {
        return headActivePassiveState;
    }

    public void setHeadActivePassiveState(int state) {
        this.headActivePassiveState = state;
    }

    public int getLeftWingActivePassiveState() {
        return leftWingActivePassiveState;
    }

    public void setLeftWingActivePassiveState(int state) {
        this.leftWingActivePassiveState = state;
    }

    public int getRightWingActivePassiveState() {
        return rightWingActivePassiveState;
    }

    public void setRightWingActivePassiveState(int state) {
        this.rightWingActivePassiveState = state;
    }

    public int getLeftWheelActivePassiveState() {
        return leftWheelActivePassiveState;
    }

    public void setLeftWheelActivePassiveState(int state) {
        leftWheelActivePassiveState = state;
    }

    public int getRightWheelActivePassiveState() {
        return rightWheelActivePassiveState;
    }

    public void setRightWheelActivePassiveState(int state) {
        rightWheelActivePassiveState = state;
    }

    /**
     * 获取机器人导航状态
     * 闲置,未处于导航 {@link #NAVIGATION_STATE_IDLE}， 普通导航{@link #NAVIGATION_STATE_NORMAL} ，充电桩 {@link #NAVIGATION_STATE_CHARGING}
     */
    public int getNavigationState() {
        return navigationState;
    }


    public void setNavigationState(int navigationState) {
        this.navigationState = navigationState;
    }

    public int getPurifierFanSpeed() {
        return purifierFanSpeed;
    }

    public void setPurifierFanSpeed(int purifierFanSpeed) {
        this.purifierFanSpeed = purifierFanSpeed;
    }

    public int getLeftWheelSuspendState() {
        return leftWheelSuspendState;
    }

    public void setLeftWheelSuspendState(int leftWheelSuspendState) {
        this.leftWheelSuspendState = leftWheelSuspendState;
    }

    public int getRightWheelSuspendState() {
        return rightWheelSuspendState;
    }

    public void setRightWheelSuspendState(int rightWheelSuspendState) {
        this.rightWheelSuspendState = rightWheelSuspendState;
    }

    public int getBindChargingPile() {
        return bindChargingPile;
    }

    public void setBindChargingPile(int bindChargingPile) {
        this.bindChargingPile = bindChargingPile;
    }

    public int getRobotLocation() {
        return robotLocation;
    }

    public void setRobotLocation(int robotLocation) {
        this.robotLocation = robotLocation;
    }

    public int getHasConnectedPile() {
        return hasConnectedPile;
    }

    public void setHasConnectedPile(int hasConnectedPile) {
        this.hasConnectedPile = hasConnectedPile;
    }

    /*  ============================     机器人APP状态       ===========================   */


    public void setVideoService(int videoService) {
        this.videoService = videoService;
    }

    public int getVideoService() {
        return videoService;
    }

    public long getLastVideoServiceCheck() {
        return lastVideoServiceCheck;
    }

    public void setLastVideoServiceCheck(long lastVideoServiceCheck) {
        this.lastVideoServiceCheck = lastVideoServiceCheck;
    }

    public int getSpeechModel() {
        return speechModel;
    }

    public void setSpeechModel(int speechModel) {
        this.speechModel = speechModel;
    }

    public long getLastSpeechModelCheck() {
        return lastSpeechModelCheck;
    }

    public void setLastSpeechModelCheck(long lastSpeechModelCheck) {
        this.lastSpeechModelCheck = lastSpeechModelCheck;
    }

    public int getSpeechModelTiming() {
        return speechModelTiming;
    }

    public void setSpeechModelTiming(int speechModelTiming) {
        this.speechModelTiming = speechModelTiming;
    }

    public long getLastSpeechModelTimingCheck() {
        return lastSpeechModelTimingCheck;
    }

    public void setLastSpeechModelTimingCheck(long lastSpeechModelTimingCheck) {
        this.lastSpeechModelTimingCheck = lastSpeechModelTimingCheck;
    }


    public long getLastDanceStateCheck() {
        return lastDanceStateCheck;
    }

    public void setLastDanceStateCheck(long lastDanceStateCheck) {
        this.lastDanceStateCheck = lastDanceStateCheck;
    }

    public int getDanceState() {
        return danceState;
    }

    public void setDanceState(int danceState) {
        this.danceState = danceState;
    }

    public void setLoseWayState(int loseWayState) {
        this.loseWayState = loseWayState;
    }

    /**
     * 获取地图构建状态
     *
     * @return
     */
    public int getLoseWayState() {
        return loseWayState;
    }

    public long getLastLoseWayStateCheck() {
        return lastLoseWayStateCheck;
    }

    public void setLastLoseWayStateCheck(long lastLoseWayStateCheck) {
        this.lastLoseWayStateCheck = lastLoseWayStateCheck;
    }

    public int getBuildMapState() {
        return buildMapState;
    }

    public void setBuildMapState(int buildMapState) {
        this.buildMapState = buildMapState;
    }

    public long getLastBuildMapStateCheck() {
        return lastBuildMapStateCheck;
    }

    public void setLastBuildMapStateCheck(long lastBuildMapStateCheck) {
        this.lastBuildMapStateCheck = lastBuildMapStateCheck;
    }


    public int getNavigationVoicePrompt() {
        return navigationVoicePrompt;
    }

    public void setNavigationVoicePrompt(int navigationVoicePrompt) {
        this.navigationVoicePrompt = navigationVoicePrompt;
    }

    public long getLastNavigationVoicePromptStateCheck() {
        return lastNavigationVoicePromptCheck;
    }

    public void setLastNavigationVoicePromptStateCheck(long lastNavigationVoicePromptCheck) {
        this.lastNavigationVoicePromptCheck = lastNavigationVoicePromptCheck;
    }

    public String getArmVersion() {
        return armVersion;
    }

    public String getUltSN() {
        return ultSN;
    }

    public void setUltSN(String ultSN) {
        this.ultSN = ultSN;
    }

    public String getMotorDriverSN() {
        return motorDriverSN;
    }

    public void setMotorDriverSN(String motorDriverSN) {
        this.motorDriverSN = motorDriverSN;
    }

    public void setArmVersion(String armVersion) {
        this.armVersion = armVersion;
    }

    public String getArmSN() {
        return armSN;
    }

    public void setArmSN(String armSN) {
        this.armSN = armSN;
    }

    public String getChargingPileVersion() {
        return chargingPileVersion;
    }

    public void setChargingPileVersion(String chargingPileVersion) {
        this.chargingPileVersion = chargingPileVersion;
    }

    public String getUltVersion() {
        return ultVersion;
    }

    public void setUltVersion(String ultVersion) {
        this.ultVersion = ultVersion;
    }

    public String getMotorDriverVersion() {
        return motorDriverVersion;
    }

    public void setMotorDriverVersion(String motorDriverVersion) {
        this.motorDriverVersion = motorDriverVersion;
    }

    public int getOldChargingPileVersion() {
        return oldChargingPileVersion;
    }

    public void setOldChargingPileVersion(int oldChargingPileVersion) {
        this.oldChargingPileVersion = oldChargingPileVersion;
    }

    public String getChargingPileSN() {
        return chargingPileSN;
    }

    public void setChargingPileSN(String chargingPileSN) {
        this.chargingPileSN = chargingPileSN;
    }

    public int getChargingPileMonth() {
        return chargingPileMonth;
    }

    public void setChargingPileMonth(int chargingPileMonth) {
        this.chargingPileMonth = chargingPileMonth;
    }

    public int getChargingPileDate() {
        return chargingPileDate;
    }

    public void setChargingPileDate(int chargingPileDate) {
        this.chargingPileDate = chargingPileDate;
    }

    public int getChargingPileNumber() {
        return chargingPileNumber;
    }

    public void setChargingPileNumber(int chargingPileNumber) {
        this.chargingPileNumber = chargingPileNumber;
    }

    public String getChargingPileId() {
        return chargingPileId;
    }

    public void setChargingPileId(String chargingPileId) {
        this.chargingPileId = chargingPileId;
    }

    public int getChargingPileFrequency() {
        return chargingPileFrequency;
    }

    public void setChargingPileFrequency(int chargingPileFrequency) {
        this.chargingPileFrequency = chargingPileFrequency;
    }

    public int getChargingPileLimit() {
        return chargingPileLimit;
    }

    public void setChargingPileLimit(int chargingPileLimit) {
        this.chargingPileLimit = chargingPileLimit;
    }

    public int getChargingPileVoltage() {
        return chargingPileVoltage;
    }

    public void setChargingPileVoltage(int chargingPileVoltage) {
        this.chargingPileVoltage = chargingPileVoltage;
    }

    public int getChargingPileElectric() {
        return chargingPileElectric;
    }

    public void setChargingPileElectric(int chargingPileElectric) {
        this.chargingPileElectric = chargingPileElectric;
    }

    public int getRobotStateWheelElectricity() {
        return robotStateWheelElectricity;
    }

    public void setRobotStateWheelElectricity(int robotStateWheelElectricity) {
        this.robotStateWheelElectricity = robotStateWheelElectricity;
    }

    public void setNavigationScene(int scene) {
        this.navigationScene = scene;
    }

    public int getNavigationScene() {
        return navigationScene;
    }

    public void setLastNavigationSceneCheck(long lastNavigationSceneCheck) {
        this.lastNavigationSceneCheck = lastNavigationSceneCheck;
    }

    public long getLastNavigationSceneCheck() {
        return lastNavigationSceneCheck;
    }

    public String getLinuxSaveNumber() {
        return linuxSaveNumber;
    }

    public void setLinuxSaveNumber(String linuxSaveNumber) {
        this.linuxSaveNumber = linuxSaveNumber;
    }

    public String getRobotDiagnosisCmd() {
        return robotDiagnosisCmd;
    }

    public void setRobotDiagnosisCmd(String robotDiagnosisCmd) {
        this.robotDiagnosisCmd = robotDiagnosisCmd;
    }

    public int getRobotDiagnosisCmdOnOff() {
        return robotDiagnosisCmdOnOff;
    }

    public void setRobotDiagnosisCmdOnOff(int robotDiagnosisCmdOnOff) {
        this.robotDiagnosisCmdOnOff = robotDiagnosisCmdOnOff;
    }

    public int getInfraredState() {
        return infraredState;
    }

    public void setInfraredState(int infraredState) {
        this.infraredState = infraredState;
    }

    public SafetyCheckModel getSafeModel() {
        return safeModel;
    }

    public int getBootUpReason() {
        return bootUpReason;
    }

    public void setBootUpReason(int bootUpReason) {
        this.bootUpReason = bootUpReason;
    }

    public int getProjectorState3dModel() {
        return projectorState3dModel;
    }

    public void setProjectorState3dModel(int projectorState3dModel) {
        this.projectorState3dModel = projectorState3dModel;
    }

    public int getProjectorGreenPlus() {
        return projectorGreenPlus;
    }

    public void setProjectorGreenPlus(int projectorGreenPlus) {
        this.projectorGreenPlus = projectorGreenPlus;
    }

    public int getProjectorBluePlus() {
        return projectorBluePlus;
    }

    public void setProjectorBluePlus(int projectorBluePlus) {
        this.projectorBluePlus = projectorBluePlus;
    }

    public int getProjectorRedPlus() {
        return projectorRedPlus;
    }

    public void setProjectorRedPlus(int projectorRedPlus) {
        this.projectorRedPlus = projectorRedPlus;
    }

    public int getProjectorFactoryInfoAndVersion() {
        return projectorFactoryInfoAndVersion;
    }

    public void setProjectorFactoryInfoAndVersion(int projectoFactoryInfoAndVersion) {
        this.projectorFactoryInfoAndVersion = projectoFactoryInfoAndVersion;
    }

    //---------------------------------SDK封装独有状态-------------------------------------

    /**
     *  投影仪状态
     */
    public enum ProjectStatus {
        /** 关闭状态 */
        CLOSED,
        /** 打开状态 */
        OPEN,
        /** 未知状态 */
        UNKNOWN,
        /** 初始化失败 */
        FAILURE;
        static ProjectStatus getProjectStatus(int projectStatus) {
            ProjectStatus[] values = ProjectStatus.values();
            return values[projectStatus];
        }
    }

    /**
     *  电池状态
     */
    public enum BatteryStatus {
        /** 未充电 */
        NOT_CHARGING,
        /** 充电中 */
        CHARGINGING,
        /** 未知状态 */
        UNKNOWN,
        /** 初始化失败 */
        FAILURE;

        static BatteryStatus getBatteryStatus(int batteryStatus) {
            BatteryStatus[] values = BatteryStatus.values();
            return values[batteryStatus];
        }
    }

    /**
     *  头部触摸状态
     */
    public enum HeadTouchStatus {
        /** 未触摸 */
        TOUCH_OFF,
        /** 触摸中 */
        TOUCH_ON,
        /** 未知状态 */
        UNKNOWN,
        /** 初始化失败 */
        FAILURE;
        static HeadTouchStatus getHeadTouchStatus(int headTouchStatus) {
            HeadTouchStatus[] values = HeadTouchStatus.values();
            return values[headTouchStatus];
        }
    }

    /**
     * 净化器状态
     */
    public enum PurifierStatus {
        /** 打开 */
        OPEN,
        /** 关闭 */
        CLOSE,
        /** 不存在 */
        INEXISTENCE,
        /** 净化器滤网不存在*/
        FILTER_GAUZE_INEXISTENCE,
        /** 未知状态 */
        UNKNOWN,
        /** 初始化失败 */
        FAILURE;

        static PurifierStatus getPurifierStatus(int purifierStatus) {
            PurifierStatus[] values = PurifierStatus.values();
            return values[purifierStatus];
        }
    }

    /**
     *  面罩状态
     */
    public enum MaskStatus {
        /** 关闭 */
        CLOSED,
        /** 中间 */
        FLOAT,
        /** 打开 */
        OPENED,
        /** 未知状态 */
        UNKNOWN,
        /** 初始化失败 */
        FAILURE;
        static MaskStatus getMaskStatus(int maskStatus){
            MaskStatus[] values = MaskStatus.values();
            return values[maskStatus];
        }
    }
    /**
     * 托盘状态变化监听
     */
    public enum SalverStatus {
        /** 关闭 */
        CLOSED,
        /** 打开 */
        OPENED,
        /** 未知状态 */
        UNKNOWN,
        /** 初始化失败 */
        FAILURE;
        static SalverStatus getSalverStatus(int salverStatus){
            SalverStatus[] values = SalverStatus.values();
            return values[salverStatus];
        }
    }

}
