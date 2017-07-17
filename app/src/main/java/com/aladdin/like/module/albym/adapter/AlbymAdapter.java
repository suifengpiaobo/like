package com.aladdin.like.module.albym.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.sunfusheng.glideimageview.GlideImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description 搜索上方横向滑动适配器
 * Created by zxl on 2017/5/1 上午6:23.
 * Email:444288256@qq.com
 */
public class AlbymAdapter extends BaseAdapter<AlbymModel.AlbymDetail> {
    onItemClickListener mItemClickListener;
    private Context mContext;
    private int pressedPosition = -1;

    public AlbymAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setPressedPosition(int position) {
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
        AlbymModel.AlbymDetail item = getItemObject(position);
        LogUtil.i("--item-->>"+item);
        if (item != null) {
            if (pressedPosition >= 0) {
                if (pressedPosition != position) {
                    viewHolder.mLayer.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mLayer.setVisibility(View.GONE);
                }
            } else {
                viewHolder.mLayer.setVisibility(View.GONE);
            }

            float scale = (DensityUtils.mScreenWidth / 2 - DensityUtils.dip2px(15)) / (float)item.width;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.mMainImg.getLayoutParams();
            params.height = (int) (item.height * scale);
            params.width = (int) (item.width * scale);
            viewHolder.mMainImg.setLayoutParams(params);

            viewHolder.mMainTypeName.setText(item.albymName);
            viewHolder.mMainTime.setText(item.createTimeStr);

            viewHolder.mMainImg.loadImage(item.albymUrl, R.color.placeholder_color);

            viewHolder.mMainImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(v, viewHolder.mMainImg, item);
                    }
                }
            });

            viewHolder.mMainImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemClickListener.onLongClickListener(v, position, item);
                    return true;
                }
            });
        }
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, AlbymModel.AlbymDetail item) {
    }

    public AlbymModel.AlbymDetail getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_ablym_item, null);
        return new HorizontalViewHolder(mView);
    }

    static class HorizontalViewHolder extends BaseViewHolder {
        @BindView(R.id.main_img)
        GlideImageView mMainImg;
        @BindView(R.id.main_type_name)
        TextView mMainTypeName;
        @BindView(R.id.main_time)
        TextView mMainTime;
        @BindView(R.id.main_item)
        RelativeLayout mMainItem;
        @BindView(R.id.img_layer)
        GlideImageView mLayer;

        HorizontalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
//        void onItemClick(AlbymModel.AlbymDetail item);

        void onItemClick(View v, GlideImageView mPrefectureBg, AlbymModel.AlbymDetail item);

        void onLongClickListener(View view, int position, AlbymModel.AlbymDetail item);
    }


}
