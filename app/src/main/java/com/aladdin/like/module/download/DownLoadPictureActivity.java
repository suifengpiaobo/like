package com.aladdin.like.module.download;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.utils.FileUtils;
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

/**
 * Description 点击图片下载页
 * Created by zxl on 2017/5/21 下午11:21.
 */
public class DownLoadPictureActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.picture)
    SimpleDraweeView mPicture;
    @BindView(R.id.download_status)
    ImageView mDownloadStatus;

    ThemeModes.Theme mTheme;

    File mFile;
    String fileName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_down_load_picture;
    }

    @Override
    protected void initView() {
        mTheme = (ThemeModes.Theme) getIntent().getSerializableExtra("THEME");
        mPicture.setImageURI(mTheme.themeImgUrl);
        mFile = new File(FileUtils.getImageRootPath());
    }

    @OnClick({R.id.back, R.id.download_status})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.download_status:
                MobclickAgent.onEvent(DownLoadPictureActivity.this,"DownLoad");
                fileName = UUID.randomUUID().toString().substring(0,16)+".png";
                savePicture();
                break;
        }
    }

    public void savePicture(){
        Uri uri = Uri.parse(mTheme.themeImgUrl);
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
                                     Toast.makeText(DownLoadPictureActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                                     saveMyBitmap(bitmap,System.currentTimeMillis()+"");
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
        mPicture.setImageURI(mTheme.themeImgUrl);
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
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f.getAbsoluteFile())));
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
