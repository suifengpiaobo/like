package com.aladdin.like.module.search.adapter;

import android.content.Context;
import android.support.v4.widget.Space;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description 搜索上方横向滑动适配器
 * Created by zxl on 2017/5/1 上午6:23.
 * Email:444288256@qq.com
 */
public class HorizontalAdapter extends BaseAdapter<PrefecturePojo.Prefecture> {
    onItemClickListener mItemClickListener;
    private Context mContext;

    public HorizontalAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, PrefecturePojo.Prefecture item) {
        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
//        PrefecturePojo.Prefecture item = getItemObject(position);

        if (item != null) {
            viewHolder.mHorizontalTypeName.setText(item.typeName);

            ImageLoaderUtils.displayRoundNative(mContext, viewHolder.mSearchHorizontalBg, R.mipmap.ic_github);
            viewHolder.mSearchHorizontalBg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(item);
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
    }

    @Override
    public int getCommonType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
//        PrefecturePojo.Prefecture item = getItemObject(position);
//        if (item != null) {
//            viewHolder.mHorizontalTypeName.setText(item.typeName);
//            ImageLoaderUtils.displayRoundNative(mContext, viewHolder.mSearchHorizontalBg, R.mipmap.ic_github);
//            viewHolder.mSearchHorizontalBg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mItemClickListener != null) {
//                        mItemClickListener.onItemClick(item);
//                    }
//                }
//            });
//        }
    }

    public PrefecturePojo.Prefecture getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_search_horizontal, null);
        return new HorizontalViewHolder(mView);
    }

    static class HorizontalViewHolder extends BaseViewHolder {
        @BindView(R.id.search_space_1)
        Space mSearchSpace1;
        @BindView(R.id.search_horizontal_bg)
        ImageView mSearchHorizontalBg;
        @BindView(R.id.horizontal_type_name)
        TextView mHorizontalTypeName;

        HorizontalViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(PrefecturePojo.Prefecture item);
    }


}
