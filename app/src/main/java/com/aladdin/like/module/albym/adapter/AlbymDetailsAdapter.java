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
import com.aladdin.utils.ImageloaderUtil;
import com.aladdin.utils.LogUtil;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;

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

    public AlbymDetailsAdapter(Context context) {
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
            LogUtil.i("scale--->>>"+scale+"--scale--AA-->>"+Float.valueOf(scale)+"--width-->>"+params.width+"  --height-->>"+params.height);
            LogUtil.i("width--->>>"+item.width+"  ---height-->>"+item.height);

//            viewHolder.mMainImg.loadImage(item.imageUrl,R.color.placeholder_color);
            ImageloaderUtil.getInstance().loadRoundImaFromUrl(item.imageUrl,viewHolder.mMainImg,0);
            viewHolder.mMainTypeName.setText(item.imageName);
            viewHolder.mMainTime.setText(item.createTimeStr);
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

    class MainViewHolder extends BaseViewHolder implements View.OnClickListener {
        @BindView(R.id.albym_img)
        ImageView mMainImg;
        @BindView(R.id.albym_type_name)
        TextView mMainTypeName;
        @BindView(R.id.albym_time)
        TextView mMainTime;
        @BindView(R.id.albym_item)
        RelativeLayout mMainItem;
        ThemeDetail.Theme item;

        MainViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mMainImg.setOnClickListener(this);
            mMainItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, mMainImg, mMainItem, item);
        }
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View v, ImageView mMainImg, RelativeLayout mMainItem, ThemeDetail.Theme item);
    }

}
