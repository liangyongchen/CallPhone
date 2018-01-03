package com.asen.callphone;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.asen.callphone.adapter.CallPhoneAdapter;
import com.asen.callphone.base.app.BaseActivity;
import com.asen.callphone.base.app.StaticInfo;
import com.asen.callphone.base.permission.PermissionApply;
import com.asen.callphone.base.setting.SettingUtil;
import com.asen.callphone.base.util.CommonUtil;
import com.asen.callphone.base.util.IntentUtil;
import com.asen.callphone.base.view.dailog.MessageDialogUtil;
import com.asen.callphone.bll.CallPhoneBll;
import com.asen.callphone.db.DBUtil;
import com.asen.callphone.model.CallPhoneModel;
import com.asen.callphone.ui.AboutActivity;
import com.asen.callphone.ui.AddContactsActivity;
import com.asen.callphone.ui.SoftwareSetActivity;
import com.asen.callphone.ui.SystemSettingsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, CallPhoneAdapter.OnLongItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.listView)
    RecyclerView mListView;
    @BindView(R.id.content_main)
    RelativeLayout mContentMain;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    @BindView(R.id.switchNightMode)
    Switch mSwitchNightMode;
    @BindView(R.id.navigationView)
    NavigationView mNavigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private CallPhoneAdapter mAdapter;

    private List<CallPhoneModel> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToobar();
        initEvent();
        // 通讯录、SD卡授权、手机型号权限请求码
        requestPermission(PermissionApply.PERMISSION_READ_CONTACTS_and_STORAGE, PermissionApply.PERMISSION_READ_CONTACTS_and_STORAGE_KEY);

    }

    @Override
    protected void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        StaticInfo.getStaticInfo(this); //  初始化设备信息
        initHeader();
        initAdapter();
    }

    @Override
    protected void permissionFail(int requestCode) {
        super.permissionFail(requestCode);

        // 提示是否再次授权，同意再次获取，不同意，直接退出界面，销毁Activity
        // 线程阻塞
        int result = MessageDialogUtil.ShowComfirm(this, getResources().getString(R.string.permissionFail));
        if (result == 1) { // 1 确定 0，取消
            // 通讯录授权申请
            requestPermission(PermissionApply.PERMISSION_READ_CONTACTS_and_STORAGE, PermissionApply.PERMISSION_READ_CONTACTS_and_STORAGE_KEY);
        } else {
            // finish();
        }

    }

    private void initToobar() {

        mFab.setBackgroundTintList(ColorStateList.valueOf(SettingUtil.getInstance().getColor()));

        mToolbar.setTitle("通讯录");
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED); // 打开手势滑动(可以不用设置，默认为该模式)
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState(); // 左上角的三行图标设置
        // 侧面item监听
        mNavigationView.setNavigationItemSelectedListener(this);

        // 双击 Toolbar
        final GestureDetector detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                mListView.setItemViewCacheSize(0);
                return super.onDoubleTap(e);
            }
        });


        mToolbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });

        mToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "双击标题栏快速返回顶部", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initAdapter() {

        List<CallPhoneModel> list = new ArrayList<>();
        list.addAll(CallPhoneBll.getContact(this));
        list.addAll(DBUtil.getInstance(this).selectAll());

        mData.removeAll(mData);
        mData.addAll(list);

        // 添加手机设备通讯录电话号码
        // mAdapter.addItem(CallPhoneBll.getContact(this));
        // 添加app存储的手机联系人

        // 添加SD卡文件存储的手机联系人

        // 初始化适配器
        mAdapter = new CallPhoneAdapter(this, mData);
        mListView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        mListView.setAdapter(mAdapter);
        mAdapter.setOnLongItemClickListener(this);
    }

    private void initHeader() {

        View headerView = mNavigationView.getHeaderView(0);
        ImageView avatar = (ImageView) headerView.findViewById(R.id.nav_header_image);
        TextView username = (TextView) headerView.findViewById(R.id.nav_header_name);
        TextView tagline = (TextView) headerView.findViewById(R.id.nav_header_tagline);
        username.setText("品牌:" + Build.MANUFACTURER + " \n型号:" + Build.MODEL + "\n系统:" + Build.VERSION.RELEASE);
        tagline.setText("V " + StaticInfo.getVersionName());

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "头像替换", Toast.LENGTH_SHORT).show();
            }
        });

        // 获取上一次设置的状态
        mSwitchNightMode.setChecked(SettingUtil.getInstance().getIsNightMode());
        mSwitchNightMode.setText(SettingUtil.getInstance().getIsNightMode() == true ? "夜间模式" : "日间模式");
        mSwitchNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (mSwitchNightMode.isChecked()) {
                    if (mode != Configuration.UI_MODE_NIGHT_YES) {
                        SettingUtil.getInstance().setIsNightMode(true);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                } else {
                    if (mode == Configuration.UI_MODE_NIGHT_YES) {
                        SettingUtil.getInstance().setIsNightMode(false);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                }
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();
            }
        });

        mSwitchNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                if (isChecked) {
                    if (mode != Configuration.UI_MODE_NIGHT_YES) {
                        SettingUtil.getInstance().setIsNightMode(true);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                } else {
                    if (mode == Configuration.UI_MODE_NIGHT_YES) {
                        SettingUtil.getInstance().setIsNightMode(false);
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                }
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();
            }
        });

    }

    private void initEvent() {

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "欢饮使用个人app - 通讯录", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    // region // menu 通知设置选项

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                // Toast.makeText(this, "系统设置", Toast.LENGTH_SHORT).show();
                IntentUtil.startActivityForResult(this, SystemSettingsActivity.class, IntentUtil.AC_AddContacts, CommonUtil.enumActionType.ACTION_FADE_IN);
                break;
            case R.id.action_notification:
                Toast.makeText(this, "通知", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_add:
                IntentUtil.startActivityForResult(this, AddContactsActivity.class, IntentUtil.AC_AddContacts, CommonUtil.enumActionType.ACTION_FADE_IN);
                // Toast.makeText(this, "添加联系人", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    // endregion

    // region // NavigationView 控件设置我的主界面的登录功能

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.equipmentContact: // 设备通讯录
                Toast.makeText(this, "设备通讯录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.appContact:       // app通讯录
                Toast.makeText(this, "app通讯录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.SDCardContact:    // SD卡通讯录
                Toast.makeText(this, "SD卡通讯录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:            // 关于我们
                IntentUtil.startActivityForResult(this, AboutActivity.class, IntentUtil.AC_About, CommonUtil.enumActionType.ACTION_FORWARD);
                break;
            case R.id.setting:          // 软件设置
                IntentUtil.startActivityForResult(this, SoftwareSetActivity.class, IntentUtil.AC_Setting, CommonUtil.enumActionType.ACTION_FORWARD);
                // Toast.makeText(this, "软件设置", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    // endregion


    // 长按某一个mData 里面的 一个 item 进行事件处理

    @Override
    public void onLongItemClick(View v, int postion) {

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IntentUtil.AC_Setting:
                mFab.setBackgroundTintList(ColorStateList.valueOf(SettingUtil.getInstance().getColor()));
                break;
        }
    }
}
