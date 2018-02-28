package com.asen.callphone.base.view.pinyin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.asen.callphone.R;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 索引拼音
 * Created by asus on 2018/2/10.
 * <p>
 * View/ViewGroup的生命周期-自定义view
 * onAttachToWindow——>onMeasure——>onSizeChanged——>
 * onLayout——> onMeasure——>onLayout——>onDraw——>onDetachedFromWindow
 */

public class IndexView extends View {

    // region  // 继承 view 生命周期流程

    /**
     * 生命周期
     * // 点击进入
     * 02-10 13:54:23.344 27570-27570/com.asen.callphone E/log: onFinishInflate
     * 02-10 13:54:23.464 27570-27570/com.asen.callphone E/log: onAttachedToWindow
     * 02-10 13:54:23.474 27570-27570/com.asen.callphone E/log: onMeasure
     * 02-10 13:54:23.474 27570-27570/com.asen.callphone E/log: onMeasure
     * 02-10 13:54:23.514 27570-27570/com.asen.callphone E/log: onMeasure
     * 02-10 13:54:23.614 27570-27570/com.asen.callphone E/log: onSizeChanged
     * 02-10 13:54:23.614 27570-27570/com.asen.callphone E/log: onLayout
     * 02-10 13:54:23.664 27570-27570/com.asen.callphone E/log: onDraw
     * 02-10 13:54:23.664 27570-27570/com.asen.callphone E/log: dispatchDraw
     * 02-10 13:54:23.714 27570-27570/com.asen.callphone E/log: onWindowFocusChanged  true
     * 02-10 13:54:23.724 27570-27570/com.asen.callphone E/log: onMeasure
     * 02-10 13:54:23.734 27570-27570/com.asen.callphone E/log: onLayout
     * 02-10 13:54:23.744 27570-27570/com.asen.callphone E/log: onDraw
     * 02-10 13:54:23.744 27570-27570/com.asen.callphone E/log: dispatchDraw
     *
     * // 进入下一个界面
     * 02-10 13:56:17.154 27570-27570/com.asen.callphone E/log: onWindowFocusChanged  false
     *
     * // 返回当前界面
     * 02-10 13:57:57.954 27570-27570/com.asen.callphone E/log: onWindowFocusChanged  true
     *
     * // 跳栈中
     * 02-10 13:58:27.864 27570-27570/com.asen.callphone E/log: onWindowFocusChanged  false
     *
     * // 返回app
     * 02-10 13:58:57.304 27570-27570/com.asen.callphone E/log: onDraw
     * 02-10 13:58:57.304 27570-27570/com.asen.callphone E/log: dispatchDraw
     * 02-10 13:58:57.324 27570-27570/com.asen.callphone E/log: onWindowFocusChanged  true
     *
     * // 退出app
     * 02-10 14:00:11.014 27570-27570/com.asen.callphone E/log: onWindowFocusChanged  false
     * 02-10 14:00:11.484 27570-27570/com.asen.callphone E/log: onDetachedFromWindow
     */

    // endregion

    // region // 继承 ViewGroup 的生命周期

    /**
     * 继承 ViewGroup 的生命周期
     * // 进入
     * 02-10 16:58:22.142 10869-10869/com.asen.callphone E/log: onFinishInflate
     * 02-10 16:58:22.212 10869-10869/com.asen.callphone E/log: onAttachedToWindow
     * 02-10 16:58:22.212 10869-10869/com.asen.callphone E/log: onMeasure
     * 02-10 16:58:22.212 10869-10869/com.asen.callphone E/log: onMeasure
     * 02-10 16:58:22.242 10869-10869/com.asen.callphone E/log: onMeasure
     * 02-10 16:58:22.322 10869-10869/com.asen.callphone E/log: onSizeChanged
     * 02-10 16:58:22.322 10869-10869/com.asen.callphone E/log: onLayout
     * 02-10 16:58:22.372 10869-10869/com.asen.callphone E/log: dispatchDraw
     * 02-10 16:58:22.412 10869-10869/com.asen.callphone E/log: onWindowFocusChanged  true
     * 02-10 16:58:22.422 10869-10869/com.asen.callphone E/log: onMeasure
     * 02-10 16:58:22.442 10869-10869/com.asen.callphone E/log: onLayout
     * 02-10 16:58:22.462 10869-10869/com.asen.callphone E/log: dispatchDraw
     * <p>
     * // 进入下一个界面
     * 02-10 16:58:56.762 10869-10869/com.asen.callphone E/log: onWindowFocusChanged  false
     * <p>
     * // 返回上一个界面
     * 02-10 16:59:12.182 10869-10869/com.asen.callphone E/log: onWindowFocusChanged  true
     * <p>
     * // 跳栈中
     * 02-10 16:59:21.762 10869-10869/com.asen.callphone E/log: onWindowFocusChanged  false
     * <p>
     * // 返回app
     * 02-10 16:59:35.352 10869-10869/com.asen.callphone E/log: dispatchDraw
     * 02-10 16:59:35.372 10869-10869/com.asen.callphone E/log: onWindowFocusChanged  true
     * <p>
     * // 直接退出
     * 02-10 17:00:00.902 10869-10869/com.asen.callphone E/log: onWindowFocusChanged  false
     * 02-10 17:00:01.362 10869-10869/com.asen.callphone E/log: onDetachedFromWindow
     */

