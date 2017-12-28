package com.asen.callphone.base.util;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.asen.callphone.R;


/**
 * Created by pc on 2017/2/25.
 */

public class AnimUtil {

/*********************** Activity页面跳转动画 *************************************************************************/

    /**
     * 页面跳转动态
     *
     * @param ac         本UI界面
     * @param ActionType 动作类型
     */
    public static void ActivityDynamic(Activity ac, CommonUtil.enumActionType ActionType) {
        switch (ActionType) {
            case ACTION_LANDING: // 登陆动态
                // 方法的第一个参数：enterAnim，是新的Activity的进入动画的resource ID；
                // 第二个参数exitAnim，是旧的Activity(当前的Activity)离开动画的resource ID。
                ac.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case ACTION_SIGN_OUT: // 退出动态
                ac.overridePendingTransition(R.anim.in_left, R.anim.out_right);
                break;
            case ACTION_SIGN_OUT2: // 退出动态2
                ac.overridePendingTransition(R.anim.in_left, R.anim.zoomout);
                break;
            case ACTION_FORWARD: // 前进动态
                ac.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case ACTION_FORWARD2: // 前进动态2
                ac.overridePendingTransition(R.anim.in_from_right, R.anim.zoomout);
                break;
            case ACTION_FADE_IN: // 淡入
                ac.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case ACTION_FADE_OUT: // 淡出
                ac.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case ACTION_OTHER: // 其他动态
                ac.overridePendingTransition(R.anim.in_from_right, R.anim.zoomin);
                break;
        }
    }

/*********************** Fragment 界面切换动画 ***********************************************************************/

    /**
     * 页面跳转动态
     *
     * @param ft         开启的服务
     * @param ActionType 动作类型
     */
    public static void FragmentDynamicStart(FragmentTransaction ft, CommonUtil.enumActionType ActionType) {
        switch (ActionType) {
            case ACTION_LANDING: // 登陆动态
                // 方法的第一个参数：enterAnim，是新的 Fragment 的进入动画的resource ID；
                // 第二个参数exitAnim，是旧的 Fragment(当前的 Fragment )离开动画的resource ID。
                ft.setCustomAnimations(R.anim.zoomin, R.anim.zoomout);
                break;
            case ACTION_SIGN_OUT: // 退出动画
                ft.setCustomAnimations(R.anim.in_left, R.anim.out_right);
                break;
            case ACTION_SIGN_OUT2: // 退出动态2
                ft.setCustomAnimations(R.anim.in_left, R.anim.zoomout);
                break;
            case ACTION_FORWARD: // 前进动态
                ft.setCustomAnimations(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case ACTION_FORWARD2: // 前进动态2
                ft.setCustomAnimations(R.anim.in_from_right, R.anim.zoomout);
                break;
            case ACTION_FADE_IN: // 淡入
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                break;
            case ACTION_FADE_OUT: // 淡出
                ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                break;
            case ACTION_OTHER: // 其他动态
                ft.setCustomAnimations(R.anim.in_from_right, R.anim.zoomin);
                break;
        }
    }

    public static void FragmentDynamicEnd(FragmentActivity ac, CommonUtil.enumActionType ActionType) {
        switch (ActionType) {
            case ACTION_LANDING: // 登陆动态
                // 方法的第一个参数：enterAnim，是新的 Fragment 的进入动画的resource ID；
                // 第二个参数exitAnim，是旧的 Fragment(当前的 Fragment )离开动画的resource ID。
                ac.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case ACTION_SIGN_OUT: // 退出动画
                ac.overridePendingTransition(R.anim.in_left, R.anim.out_right);
                break;
            case ACTION_SIGN_OUT2: // 退出动态2
                ac.overridePendingTransition(R.anim.in_left, R.anim.zoomout);
                break;
            case ACTION_FORWARD: // 前进动态
                ac.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                break;
            case ACTION_FORWARD2: // 前进动态2
                ac.overridePendingTransition(R.anim.in_from_right, R.anim.zoomout);
                break;
            case ACTION_FADE_IN: // 淡入
                ac.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case ACTION_FADE_OUT: // 淡出
                ac.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case ACTION_OTHER: // 其他动态
                ac.overridePendingTransition(R.anim.in_from_right, R.anim.zoomin);
                break;
        }
    }


}
