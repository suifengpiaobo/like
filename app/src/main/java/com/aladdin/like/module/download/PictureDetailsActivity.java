package com.aladdin.like.module.download;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.dialog.ShareDialog;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.download.adapter.PictureDetailsAdapter;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 图片详情页
 * Created by zxl on 2017/5/1 上午3:24.
 */
public class PictureDetailsActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.share)
    ImageView mShare;
    @BindView(R.id.picture)
    ImageView mPicture;
    @BindView(R.id.type_name)
    TextView mTypeName;
    @BindView(R.id.parise_num)
    TextView mPariseNum;
    @BindView(R.id.click_praise)
    ImageView mClickPraise;
    @BindView(R.id.download_recycle)
    RecyclerView mDownloadRecycle;


    PictureDetailsAdapter mAdapter;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    PrefecturePojo.Prefecture mPrefecture;

    String[] name = {"日韩美图","微信素材","另类图集","美食图集","健美图片","欧美情侣",
            "运动名将","奢侈生活","电影明星","轻松搞笑","夜生活","专辑封面"};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected void initView() {
        mPrefecture = (PrefecturePojo.Prefecture) getIntent().getSerializableExtra("PREFECTURE");

        for (int i = 0; i< 10;i++){
            mPrefecture = new PrefecturePojo.Prefecture();
            mPrefecture.typeName = name[i];
            mPrefectures.add(mPrefecture);
        }
        ImageLoaderUtils.displayRoundNative(PictureDetailsActivity.this,mPicture,R.drawable.picture_11);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new PictureDetailsAdapter(PictureDetailsActivity.this);
        mDownloadRecycle.setLayoutManager(staggered);
        mDownloadRecycle.addItemDecoration(new SpacesItemDecoration(10));
        mDownloadRecycle.setAdapter(mAdapter);
        mAdapter.addAll(mPrefectures);

        bindEvent();
    }

    private void bindEvent() {
        mAdapter.setOnItemClickListener(new PictureDetailsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(PrefecturePojo.Prefecture item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("PREFECTURE", item);
                startActivity(DownLoadPictureActivity.class, bundle);
            }
        });

    }

    @OnClick({R.id.back, R.id.share, R.id.click_praise,R.id.picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.share:
                ShareDialog shareDialog = new ShareDialog();
                shareDialog.show(getSupportFragmentManager(), "share_dialog");
                break;
            case R.id.picture:
                startActivity(DownLoadPictureActivity.class);
                break;
            case R.id.click_praise:
                break;
        }
    }
}
