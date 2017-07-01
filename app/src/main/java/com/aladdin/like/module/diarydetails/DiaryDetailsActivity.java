package com.aladdin.like.module.diarydetails;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.widget.ShareDialog;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.ToastUtil;
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
import com.zxl.network_lib.Inteface.HttpResultCallback;

import butterknife.BindView;
import butterknife.OnClick;

public class DiaryDetailsActivity extends BaseActivity {

    @BindView(R.id.dirary_img)
    SimpleDraweeView mDiarary;
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.share_img)
    ImageView mShareImg;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.collection_times)
    TextView mCollectionTimes;
    @BindView(R.id.collection_picture)
    ImageView mCollectionPicture;
    @BindView(R.id.picture_info)
    RelativeLayout mPictureInfo;

    DiaryDetail.Diary mDiary;

    ShareDialog shareDialog;

    boolean hidden = false;

    double mAnimStart;
    double mLastTime,mCurrent;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_diary_details;
    }

    @Override
    protected void initView() {
        mDiary = (DiaryDetail.Diary) getIntent().getSerializableExtra("DIARY");
        if (mDiary != null) {
            boolean isCacheinDisk = Fresco.getImagePipelineFactory().getMainDiskStorageCache().hasKey(new SimpleCacheKey(Uri.parse(mDiary.diaryImage).toString()));

            if (isCacheinDisk) {
                setImg();
            }
        }

        mDiarary.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        mLastTime = mCurrent;
                        mCurrent = System.currentTimeMillis();
                        if (mCurrent - mLastTime < 500 && mCurrent-mAnimStart > 500) {
                            hide();
                            mAnimStart = System.currentTimeMillis();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });
    }

    public void hide(){
        if (!hidden){
            startAnimation(true,1.0f,0.0f);
        }else{
            startAnimation(false,0.0f,1.0f);
        }
        hidden = !hidden;
    }

    private void startAnimation(final boolean endState, float startValue, float endValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(startValue,endValue).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y1,y2;
                if(endState){
                    y1 = (0 - animation.getAnimatedFraction())*DensityUtils.dip2px(48);
                    y2 = (animation.getAnimatedFraction())*DensityUtils.dip2px(48);
                }else{
                    y1 = (animation.getAnimatedFraction() - 1)*DensityUtils.dip2px(48);
                    y2 = (1-animation.getAnimatedFraction())*DensityUtils.dip2px(48);
                }
                mTitle.setTranslationY(y1);
                mPictureInfo.setTranslationY(y2);
            }
        });
        animator.start();
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
                                     float scale = (DensityUtils.mScreenWidth) / (float) bitmap.getWidth();
                                     LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDiarary.getLayoutParams();
                                     params.height = (int) (bitmap.getHeight() * scale);
                                     params.width = (int) (bitmap.getWidth() * scale);
                                     mDiarary.setLayoutParams(params);
                                     mDiarary.setImageBitmap(bitmap);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

    @OnClick({R.id.back, R.id.share_img, R.id.picture_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
//                finish();
                super.onBackPressed();
                break;
            case R.id.share_img:
                showShareDialog();
                break;
            case R.id.picture_info:
                collection();
                break;
        }
    }

    public void showShareDialog(){
        shareDialog = new ShareDialog();
        shareDialog.show(getSupportFragmentManager(),"share_dialog");
    }

    public void collection(){
        HttpManager.INSTANCE.collectionImage(LikeAgent.getInstance().getUid(), mDiary.diaryId, new HttpResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("收藏成功");
                    }
                });
            }

            @Override
            public void onFailure(String code, String msg) {
                ToastUtil.showToast("收藏失败");
            }
        });
    }
}
