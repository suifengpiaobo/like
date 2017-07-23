package com.aladdin.like.module.download;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.utils.FileUtils;
import com.aladdin.like.utils.ImageTools;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
import com.aladdin.utils.ToastUtil;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sunfusheng.glideimageview.GlideImageView;
import com.umeng.analytics.MobclickAgent;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/*
 *Description 相关图片下载页
 *Created by zxl on 2017/6/19下午4:43.
*/
public class CorrelationActivity extends BaseActivity {
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.root_view)
    FrameLayout mLayout;
    @BindView(R.id.picture)
    GlideImageView mPicture;
    @BindView(R.id.download_status)
    ImageView mDownloadStatus;
//    @BindView(R.id.watermark_pic_rl)
//    RelativeLayout mBgRl;
    @BindView(R.id.title)
    RelativeLayout mTitle;
    @BindView(R.id.rl_bottom)
    RelativeLayout mBottom;

    ThemeDetail.Theme mTheme;

    File mFile;
    String fileName;

    public static final String TRANSIT_PIC = "picture";

    boolean hidden = false;

    double mAnimStart;
    double mLastTime, mCurrent;
    @BindView(R.id.collection_times)
    TextView mCollectionTimes;
    @BindView(R.id.collection_picture)
    ImageView mCollectionPicture;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_down_load_picture;
    }


    @Override
    protected void initView() {
        mTheme = (ThemeDetail.Theme) getIntent().getSerializableExtra("CORRELATION");
        mFile = new File(FileUtils.getImageRootPath());
        boolean isCacheinDisk = Fresco.getImagePipelineFactory().getMainDiskStorageCache().hasKey(new SimpleCacheKey(Uri.parse(mTheme.imageUrl).toString()));
        mCollectionTimes.setText(mTheme.collectionTimes+"人喜欢了此图片");
        if (isCacheinDisk) {
//            setImg();
        }

        float scale = DensityUtils.mScreenWidth/(float)mTheme.width;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mPicture.getLayoutParams();
        params.height = (int)(mTheme.height*scale);
        params.width = (int)(mTheme.width*scale);
        LogUtil.i("width-->>"+params.width+"  --height-->>"+params.height);
        mPicture.setLayoutParams(params);


        mPicture.loadImage(mTheme.imageUrl,R.color.placeholder_color);

        mPicture.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastTime = mCurrent;
                        mCurrent = System.currentTimeMillis();
                        if (mCurrent - mLastTime < 500 && mCurrent - mAnimStart > 500) {
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

    public void hide() {
        if (!hidden) {
            startAnimation(true, 1.0f, 0.0f);
        } else {
            startAnimation(false, 0.0f, 1.0f);
        }
        hidden = !hidden;
    }

    private void startAnimation(final boolean endState, float startValue, float endValue) {
        ValueAnimator animator = ValueAnimator.ofFloat(startValue, endValue).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float y1, y2;
                if (endState) {
                    y1 = (0 - animation.getAnimatedFraction()) * DensityUtils.dip2px(48);
                    y2 = (animation.getAnimatedFraction()) * DensityUtils.dip2px(48);
                } else {
                    y1 = (animation.getAnimatedFraction() - 1) * DensityUtils.dip2px(48);
                    y2 = (1 - animation.getAnimatedFraction()) * DensityUtils.dip2px(48);
                }
                mTitle.setTranslationY(y1);
                mBottom.setTranslationY(y2);
            }
        });
        animator.start();
    }

    void setImg() {
        Uri uri = Uri.parse(mTheme.imageUrl);
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
                                     runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             float scale = (DensityUtils.mScreenWidth) / (float) bitmap.getWidth();
                                             RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mPicture.getLayoutParams();
                                             params.height = (int) (bitmap.getHeight() * scale);
                                             params.width = (int) (bitmap.getWidth() * scale);
                                             mPicture.setLayoutParams(params);
                                             mPicture.setImageBitmap(bitmap);
                                         }
                                     });
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

    public static Intent getPhotoDetailIntent(Context context, ThemeDetail.Theme theme) {
        Intent intent = new Intent(context, CorrelationActivity.class);
        intent.putExtra("CORRELATION", theme);
        return intent;
    }


    @OnClick({R.id.back, R.id.download_status,R.id.collection_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                super.onBackPressed();
                break;
            case R.id.download_status:
                MobclickAgent.onEvent(CorrelationActivity.this, "DownLoad");
                fileName = UUID.randomUUID().toString().substring(0, 16) + ".jpeg";
                savePicture();

                break;
            case R.id.collection_picture:
                HttpManager.INSTANCE.collectionImage(LikeAgent.getInstance().getOpenid(), mTheme.imageId, new HttpResultCallback<String>() {
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

                    }
                });
                break;
        }
    }

    public void savePicture() {
        Uri uri = Uri.parse(mTheme.imageUrl);
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
                                     saveMyBitmap(bitmap, System.currentTimeMillis() + "");
//                                     MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
//        mPicture.setImageURI(mTheme.imageUrl);
    }

    public void saveMyBitmap(Bitmap mBitmap, String bitName) {
        Bitmap water = BitmapFactory.decodeResource(getResources(),R.drawable.logo_watermark);
        Bitmap bitmap=ImageTools.createWaterMaskRightBottom(CorrelationActivity.this,mBitmap,water,16,16);

        File f = new File(FileUtils.getImageRootPath() + bitName + ".jpeg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f.getAbsoluteFile());
        intent.setData(uri);
        sendBroadcast(intent);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadStatus.setBackgroundResource(R.drawable.download_success_icon);
                Toast.makeText(CorrelationActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }
        });

//        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f.getAbsoluteFile())));
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mPicture.setController(null);
    }
}
