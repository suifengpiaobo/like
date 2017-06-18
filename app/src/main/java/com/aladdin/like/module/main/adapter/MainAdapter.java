package com.aladdin.like.module.main.adapter;

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
import com.aladdin.like.model.ThemeModes;
import com.aladdin.utils.DensityUtils;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;

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
            viewHolder.mMainTypeName.setText(item.themeName);
            viewHolder.mMainTime.setText(item.createTimeStr);

            Uri uri = Uri.parse(item.themeImgUrl);
            Bitmap bitmap = returnBitmap(uri);
            int width = bitmap.getWidth();//994
            float scale = (DensityUtils.mScreenWidth/2-DensityUtils.dip2px(15))/(float)width;
            int height = bitmap.getHeight();
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mMainImg.getLayoutParams();
            params.height = (int) (height*scale);
            params.weight = (int)(width*scale);
            viewHolder.mMainImg.setLayoutParams(params);

            viewHolder.mMainImg.setImageURI(item.themeImgUrl);
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
