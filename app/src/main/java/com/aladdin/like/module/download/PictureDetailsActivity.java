package com.aladdin.like.module.download;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.dialog.ShareDialog;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.download.adapter.PictureDetailsAdapter;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.utils.ToastUtil;
import com.aladdin.widget.NormalTitleBar;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 图片详情页
 * Created by zxl on 2017/5/1 上午3:24.
 */
public class PictureDetailsActivity extends BaseActivity implements XRecyclerView.LoadingListener {

    PrefecturePojo.Prefecture mPrefecture;
    @BindView(R.id.title)
    NormalTitleBar mTitle;
    ImageView mPicture;
    TextView mTypeName;
    ImageView mDownload;
    TextView mPraiseNum;

    @BindView(R.id.download_recycle)
    XRecyclerView mDownloadRecycle;

    PictureDetailsAdapter mAdapter;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();

    private int cursor = 0;
    private int width;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected void initView() {
        mPrefecture = (PrefecturePojo.Prefecture) getIntent().getSerializableExtra("PREFECTURE");
        width = DensityUtils.mScreenWidth;
        initTitle();

        mDownloadRecycle.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        mDownloadRecycle.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        mDownloadRecycle.setArrowImageView(R.mipmap.icon_refresh);

        View header = LayoutInflater.from(this).inflate(R.layout.layout_picture_details_header, (ViewGroup) findViewById(android.R.id.content), false);
        mPicture = (ImageView) header.findViewById(R.id.download_picture);
        mTypeName = (TextView) header.findViewById(R.id.type_name);
        mDownload = (ImageView) header.findViewById(R.id.download);
        mPraiseNum = (TextView) header.findViewById(R.id.praise_num);
        mDownloadRecycle.addHeaderView(header);

        ViewGroup.LayoutParams lp = mPicture.getLayoutParams();
        lp.width = width - DensityUtils.dip2px(30);
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        mPicture.setLayoutParams(lp);

        mPicture.setMaxWidth(width);
        mPicture.setMaxHeight((int) (width * 5));
        ImageLoaderUtils.displayRoundNative(PictureDetailsActivity.this, mPicture, R.mipmap.ic_github);

        mAdapter = new PictureDetailsAdapter(PictureDetailsActivity.this);
        mDownloadRecycle.setAdapter(mAdapter);


        mDownloadRecycle.setLoadingListener(this);

        mDownloadRecycle.setRefreshing(true);

        bindEvent();
    }

    private void bindEvent() {
        mAdapter.setOnItemClickListener(new PictureDetailsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(PrefecturePojo.Prefecture item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("PREFECTURE", item);
                startActivity(DownLoadActivity.class, bundle);
            }
        });

        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.sToastUtil.shortDuration("下载");
            }
        });
    }

    private void initTitle() {
        mTitle.setBackVisibility(true);
        mTitle.setRightTitleVisibility(true);
        mTitle.setRightTitle(R.mipmap.share);

        mTitle.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitle.setOnRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog();
                shareDialog.show(getSupportFragmentManager(), "share_dialog");
            }
        });
    }

    @Override
    public void onRefresh() {
        cursor = 0;
        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            mAdapter.clear();
        }
        for (int i = 0; i < 10; i++) {
            PrefecturePojo.Prefecture prefecture = new PrefecturePojo.Prefecture();
            prefecture.time = 10192;
            prefecture.typeName = "健身美图" + i;

            mPrefectures.add(prefecture);
        }

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mDownloadRecycle.setLayoutManager(staggered);
        mAdapter.addAll(mPrefectures);
        mAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDownloadRecycle.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        for (int i = 0; i < 10; i++) {
            PrefecturePojo.Prefecture prefecture = new PrefecturePojo.Prefecture();
            prefecture.time = 10192;
            prefecture.typeName = "健身美图" + i;

            mPrefectures.add(prefecture);
        }

        mAdapter.addAll(mPrefectures);
        mAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDownloadRecycle.loadMoreComplete();
            }
        }, 1000);
    }

}
