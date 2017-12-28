package com.asen.callphone.fragment.software_setting;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.asen.callphone.R;
import com.asen.callphone.base.setting.SettingUtil;
import com.jaygoo.widget.RangeSeekBar;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by asus on 2017/12/20.
 */

public class TextSizeFragment extends Fragment {

    @BindView(R.id.text)
    TextView mText;
    @BindView(R.id.seekbar)
    RangeSeekBar mSeekbar;
    private View view;
    private Unbinder unbinder;
    private DecimalFormat df = new DecimalFormat("0");
    private int currentSize = -1;
    private SettingUtil settingUtil = SettingUtil.getInstance();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setting_textsize, container, false);
        unbinder = ButterKnife.bind(this, v);
        initView(v);
        return v;
    }

    private void initView(View v) {

        mText.setTextSize(settingUtil.getTextSize());
        mSeekbar.setValue(settingUtil.getTextSize() - 14);
        mSeekbar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, final float min, float max, boolean isFromUser) {
                if (isFromUser) {
                    int size = Integer.parseInt(df.format(min));
                    if (currentSize != size) {
                        setText(size);
                        currentSize = size;
                    }
                }
            }
        });
    }

    private void setText(int size) {
        // 最小 14sp
        size = 14 + size;
        mText.setTextSize(size);
        settingUtil.setTextSize(size);
        //RxBus.getInstance().post(BaseListFragment.TAG, size);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
