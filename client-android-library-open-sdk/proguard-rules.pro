# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Software\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------
   -keep class com.efrobot.library.speechsdk.** { *; }
   -keep class com.efrobot.library.message.** { *; }
   -keep class com.efrobot.library.RobotStatus{*;}
   -keep class com.efrobot.library.RobotManager{*;}
   -keep class com.efrobot.library.utils.FaceType{*;}
   -keep class com.efrobot.library.OnInitCompleteListener{*;}
   -keep class com.efrobot.library.OnInitUnLinkListener{*;}
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
-keepnames public class com.efrobot.library.RobotManager,com.efrobot.library.RobotManager$* {
    public <fields>;
    public <methods>;
}
-keepnames public class com.efrobot.library.RobotStatus,com.efrobot.library.RobotStatus* {
    public <fields>;
    public <methods>;
}
#-keepnames public class com.efrobot.library.OnInitUnLinkListener
#    public <fields>;
#    public <methods>;
#}
##不要混淆PayTask里面的内部类PayResultCallBack的所以方法和属性
#-keep class com.efrobot.library.OnInitUnLinkListenerultCode {
#    *;
#}



#不要混淆PayTask类的所有public属性和方法
-keep public class com.efrobot.library.pay.PayTask {
    public <fields>;
    public <methods>;
}


#不要混淆PayTask里面的内部类PayResultCallBack的所以方法和属性
-keep interface com.efrobot.library.pay.PayTask$IPayResultCallBack {
    *;
}
#除了类名和getResultStatus、getResult、getMemo方法不混淆其他的混淆，**代表返回值，可以代表基本数据类型，对象没试过
#如果返回的是对象，这个**就换成那个对象的全路径
-keepclassmembers class com.efrobot.paylibrary.PayResult {
   	public ** getResultCode();
   	public ** getData();
   	public ** getAppId();
   	public ** getDevId();
   	public ** getTimestamp();
   	public ** getOutTradeNo();
   	public ** getTradeNo();
   	public ** getTotalAmount();
   	public ** getEnvironment();
}
#这个返回值是void类型，类型是public，public void的方法要去掉修饰符，参数的类型要全路径
-keepclassmembers class com.efrobot.paylibrary.OrderInfo {
   void setProductCode(java.lang.String);
   void setTotalAmount(java.lang.String) ;
   void setOutTradeNo(java.lang.String);
   void setSubject(java.lang.String);
   void setTimestamp(java.lang.String);
   void setNotifyUrl(java.lang.String);
   void setBody(java.lang.String);
   void setDevId(java.lang.String);
   void setAppId(java.lang.String);
   void setSign(java.lang.String);
   void setPrivateKey(java.lang.String);
}
-keep class org.apache.ws.commons.** { *; }
-keep class org.apache.commons.logging.** { *; }
-keep class std_msgs.** { *; }
-keep class rosgraph_msgs.** { *; }

-keep class com.squareup.okhttp.** { *; }


-keep class com.google.common.** { *; }
-keep class javax.annotation.** { *; }
-keep class org.** { *; }
-keep class com.efrobot.library.task.** { *;}

-dontwarn org.apache.commons.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement