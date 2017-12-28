package com.asen.callphone.base.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.File;

/**
 * Created by asus on 2017/7/18.
 */

public class StaticInfo {

    // region
    // endregion

    // region 河马特有项目

    protected static String appName = "CallPhone";

    public static boolean DEBUG = true;

    // 登录判断
    public final static String LOGIN_RESULT_KEY = "LOGIN_RESULT"; // 判断是否之前已经登录过的key
    public static boolean LOGIN_RESULT; // 判断是否之前已经登录过的key

    // endregion


    // region 单例模式


    private static StaticInfo mStaticInfo;

    private StaticInfo(Context mContext) {
        setAndroidDevice(mContext);
        setAndroidVersion(mContext);
        getSharedAndroidVersion(mContext);
    }

    public static StaticInfo getStaticInfo(Context mContext) {
        if (mStaticInfo == null) {
            mStaticInfo = new StaticInfo(mContext);
        }
        return mStaticInfo;
    }


    /**
     * 优化内存，释放静态变量
     */
    public static void releaseResources() {
        if (mStaticInfo != null) {
            mStaticInfo = null;
        }
    }


    // endregion


    // region SharedPreferences 数据库

    /**
     * 存储本地数据Name(公用)
     */
    public static final String SharedData_Name = "SharedData_Name";

    /**
     * 设置String数据保存于手机
     */
    public static boolean setSharedDataString_Key_Value(Context context, String k, String v) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(StaticInfo.SharedData_Name, Context.MODE_PRIVATE).edit();
            editor.putString(k, v);
            editor.commit();
            return true;
        } catch (Exception ex) {
            Log.e(ex.toString(), ex.getMessage());
            return false;
        }
    }

    /**
     * 设置String数据保存于手机
     */
    public static boolean setSharedDataBoolean_Key_Value(Context context, String k, boolean v) {
        try {
            SharedPreferences.Editor editor = context.getSharedPreferences(StaticInfo.SharedData_Name, Context.MODE_PRIVATE).edit();
            editor.putBoolean(k, v);
            editor.commit();
            return true;
        } catch (Exception ex) {
            Log.e(ex.toString(), ex.getMessage());
            return false;
        }
    }

    /**
     * 获取保存于手机的数据
     */
    public static String getSharedDataString_Key(Context context, String Key) {
        return context.getSharedPreferences(StaticInfo.SharedData_Name, Context.MODE_PRIVATE).getString(Key, defaultValue);
    }

    /**
     * 获取保存于手机的数据
     */
    public static boolean getSharedDataBoolean_Key(Context context, String Key) {
        return context.getSharedPreferences(StaticInfo.SharedData_Name, Context.MODE_PRIVATE).getBoolean(Key, false);
    }

    // endregion


    // region 获取手机设备号

    /**
     * 手机设备号IMEI
     */
    private static String IMEI;

    private static void setAndroidDevice(Context mContext) {
        StaticInfo.IMEI = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static String getAndroidDevice() {
        return StaticInfo.IMEI;
    }

    // endregion


    // region  当前版本号信息

    /**
     * APP版本号
     */
    public static final String Android_Version_RELEASE = Build.VERSION.RELEASE;

    /**
     * 用户安卓系统版本编号（SDK） 例如：16（4.1.1）
     */
    public static final int Android_Version_SDK_INI = Build.VERSION.SDK_INT;

    /**
     * 当前APK版本号Code、Name
     */
    protected static int VersionCode = -9999;
    protected static String VersionName;

    private static void setAndroidVersion(Context mContext) {

//        if (VersionCode != -9999 && !StringUtils.isEmpty(VersionName)) {
//            return;
//        }

        try {

            PackageManager packageManager = mContext.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            StaticInfo.VersionName = packInfo.versionName;
            StaticInfo.VersionCode = packInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getVersionCode() {
        return VersionCode;
    }

    public static String getVersionName() {
        return VersionName;
    }

    // endregion


    // region SharedData存储的版本号

    /**
     * 数据库APP版本号
     */
    public static String Key_SharedDataVersion = "Key_SharedDataVersion";
    public static String Value_SharedDataVersion;
    protected static final String defaultValue = ""; // 获取String为null就肤质這个参数

    // 获取APK存储的版本号
    public static void getSharedAndroidVersion(Context mContext) {
        StaticInfo.Value_SharedDataVersion = mContext
                .getSharedPreferences(StaticInfo.SharedData_Name, Context.MODE_PRIVATE)
                .getString(StaticInfo.Key_SharedDataVersion, defaultValue);

        if (StaticInfo.Value_SharedDataVersion == null || StaticInfo.Value_SharedDataVersion.isEmpty()) {
            StaticInfo.Value_SharedDataVersion = StaticInfo.VersionName;
            setSharedDataString_Key_Value(mContext, StaticInfo.Key_SharedDataVersion, StaticInfo.Value_SharedDataVersion);
        }

    }


    // endregion


    // region   SD卡存储

    /**
     * SD卡存储路径
     */
    protected static String sdCard_FilePath;

    /**
     * 获取SD卡存储路径<br/>
     * 输出不带斜杠 (例子: "/sdcard/asen框架")
     *
     * @return
     */
    public static String GetSDCard_FilePath() {
        if (StaticInfo.sdCard_FilePath == null || StaticInfo.sdCard_FilePath.isEmpty()) {
            File path = Environment.getExternalStorageDirectory();
            StaticInfo.sdCard_FilePath = path.toString() + "/" + StaticInfo.appName;
        }

        return StaticInfo.sdCard_FilePath;
    }

    /**
     * SD卡错误日志存储路径
     */
    protected static String sdCard_ErrorLogFilePath;

    /**
     * 获取SD卡错误日志存储路径<br/>
     * 输出不带斜杠 (例子: "/sdcard/Asen安卓框架/ErrorLogs")
     *
     * @return
     */
    public static String GetSDCard_ErrorLogFilePath() {
        if (StaticInfo.sdCard_ErrorLogFilePath == null || StaticInfo.sdCard_ErrorLogFilePath.isEmpty()) {
            StaticInfo.sdCard_ErrorLogFilePath = StaticInfo.GetSDCard_FilePath() + "/ErrorLogs";
        }
        return StaticInfo.sdCard_ErrorLogFilePath;
    }


    // endregion


}
