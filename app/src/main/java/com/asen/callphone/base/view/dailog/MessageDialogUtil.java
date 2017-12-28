package com.asen.callphone.base.view.dailog;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by asus on 2017/7/20.
 */

public class MessageDialogUtil {

    /**
     * 播放错误提示音
     */
    public static boolean IsExceptionPlayErrorSound = true;

    /**
     * 业务逻辑错误播放错误提示音
     */
    public static boolean IsBusinessExceptionPlayErrorSound = true;

    public static String ErrorTitle = "错误";

    public static int ShowInfoDialog(Activity activity, String info) {
        MessageDialog msgBox = new MessageDialog(activity);
        return msgBox.showDialog(info, "提示");
    }

    public static int ShowComfirm(Activity activity, String info) {
        MessageDialog msgBox = new MessageDialog(activity, MessageDialog.MessageDialogStyle_Confirm);
        return msgBox.showDialog(info, "提示");
    }

    public static int ShowComfirmNoBlock(Activity activity, String info) {
        MessageDialog msgBox = new MessageDialog(activity, MessageDialog.MessageDialogStyle_Confirm);
        return msgBox.showDialogNoBlock(info, "提示");
    }

    /***
     * 联网等待提示
     * @param c
     */
    public static void ShowWaitingToast(Context c) {
        String e = "正在连接服务器，请稍后。。。";
        Toast.makeText(c, e, Toast.LENGTH_LONG).show();
    }

    public static void ShowExceptionToast(Context c, String e) {
        Toast.makeText(c, e, Toast.LENGTH_LONG).show();
    }

    public static void ShowExceptionToast(Context c, Exception e) {
        String errorMsg = e.getMessage() + "\r\n" + e.getStackTrace();
        Toast.makeText(c, errorMsg, Toast.LENGTH_LONG).show();
    }


    /**
     * 仿照C# 的 MessageBox.ShowDialog() 线程阻塞的
     *
     * @param activity
     * @param e
     */
    public static void ShowDialog(Activity activity, String e) {

        MessageDialog msgBox = new MessageDialog(activity);
        msgBox.showDialog(e, "提示");
    }

    /**
     * 仿照C# 的 MessageBox.ShowDialog() 线程阻塞的
     *
     * @param activity
     * @param e
     */
    public static void ShowExceptionDialog(Activity activity, String e) {

        MessageDialog msgBox = new MessageDialog(activity);
        msgBox.showDialog(e, ErrorTitle);
    }

    /**
     * 仿照C# 的 MessageBox.ShowDialog() 线程阻塞的
     *
     * @param activity
     * @param e
     */
    public static void ShowExceptionDialog(Activity activity, Exception e) {
        MessageDialog msgBox = new MessageDialog(activity);
        String errorMsg = e.getMessage() + "\r\n" + e.getStackTrace();
        msgBox.showDialog(errorMsg, ErrorTitle);
    }


}
