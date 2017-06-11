package com.aladdin.like.module.download;

import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.utils.ImageLoaderUtils;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;

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
    ImageView mPicture;
    @BindView(R.id.download_status)
    ImageView mDownloadStatus;

    ThemeDetail.Theme mTheme;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_down_load_picture;
    }

    @Override
    protected void initView() {
        ImageLoaderUtils.displayRoundNative(DownLoadPictureActivity.this, mPicture, R.drawable.download_picture);

        mTheme = (ThemeDetail.Theme) getIntent().getSerializableExtra("PREFECTURE");
    }

    @OnClick({R.id.back, R.id.download_status})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.download_status:
                Aria.download(this)
                        .load(mTheme.imgeUrl)     //读取下载地址
                        .setDownloadPath(Environment.getExternalStorageDirectory().getPath() + "/test.apk")    //设置文件保存的完整路径
                        .start();   //启动下载
                break;
        }
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
            if (task.getDownloadEntity().isDownloadComplete()){
                downloadSuc();
            }
        }
    }

    public void downloadSuc(){
        mDownloadStatus.setBackgroundResource(R.drawable.download_success_icon);
        Toast.makeText(DownLoadPictureActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
    }
}
