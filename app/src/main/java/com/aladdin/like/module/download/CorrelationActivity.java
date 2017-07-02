package com.aladdin.like.module.download;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.utils.FileUtils;
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
import com.umeng.analytics.MobclickAgent;

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
    @BindView(R.id.picture)
    SimpleDraweeView mPicture;
    @BindView(R.id.download_status)
    ImageView mDownloadStatus;

    ThemeDetail.Theme mTheme;

    File mFile;
    String fileName;

    public static final String TRANSIT_PIC = "picture";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_down_load_picture;
    }


    @Override
    protected void initView() {
//        ViewCompat.setTransitionName(mPicture, Constant.TRANSITION_ANIMATION_NEWS_PHOTOS);

        mTheme = (ThemeDetail.Theme) getIntent().getSerializableExtra("CORRELATION");
        mFile = new File(FileUtils.getImageRootPath());
        boolean isCacheinDisk = Fresco.getImagePipelineFactory().getMainDiskStorageCache().hasKey(new SimpleCacheKey(Uri.parse(mTheme.imageUrl).toString()));

        if (isCacheinDisk){
            setImg();
        }
    }

    void setImg(){
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
                                     float scale = (DensityUtils.mScreenWidth)/(float)bitmap.getWidth();
                                     FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mPicture.getLayoutParams();
                                     params.height = (int) (bitmap.getHeight()*scale);
                                     params.width = (int)(bitmap.getWidth()*scale);
                                     mPicture.setLayoutParams(params);
                                     mPicture.setImageBitmap(bitmap);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

    public static Intent getPhotoDetailIntent(Context context, ThemeDetail.Theme theme){
        Intent intent = new Intent(context,CorrelationActivity.class);
        intent.putExtra("CORRELATION",theme);
        return intent;
    }


    @OnClick({R.id.back, R.id.download_status})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                super.onBackPressed();
                break;
            case R.id.download_status:
//                HttpManager.INSTANCE.collectionImage(LikeAgent.getInstance().getUid(), mTheme.imageId, new HttpResultCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                    }
//                    @Override
//                    public void onFailure(String code, String msg) {
//                    }
//                });
                MobclickAgent.onEvent(CorrelationActivity.this, "DownLoad");
                fileName = UUID.randomUUID().toString().substring(0, 16) + ".jpeg";
                savePicture();
//                Aria.download(this)
//                        .load(mTheme.themeImgUrl)     //读取下载地址
//                        .setDownloadPath(mFile.getAbsolutePath()+"/"+fileName)    //设置文件保存的完整路径
//                        .start();   //启动下载
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
                                     mDownloadStatus.setBackgroundResource(R.drawable.download_success_icon);
                                     Toast.makeText(CorrelationActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                                     saveMyBitmap(bitmap,System.currentTimeMillis()+"");
//                                     MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
        mPicture.setImageURI(mTheme.imageUrl);
    }

    public void saveMyBitmap(Bitmap mBitmap,String bitName)  {
        File f = new File( FileUtils.getImageRootPath() + bitName+".jpeg");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f.getAbsoluteFile());
        intent.setData(uri);
        sendBroadcast(intent);

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
}
