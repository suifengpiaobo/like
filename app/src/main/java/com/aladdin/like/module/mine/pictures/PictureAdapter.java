package com.aladdin.like.module.mine.pictures;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.utils.LogUtil;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/5/21 下午11:43.
 * Email:444288256@qq.com
 */
public class PictureAdapter extends BaseAdapter<PrefecturePojo.Prefecture> {

    private Integer[] imgs = {
            R.drawable.picture_1, R.drawable.picture_2, R.drawable.picture_3,
            R.drawable.picture_4, R.drawable.picture_6,
            R.drawable.picture_7, R.drawable.picture_8,
            R.drawable.picture_10, R.drawable.picture_11, R.drawable.picture_12,
    };

    public PictureAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, PrefecturePojo.Prefecture item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PictureViewHolder viewHolder = (PictureViewHolder) holder;
        PrefecturePojo.Prefecture item= getItemObject(position);
        LogUtil.i("--item-->>"+item);
        if (item != null){
            ImageLoaderUtils.displayRoundNative(mContext, viewHolder.mMinePictureImg, imgs[position]);
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

    public PrefecturePojo.Prefecture getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }


    static class PictureViewHolder extends BaseViewHolder{
        @BindView(R.id.mine_picture_img)
        ImageView mMinePictureImg;

        PictureViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
