package com.asen.callphone.base.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatDelegate;

import com.asen.callphone.base.exception.CrashHandler;
import com.asen.callphone.base.setting.SettingUtil;
import com.asen.callphone.base.util.ImageDisplayUtil;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by asus on 2017/9/22.
 */

public class BaseApp extends Application {

    private static String TAG = "";

    private static Context mContext;

    public static synchronized BaseApp getContext() {
        return (BaseApp) mContext;
    }

    // region  // 阿里巴巴字体图标

    private Typeface iconTypeFace;

    public Typeface getIconTypeFace() {
        return iconTypeFace;
    }

    // endregion

    // region // 网络请求（OK3）

    private volatile static OkHttpClient client;

    public static OkHttpClient getClient() {
        if (client == null) {
            synchronized (OkHttpClient.class) {
                if (client == null) {
                    client = new OkHttpClient.Builder().connectTimeout(5 * 60, TimeUnit.SECONDS).readTimeout(5 * 60, TimeUnit.SECONDS).build();
                }
            }
        }
        return client;
    }

    // endregion

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();

        // debug 模式
        DebugLog.LOG_ENABLE = true;

        // 全程捕获异常信息
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        getClient(); // 初始化OK3

        // 设置阿里巴巴矢量字体初始化，防止在Activity加载造成卡顿现象
        this.iconTypeFace = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");

        // 图片加载初始化
        ImageDisplayUtil.initImageLoaderConfiguration(this);

        // 主题设置
        initTheme();

    }

    // 设置 app 的 主题 是什么模式
    private void initTheme() {

        SettingUtil sUtil = SettingUtil.getInstance();

        // 判断是否是自动模式
        if (sUtil.getIsAutoNightMode()) {

            // 获取设置的时间
            int nightStartHour = Integer.parseInt(sUtil.getNightStartHour());
            int nightStartMinute = Integer.parseInt(sUtil.getNightStartMinute());
            int dayStartHour = Integer.parseInt(sUtil.getDayStartHour());
            int dayStartMinute = Integer.parseInt(sUtil.getDayStartMinute());

            // 获取当前的时间
            Calendar calendar = Calendar.getInstance(); // 日历
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // 24小时制
            int currentMinute = calendar.get(Calendar.MINUTE);    // 分钟

            int nightValue = nightStartHour * 60 + nightStartMinute;
            int dayValue = dayStartHour * 60 + dayStartMinute;
            int currentValue = currentHour * 60 + currentMinute;

            // 当设置的夜间时间比日间时间大时:当前时间要大于日间小于夜间为日间
            if (nightValue >= dayValue) {
                // 日间模式
                if (currentValue >= dayValue && currentValue <= nightValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sUtil.setIsNightMode(false);
                } else { // 夜间模式
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sUtil.setIsNightMode(true);
                }
            } else { // 当设置的日间时间比夜间时间大时：当前时间要大于夜间小于日间
                // 夜间模式
                if (currentValue >= nightValue && currentValue <= dayValue) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sUtil.setIsNightMode(true);
                } else { // 日间模式
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sUtil.setIsNightMode(false);
                }
            }

        } else {
            // 获取当前主题
            if (sUtil.getIsNightMode()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }

    }


}
