package com.asen.callphone.fragment.software_setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.asen.callphone.R;
import com.asen.callphone.base.setting.SettingUtil;
import com.asen.callphone.base.util.CacheDataManager;
import com.asen.callphone.preferences.IconPreference;
import com.asen.callphone.ui.SoftwareSetActivity;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.BSD3ClauseLicense;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;


/**
 * Created by asus on 2017/12/12.
 */

public class SoftwareSetFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private IconPreference colorPreview;
    SoftwareSetActivity context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_software_set);
        context = (SoftwareSetActivity) getActivity();
        setHasOptionsMenu(true);
        setClearCacheText();
        initEvent();
    }

    private void initEvent() {

        // region // 自动切换夜间模式

        findPreference("auto_nightMode").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // 设置有返回该 Fragment 界面的回调方法 onActivityResult
                context.startWithFragment(AutoNightModeFragment.class.getName(), null, SoftwareSetFragment.this, 0, null);
                return true;
            }
        });

        // endregion

        // region // 设置字体大小（可以自定义控件,把共同属性设置在一起）

        findPreference("text_size").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                context.startWithFragment(TextSizeFragment.class.getName(), null, null, 0, null);
                return true;
            }
        });

        // endregion

        // region // 主题颜色

        findPreference("color").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                // 打开颜色选择对话框
                new ColorChooserDialog.Builder(context, R.string.choose_theme_color)
                        .backButton(R.string.back)
                        .cancelButton(R.string.cancel)
                        .doneButton(R.string.done)
                        .customButton(R.string.custom)
                        .presetsButton(R.string.back)
                        .allowUserColorInputAlpha(false)
                        .show();
                return false;
            }
        });

        colorPreview = (IconPreference) findPreference("color");

        // endregion

        // region // 导航栏颜色设置

        findPreference("nav_bar").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                int color = SettingUtil.getInstance().getColor();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (SettingUtil.getInstance().getNavBar()) {
                        context.getWindow().setNavigationBarColor(CircleView.shiftColorDown(CircleView.shiftColorDown(color)));
                    } else {
                        context.getWindow().setNavigationBarColor(Color.BLACK);
                    }
                }
                return false;
            }
        });

        // endregion

        // region // 清理缓存

        findPreference("clearCache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                CacheDataManager.clearAllCache(context);
                Snackbar.make(getView(), R.string.clear_cache_successfully, Snackbar.LENGTH_SHORT).show();
                setClearCacheText();
                return false;
            }
        });

        // endregion

        // region // 版本信息

        try {
            String version = "当前版本 " + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            findPreference("version").setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // endregion

        // region // 开源许可

        findPreference("licenses").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                createLicenseDialog();
                return false;
            }
        });

        // endregion

    }

    private void setClearCacheText() {
        try {
            findPreference("clearCache").setSummary(CacheDataManager.getTotalCacheSize(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLicenseDialog() {
        Notices notices = new Notices();
        notices.addNotice(new Notice("PhotoView", "https://github.com/chrisbanes/PhotoView", "Copyright 2017 Chris Banes", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("OkHttp", "https://github.com/square/okhttp", "Copyright 2016 Square, Inc.", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Gson", "https://github.com/google/gson", "Copyright 2008 Google Inc.", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Glide", "https://github.com/bumptech/glide", "Sam Judd - @sjudd on GitHub, @samajudd on Twitter", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("Stetho", "https://github.com/facebook/stetho", "Copyright (c) 2015, Facebook, Inc. All rights reserved.", new BSD3ClauseLicense()));
        notices.addNotice(new Notice("PersistentCookieJar", "https://github.com/franmontiel/PersistentCookieJar", "Copyright 2016 Francisco José Montiel Navarro", new ApacheSoftwareLicense20()));
        notices.addNotice(new Notice("jsoup", "https://jsoup.org", "Copyright © 2009 - 2016 Jonathan Hedley (jonathan@hedley.net)", new MITLicense()));

        new LicensesDialog.Builder(context)
                .setNotices(notices)
                .setIncludeOwnLicense(true)
                .build()
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 开启广播事件监听所有的 SharedPreferences 的改变状态
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // 监听返回的 key 是 xml 上面绑定哪个的 Preference
        switch (key) {
            case "color":
                colorPreview.setView();
                break;
            case "slidable":
                context.recreate(); // 刷新状态
                break;
        }
    }

}