    // endregion


    // 需要画的字母(自己定义的数据)
//    private String[] arrays = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private String[] arrays;
    // 获取通讯录传过来的数据
    private List<? extends BasePinyinInfo> mDatas;

    // 右边显示索引的宽
    private int mViewWidth, mViewHeigth;

    // textWidth：索引字母的文字宽度，textHeiht：获取所有字母的总体高度
    int textWidth, textHeiht;

    // 背景画笔
    private Paint bgPaint;

    // 字体画笔
    private TextPaint textPaint;
    private Paint.FontMetrics mFontMetrics;  // 字体度量

    private static int COLOR_BG;   // 背景颜色
    private static int COLOR_TEXT; // 标题框字体颜色
    private static int TEXT_SIZE;  // 文字尺寸

    private boolean isTouch = false; // 判断是否都触摸到 IndexView

    public IndexView(Context context) {
        super(context);
        init(context);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IndexView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mContext) {

        // 如果在xml中设置有属性值， 用 TypedArray  反射获取回来

        // 设置颜色值
        COLOR_BG = mContext.getResources().getColor(R.color.index_view_bg);
        COLOR_TEXT = mContext.getResources().getColor(R.color.textColorPrimary);

        // 设置文字大小
        TEXT_SIZE = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext.getResources().getDisplayMetrics());

        // 初始化画笔
        bgPaint = new Paint();
        bgPaint.setColor(COLOR_BG);
        bgPaint.setAlpha(100); // 设置背景的透明度
        bgPaint.setAntiAlias(true); // 设置锯齿

        textPaint = new TextPaint();
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setColor(COLOR_TEXT);
        mFontMetrics = textPaint.getFontMetrics();

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("log", "onFinishInflate");
    }

    @Override
    protected void onAttachedToWindow() {
        // Activity onResume后调用view的onAttachedToWindow，因此常常在onAttachedToWindow方法中做初始化工作，比如注册一些广播、开始动画等等……
        super.onAttachedToWindow();
        Log.e("log", "onAttachedToWindow");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// 1073741872 、1073743054
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("log", "onMeasure");
    }

    // 这时在父容器里面 自身所占的 宽高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {// 48、1230、0、0
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("log", "onSizeChanged");

        // 获取视图的宽高
        mViewWidth = w;
        mViewHeigth = h;

    }

    // 这时在父容器布局的尺寸：左上右下坐标
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) { // 尺寸是否改变：左上右下 672、0、720、1230
        super.onLayout(changed, l, t, r, b);
        Log.e("log", "onLayout");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("log", "onDraw");

        if (isTouch) {
            bgPaint.setAlpha(100); // 触摸到的时候设置透明度为100
        } else {
            bgPaint.setAlpha(0); // 没有触摸到的时候设置透明度为0
        }

        canvas.drawRect(0, 0, mViewWidth, mViewHeigth, bgPaint);

        for (int i = 0; i < arrays.length; i++) {

            if (StringUtils.isNotEmpty(arrays[i])) {
                textWidth = (mViewWidth - (int) textPaint.measureText(arrays[i])) / 2; // textPaint.measureText 获取字符串的宽度值
                textHeiht = (mViewHeigth / arrays.length - (int) mFontMetrics.descent - (int) mFontMetrics.leading) * (i + 1);
                canvas.drawText(arrays[i], textWidth, textHeiht, textPaint);
            }
        }

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.e("log", "dispatchDraw");

    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.e("log", "onWindowFocusChanged" + "  " + hasWindowFocus);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.e("log", "onDetachedFromWindow");
    }


    int position = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = 0;
        int y;
        switch (event.getAction()) {

            // 点击按下和触摸滑动的时候执行
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:

                y = (int) event.getY();

                index = y / ((textHeiht) / arrays.length); // 获取当前位置

                if (index < 1) {
                    index = 0;
                } else if (index >= arrays.length) {
                    index = arrays.length - 1;
                }

                if (position != index) {
                    position = index;
                    //Log.d("Y == ", arrays[position]);
                    if (indexText != null && StringUtils.isNotEmpty(arrays[position])) {
                        indexText.onIndexText(this, arrays[position], position);
                    }
                }

                isTouch = true;
                break;

            // 事件抬起和事件取消的时候执行
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (indexText != null) {
                    indexText.onHide();
                }
                isTouch = false;
                break;

        }
        invalidate();
        return true; // 返回true默认是执行该控件的事件处理
    }


    // 设置需要显示的数据
    public IndexView setData(List<? extends BasePinyinInfo> data) {

        if (null == data || data.size() == 0) {
            return this;
        }
        List<? extends BasePinyinInfo> arrs = Stream.of(data).filter(i -> i.isShowPinyin()).collect(Collectors.toList());
        arrays = new String[arrs.size()];
        // 对数据进行筛选到数组中
        for (int i = 0; i < arrs.size(); i++) {
            if (arrs.get(i).isShowPinyin() && StringUtils.isNotEmpty(arrs.get(i).getFirstPinyin())) {
                arrays[i] = arrs.get(i).getFirstPinyin();
            }
        }

        return this;
    }


    public interface OnIndexText {
        void onIndexText(View v, String text, int position);

        void onHide();
    }

    private OnIndexText indexText;

    public void setOnIndexText(OnIndexText index) {
        indexText = index;
    }

}
