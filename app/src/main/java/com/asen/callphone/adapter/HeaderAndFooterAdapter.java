package com.asen.callphone.adapter;

import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by 河马安卓 on 2018/3/26.
 */

public abstract class HeaderAndFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM_HEADER = 1000000; // 设置头部的类型基准
    private static final int TYPE_ITEM_FOOTER = 2000000; // 设置脚本的类型基准

    private RecyclerView.Adapter mAdapter;

    // 头部视图, item 从 0 开始：头部双层map，为了可以调用同一xml布局
    private SparseArrayCompat<SparseArrayCompat> mHeaderData = new SparseArrayCompat<SparseArrayCompat>(); // 简map集合SparseArrayCompat
    // 脚本试图, item 从后面开始
    private SparseArrayCompat<View> mFooterData = new SparseArrayCompat<View>(); // 简map集合SparseArrayCompat

    // 获取适配器的所有 item
    public int getInnerItemCount() {
        return mAdapter != null ? mAdapter.getItemCount() : 0;
    }

    // 获取头部视图数量
    public int getHeaderViewCount() {
        return mHeaderData.size();
    }

    // 获取脚本视图数量
    public int getFooterViewCount() {
        return mFooterData.size();
    }

    // 添加脚本视图，以 layoutid 布局做 key
    public void addFooterView(int layoutid_key, View view) {
        mFooterData.put(layoutid_key, view);
    }

    // 添加头部视图，以 layoutid 布局做 key
    public void addHeaderView(int layoutid_key, Object obj) {
        SparseArrayCompat headerContainer = new SparseArrayCompat();
        headerContainer.put(layoutid_key, obj);
        mHeaderData.put(mHeaderData.size() + TYPE_ITEM_HEADER, headerContainer);
    }

    // 设置方式添加头部视图
    public void setHeaderView(int headerPosition, int layoutId, Object data) {

        // 判断是否之前添加有该 headerPosition 的视图，有就替换掉
        if (mHeaderData.size() > headerPosition) {
            SparseArrayCompat headerContainer = new SparseArrayCompat();
            headerContainer.put(layoutId, data);
            mHeaderData.setValueAt(headerPosition, headerContainer);
        } else {
            addHeaderView(layoutId, data);
        }

    }


    // 判断是否是头部视图，头部视图从0开始
    private boolean isHeaderView(int position) {
        return getHeaderViewCount() > position;
    }

    // 判断是否为脚部视图，position 大于 头部加适配器的item数量，剩下的就是脚本数量
    private boolean isFooterView(int position) {
        return position >= (getHeaderViewCount() + getInnerItemCount());
    }

    public HeaderAndFooterAdapter(Adapter adapter) {
        this.mAdapter = adapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // viewType 是 key 值,也是 布局

        // 判断是否为头部视图
        if (mHeaderData.get(viewType) != null) {
            View v = LayoutInflater.from(parent.getContext()).inflate(mHeaderData.get(viewType).keyAt(0), parent, false);
            return new ViewHolder(v);
        }

        // 判断是否为脚本视图
        if (mFooterData.get(viewType) != null) {
            View v = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
            return new ViewHolder(v);
        }

        return mAdapter.onCreateViewHolder(parent, viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // 返回头部数据
        if (isHeaderView(position)) {
            int layoutId = mHeaderData.get(getItemViewType(position)).keyAt(0);
            onBindHeaderHolder((ViewHolder) holder, position, layoutId, mHeaderData.get(getItemViewType(position)).get(layoutId));
            return;
        }

        // 返回脚本数据
        if (isFooterView(position)) {
            int layoutId = mFooterData.keyAt(position - getHeaderViewCount() - getInnerItemCount());
            onBindFooterHolder((ViewHolder) holder, position, layoutId, mFooterData.get(position));
            return;
        }

        mAdapter.onBindViewHolder(holder, position - getHeaderViewCount());
    }

    // 添加 view 或 自定义view 或layout 的头部视图
    protected abstract void onBindHeaderHolder(ViewHolder holder, int position, int layoutID, Object obj);

    // 添加 view 或自定义view 或layout 的脚本视图
    protected abstract void onBindFooterHolder(ViewHolder holder, int position, int layoutID, Object obj);

    @Override
    public int getItemCount() {
        return getInnerItemCount() + getHeaderViewCount() + getFooterViewCount();
    }

    @Override
    public int getItemViewType(int position) {

        // 判断是否为头部试图
        if (isHeaderView(position)) {
            return mHeaderData.keyAt(position); // 设置 key 值，這里的 key 是 xml布局返回的int数值
        }

        // 判断是否为脚本视图
        if (isFooterView(position)) {
            return mFooterData.keyAt(position -
                    getHeaderViewCount() -
                    getInnerItemCount()); // 设置 key 值，這里的 key 是 xml布局返回的int数值
        }

        return super.getItemViewType(position);
    }


    protected class ViewHolder extends RecyclerView.ViewHolder {

        private View mConvertView;

        public ViewHolder(View itemView) {
            super(itemView);
            mConvertView = itemView;
        }

        public <T extends View> T getView(int viewId) {
            return mConvertView.findViewById(viewId);
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mAdapter.onAttachedToRecyclerView(recyclerView);
        //为了兼容GridLayout
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (mHeaderData.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (mFooterData.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }

    }

    // 当列表项出现到可视界面的时候调用
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderView(position) || isFooterView(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p =
                        (StaggeredGridLayoutManager.LayoutParams) lp;

                // 如果是刷新、加载更多或头布局、脚布局独占一行,否则按照设置展示
                p.setFullSpan(true);
                // 这样,即使布局为 GridLayoutManager 或者 StaggeredGridLayoutManager 刷新、加载更多、头布局、脚布局都是单独的一行了。
            }
        }
    }


}
