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
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.sunfusheng.glideimageview.GlideImageView;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/4/30 下午4:08.
 * Email:444288256@qq.com
 */
public class AlbymDetailsAdapter extends BaseAdapter<ThemeDetail.Theme> {
    onItemClickListener mItemClickListener;
    private Context mContext;
    private int pressedPosition = -1;

    public AlbymDetailsAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setPressedPosition(int position) {
        pressedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public int getCommonType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainViewHolder viewHolder = (MainViewHolder) holder;
        ThemeDetail.Theme item = getItemObject(position);
        if (item != null) {
            viewHolder.item = item;
            double scale1 = ((DensityUtils.mScreenWidth/2 -DensityUtils.dip2px(15))) / (float) item.width;
            NumberFormat ddf1= NumberFormat.getNumberInstance() ;
            ddf1.setMaximumFractionDigits(2);
            String scale = ddf1.format(scale1);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.mMainImg.getLayoutParams();
            params.height = (int) (item.height * Float.valueOf(scale));
            params.width = (int) (item.width * Float.valueOf(scale));
            viewHolder.mMainImg.setLayoutParams(params);

//            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) viewHolder.mMainImg.getLayoutParams();
//            params.height = (int) (item.height+DensityUtils.dip2px(100) * Float.valueOf(scale));
//            params.width = (int) (item.width * Float.valueOf(scale));
//            viewHolder.mLayer.setLayoutParams(params1);

            viewHolder.mMainImg.loadImage(item.imageUrl,R.color.placeholder_color);
            viewHolder.mMainTypeName.setText(item.imageName);
            viewHolder.mMainTime.setText(item.createTimeStr);

            if (pressedPosition >= 0) {
                if (pressedPosition != position) {
                    viewHolder.mLayer.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mLayer.setVisibility(View.GONE);
                }
            } else {
                viewHolder.mLayer.setVisibility(View.GONE);
            }

            viewHolder.mMainImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null){
                        mItemClickListener.onItemClick(v, viewHolder.mMainImg,item);
                    }
                }
            });

            viewHolder.mMainImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mItemClickListener!=null){
                        mItemClickListener.onLongClickListener(v,position,item);
                    }
                    return false;
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
        View mView = View.inflate(mContext, R.layout.layout_albym_details, null);
        return new MainViewHolder(mView);
    }

    class MainViewHolder extends BaseViewHolder  {
        @BindView(R.id.albym_img)
        GlideImageView mMainImg;
        @BindView(R.id.albym_type_name)
        TextView mMainTypeName;
        @BindView(R.id.albym_time)
        TextView mMainTime;
        @BindView(R.id.albym_item)
        RelativeLayout mMainItem;
        @BindView(R.id.img_layer)
        ImageView mLayer;

        ThemeDetail.Theme item;

        MainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View v, GlideImageView mMainImg, ThemeDetail.Theme item);
        void onLongClickListener(View view, int position, ThemeDetail.Theme item);
    }

}
