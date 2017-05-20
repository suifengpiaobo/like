package com.aladdin.like.module.download.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.utils.ImageLoaderUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/5/1 上午3:55.
 * Email:444288256@qq.com
 */
public class PictureDetailsAdapter extends BaseAdapter<PrefecturePojo.Prefecture> {

    private onItemClickListener mOnItemClickListener;



    public PictureDetailsAdapter(Context mContext) {
        super(mContext);

    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, PrefecturePojo.Prefecture item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainViewHolder viewHolder = (MainViewHolder) holder;
        PrefecturePojo.Prefecture item = getItemObject(position);
        if (item != null){
            ImageLoaderUtils.displayRoundNative(mContext,viewHolder.mPrefectureBg,R.mipmap.ic_github);

            viewHolder.mTypeName.setText(item.typeName);
            viewHolder.mTime.setText(item.time+"");

            viewHolder.mPrefectureBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null){
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

    public PrefecturePojo.Prefecture getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_main_prefecture, null);
        return new MainViewHolder(mView);
    }

    static class MainViewHolder extends BaseViewHolder {
        @BindView(R.id.prefecture_bg)
        ImageView mPrefectureBg;
        @BindView(R.id.type_name)
        TextView mTypeName;
        @BindView(R.id.time)
        TextView mTime;

        MainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface onItemClickListener{
        void onItemClick(PrefecturePojo.Prefecture item);
    }
}
