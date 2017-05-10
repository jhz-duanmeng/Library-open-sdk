package com.efrobot.sdk.control_sdk_sample.ui.base;

/**
 * Created by Administrator on 2016/7/4.
 */
public class TitileBarConfig {
    private static int mTitleBarRootLayout;
    public  static  boolean useTitleBar;
    
    public static int getTitleBarRootLayout() {
        return mTitleBarRootLayout;
    }
    
    public static void setTitleBarRootLayout(int titleBarRootLayout) {
        useTitleBar=true;
        mTitleBarRootLayout = titleBarRootLayout;
    }
    
    
}
