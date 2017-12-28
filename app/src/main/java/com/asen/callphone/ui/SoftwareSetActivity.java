package com.asen.callphone.ui;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.afollestad.materialdialogs.color.CircleView;
import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.asen.callphone.R;
import com.asen.callphone.base.app.BaseActivity;
import com.asen.callphone.base.setting.SettingUtil;
import com.asen.callphone.base.util.IntentAction;
import com.asen.callphone.fragment.software_setting.SoftwareSetFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by asus on 2017/12/12.
 */

public class SoftwareSetActivity extends BaseActivity implements ColorChooserDialog.ColorCallback{

    public static final String EXTRA_SHOW_FRAGMENT = "show_fragment";                // 正在显示/已经显示的 Fragment
    public static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = "show_fragment_args"; // Fragment 参数
    public static final String EXTRA_SHOW_FRAGMENT_TITLE = "show_fragment_title";    // Fragment 标题


    // region // 初始化控件

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.container)
    FrameLayout mContainer;

    // endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_software_set);
        ButterKnife.bind(this);
        initView(savedInstanceState);
    }

    public void initView(Bundle savedInstanceState) {

        String initFragment = getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT);
        Bundle initArguments = getIntent().getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS);
        String initTitle = getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT_TITLE);

        // 第一次进入的时候为null
        if (TextUtils.isEmpty(initFragment)) {
            setupFragment(SoftwareSetFragment.class.getName(), initArguments);
        } else {
            setupFragment(initFragment, initArguments);
        }

        // 初始化 Toolbar
        initToolBar(mToolbar, true, TextUtils.isEmpty(initTitle) ? "软件设置" : initTitle);

    }

    private void setupFragment(String fragmentName, Bundle args) {
        Fragment fragment = Fragment.instantiate(this, fragmentName, args);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.container, fragment);
        transaction.commitAllowingStateLoss();
    }


    // region // 在 SoftwareSetFragment 之后的加载新的 Fragment

    public void startWithFragment(String fragmentName, Bundle args,
                                  Fragment resultTo, int resultRequestCode, String title) {
        Intent intent = onBuildStartFragmentIntent(fragmentName, args, title);
        if (resultTo == null) {
            startActivity(intent);
        } else {
            resultTo.startActivityForResult(intent, resultRequestCode);
        }
    }

    public Intent onBuildStartFragmentIntent(String fragmentName, Bundle args, String title) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(this, getClass());
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentName);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_TITLE, title);
        return intent;
    }

    // endregion


    // region // 分享功能

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_share) {
            IntentAction.send(this, "分享通讯录app");
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.toolbar, R.id.appBarLayout, R.id.container})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:
                break;
            case R.id.appBarLayout:
                break;
            case R.id.container:
                break;
        }
    }

    // endregion

    // region // 颜色监听事件返回

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(selectedColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // 状态栏上色
            getWindow().setStatusBarColor(CircleView.shiftColorDown(selectedColor));
            // 最近任务栏上色
            ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(
                    getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.icon_mail_list),
                    selectedColor);
            setTaskDescription(tDesc);
            // 导航栏上色
            if (SettingUtil.getInstance().getNavBar()) {
                getWindow().setNavigationBarColor(CircleView.shiftColorDown(selectedColor));
            } else {
                getWindow().setNavigationBarColor(Color.BLACK);
            }
        }
        if (!dialog.isAccentMode()) {
            SettingUtil.getInstance().setColor(selectedColor);
        }
    }

    @Override
    public void onColorChooserDismissed(@NonNull ColorChooserDialog dialog) {

    }

    // endregion


}
