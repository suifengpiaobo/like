package com.aladdin.like.module.mine.atlas.adapter;

import android.content.Context;
import android.support.v4.widget.Space;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.AtlasPicturePojo;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.utils.LogUtil;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description 我的 主题 适配器
 * Created by zxl on 2017/5/20 下午11:29.
 * Email:444288256@qq.com
 */
public class AtlasNotChooseAdapter extends BaseAdapter<AtlasPicturePojo.AtlasPicture> {
    onItemClickListener mItemClickListener;
    private Context mContext;

    private Integer[] imgs = {
            R.drawable.picture_1, R.drawable.picture_2, R.drawable.picture_3,
            R.drawable.picture_4, R.drawable.picture_5, R.drawable.picture_6,
            R.drawable.picture_7, R.drawable.picture_8, R.drawable.picture_9,
            R.drawable.picture_10, R.drawable.picture_11, R.drawable.picture_12,
    };

    public AtlasNotChooseAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, AtlasPicturePojo.AtlasPicture item) {

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
        AtlasPicturePojo.AtlasPicture item = getItemObject(position);
        LogUtil.i("item--->>>"+item);

        if (position == 11){
            viewHolder.mSearchSpace2.setVisibility(View.VISIBLE);
        }else{
            viewHolder.mSearchSpace2.setVisibility(View.GONE);
        }
        if (item != null) {
            viewHolder.mHorizontalTypeName.setText(item.name);

            ImageLoaderUtils.loadResPic(mContext, viewHolder.mSearchHorizontalBg, imgs[position]);
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
    public int getCommonType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public AtlasPicturePojo.AtlasPicture getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_mine_not_atlas, null);
        return new HorizontalViewHolder(mView);
    }

    static class HorizontalViewHolder extends BaseViewHolder {
        @BindView(R.id.search_space_1)
        Space mSearchSpace1;
        @BindView(R.id.search_space_2)
        Space mSearchSpace2;
        @BindView(R.id.search_horizontal_bg)
        SimpleDraweeView mSearchHorizontalBg;
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
        void onItemClick(AtlasPicturePojo.AtlasPicture item);
    }


}
