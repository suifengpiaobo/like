package com.aladdin.like.module.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
 * Description
 * Created by zxl on 2017/4/30 下午4:08.
 * Email:444288256@qq.com
 */
public class MainAdapter extends BaseAdapter<PrefecturePojo.Prefecture> {
    onItemClickListener mItemClickListener;
    private Context mContext;

    private Integer[] imgs = {
            R.drawable.picture_1, R.drawable.picture_2, R.drawable.picture_3,
            R.drawable.picture_4,  R.drawable.picture_6,
            R.drawable.picture_7, R.drawable.picture_8,
            R.drawable.picture_10, R.drawable.picture_11, R.drawable.picture_12,
    };

    public MainAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        MainViewHolder viewHolder = (MainViewHolder) holder;
        PrefecturePojo.Prefecture item = getItemObject(position);
        Log.e("MainApadter","item--->>>"+item);
        if (item != null) {
            viewHolder.mMainTypeName.setText(item.typeName);

            ImageLoaderUtils.displayRoundNative(mContext, viewHolder.mMainImg, imgs[position]);
            viewHolder.mMainItem.setOnClickListener(new View.OnClickListener() {
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
    public int getCommonType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, PrefecturePojo.Prefecture item) {
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
        @BindView(R.id.main_img)
        ImageView mMainImg;
        @BindView(R.id.main_type_name)
        TextView mMainTypeName;
        @BindView(R.id.main_time)
        TextView mMainTime;
        @BindView(R.id.main_item)
        LinearLayout mMainItem;

        MainViewHolder(View view) {
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
