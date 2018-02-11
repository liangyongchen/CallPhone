package com.asen.callphone.base.view.pinyin.recycler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.View;

import com.asen.callphone.R;
import com.asen.callphone.base.view.pinyin.BasePinyinInfo;

import java.util.List;

/**
 * getItemOffsets、onDrawOver、onDraw
 * getItemOffsets：设置分割区域的大小范围
 * onDraw：在分割区域块里面绘制(ItemDecoration)
 * onDrawOver：在内容区域块里面绘制(item)
 * Created by asus on 2018/2/6.
 */

public class SuspensionDecoration extends RecyclerView.ItemDecoration {

    Context mContext;

    private List<? extends BasePinyinInfo> mDatas;

    private Paint mPaint;         // 分割线用 padding 出来的空间的画笔
    private TextPaint mTextPaint; // 字体画笔

    private Paint.FontMetrics mFontMetrics;  // 字体度量

    private int mRectHeight;             // 分割线用 padding 出来的空间
    private int mDividerHeight = 1;          // 分割线 (无标题的时候可用)
    private static int mTitleFontSize;   //title字体大小
    private static int COLOR_RECT_BG;    // 标题框背景颜色
    private static int COLOR_TITLE_FONT; // 标题框字体颜色

    public  void setData(List<? extends BasePinyinInfo> data){
        this.mDatas = data;
    }

    public SuspensionDecoration(Context mContext, List<? extends BasePinyinInfo> data) {
        this.mContext = mContext;
        this.mDatas = data;
        COLOR_RECT_BG = mContext.getResources().getColor(R.color.line_divider);
        COLOR_TITLE_FONT = mContext.getResources().getColor(R.color.colorAccent);

        mRectHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, mContext.getResources().getDisplayMetrics());
        mTitleFontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, mContext.getResources().getDisplayMetrics());

        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(mTitleFontSize);
        mTextPaint.setColor(COLOR_TITLE_FONT);
        mFontMetrics = mTextPaint.getFontMetrics();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);// 设置抗锯齿
        mPaint.setColor(COLOR_RECT_BG);

    }


    /**
     * 设置分割线开端
     * 设置分割线框的大小
     *
     * @param outRect 分割线框
     * @param view
     * @param parent
     * @param state
     */
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        super.getItemOffsets(outRect, view, parent, state);

//        int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        int position = parent.getChildAdapterPosition(view);

        boolean asa = mDatas.get(position).isShowPinyin();

        if (mDatas.get(position).isShowPinyin()) {
            // outRect.set(0, mTitleHeight, 0, 0);
            // 或
            outRect.top = mRectHeight;
        } else {
            outRect.top = mDividerHeight;
        }


    }

    /**
     * 在分割区域的绘制
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
//        int chidCount = parent.getChildCount();
//        int left = parent.getPaddingLeft();
//        int right = parent.getWidth() - parent.getPaddingRight();
//        for (int i = 0; i < chidCount; i++) {
//
//            // 做个接口控制什么情况下画标题框
//
//            View view = parent.getChildAt(i);// 获取item的view
//            int top = view.getTop() - mRectHeight;
//            int bottom = view.getTop();
//
//            c.drawRect(left, top, right, bottom, mPaint);
//            c.drawText("北京", view.getPaddingLeft() + 20, view.getTop() - mRectHeight / 2 + mTitleFontSize / 2, mTextPaint);
//
//        }


    }

    /**
     * 在内容区域上面的绘制
     *
     * @param c
     * @param parent
     * @param state
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        // 悬浮标题
        int chidCount = parent.getChildCount(); // 获取界面能显示的item总数
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < chidCount; i++) {

            View view = parent.getChildAt(i);

            int position = parent.getChildAdapterPosition(view); // 根据当前 view 获取 在列表中的位置position

            if (i != 0) {

                int top = view.getTop() - mRectHeight;
                int bottom = view.getTop();

                if (mDatas.get(position).isShowPinyin()) {

                    drawHeaderRect(c, position, left, top, right, bottom);

                } else {

                    c.drawLine(left, bottom, right, bottom, mPaint);
                }

            } else {

                int top = parent.getPaddingTop(); // 父控件的最上端

                int suggesTop = view.getBottom() - mRectHeight; // 获取view最上层的高度

                if (position == 0) {

                    if (suggesTop < top)
                        top = suggesTop;

                    // 判断当前的view的下一个view是否显示 拼音 ，显示当前 view 的 bottpm - rect 的 高度 获取 top ，让后让画布画出来
                } else if (mDatas.get(position + 1).isShowPinyin()) {

                    if (suggesTop < top)
                        top = suggesTop;
                }
                int bottom = top + mRectHeight;
                drawHeaderRect(c, position, left, top, right, bottom);
            }

        }

    }

    private void drawHeaderRect(Canvas c, int position, int left, int top, int right, int bottom) {

        // 绘制Rect
        c.drawRect(left, top, right, bottom, mPaint);

        float titleX = left + 20;
        float titleY = bottom - mFontMetrics.descent;
        // 绘制Title
        c.drawText(mDatas.get(position).getFirstPinyin(), titleX, titleY, mTextPaint);

    }

}
