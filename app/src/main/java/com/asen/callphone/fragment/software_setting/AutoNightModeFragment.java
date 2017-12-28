package com.asen.callphone.fragment.software_setting;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.widget.TimePicker;

import com.asen.callphone.R;
import com.asen.callphone.base.setting.SettingUtil;


/**
 * Created by asus on 2017/12/13.
 */

public class AutoNightModeFragment extends PreferenceFragment {

    private SettingUtil setUtil = SettingUtil.getInstance();
    private String nightStartHour, nightStartMinute; // 夜间时分
    private String dayStartHour, dayStartMinute;     // 日间时分

    private Preference autoNight, autoDay; // 自动 夜/日间

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_software_autonight);
        setHasOptionsMenu(true);
        init();
        initEvent();

    }

    private void init() {
        // 绑定 xml 控件的 key 值
        autoNight = findPreference("auto_night");
        autoDay = findPreference("auto_day");
        setText();
    }

    private void initEvent() {

        autoNight.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                TimePickerDialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hour, int minute) {
                        // 单数/双数 判断
                        setUtil.setNightStartHour(hour > 9 ? "" + hour : "0" + hour);
                        setUtil.setNightStartMinute(minute > 9 ? "" + minute : "0" + minute);
                        setText();
                    }
                }, Integer.parseInt(nightStartHour), Integer.parseInt(nightStartMinute), true);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("完成");
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
                return false;
            }
        });

        autoDay.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                TimePickerDialog dialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                // 单数/双数 判断
                                setUtil.setDayStartHour(hour > 9 ? "" + hour : "0" + hour);
                                setUtil.setDayStartMinute(minute > 9 ? "" + minute : "0" + minute);
                                setText();
                            }
                        }, Integer.parseInt(dayStartHour), Integer.parseInt(dayStartMinute), true);
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setText("完成");
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setText("取消");
                return false;
            }
        });

    }

    private void setText() {
        nightStartHour = setUtil.getNightStartHour();
        nightStartMinute = setUtil.getNightStartMinute();
        dayStartHour = setUtil.getDayStartHour();
        dayStartMinute = setUtil.getDayStartMinute();

        autoNight.setSummary(nightStartHour + ":" + nightStartMinute);
        autoDay.setSummary(dayStartHour + ":" + dayStartMinute);
    }

}
