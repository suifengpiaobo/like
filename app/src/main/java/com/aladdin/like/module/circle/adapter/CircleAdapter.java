package com.aladdin.like.module.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

    private int pressedPosition = -1;

    public CircleAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    public void setPressedPosition(int position){
        pressedPosition = position;
        notifyDataSetChanged();
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
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.mMainImg.getLayoutParams();
                params.height = (int) (item.height*scale);
                params.width = (int)(item.width*scale);
                viewHolder.mMainImg.setLayoutParams(params);
                viewHolder.mMainImgLayer.setLayoutParams(params);

                viewHolder.mMainImg.setImageURI(item.diaryImage);

                if (pressedPosition >=0){
                    if (pressedPosition!=position){
                        viewHolder.mMainImgLayer.setVisibility(View.VISIBLE);
                    }else{
                        viewHolder.mMainImgLayer.setVisibility(View.GONE);
                    }
                }else{
                    viewHolder.mMainImgLayer.setVisibility(View.GONE);
                }
                viewHolder.mMainImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.onItemClick(v,viewHolder.mMainImg,item);
                    }
                });
//                viewHolder.mMainImg.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        mItemClickListener.onLongClickListener(v,position,item);
//
//                        return true;
//                    }
//                });
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

    class CircleViewHolder extends BaseViewHolder {
        @BindView(R.id.main_img)
        SimpleDraweeView mMainImg;
        @BindView(R.id.main_img_layer)
        SimpleDraweeView mMainImgLayer;

        DiaryDetail.Diary item;

        CircleViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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

        void onLongClickListener(View view,int position,DiaryDetail.Diary item);
    }
}
