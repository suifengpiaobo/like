package com.aladdin.like.module.mine.diary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aladdin.like.R;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.utils.DensityUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/5/22 上午12:15.
 * Email:444288256@qq.com
 */
public class MineDiaryAdapter extends BaseAdapter<DiaryDetail.Diary> {

    public MineDiaryAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, DiaryDetail.Diary item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PictureViewHolder viewHolder = (PictureViewHolder) holder;
        DiaryDetail.Diary item= getItemObject(position);
        if (item != null){
            float scale = (DensityUtils.mScreenWidth/2-DensityUtils.dip2px(15))/(float)item.width;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mMinePictureImg.getLayoutParams();
            params.height = (int) (item.height*scale);
            params.weight = (int)(item.width*scale);
            viewHolder.mMinePictureImg.setLayoutParams(params);

            viewHolder.mMinePictureImg.setImageURI(item.diaryImage.substring(29));
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

    public DiaryDetail.Diary getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }


    static class PictureViewHolder extends BaseViewHolder {
        @BindView(R.id.mine_picture_img)
        SimpleDraweeView mMinePictureImg;

        PictureViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
