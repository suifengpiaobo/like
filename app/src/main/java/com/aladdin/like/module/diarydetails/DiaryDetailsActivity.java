package com.aladdin.like.module.diarydetails;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
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
import uk.co.senab.photoview.PhotoViewAttacher;

public class DiaryDetailsActivity extends BaseActivity {

    @BindView(R.id.picture)
    ImageView mPicture;
    @BindView(R.id.collection_picture)
    ImageView mCollectionPicture;
    @BindView(R.id.picture_info)
    RelativeLayout mPictureInfo;

    DiaryDetail.Diary mDiary;

    ShareDialog shareDialog;

    @BindView(R.id.user_avatar)
    SimpleDraweeView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.user_info)
    RelativeLayout mUserInfo;
    @BindView(R.id.collection_times)
    TextView mCollectionTimes;
    @BindView(R.id.download_picture)
    ImageView mDownloadPicture;

    PhotoViewAttacher mPhotoViewAttacher;
    protected boolean mIsHidden = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_diary_details;
    }

    @Override
    protected void initView() {
        ViewCompat.setTransitionName(mPicture, "transition_animation_circe_photos");
        mDiary = (DiaryDetail.Diary) getIntent().getSerializableExtra("DIARY");

        mUserAvatar.setImageURI(LikeAgent.getInstance().getUserPojo().headimgurl);
        mUserName.setText(LikeAgent.getInstance().getUserPojo().nickname);
        mTime.setText(mDiary.diaryTimeStr);


        Glide.with(DiaryDetailsActivity.this).load(mDiary.diaryImage).into(mPicture);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mPhotoViewAttacher = new PhotoViewAttacher(mPicture);
                mPhotoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                    @Override
                    public void onViewTap(View view, float x, float y) {
                        hideOrShowToolbar();
                    }
                });
            }
        },300);
    }

    protected void hideOrShowToolbar() {
        mPictureInfo.animate().translationY(mIsHidden?0:DensityUtils.dip2px(100)).setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
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
//                                     float scale = (DensityUtils.mScreenWidth) / (float) bitmap.getWidth();
//                                     LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mDiarary.getLayoutParams();
//                                     params.height = (int) (bitmap.getHeight() * scale);
//                                     params.width = (int) (bitmap.getWidth() * scale);
//                                     mDiarary.setLayoutParams(params);
//                                     mDiarary.setImageBitmap(bitmap);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

//    @OnClick({R.id.back, R.id.share_img, R.id.picture_info})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.back:
////                finish();
//                super.onBackPressed();
//                break;
//            case R.id.share_img:
//                showShareDialog();
//                break;
//            case R.id.picture_info:
//                collection();
//                break;
//        }
//    }

    public void showShareDialog() {
        shareDialog = new ShareDialog();
        shareDialog.setBitmapUrl(mDiary.diaryImage);
        shareDialog.show(getSupportFragmentManager(), "share_dialog");
    }

    public void collection() {
        HttpManager.INSTANCE.collectionImage(LikeAgent.getInstance().getOpenid(), mDiary.diaryId, new HttpResultCallback<String>() {
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
