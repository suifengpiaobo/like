package com.aladdin.like.module.download.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.utils.DensityUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/5/1 上午3:55.
 * Email:444288256@qq.com
 */
public class PictureDetailsAdapter extends BaseAdapter<ThemeDetail.Theme> {

    private onItemClickListener mOnItemClickListener;

    public PictureDetailsAdapter(Context mContext) {
        super(mContext);

    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, ThemeDetail.Theme item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainViewHolder viewHolder = (MainViewHolder) holder;
        ThemeDetail.Theme item = getItemObject(position);
        if (item != null) {
            float scale = (DensityUtils.mScreenWidth/2-DensityUtils.dip2px(15))/(float)item.width;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mPrefectureBg.getLayoutParams();
            params.height = (int) (item.height*scale);
            params.weight = (int)(item.width*scale);
            viewHolder.mPrefectureBg.setLayoutParams(params);
            viewHolder.mPrefectureBg.setImageURI(item.imageUrl);
            viewHolder.mTypeName.setText(item.imageName);
            viewHolder.mTime.setText(item.collectionTimes + "");


            viewHolder.mPrefectureBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(item);
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
    public int getItemCount() {
        return mDatas.size();
    }

    public ThemeDetail.Theme getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_main_prefecture, null);
        return new MainViewHolder(mView);
    }

    static class MainViewHolder extends BaseViewHolder {
        @BindView(R.id.main_img)
        SimpleDraweeView mPrefectureBg;
        @BindView(R.id.main_type_name)
        TextView mTypeName;
        @BindView(R.id.main_time)
        TextView mTime;

        MainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(ThemeDetail.Theme item);
    }
}
