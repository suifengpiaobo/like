package com.aladdin.like.module.download;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.download.adapter.PictureDetailsAdapter;
import com.aladdin.like.module.download.contract.PictureDetailsContract;
import com.aladdin.like.module.download.presenter.PictureDetailsPrestener;
import com.aladdin.like.widget.ShareDialog;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
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
        LogUtil.i("--mTheme-->>>"+mTheme);

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
        mDownloadRecycle.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f),DensityUtils.dip2px(7.5f)));
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
//        ImageLoaderUtils.displayRound(PictureDetailsActivity.this,mPicture,mTheme.themeImgUrl);
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
            public void onItemClick(View v, SimpleDraweeView mPrefectureBg, LinearLayout mLayout, ThemeDetail.Theme item) {
            }

            @Override
            public void onItemClick(View v, SimpleDraweeView mPrefectureBg, ThemeDetail.Theme item) {
                startCorrelationActivity(mPrefectureBg,item);
            }

            @Override
            public void onItemLongClick(ThemeDetail.Theme item, int width, int height) {

            }
        });
    }

    //新添加
    public void startCorrelationActivity(SimpleDraweeView mPrefectureBg, ThemeDetail.Theme item){
        Intent intent = CorrelationActivity.getPhotoDetailIntent(PictureDetailsActivity.this,item);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    PictureDetailsActivity.this,
                    mPrefectureBg,
                    Constant.TRANSITION_ANIMATION_NEWS_PHOTOS);
            startActivity(intent,options.toBundle());
        }else{
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    mPrefectureBg,
                    mPrefectureBg.getWidth() / 2,
                    mPrefectureBg.getHeight() / 2,
                    0,
                    0);
            ActivityCompat.startActivity(PictureDetailsActivity.this,intent,optionsCompat.toBundle());
        }
    }

    private void startPictureActivity(ThemeDetail.Theme item, View transitView) {
        Intent intent = new Intent(PictureDetailsActivity.this,CorrelationActivity.class);
        intent.putExtra("CORRELATION",item);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                PictureDetailsActivity.this, transitView, CorrelationActivity.TRANSIT_PIC);
        try {
            ActivityCompat.startActivity(PictureDetailsActivity.this, intent, optionsCompat.toBundle());
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            startActivity(intent);
        }
    }

    private void scaleUpAnimation(View view,ThemeDetail.Theme item) {
        //让新的Activity从一个小的范围扩大到全屏
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view, //The View that the new activity is animating from
                        (int)view.getWidth()/2, (int)view.getHeight()/2, //拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        startNewAcitivity(options,item);
    }

    private void startNewAcitivity(ActivityOptionsCompat options,ThemeDetail.Theme item) {
        Intent intent = new Intent(this,CorrelationActivity.class);
        intent.putExtra("CORRELATION",item);
        ActivityCompat.startActivity(this, intent, options.toBundle());
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
