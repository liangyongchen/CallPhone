package com.asen.callphone.base.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by asus on 2017/8/8.
 */

public class FragmentUtil {


    // 普通删除Fragment
    public static void removeFragment(FragmentActivity ac, FragmentManager fragmentManager, Fragment fragment, CommonUtil.enumActionType ActionType) {
        FragmentTransaction FT = fragmentManager.beginTransaction();
        // 自定义动画
        AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画
        FT.remove(fragment);
        FT.commit();
        AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
    }

    public static void removeFragment(FragmentActivity ac, Fragment fragment, CommonUtil.enumActionType ActionType) {
        if (fragment == null) return;
        FragmentTransaction FT = ac.getSupportFragmentManager().beginTransaction();
        // 自定义动画
        AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画
        FT.remove(fragment);
        FT.commit();
        // ac.getSupportFragmentManager().popBackStackImmediate();
        ac.getSupportFragmentManager().popBackStack();
        AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
    }


    // region    Fragment 和 Fragment 之间的跳转与返回事件处理


    /**
     * 普通跳转
     *
     * @param ac              FragmentActivity
     * @param fragmentManager 管理器
     * @param farmentID       给Fragment匹配的控件ID
     * @param fragment        跳转到该界面的Fragment
     * @param ActionType      跳转的动态
     */
    public static void FragmentStartAdd(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment fragment, CommonUtil.enumActionType ActionType) {
        if (!fragment.isAdded()) {
            //通过调用fragmentManager中的beginTransaction方法啦开启事务
            FragmentTransaction FT = fragmentManager.beginTransaction();
            // 自定义动画
            AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

            //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
            FT.add(farmentID, fragment);
            //提交事务
            FT.commit();
            AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
        }
    }

    /**
     * 普通跳转,设置Tag
     *
     * @param ac              FragmentActivity
     * @param fragmentManager 管理器
     * @param farmentID       给Fragment匹配的控件ID
     * @param fragment        跳转到该界面的Fragment
     * @param tag             返回栈中匹配的Tag值
     * @param ActionType      跳转的动态
     */
    public static void FragmentStartAdd(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment fragment, String tag, CommonUtil.enumActionType ActionType) {
        if (!fragment.isAdded()) {
            //通过调用fragmentManager中的beginTransaction方法啦开启事务
            FragmentTransaction FT = fragmentManager.beginTransaction();
            // 自定义动画
            AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

            //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
            FT.add(farmentID, fragment, tag);
            //提交事务
            FT.commit();
            AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
        }
    }

    /**
     * 普通跳转传参数
     *
     * @param ac              FragmentActivity
     * @param fragmentManager 管理器
     * @param farmentID       给Fragment匹配的控件ID
     * @param fragment        跳转到该界面的Fragment
     * @param ActionType      跳转的动态
     */
    public static void FragmentStartAdd(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment fragment, Bundle b, CommonUtil.enumActionType ActionType) {
        if (!fragment.isAdded()) {
            //通过调用fragmentManager中的beginTransaction方法啦开启事务
            FragmentTransaction FT = fragmentManager.beginTransaction();
            // 自定义动画
            AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画
            fragment.setArguments(b);
            //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
            FT.add(farmentID, fragment);
            //提交事务
            FT.commit();
            AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
        }
    }


    /**
     * Fragment 之间的跳转，跳转后的Fragment 返回上一层 Fragment 的时候， 不会调用 onCreateView 开启生命 ，而是直接返回上一层，不会修改这里的数据
     * 跳转到新的Fragment 会执行生命周期，但是 旧的不会执行生命周期的任何一项，所以这个启动可以保存上一层操作过的所有数据
     *
     * @param ac              FragmentActivity
     * @param fragmentManager 管理器
     * @param farmentID       给Fragment匹配的控件ID
     * @param Old             跳转前的Fragment
     * @param New             跳转后的Fragment
     * @param ActionType      跳转的动态
     */
    public static void FragmentStartAddToBackStack(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment Old, Fragment New, CommonUtil.enumActionType ActionType) {
        if (!New.isAdded()) {
            //通过调用fragmentManager中的beginTransaction方法啦开启事务
            FragmentTransaction FT = fragmentManager.beginTransaction();
            // 自定义动画
            AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

            FT.hide(Old);
            //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
            FT.add(farmentID, New);
            FT.addToBackStack(null); // 我们调用tx.addToBackStack(null);将当前的事务添加到了回退栈，所以 fragment 实例不会被销毁，但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView，
            //提交事务
            FT.commit();
            AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
        }
    }

