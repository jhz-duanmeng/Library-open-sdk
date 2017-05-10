package com.efrobot.library.net;

/**
 * Created by zhangyun on 2016/10/27.
 */

public class UrlConstants {

    /**
     * debug模式开关.
     */
    public static boolean DEBUG = true;

    /**
     * sdk的版本号.
     */
    private static String VERSION = "v1";

//    /**
//     * 服务器端主机.测试
//     */
//    private static final String HOST_IP = "http://192.168.15.167/openplatform-app/";
    /**
     * 服务器端主机.正式
     */
    private static final String HOST_IP = "http://openplatform.efrobot.com/";

    /**
     * sdk授权地址 .
     */
    public static final String SDK_AUTH_URL =HOST_IP + "sdkAuth/authentication" ;

    /**
     * 获取sdk的版本号.
     *
     * @return sdk版本号
     */
    public static String getVERSION() {
        return VERSION;
    }
}
