package com.aladdin.like.module.mine.pictures.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aladdin.like.R;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.utils.DensityUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.sunfusheng.glideimageview.GlideImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/5/21 下午11:43.
 * Email:444288256@qq.com
 */
public class PictureAdapter extends BaseAdapter<CollectionImage.Collection> {

    onItemClickListener mItemClickListener;
    public PictureAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, CollectionImage.Collection item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PictureViewHolder viewHolder = (PictureViewHolder) holder;
        CollectionImage.Collection item= getItemObject(position);
        if (item != null){
            float scale = (DensityUtils.mScreenWidth/2-DensityUtils.dip2px(15))/(float)item.width;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mMinePictureImg.getLayoutParams();
            params.height = (int) (item.height*scale);
            params.weight = (int)(item.width*scale);
            viewHolder.mMinePictureImg.setLayoutParams(params);

            viewHolder.mMinePictureImg.loadImage(item.resourceUrl,R.color.placeholder_color);

            viewHolder.mMinePictureImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItemClickListener != null){
                        mItemClickListener.onItemClick(v,viewHolder.mMinePictureImg,item);
                    }
                }
            });
        }
    }

    @Override
    public int getCommonType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_mine_picture, null);
        return new PictureViewHolder(mView);
    }

    public CollectionImage.Collection getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }


    static class PictureViewHolder extends BaseViewHolder{
        @BindView(R.id.mine_picture_img)
        GlideImageView mMinePictureImg;

        PictureViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View view, GlideImageView pic, CollectionImage.Collection item);
    }
}
