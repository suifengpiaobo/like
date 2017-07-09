package com.aladdin.like.module.albym.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.width;

/**
 * Description 搜索上方横向滑动适配器
 * Created by zxl on 2017/5/1 上午6:23.
 * Email:444288256@qq.com
 */
public class AlbymPicAdapter extends BaseAdapter<ThemeDetail.Theme> {
    onItemClickListener mItemClickListener;
    private Context mContext;
    private int pressedPosition = -1;

    public AlbymPicAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setPressedPosition(int position){
        pressedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

    }

    @Override
    public int getCommonType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
        ThemeDetail.Theme item = getItemObject(position);
        if (item != null) {
            viewHolder.mResultTypeName.setText(item.imageName);
            viewHolder.mResultTime.setText(item.createTimeStr);

            float scale = (DensityUtils.mScreenWidth / 2 - DensityUtils.dip2px(15)) / (float) item.width;
            int height = item.height;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.mResultImg.getLayoutParams();
            params.height = (int) (height * scale);
            params.width = (int) (width * scale);
            viewHolder.mResultImg.setLayoutParams(params);
            viewHolder.mResultImg.setImageURI(item.imageUrl);

            LogUtil.i("--pressedPosition-->>"+pressedPosition);
            if (pressedPosition >=0){
                if (pressedPosition!=position){
                    viewHolder.mLayer.setVisibility(View.VISIBLE);
                    LogUtil.i("--AB-->>"+pressedPosition);
                }else{
                    viewHolder.mLayer.setVisibility(View.GONE);
                    LogUtil.i("--CD->>"+pressedPosition);
                }
            }else{
                viewHolder.mLayer.setVisibility(View.GONE);
                LogUtil.i("--EF->>"+pressedPosition);
            }

            viewHolder.mResultItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, viewHolder.mResultImg, viewHolder.mWaterMark, item);
                    }
                }
            });

            viewHolder.mResultItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemClickListener.onLongClickListener(v,position,item);
                    return true;
                }
            });
        }
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, ThemeDetail.Theme item) {
    }

    public ThemeDetail.Theme getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_albym_pic, null);
        return new HorizontalViewHolder(mView);
    }

    static class HorizontalViewHolder extends BaseViewHolder {
        @BindView(R.id.result_img)
        SimpleDraweeView mResultImg;
        @BindView(R.id.result_type_name)
        TextView mResultTypeName;
        @BindView(R.id.result_time)
        TextView mResultTime;
        @BindView(R.id.result_item)
        RelativeLayout mResultItem;
        @BindView(R.id.watermark)
        ImageView mWaterMark;
        @BindView(R.id.main_img_layer)
        SimpleDraweeView mLayer;

        HorizontalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View v, SimpleDraweeView mPrefectureBg, ImageView mWaterMark, ThemeDetail.Theme item);

        void onLongClickListener(View view,int position,ThemeDetail.Theme item);
    }


}
