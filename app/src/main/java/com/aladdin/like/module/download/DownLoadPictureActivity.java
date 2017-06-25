package com.aladdin.like.module.download;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.utils.FileUtils;
import com.aladdin.utils.ToastUtil;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;
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
//                Aria.download(this)
//                        .load(mTheme.themeImgUrl)     //读取下载地址
//                        .setDownloadPath(mFile.getAbsolutePath()+"/"+fileName)    //设置文件保存的完整路径
//                        .start();   //启动下载
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
                                     MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "title", "description");
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
        mPicture.setImageURI(mTheme.themeImgUrl);
//        mPariseNum.setText(mTheme.followSign);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Aria.download(DownLoadPictureActivity.this).addSchedulerListener(new MySchedulerListener());
    }

    final class MySchedulerListener extends Aria.DownloadSchedulerListener {
        @Override
        public void onTaskPre(DownloadTask task) {
            super.onTaskPre(task);
        }

        @Override
        public void onTaskStop(DownloadTask task) {
            super.onTaskStop(task);
        }

        @Override
        public void onTaskCancel(DownloadTask task) {
            super.onTaskCancel(task);
        }

        @Override
        public void onTaskRunning(DownloadTask task) {
            super.onTaskRunning(task);
        }

        @Override
        public void onTaskFail(DownloadTask task) {
            super.onTaskFail(task);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            ToastUtil.showToast("下载失败！");
        }

        @Override
        public void onTaskComplete(DownloadTask task) {
            super.onTaskComplete(task);
            downloadSuc();
        }
    }

    public void downloadSuc(){
        mDownloadStatus.setBackgroundResource(R.drawable.download_success_icon);
        ToastUtil.showToast("下载成功");

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mFile.getAbsolutePath()+"/"+fileName))));
    }
}
