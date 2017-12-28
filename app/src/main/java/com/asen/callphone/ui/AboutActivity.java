package com.asen.callphone.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.asen.callphone.R;
import com.asen.callphone.base.app.BaseActivity;
import com.asen.callphone.base.util.IntentAction;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        // 初始化 Toolbar
        initToolBar(mToolbar, true, getResources().getString(R.string.title_activity_about));

    }

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

    // endregion

}
