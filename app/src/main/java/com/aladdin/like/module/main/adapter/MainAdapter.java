package com.aladdin.like.module.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.utils.DensityUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/4/30 下午4:08.
 * Email:444288256@qq.com
 */
public class MainAdapter extends BaseAdapter<ThemeModes.Theme> {
    onItemClickListener mItemClickListener;
    private Context mContext;

    public MainAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getCommonType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainViewHolder viewHolder = (MainViewHolder) holder;
        ThemeModes.Theme item = getItemObject(position);
        if (item != null) {
            viewHolder.mMainImg.setImageURI(item.themeImgUrl);
            float scale = (DensityUtils.mScreenWidth/2-DensityUtils.dip2px(15))/(float)item.width;
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mMainImg.getLayoutParams();
            params.height = (int) (item.height*scale);
            params.weight = (int)(item.width*scale);
            viewHolder.mMainImg.setLayoutParams(params);

            viewHolder.mMainImg.setImageURI(item.themeImgUrl);

            viewHolder.mMainTypeName.setText(item.themeName);
            viewHolder.mMainTime.setText(item.createTimeStr);

            viewHolder.mMainItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(item);
                    }
                }
            });

            viewHolder.setIsRecyclable(true);
        }
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, ThemeModes.Theme item) {

    }

    public ThemeModes.Theme getItemObject(int position) {
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
        SimpleDraweeView mMainImg;
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
        void onItemClick(ThemeModes.Theme item);
    }

}
