package com.asen.callphone.base.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.afollestad.materialdialogs.color.CircleView;
import com.asen.callphone.R;
import com.asen.callphone.base.permission.OnPermission;
import com.asen.callphone.base.permission.PermissionApply;
import com.asen.callphone.base.setting.Constant;
import com.asen.callphone.base.setting.SettingUtil;
import com.asen.callphone.base.util.CommonUtil;
import com.asen.callphone.base.util.IntentUtil;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrInterface;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by asus on 2017/12/15.
 */

public abstract class BaseActivity extends RxAppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        initSlidable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 设置状态栏和标题栏的颜色
        int color = SettingUtil.getInstance().getColor();
        int drawable = R.mipmap.icon_mail_list; // 在栈中显示的图标样式图片选择
        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(CircleView.shiftColorDown(color));
            // 最近任务栏上色
            ActivityManager.TaskDescription tDesc = new ActivityManager.TaskDescription(
                    getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), drawable),
                    color);
            setTaskDescription(tDesc);
            if (SettingUtil.getInstance().getNavBar()) {
                getWindow().setNavigationBarColor(CircleView.shiftColorDown(color));
            } else {
                getWindow().setNavigationBarColor(Color.BLACK);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Fragment 逐个出栈
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            IntentUtil.destroyActivity(this, CommonUtil.enumActionType.ACTION_SIGN_OUT);
        } else {
            getSupportFragmentManager().popBackStack();
            //FragmentUtil.removeFragment(this, mBackHandedFragment, CommonUtil.enumActionType.ACTION_SIGN_OUT2); // 动画销毁
        }
    }


    // region // 全局设置 Toolbar

    /**
     * 初始化 Toolbar
     */
    protected void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnabled);
    }

    // endregion


    // region // 设置 Edittext 之外点击随意地方，隐藏键盘

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();
            if (isShouHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            return super.dispatchTouchEvent(ev);
        }

        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    //     计算 Edittext 之外的位置
    private boolean isShouHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的 location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();

            // 点击的是输入框的区域，保存点击EditText事件
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    // endregion


    // region  // android 授权的方法

    private OnPermission onPermission;

    public void setOnPermission(OnPermission mOnPermission) {
        onPermission = mOnPermission;
    }


    private int mRequestCode;

    /**
     * 请求权限
     *
     * @param permissions 需要的权限列表
     * @param requestCode 请求码
     */
    protected void requestPermission(String[] permissions, int requestCode) {
        mRequestCode = requestCode;
        if (checkPermissions(permissions)) {
            permissionSuccess(requestCode);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), requestCode);
        }
    }

    /**
     * 检查所需的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        // 手机版本 SDK 低于23 ，在Manifest 上注册有效，大于 23 的（android6.0以后的），读取手机的隐私需要在代码动态申请
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取所需权限列表中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            // 检查权限,如果没有授权就添加
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 权限请求成功
     *
     * @param requestCode
     */
    protected void permissionSuccess(int requestCode) {
        Log.d("permissionSuccess====== ", "获取权限成功：" + requestCode);
        if (onPermission != null) {
            onPermission.permissionSuccess(requestCode);
        }
    }

    /**
     * 权限请求失败
     *
     * @param requestCode
     */
    protected void permissionFail(int requestCode) {
        Log.d("permissionFail====== ", "获取权限失败：" + requestCode);
        if (onPermission != null) {
            onPermission.permissionFail(requestCode);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //系统请求权限回调
        if (requestCode == mRequestCode) {
            if (PermissionApply.verifyPermissions(grantResults)) {
                permissionSuccess(mRequestCode);
            } else {
                permissionFail(mRequestCode);
            }
        }
    }

    // endregion


    // region // Slidr 框架实现右滑退出Activity （锁住activity 界面 和开启滑动时间功能）

    private static final String TAG = "BaseActivity";
    protected SlidrInterface slidrInterface;
    private int iconType = -1;

    /**
     * 初始化滑动返回
     */
    protected void initSlidable() {
        int isSlidable = SettingUtil.getInstance().getSlidable(); // 获取滑动状态值
        if (isSlidable != Constant.SLIDABLE_DISABLE) { // 滑动不关闭的时候
            SlidrConfig config = new SlidrConfig.Builder()
                    .edge(isSlidable == Constant.SLIDABLE_EDGE)
                    .build();
            slidrInterface = Slidr.attach(this, config);
        }
    }

    // endregion


}
