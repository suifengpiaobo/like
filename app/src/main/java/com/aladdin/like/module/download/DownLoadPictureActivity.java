package com.aladdin.like.module.download;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.utils.ImageLoaderUtils;

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_down_load_picture;
    }

    @Override
    protected void initView() {
        ImageLoaderUtils.displayRoundNative(DownLoadPictureActivity.this,mPicture,R.drawable.download_picture);
    }

    @OnClick({R.id.back, R.id.download_status})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.download_status:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadStatus.setBackgroundResource(R.drawable.download_success_icon);
                        Toast.makeText(DownLoadPictureActivity.this,"下载成功",Toast.LENGTH_SHORT).show();
                    }
                },1500);
                break;
        }
    }
}
