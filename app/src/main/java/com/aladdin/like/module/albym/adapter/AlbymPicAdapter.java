package com.aladdin.like.module.albym.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.utils.DensityUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description 搜索上方横向滑动适配器
 * Created by zxl on 2017/5/1 上午6:23.
 * Email:444288256@qq.com
 */
public class AlbymPicAdapter extends BaseAdapter<ThemeDetail.Theme> {
    onItemClickListener mItemClickListener;
    private Context mContext;

    public AlbymPicAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {

    }

    private Bitmap returnBitmap(Uri uri) {
        Bitmap bitmap = null;
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(uri.toString()));
        if (resource != null){
            File file = resource.getFile();
            if (file != null && !TextUtils.isEmpty(file.getPath())) {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            }
        }
        return bitmap;

    }

    @Override
    public int getCommonType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
        ThemeDetail.Theme item = getItemObject(position);
        if (item != null) {
            viewHolder.mResultImg.setImageURI(item.imageUrl);
            viewHolder.mResultTypeName.setText(item.imageName);
            viewHolder.mResultTime.setText(item.createTimeStr);

            Uri uri = Uri.parse(item.imageUrl);
            Bitmap bitmap = returnBitmap(uri);
            int width = bitmap.getWidth();//994
            float scale = (DensityUtils.mScreenWidth/2- DensityUtils.dip2px(15))/(float)width;
            int height = bitmap.getHeight();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mResultImg.getLayoutParams();
            params.height = (int) (height*scale);
            params.weight = (int)(width*scale);
            viewHolder.mResultImg.setLayoutParams(params);

//            viewHolder.mResultImg.setImageURI(item.themeImgUrl);

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
    public void onBindCommon(RecyclerView.ViewHolder holder, ThemeDetail.Theme item) {
    }

    public ThemeDetail.Theme getItemObject(int position) {
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
        void onItemClick(ThemeDetail.Theme item);
    }


}
