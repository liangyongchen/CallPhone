package com.asen.callphone.base.view.dailog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.asen.callphone.R;


/**
 * 提示框，线程阻塞
 * Created by asus on 2017/7/20.
 */

public class MessageDialog extends Dialog {

    public static final String MessageDialogStyle_Simple = "SIMPLE";   // 简单的
    public static final String MessageDialogStyle_Confirm = "CONFIRM"; // 确认


    int dialogResult;
    Handler mHandler;


    public MessageDialog(Activity context) {
        super(context);
        dialogResult = 0;
        init(context, MessageDialog.MessageDialogStyle_Simple);
    }

    public MessageDialog(Activity context, String style) {
        super(context);
        init(context, style);
    }

    private void init(Activity context, String style) {
        setOwnerActivity(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        onCreate(style);
    }

    public void onCreate(String style) {
        switch (style) {
            case "SIMPLE":
                setContentView(R.layout.message_dialog_singlebutton);
                break;
            case "CONFIRM":
                setContentView(R.layout.messagebox);
                break;
            default:
                setContentView(R.layout.message_dialog_singlebutton);
                break;
        }

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                endDialog(0);
            }
        });
        findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View paramView) {
                endDialog(1);
            }
        });
    }

    public void endDialog(int result) {
        dismiss();
        setDialogResult(result);
        Message m = mHandler.obtainMessage();
        mHandler.sendMessage(m);
    }

    public void setDialogResult(int dialogResult) {
        this.dialogResult = dialogResult;
    }

    // 调用该方法显示对话框
    public int showDialog(String Msg, String Title) {
        TextView TvErrorInfo = (TextView) findViewById(R.id.textViewInfo);
        TvErrorInfo.setText(Msg);
        TvErrorInfo.setMovementMethod(ScrollingMovementMethod.getInstance());

        TextView TvTitle = (TextView) findViewById(R.id.textViewTitle);
        TvTitle.setText(Title);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                throw new RuntimeException();
            }
        };
        this.setCancelable(false); // 设置点击屏幕Dialog不消失
        super.show();
        try { // 线程阻塞
            Looper.getMainLooper();
            Looper.loop();
        } catch (RuntimeException e2) {
        }
        return dialogResult;
    }


    // TODO Test No BlockResult
    public int showDialogNoBlock(String Msg, String Title) {
        TextView TvErrorInfo = (TextView) findViewById(R.id.textViewInfo);
        TvErrorInfo.setText(Msg);
        TvErrorInfo.setMovementMethod(ScrollingMovementMethod.getInstance());

        TextView TvTitle = (TextView) findViewById(R.id.textViewTitle);
        TvTitle.setText(Title);

        this.setCancelable(false); // 设置点击屏幕Dialog不消失
        super.show();
        return dialogResult;
    }


}