    private static long exitTime = 0;

    /**
     * Fragment 之间的跳转，跳转后的Fragment 返回上一层 Fragment 的时候， 不会调用 onCreateView 开启生命 ，而是直接返回上一层，不会修改这里的数据
     * 跳转到新的Fragment 会执行生命周期，但是 旧的不会执行生命周期的任何一项，所以这个启动可以保存上一层操作过的所有数据
     *
     * @param ac              FragmentActivity
     * @param fragmentManager 管理器
     * @param farmentID       给Fragment匹配的控件ID
     * @param Old             跳转前的Fragment
     * @param New             跳转后的Fragment
     * @param ActionType      跳转的动态
     */
    public static void FragmentStartAddToBackStack(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment Old, Fragment New, Bundle b, CommonUtil.enumActionType ActionType) {

        if ((System.currentTimeMillis() - exitTime) > 1000) { // 防止近时间内加载多次报错
            exitTime = System.currentTimeMillis();

            if (!New.isAdded()) {
                //通过调用fragmentManager中的beginTransaction方法啦开启事务
                FragmentTransaction FT = fragmentManager.beginTransaction();
                // 自定义动画
                AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画
                New.setArguments(b);
                FT.hide(Old);
                //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
                FT.add(farmentID, New);
                FT.addToBackStack(null); // 我们调用tx.addToBackStack(null);将当前的事务添加到了回退栈，所以 fragment 实例不会被销毁，但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView，
                //提交事务
                FT.commit();
                AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
            }

        }
    }

    /**
     * Fragment 之间的跳转，跳转后的Fragment 返回上一层 Fragment 的时候， 不会调用 onCreateView 开启生命 ，而是直接返回上一层，不会修改这里的数据
     * 跳转到新的Fragment 会执行生命周期，但是 旧的不会执行生命周期的任何一项，所以这个启动可以保存上一层操作过的所有数据
     *
     * @param ac              FragmentActivity
     * @param fragmentManager 管理器
     * @param farmentID       给Fragment匹配的控件ID
     * @param Old             跳转前的Fragment
     * @param New             跳转后的Fragment
     * @param tag             返回栈中匹配的Tag值
     * @param ActionType      跳转的动态
     */
    public static void FragmentStartAddToBackStack(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment Old, Fragment New, String tag, CommonUtil.enumActionType ActionType) {
        if (!New.isAdded()) {
            //通过调用fragmentManager中的beginTransaction方法啦开启事务
            FragmentTransaction FT = fragmentManager.beginTransaction();
            // 自定义动画
            AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

            FT.hide(Old);
            //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
            FT.add(farmentID, New, tag);
            FT.addToBackStack(null); // 我们调用tx.addToBackStack(null);将当前的事务添加到了回退栈，所以 fragment 实例不会被销毁，但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView，
            //提交事务
            FT.commit();
            AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画
        }
    }


    /**
     * replace 跳转是 remove 上一个 Fragment 和 添加新的 Fragment ，后退不会退回栈中
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount():从管理器中得到Fragment当前已加入Fragment回退栈中的fragment的数量
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount() 不会存在跳转前的 Fragment 的数量，返回直接销毁
     *
     * @param ac
     * @param fragmentManager
     * @param farmentID
     * @param fragment
     * @param ActionType
     */
    public static void FragmentStartReplace(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment fragment, CommonUtil.enumActionType ActionType) {

        //通过调用fragmentManager中的beginTransaction方法啦开启事务
        FragmentTransaction FT = fragmentManager.beginTransaction();
        // 自定义动画
        AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

        //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
        FT.replace(farmentID, fragment); // 使用了replace方法，如果你看了前一篇博客，一定记得replace是remove和add的合体，并且如果不添加事务到回退栈，前一个Fragment实例会被销毁。
        //提交事务
        FT.commit();
        AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画

    }

