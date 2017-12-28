package com.asen.callphone.ui;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.asen.callphone.R;

import java.util.List;

/**
 * 系统设置界面设置界面
 */
public class SystemSettingsActivity extends AppCompatPreferenceActivity {
    /**
     * 更新偏好的摘要以反映其新值的偏好值更改侦听器。
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {

                // 对于列表首选项，请在首选项的“条目”列表中查找正确的显示值。
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                // 设置摘要以反映新值。
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

            } else if (preference instanceof RingtonePreference) {

                // 对于铃声的喜好，查找使用ringtonemanager正确的显示值。
                if (TextUtils.isEmpty(stringValue)) {
                    // 空值对应“静音”（无铃声）
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {

                    Ringtone ringtone = RingtoneManager.getRingtone(preference.getContext(), Uri.parse(stringValue));
                    if (ringtone == null) {
                        // 如果有查找错误，请清除摘要。
                        preference.setSummary(null);
                    } else {
                        // 设置摘要以反映新的铃声显示名称。
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }

                }

            } else {
                // 对于所有其他首选项，将概要设置为值的简单字符串表示形式。
                preference.setSummary(stringValue);
            }
            return true;
        }
    };


    /**
     * 将偏好的摘要绑定到其值。更具体地说，当更改偏好值时，
     * 它的摘要（标题下的文本行）会更新以反映值。在调用此方法时，
     * 概要也会立即更新。精确的显示格式取决于首选项的类型。
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // 设置侦听器以观察值变化。
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // 立即用首选项的当前值触发侦听器。
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 在动作栏中显示UP按钮。
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * 帮助方法确定设备是否有超大屏幕。例如，“10”片剂是特大型的。
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * 点击三个 Fragment 的方法进入事件判断
     * 此方法停止恶意应用程序中的碎片(fragment)注入。确保在这里拒绝任何未知片段。
     */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * 此片段只显示一般偏好。它在activity显示两个窗格设置UI时使用。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // 结合编辑/列表/对话/铃声偏好总结他们的价值观。
            // 当他们的价值观发生变化时，他们的总结被更新以反映新的价值，根据Android的设计准则。
            bindPreferenceSummaryToValue(findPreference("example_text"));
            bindPreferenceSummaryToValue(findPreference("example_list"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SystemSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 此片段仅显示通知首选项。它是在activity显示两个窗格设置UI。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // 结合编辑/列表/对话/铃声偏好总结他们的价值观。
            // 当他们的价值观发生变化时，他们的总结被更新以反映新的价值，根据Android的设计准则。
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SystemSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 此片段只显示数据和同步首选项。它在activity显示两个窗格设置UI时使用。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DataSyncPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_data_sync);
            setHasOptionsMenu(true);

            // 结合编辑/列表/对话/铃声偏好总结他们的价值观。
            // 当他们的价值观发生变化时，他们的总结被更新以反映新的价值，根据Android的设计准则。
            bindPreferenceSummaryToValue(findPreference("sync_frequency"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SystemSettingsActivity.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

}
