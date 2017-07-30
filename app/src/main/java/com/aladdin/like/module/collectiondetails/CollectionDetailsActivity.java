package com.aladdin.like.module.collectiondetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
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
import com.aladdin.like.constant.Constant;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.utils.FileUtils;
import com.aladdin.like.utils.ImageTools;
import com.aladdin.like.widget.ShareDialog;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.SharedPreferencesUtil;
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

/**
 * Description 收藏详情
 * Created by zxl on 2017/7/27 下午2:07.
 */
public class CollectionDetailsActivity extends BaseActivity {

    @BindView(R.id.picture)
    ImageView mPicture;
    @BindView(R.id.collection_picture)
    ImageView mCollectionPicture;
    @BindView(R.id.picture_info)
    RelativeLayout mPictureInfo;

    CollectionImage.Collection mCollection;

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

    @BindView(R.id.app_bar_layout)
    AppBarLayout mBarLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    PhotoViewAttacher mPhotoViewAttacher;
    protected boolean mIsHidden = false;

    String fileName;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_diary_details;
    }

    @Override
    protected void initView() {
        ViewCompat.setTransitionName(mPicture, "transition_animation_circe_photos");
        mCollection = (CollectionImage.Collection) getIntent().getSerializableExtra("COLLECTION");

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_selector);

        if (!TextUtils.isEmpty(mCollection.avatar)){
            mUserAvatar.setImageURI(mCollection.avatar);
        }else{
            mUserAvatar.setImageURI(LikeAgent.getInstance().getUserPojo().headimgurl);
        }
        if (!TextUtils.isEmpty(mCollection.nickName)){
            mUserName.setText(mCollection.nickName);
        }else{
            mUserName.setText(LikeAgent.getInstance().getUserPojo().nickname);
        }
        mTime.setText(mCollection.recordTimeStr);
        if (!"".equals(mCollection.collectTimes) && Integer.valueOf(mCollection.collectTimes)>0){
            mCollectionTimes.setText(mCollection.collectTimes+"人喜欢了此图片");
        }

        Glide.with(CollectionDetailsActivity.this).load(mCollection.resourceUrl).into(mPicture);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_share) {
            ShareDialog dialog = new ShareDialog();
            dialog.setBitmapUrl(mCollection.resourceUrl);
            dialog.show(getSupportFragmentManager(), "share_dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideOrShowToolbar() {
        mBarLayout.animate()
                .translationY(mIsHidden ? 0 : -mBarLayout.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mPictureInfo.animate().translationY(mIsHidden?0:DensityUtils.dip2px(100)).setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

    @OnClick({R.id.download_picture,R.id.collection_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.download_picture:
                fileName = UUID.randomUUID().toString().substring(0, 16) + ".jpeg";
                savePicture();
                break;
            case R.id.collection_picture:
                collection();
                break;
        }
    }

    public void savePicture() {
        Uri uri = Uri.parse(mCollection.resourceUrl);
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
                                     saveMyBitmap(bitmap);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

    public void saveMyBitmap(Bitmap mBitmap) {
        Bitmap water = BitmapFactory.decodeResource(getResources(), R.drawable.logo_watermark);
        File f = new File(FileUtils.getPhotoDirectory(),fileName);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int shareCount = SharedPreferencesUtil.INSTANCE.getInt(Constant.SHARE_TIMES, 0);
        if (shareCount < 20) {
            Bitmap bitmap = ImageTools.createWaterMaskRightBottom(CollectionDetailsActivity.this, mBitmap, water, 16, 16);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
        } else {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
        }

        MobclickAgent.onEvent(CollectionDetailsActivity.this,"DownLoad");
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            MediaStore.Images.Media.insertImage(getContentResolver(),
                    f.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                // 保存后无法在相册查看到，发送更新图库的广播
                Uri uri = Uri.fromFile(f);
                Intent intent = new Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,uri);
                sendBroadcast(intent);
                Toast.makeText(CollectionDetailsActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void collection() {
        HttpManager.INSTANCE.collectionImage(LikeAgent.getInstance().getOpenid(), mCollection.resourceId, 2,new HttpResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("取消收藏成功");
                        MobclickAgent.onEvent(CollectionDetailsActivity.this,"Collection");
                    }
                });
            }

            @Override
            public void onFailure(String code, String msg) {
                ToastUtil.showToast("取消收藏失败");
            }
        });
    }

}