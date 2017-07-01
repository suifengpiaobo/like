package com.aladdin.like.module.circle.adapter;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description
 * Created by zxl on 2017/5/1 上午9:47.
 * Email:444288256@qq.com
 */
public class CircleAdapter extends BaseAdapter<DiaryDetail.Diary> {
    public onItemClickListener mItemClickListener;
    private Context mContext;

    public CircleAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, DiaryDetail.Diary item) {

    }

    @Override
    public int getCommonType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        View mView = View.inflate(mContext, R.layout.layout_circle, null);
        return new CircleViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CircleViewHolder viewHolder = (CircleViewHolder) holder;
        DiaryDetail.Diary item = getItemObject(position);
        if (item != null) {
            if (item != null){
                viewHolder.item = item;
                float scale = (DensityUtils.mScreenWidth/2-DensityUtils.dip2px(15))/(float)item.width;
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mMainImg.getLayoutParams();
                params.height = (int) (item.height*scale);
                params.weight = (int)(item.width*scale);
                viewHolder.mMainImg.setLayoutParams(params);

                viewHolder.mMainImg.setImageURI(item.diaryImage);
            }

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

    }

    public DiaryDetail.Diary getItemObject(int position) {
        if (position < 0 || position > mDatas.size() - 1) return null;
        return mDatas.get(position);
    }

    class CircleViewHolder extends BaseViewHolder implements View.OnClickListener{
        @BindView(R.id.main_img)
        SimpleDraweeView mMainImg;
        @BindView(R.id.main_item)
        LinearLayout mMainItem;

        DiaryDetail.Diary item;

        CircleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mMainImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v,mMainImg,item);
        }
    }

    public onItemClickListener getItemClickListener() {
        return mItemClickListener;
    }

    public void setItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface onItemClickListener {
        void onItemClick(View view,SimpleDraweeView simpleDraweeView,DiaryDetail.Diary item);
    }
}
