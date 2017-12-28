package com.asen.callphone.base.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;


/**
 * Created by pc on 2017/2/24.
 */

public class IntentUtil {

    // region
    // endregion


    // 使命·实名认证界面跳转编码
    public final static int AC_AddContacts = 1000;  // 新增联系人
    public final static int AC_Setting = 1001;      // 软件设置界面
    public final static int AC_About = 1002;        // 关于我们

    // region    // Activity页面跳转


    /**
     * 直接跳转
     *
     * @param ac         本页面Activity
     * @param cls        需要跳转到Activity
     * @param ActionType 跳转的动画
     */
    public static void startActivity(Activity ac, Class<?> cls, CommonUtil.enumActionType ActionType) {
        try {
            Intent intent = new Intent(ac, cls);
            ac.startActivity(intent);
            AnimUtil.ActivityDynamic(ac, ActionType);
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    /**
     * 传参跳转
     *
     * @param ac         现在UI
     * @param cls        需要跳转到的UI
     * @param bu         传到 cls 的参数
     * @param ActionType 跳转的动画
     */
    public static void startActivity(Activity ac, Class<?> cls, Bundle bu, CommonUtil.enumActionType ActionType) {
        try {
            Intent intent = new Intent(ac, cls);
            intent.putExtras(bu);
            ac.startActivity(intent);
            AnimUtil.ActivityDynamic(ac, ActionType);
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    /**
     * 传参跳转，返回上一层UI可调用 setResult 方法传参返回
     *
     * @param ac          现在UI
     * @param cls         需要跳转到的UI
     * @param requestCode 返回 ac 页面时，返回 onActivityResult(int requestCode, int resultCode, Intent data) 方法
     *                    的 requestCode 参数
     * @param ActionType  跳转的动画
     */
    public static void startActivityForResult(Activity ac, Class<?> cls, int requestCode, CommonUtil.enumActionType ActionType) {
        try {
            Intent intent = new Intent(ac, cls);
            ac.startActivityForResult(intent, requestCode);
            AnimUtil.ActivityDynamic(ac, ActionType);
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }


    /**
     * 传参跳转，返回上一层UI可调用 setResult 方法传参返回
     *
     * @param ac          现在UI
     * @param cls         需要跳转到的UI
     * @param bu          传到 cls 的参数
     * @param requestCode 返回 ac 页面时，返回 onActivityResult(int requestCode, int resultCode, Intent data) 方法
     *                    的 requestCode 参数
     * @param ActionType  跳转的动画
     */
    public static void startActivityForResult(Activity ac, Class<?> cls, Bundle bu, int requestCode, CommonUtil.enumActionType ActionType) {
        try {
            Intent intent = new Intent(ac, cls);
            intent.putExtras(bu);
            ac.startActivityForResult(intent, requestCode);
            AnimUtil.ActivityDynamic(ac, ActionType);
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    // endregion

    // region // Activity、Fragment页面销毁

    /**
     * 一般界面销毁
     *
     * @param ac         需要销毁的UI
     * @param ActionType 销毁的动画
     */
    public static void destroyActivity(Activity ac, CommonUtil.enumActionType ActionType) {
        try {
            ac.finish();
            AnimUtil.ActivityDynamic(ac, ActionType);
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    /**
     * 一般界面销毁
     *
     * @param ac         需要销毁的UI
     * @param ActionType 销毁的动画
     */
    public static void ResultActivitys(Activity ac, Bundle bu, int resultCode, CommonUtil.enumActionType ActionType) {
        try {
            Intent i = new Intent();
            i.putExtras(bu);
            ac.setResult(resultCode, i);
            ac.finish();
            AnimUtil.ActivityDynamic(ac, ActionType);
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    /**
     * 一般界面销毁
     *
     * @param ac         需要销毁的UI
     * @param ActionType 销毁的动画
     */
    public static void ResultActivitys(Activity ac, int resultCode, CommonUtil.enumActionType ActionType) {
        try {
            Intent i = new Intent();
            ac.setResult(resultCode, i);
            ac.finish();
            AnimUtil.ActivityDynamic(ac, ActionType);
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    // endregion

    // region Fragment页面跳转到Activity返回数据到onActivityResult

    /**
     * 传参跳转，返回上一层UI可调用 setResult 方法传参返回
     *
     * @param ac          现在UI
     * @param cls         需要跳转到的UI
     * @param requestCode 返回 ac 页面时，返回 onActivityResult(int requestCode, int resultCode, Intent data) 方法
     *                    的 requestCode 参数
     * @param ActionType  跳转的动画
     */
    public static void startActivityForResult(Fragment f, Activity ac, Class<?> cls, int requestCode, CommonUtil.enumActionType ActionType) {
        try {
            if (f.isAdded()) {
                Intent intent = new Intent(ac, cls);
                f.startActivityForResult(intent, requestCode);
                AnimUtil.ActivityDynamic(ac, ActionType);
            }
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    /**
     * 传参跳转，返回上一层UI可调用 setResult 方法传参返回
     *
     * @param ac          现在UI
     * @param cls         需要跳转到的UI
     * @param requestCode 返回 ac 页面时，返回 onActivityResult(int requestCode, int resultCode, Intent data) 方法
     *                    的 requestCode 参数
     * @param ActionType  跳转的动画
     */
    public static void startActivityForResult(Fragment f, Activity ac, Class<?> cls, Bundle b, int requestCode, CommonUtil.enumActionType ActionType) {
        try {
            if (f.isAdded()) {
                Intent intent = new Intent(ac, cls);
                intent.putExtras(b);
                f.startActivityForResult(intent, requestCode);
                AnimUtil.ActivityDynamic(ac, ActionType);
            }
        } catch (Exception e) {
            new DialogUtil(ac).showError(e.getMessage());
        }
    }

    // endregion


}
