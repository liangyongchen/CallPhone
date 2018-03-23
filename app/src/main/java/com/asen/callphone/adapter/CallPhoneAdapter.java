package com.asen.callphone.adapter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asen.callphone.R;
import com.asen.callphone.base.app.BaseActivity;
import com.asen.callphone.model.CallPhoneModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by asus on 2017/11/24.
 */

public class CallPhoneAdapter extends RecyclerView.Adapter<ViewHolder> {

    private BaseActivity mContext;
    private List<CallPhoneModel> mData;
    private LayoutInflater mInflater;


    public CallPhoneAdapter(BaseActivity context, List<CallPhoneModel> list) {
        this.mContext = context;
        this.mData = list;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mInflater.inflate(R.layout.call_list_view, parent, false);

        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            ItemViewHolder h = (ItemViewHolder) holder;
            h.setData(mData.get(position), position);
            h.initEvent(position);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ItemViewHolder extends ViewHolder {

        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.phone)
        TextView mPhone;
        @BindView(R.id.type)
        TextView mType;
        @BindView(R.id.size)
        TextView mSize;
        @BindView(R.id.photo)
        ImageView mPhoto;
        @BindView(R.id.callPhone)
        ImageView mCallPhone;
        @BindView(R.id.btnCallItem)
        LinearLayout mBtnCallItem;
        @BindView(R.id.line)
        View mLine;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setData(CallPhoneModel m, int position) {

            if (m == null) {
                return;
            }

            mName.setText(String.format("%s", m.getName()));
            mPhone.setText(String.format("%s", m.getPhone()));
            mType.setText(String.format("%s", m.getType()));
//            ImageLoader.getInstance().displayImage(，
//                    m.getPhoto(),
//                    mPhoto,
//                    ImageDisplayUtil.setCircleBitmapDisplayer(5),
//                    new ImageDisplayUtil.AnimateFirstDisplayListener());

            mLine.setVisibility(View.GONE);
            mSize.setVisibility(View.GONE);
            if (mData.size() == (position + 1)) {
                mLine.setVisibility(View.VISIBLE);
                mSize.setVisibility(View.VISIBLE);
                mSize.setText(String.format("显示 %s 联系人", mData.size()));
            }

        }

        public void initEvent(int postion) {
            // 打电话
            mCallPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 跳到拨打界面拨打电话
                    // Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mData.get(postion).getPhone()));
                    // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // 跳过拨号界面，直接拨打电话
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mData.get(postion).getPhone()));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mContext.startActivity(intent);
                }
            });

            mBtnCallItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongItem.onLongItemClick(v, postion);
                    return false;
                }
            });

        }

    }

    // 界面刷新
    public void addItem(List<CallPhoneModel> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(View v, int postion);
    }

    private OnLongItemClickListener onLongItem;

    public void setOnLongItemClickListener(OnLongItemClickListener onLongItem) {
        this.onLongItem = onLongItem;
    }


}
