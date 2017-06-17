package com.aladdin.like.module.search.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.utils.LogUtil;
import com.ease.adapter.BaseAdapter;
import com.ease.holder.BaseViewHolder;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description 搜索上方横向滑动适配器
 * Created by zxl on 2017/5/1 上午6:23.
 * Email:444288256@qq.com
 */
public class SearchResultAdapter extends BaseAdapter<ThemeModes.Theme> {
    onItemClickListener mItemClickListener;
    private Context mContext;

    public SearchResultAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
        ThemeModes.Theme item = getItemObject(position);
        if (item != null) {
            viewHolder.mResultTypeName.setText(item.themeName);

            Uri uri = Uri.parse(item.themeImgUrl);
            ImageRequest imageRequest = ImageRequestBuilder
                    .newBuilderWithSource(uri)
                    .setProgressiveRenderingEnabled(true)
                    .build();

            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            DataSource<CloseableReference<CloseableImage>>
                    dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);

            dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                     @Override
                                     public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                         int height = bitmap.getHeight();
                                         LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mResultImg.getLayoutParams();
                                         params.height = height;
                                         viewHolder.mResultImg.setLayoutParams(params);
                                     }

                                     @Override
                                     public void onFailureImpl(DataSource dataSource) {
                                     }
                                 },
                    CallerThreadExecutor.getInstance());

            viewHolder.mResultImg.setImageURI(item.themeImgUrl);

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
        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        HorizontalViewHolder viewHolder = (HorizontalViewHolder) holder;
//        ThemeModes.Theme item = getItemObject(position);
//        if (item != null) {
//            viewHolder.mResultTypeName.setText(item.themeName);
//
//            Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), imgs[position]);
//            int height = bitmap.getHeight();
//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.mResultImg.getLayoutParams();
//            params.height=height;
//            viewHolder.mResultImg.setLayoutParams(params);
//            ImageLoaderUtils.loadResPic(mContext, viewHolder.mResultImg, imgs[position]);
//            viewHolder.mResultItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mItemClickListener != null) {
//                        mItemClickListener.onItemClick(item);
//                    }
//                }
//            });
//        }
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, ThemeModes.Theme item) {
        LogUtil.i("--onBindCommon-->>");
    }

    public ThemeModes.Theme getItemObject(int position) {
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
        void onItemClick(ThemeModes.Theme item);
    }


}
