package com.aladdin.like.module.download;

import android.view.View;
import android.widget.ImageView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.utils.ToastUtil;
import com.aladdin.widget.NormalTitleBar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 下载页
 * Created by zxl on 2017/5/1 上午4:02.
 */
public class DownLoadActivity extends BaseActivity {

    @BindView(R.id.title)
    NormalTitleBar mTitle;

    PrefecturePojo.Prefecture mPrefecture;
    @BindView(R.id.picture)
    ImageView mPicture;
    @BindView(R.id.download_img)
    ImageView mDownloadImg;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_down_load;
    }

    @Override
    protected void initView() {
        mPrefecture = (PrefecturePojo.Prefecture) getIntent().getSerializableExtra("PREFECTURE");

//        ViewGroup.LayoutParams lp = mPicture.getLayoutParams();
//        lp.width = width - DensityUtils.dip2px(30);
//        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//        mPicture.setLayoutParams(lp);
//        mPicture.setMaxWidth(width);
//        mPicture.setMaxHeight((int) (width * 5));
//        ImageLoaderUtils.displayRoundNative(DownLoadActivity.this, mPicture, R.mipmap.ic_github);

        initTitle();
    }

    private void initTitle() {
        mTitle.setBackVisibility(true);
        mTitle.setTitleText("下载");
        mTitle.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.download_img)
    public void onViewClicked() {
        ToastUtil.sToastUtil.shortDuration("下载");
    }
}
