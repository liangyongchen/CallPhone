package com.asen.callphone.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asen.callphone.R;
import com.asen.callphone.base.app.BaseActivity;
import com.asen.callphone.base.setting.SettingUtil;
import com.asen.callphone.base.util.CommonUtil;
import com.asen.callphone.base.util.IntentUtil;
import com.asen.callphone.db.DBUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.pacific.timer.Rx2Timer;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class AddContactsActivity extends BaseActivity {

    @BindView(R.id.mToolbar)
    LinearLayout mToolbar;
    @BindView(R.id.cancel)
    TextView mCancel;
    @BindView(R.id.save)
    TextView mSave;
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.phone)
    EditText mPhone;
    @BindView(R.id.mailbox)
    EditText mMailbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);
        ButterKnife.bind(this);
        initView(savedInstanceState);
        initEvent();
    }

    public void initView(Bundle savedInstanceState) {
        mToolbar.setBackgroundColor(SettingUtil.getInstance().getColor());
    }

    public void initEvent() {

        RxView.clicks(mCancel)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
//                        timer.start();
                        //finish();
                        onBackPressed();
                    }
                });

        RxView.clicks(mSave)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (isNull()) {

                            if (DBUtil.getInstance(AddContactsActivity.this).InsertData(
                                    mName.getText().toString().trim(),
                                    mPhone.getText().toString().trim(),
                                    mMailbox.getText().toString().trim())
                                    ) {
                                Toast.makeText(AddContactsActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                IntentUtil.ResultActivitys(AddContactsActivity.this,new Bundle(),IntentUtil.AC_AddContacts, CommonUtil.enumActionType.ACTION_SIGN_OUT);
                            } else {
                                Toast.makeText(AddContactsActivity.this, "保存失败\n电话号码格式错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

        // Rx2 倒计时学习
        timer = Rx2Timer.builder()
                .initialDelay(0) //default is 0
                // 循环期数
                //.period(1)       //default is 1
                // 倒计时30秒
                .take(5)        //default is 60
                .unit(TimeUnit.SECONDS) // default is TimeUnit.SECONDS
                .onEmit(count -> {
                    if (count < 10) {
                        RxTextView.text(mSave).accept("0" + count + " s");
                    } else {
                        RxTextView.text(mSave).accept(count + " s");
                    }
                })
                .onError(e -> RxTextView.text(mSave).accept("错误"))
//                .onComplete(() ->  RxTextView.text(mSave).accept("保存"))
                .onComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        RxTextView.text(mSave).accept("保存");
                        finish();
                    }
                })
                .build();

    }


    /**
     * timer.start();   // 开始
     * timer.stop();    // 停止
     * timer.restart(); // 重新开始
     * timer.pause();   // 暂停
     * timer.resume();  // 继续
     */
    Rx2Timer timer;

    @Override
    public void onResume() {
        super.onResume();
        timer.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        timer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.stop();
    }

    private boolean isNull() {
        String name = mName.getText().toString();
        String phone = mPhone.getText().toString();

        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入姓名和手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringUtils.isEmpty(phone)) {
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
