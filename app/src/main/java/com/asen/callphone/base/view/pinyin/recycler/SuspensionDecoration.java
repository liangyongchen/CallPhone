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

    // 头部添加的总数不需要绘制
    private int mHeaderViewCount = 0;

    // 脚本添加的总数不需要绘制
    private int mFooterViewCount = 0;


    public void setData(List<? extends BasePinyinInfo> data) {
        this.mDatas = data;
    }

    // 设置头部的item数量
    public SuspensionDecoration setHeaderViewCount(int headerViewCount) {
        this.mHeaderViewCount = headerViewCount;
        return this;
    }

    // 设置脚本的item数量
    public SuspensionDecoration setFooterViewCount(int footerViewCount) {
        this.mFooterViewCount = footerViewCount;
        return this;
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

        // int position = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        int position = parent.getChildAdapterPosition(view);

        if (mDatas == null || mDatas.isEmpty() || position > (mDatas.size() + mHeaderViewCount - 1)) {
            return; // 防止越界
        }

        if (position >= mHeaderViewCount) {
            if (mDatas.get(position - mHeaderViewCount).isShowPinyin()) {
                // outRect.set(0, mTitleHeight, 0, 0);
                // 或
                outRect.top = mRectHeight;
            } else {
                outRect.top = mDividerHeight;
            }
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

            if (mDatas == null || mDatas.isEmpty() || position > (mDatas.size() + mHeaderViewCount - 1)) {
                return; // 防止越界
            }

            if (position >= mHeaderViewCount) {

                if (i != 0) {

                    int top = view.getTop() - mRectHeight; // 获取当前 view 在现实屏幕的的top位置（mRectHeight 是 item分割线的高度）
                    int bottom = view.getTop();

                    if (mDatas.get(position - mHeaderViewCount).isShowPinyin()) {

                        drawHeaderRect(c, position - mHeaderViewCount, left, top, right, bottom);// 画字母

                    } else {

                        c.drawLine(left, bottom, right, bottom, mPaint); // 画分割线
                    }

                } else {

                    int top = parent.getPaddingTop(); // 父控件的最上端

                    int suggesTop = view.getBottom() - mRectHeight; // 获取view最上层的高度

                    // 判断当前的view的下一个view是否显示 拼音 ，显示当前 view 的 bottpm - rect 的 高度 获取 top ，让后让画布画出来,替换上一个字母
                    if (mDatas.get(position - mHeaderViewCount + 1).isShowPinyin()) {

                        if (suggesTop < top)
                            top = suggesTop;
                    }
                    int bottom = top + mRectHeight;
                    drawHeaderRect(c, position - mHeaderViewCount, left, top, right, bottom);
                }

            } else {
                c.drawLine(left, view.getTop(), right, view.getTop(), mPaint); // 画分割线
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