    /**
     * replace 跳转是 remove 上一个 Fragment 和 添加新的 Fragment ，后退不会退回栈中
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount():从管理器中得到Fragment当前已加入Fragment回退栈中的fragment的数量
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount() 不会存在跳转前的 Fragment 的数量，返回直接销毁
     *
     * @param ac
     * @param fragmentManager
     * @param farmentID
     * @param fragment
     * @param tag
     * @param ActionType
     */
    public static void FragmentStartReplace(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment fragment, String tag, CommonUtil.enumActionType ActionType) {

        //通过调用fragmentManager中的beginTransaction方法啦开启事务
        FragmentTransaction FT = fragmentManager.beginTransaction();
        // 自定义动画
        AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

        //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
        FT.replace(farmentID, fragment, tag); // 使用了replace方法，如果你看了前一篇博客，一定记得replace是remove和add的合体，并且如果不添加事务到回退栈，前一个Fragment实例会被销毁。
        //提交事务
        FT.commit();
        AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画

    }

    /**
     * replace 跳转是 remove 上一个 Fragment 和 添加新的 Fragment
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount():从管理器中得到Fragment当前已加入Fragment回退栈中的fragment的数量
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount() 存在跳转前的 Fragment 的数量，返回执行：onCreateView 开始启动生命周期
     *
     * @param ac
     * @param fragmentManager
     * @param farmentID
     * @param fragment
     * @param ActionType
     */
    public static void FragmentStartReplaceToBackStack(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, Fragment fragment, CommonUtil.enumActionType ActionType) {

        //通过调用fragmentManager中的beginTransaction方法啦开启事务
        FragmentTransaction FT = fragmentManager.beginTransaction();
        // 自定义动画
        AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

        //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
        FT.replace(farmentID, fragment); // 使用了replace方法，如果你看了前一篇博客，一定记得replace是remove和add的合体，并且如果不添加事务到回退栈，前一个Fragment实例会被销毁。
        FT.addToBackStack(null); // 我们调用tx.addToBackStack(null);将当前的事务添加到了回退栈，所以 fragment 实例不会被销毁，但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView，
        //提交事务
        FT.commit();
        AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画

    }

    /**
     * replace 跳转是 remove 上一个 Fragment 和 添加新的 Fragment
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount():从管理器中得到Fragment当前已加入Fragment回退栈中的fragment的数量
     * <p>
     * getSupportFragmentManager().getBackStackEntryCount() 存在跳转前的 Fragment 的数量，返回执行：onCreateView 开始启动生命周期
     *
     * @param ac
     * @param fragmentManager
     * @param farmentID
     * @param fragment
     * @param tag
     * @param ActionType
     */
    public static void FragmentStartReplaceToBackStack(FragmentActivity ac, FragmentManager fragmentManager, int farmentID, String tag, Fragment fragment, CommonUtil.enumActionType ActionType) {

        //通过调用fragmentManager中的beginTransaction方法啦开启事务
        FragmentTransaction FT = fragmentManager.beginTransaction();
        // 自定义动画
        AnimUtil.FragmentDynamicStart(FT, ActionType); // 开始动画跳转过来的Fragment动画

        //该方法用于在某个布局中添加指定的fragemnt，第一个参数指是哪个布局，第二个参数为对应的fragemnt
        FT.replace(farmentID, fragment, tag); // 使用了replace方法，如果你看了前一篇博客，一定记得replace是remove和add的合体，并且如果不添加事务到回退栈，前一个Fragment实例会被销毁。
        FT.addToBackStack(null); // 我们调用tx.addToBackStack(null);将当前的事务添加到了回退栈，所以 fragment 实例不会被销毁，但是视图层次依然会被销毁，即会调用onDestoryView和onCreateView，
        //提交事务
        FT.commit();
        AnimUtil.FragmentDynamicEnd(ac, ActionType); // 当前结束的 Fragment 动画

    }

    // endregion


}
