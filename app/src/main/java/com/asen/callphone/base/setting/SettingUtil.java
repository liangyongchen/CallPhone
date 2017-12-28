package com.asen.callphone.base.setting;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import com.asen.callphone.R;
import com.asen.callphone.base.app.BaseApp;


/**
 * Created by asus on 2017/12/15.
 */

public class SettingUtil {

    private SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(BaseApp.getContext());

    public static SettingUtil getInstance() {
        return SettingsUtilInstance.instance;
    }

    private static final class SettingsUtilInstance {
        private static final SettingUtil instance = new SettingUtil();
    }



    // region // 自动切换 — 日夜间模式 ：时间设置


    // region // 日夜间模式设置 — key : 没有绑定 xml 布局的 key 值

    /**
     * 设置夜间模式
     * <p>
     * key:没有绑定xml布局的key值
     */
    public void setIsNightMode(boolean flag) {
        setting.edit().putBoolean("switch_nightMode", flag).apply();
    }

    /**
     * 获取夜间模式—是否开启
     * <p>
     * key:没有绑定xml布局的key值
     */
    public boolean getIsNightMode() {
        return setting.getBoolean("switch_nightMode", false);
    }

    // endregion

    // region // 时间设置 — key : 没有绑定 xml 布局的 key 值

    // 夜间 — 时
    public String getNightStartHour() {
        return setting.getString("night_startHour", "22");
    }

    public void setNightStartHour(String nightStartHour) {
        setting.edit().putString("night_startHour", nightStartHour).apply();
    }

    // 夜间 — 分
    public String getNightStartMinute() {
        return setting.getString("night_startMinute", "00");
    }

    public void setNightStartMinute(String nightStartMinute) {
        setting.edit().putString("night_startMinute", nightStartMinute).apply();
    }

    // 日间 — 时
    public String getDayStartHour() {
        return setting.getString("day_startHour", "06");
    }

    public void setDayStartHour(String dayStartHour) {
        setting.edit().putString("day_startHour", dayStartHour).apply();
    }

    // 日间 — 分
    public String getDayStartMinute() {
        return setting.getString("day_startMinute", "00");
    }

    public void setDayStartMinute(String dayStartMinute) {
        setting.edit().putString("day_startMinute", dayStartMinute).apply();
    }

    // endregion

    // region // 自动切换日/夜间模式设置 — key : 有绑定 xml 布局的 key 值

    /**
     * 获取 自动切换夜间模式 是否开启
     * key : 有绑定 xml 布局的 key 值
     */
    public boolean getIsAutoNightMode() {
        return setting.getBoolean("auto_nightMode", false);
    }

    /**
     * 获取是否开启自动切换夜间模式
     */
    public void setIsAutoNightMode(boolean flag) {
        setting.edit().putBoolean("auto_nightMode", flag).apply();
    }

    // endregion


    // endregion


    // region // 获取是否开启导航栏上色 （xml 有 key 值）

    /**
     * 获取是否开启导航栏上色 （xml 有 key 值）
     */
    public boolean getNavBar() {
        return setting.getBoolean("nav_bar", false);
    }

    // endregion


    // region // 主题颜色 获取/设置，在xml里面设置 （xml 中有 绑定 key 值）

    /**
     * 获取主题颜色 （xml 中有 绑定 key 值）
     */
    public int getColor() {
        // 获取当前日间/夜间模式的颜色值
        int defaultColor = BaseApp.getContext().getResources().getColor(R.color.colorPrimary);
        int color = setting.getInt("color", defaultColor); // 如果手动设置有颜色就不需要 defaultColor 颜色
        if ((color != 0) && Color.alpha(color) != 255) {
            return defaultColor;
        }
        return color;
    }

    /**
     * 设置主题颜色
     */
    public void setColor(int color) {
        setting.edit().putInt("color", color).apply();
    }

    // endregion


    // region // 获取滑动返回值 (key:xml有key值)

    /**
     * 获取滑动返回值 (key:xml有key值)
     * 获取在xml的 Preferences 里面设置的值
     */
    public int getSlidable() {
        String s = setting.getString("slidable", "0"); // 默认不能滑动
        return Integer.parseInt(s);
    }

    // endregion


    // region // 设置字体大小 （xml 没有 key 绑定）

    /**
     * 获取字体大小（XMl 没有 key 值）
     */
    public int getTextSize() {
        return setting.getInt("textsize", 16);
    }

    /**
     * 设置字体大小
     */
    public void setTextSize(int textSize) {
        setting.edit().putInt("textsize", textSize).apply();
    }

    // endregion

}
