package com.aladdin.like.module.download;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.download.adapter.PictureDetailsAdapter;
import com.aladdin.like.module.download.contract.PictureDetailsContract;
import com.aladdin.like.module.download.presenter.PictureDetailsPrestener;
import com.aladdin.like.widget.ShareDialog;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 图片详情页
 * Created by zxl on 2017/5/1 上午3:24.
 */
public class PictureDetailsActivity extends BaseActivity implements PictureDetailsContract.View,XRecyclerView.LoadingListener{
    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.share)
    ImageView mShare;
    @BindView(R.id.picture)
    SimpleDraweeView mPicture;
    @BindView(R.id.type_name)
    TextView mTypeName;
    @BindView(R.id.parise_num)
    TextView mPariseNum;
    @BindView(R.id.click_praise)
    ImageView mClickPraise;
    @BindView(R.id.download_recycle)
    XRecyclerView mDownloadRecycle;


    PictureDetailsAdapter mAdapter;

    ThemeModes.Theme mTheme;

    String themeId;
    int page = 1;
    int page_num = 10;

    PictureDetailsContract.Prestener mPrestener;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    @Override
    protected void initView() {
        mPrestener = new PictureDetailsPrestener(this);
        mTheme = (ThemeModes.Theme) getIntent().getSerializableExtra("PREFECTURE");
        themeId = mTheme.themeId;

        if (mAdapter != null && mAdapter.getItemCount() > 0){
            mAdapter.clear();
        }

        mPrestener.getData(LikeAgent.getInstance().getUid(),themeId,page,page_num);

        setPicture();

        mDownloadRecycle.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mDownloadRecycle.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mDownloadRecycle.setArrowImageView(R.drawable.icon_refresh);
        mDownloadRecycle.setLoadingListener(this);
        mDownloadRecycle.setRefreshing(false);
        mDownloadRecycle.setPullRefreshEnabled(false);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new PictureDetailsAdapter(PictureDetailsActivity.this);
        mDownloadRecycle.setLayoutManager(staggered);
        mDownloadRecycle.addItemDecoration(new SpacesItemDecoration(10));
        mDownloadRecycle.setAdapter(mAdapter);

        bindEvent();
    }

    public void setPicture(){
        Uri uri = Uri.parse(mTheme.themeImgUrl);
        Bitmap bitmap = returnBitmap(uri);

        int width = bitmap.getWidth();//994
        float scale = (DensityUtils.mScreenWidth-DensityUtils.dip2px(20))/(float)width;
        int height = bitmap.getHeight();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPicture.getLayoutParams();
        params.height = (int) (height*scale);
        params.weight = (int)(width*scale);
        mPicture.setLayoutParams(params);
        mPicture.setImageURI(mTheme.themeImgUrl);
        mTypeName.setText(mTheme.themeName);
//        mPariseNum.setText(mTheme.followSign);
    }

    private Bitmap returnBitmap(Uri uri) {
        Bitmap bitmap = null;
        FileBinaryResource resource = (FileBinaryResource) Fresco.getImagePipelineFactory().getMainDiskStorageCache().getResource(new SimpleCacheKey(uri.toString()));
        if (resource != null){
            File file = resource.getFile();
            if (file != null && !TextUtils.isEmpty(file.getPath())) {
                bitmap = BitmapFactory.decodeFile(file.getPath());
            }
        }
        return bitmap;
    }

    private void bindEvent() {
        mAdapter.setOnItemClickListener(new PictureDetailsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ThemeDetail.Theme item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("CORRELATION", item);
                startActivity(CorrelationActivity.class, bundle);
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
                Bundle bundle = new Bundle();
                bundle.putSerializable("THEME", mTheme);
                startActivity(DownLoadPictureActivity.class,bundle);
                break;
            case R.id.click_praise:
//                mPrestener.collectionImage(LikeAgent.getInstance().getUid(),mTheme.themeId);
                break;
        }
    }

    @Override
    public void showLoading() {
        startProgressDialog();
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDownloadRecycle.refreshComplete();
                mDownloadRecycle.loadMoreComplete();
                showToast(msg);
            }
        });
    }

    @Override
    public void setData(ThemeDetail themeDetail) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                page = themeDetail.per_page;
                mDownloadRecycle.refreshComplete();
                mAdapter.addAll(themeDetail.imageList);
            }
        });
    }

    @Override
    public void collectionResult(String result) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        mPrestener.getData(LikeAgent.getInstance().getUid(),themeId,page,page_num);
    }
}
