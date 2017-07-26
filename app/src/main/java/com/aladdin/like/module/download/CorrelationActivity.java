package com.aladdin.like.module.download;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
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
import com.aladdin.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.umeng.analytics.MobclickAgent;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;

/*
 *Description 相关图片下载页
 *Created by zxl on 2017/6/19下午4:43.
*/
public class CorrelationActivity extends BaseActivity {
    @BindView(R.id.root_view)
    RelativeLayout mLayout;
    @BindView(R.id.picture)
    ImageView mPicture;
    @BindView(R.id.download_status)
    ImageView mDownloadStatus;

    @BindView(R.id.rl_bottom)
    RelativeLayout mBottom;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    PhotoViewAttacher mPhotoViewAttacher;
    ThemeDetail.Theme mTheme;

    File mFile;
    String fileName;

    protected boolean mIsHidden = false;

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
        ViewCompat.setTransitionName(mPicture, "transition_animation_albym");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_selector);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        mTheme = (ThemeDetail.Theme) getIntent().getSerializableExtra("CORRELATION");
        mFile = new File(FileUtils.getImageRootPath());
        mCollectionTimes.setText(mTheme.collectionTimes+"人喜欢了此图片");

        Glide.with(CorrelationActivity.this).load(mTheme.imageUrl).into(mPicture);
        mBarLayout.setAlpha(0.7f);
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

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    protected void hideOrShowToolbar() {
        mBarLayout.animate()
                .translationY(mIsHidden ? 0 : -mBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mBottom.animate().translationY(mIsHidden?0:DensityUtils.dip2px(48)).setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
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


    @OnClick({ R.id.download_status,R.id.collection_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
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
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
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
        mPhotoViewAttacher.cleanup();
//        mPicture.setController(null);
    }
}
