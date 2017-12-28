package com.asen.callphone.base.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Vibrator;

/**
 * Created by pc on 2017/2/23.
 */

public class DialogUtil {

    /**
     * 震动
     */
    private Vibrator vibrator;
    /**
     * 上下文本
     */
    private Context content;

    public DialogUtil(Context v) {
        this.content = v;
        this.vibrator = (Vibrator) content.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 错误提示
     *
     * @param str
     */
    public void showError(String str) {
        vibrator.vibrate(1200);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.content);
        dialog.setTitle("错误");
        dialog.setMessage(str);
        dialog.show();
    }

    /**
     * 一般提示提示
     *
     * @param str
     */
    public void showDialog(String str) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.content);
        dialog.setTitle("提示");
        dialog.setMessage(str);
        dialog.show();
    }

    /**
     * 上传成功返回提示信息提示
     *
     * @param str
     */
    public void showSuccess(String str) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.content);
        dialog.setTitle("提示");
        dialog.setMessage(str);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


}
