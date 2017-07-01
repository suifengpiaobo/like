package com.aladdin.like.module.diarydetails;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.widget.FrameLayout;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.utils.DensityUtils;
import com.facebook.cache.common.SimpleCacheKey;
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

import butterknife.BindView;

public class DiaryDetailsActivity extends BaseActivity {

    @BindView(R.id.dirary_img)
    SimpleDraweeView mDiarary;

    DiaryDetail.Diary mDiary;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_diary_details;
    }

    @Override
    protected void initView() {
        mDiary = (DiaryDetail.Diary) getIntent().getSerializableExtra("DIARY");
        if (mDiary != null){
            boolean isCacheinDisk = Fresco.getImagePipelineFactory().getMainDiskStorageCache().hasKey(new SimpleCacheKey(Uri.parse(mDiary.diaryImage).toString()));

            if (isCacheinDisk){
                setImg();
            }
        }
    }

    private void setImg() {
        Uri uri = Uri.parse(mDiary.diaryImage);
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
                                     float scale = (DensityUtils.mScreenWidth)/(float)bitmap.getWidth();
                                     FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mDiarary.getLayoutParams();
                                     params.height = (int) (bitmap.getHeight()*scale);
                                     params.width = (int)(bitmap.getWidth()*scale);
                                     mDiarary.setLayoutParams(params);
                                     mDiarary.setImageBitmap(bitmap);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

}
