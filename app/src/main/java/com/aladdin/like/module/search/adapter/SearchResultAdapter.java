package com.aladdin.like.module.search.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.utils.ImageLoaderUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description 搜索上方横向滑动适配器
 * Created by zxl on 2017/5/1 上午6:23.
 * Email:444288256@qq.com
 */
public class SearchResultAdapter extends BaseAdapter<PrefecturePojo.Prefecture> {
    onItemClickListener mItemClickListener;
    private Context mContext;

    private Integer[] imgs = {
            R.drawable.picture_1, R.drawable.picture_2, R.drawable.picture_3,
            R.drawable.picture_4,  R.drawable.picture_6,
            R.drawable.picture_7, R.drawable.picture_8,
            R.drawable.picture_10, R.drawable.picture_11, R.drawable.picture_12,
    };

    public SearchResultAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
        PrefecturePojo.Prefecture item = getItemObject(position);
        if (item != null) {
            viewHolder.mResultTypeName.setText(item.typeName);

            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imgs[position]);
            int height = bitmap.getHeight();

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mResultImg.getLayoutParams();
            params.height=height;
            viewHolder.mResultImg.setLayoutParams(params);

            ImageLoaderUtils.loadResPic(mContext, viewHolder.mResultImg, imgs[position]);
            viewHolder.mResultItem.setOnClickListener(new View.OnClickListener() {
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
        View mView = View.inflate(mContext, R.layout.layout_search_result, null);
        return new HorizontalViewHolder(mView);
    }

    static class HorizontalViewHolder extends BaseViewHolder {
        @BindView(R.id.result_img)
        SimpleDraweeView mResultImg;
        @BindView(R.id.result_type_name)
        TextView mResultTypeName;
        @BindView(R.id.result_time)
        TextView mResultTime;
        @BindView(R.id.result_item)
        LinearLayout mResultItem;

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
