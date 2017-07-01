package com.aladdin.like.module.download;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
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
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

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

    public static Intent getPhotoDetailIntent(Context context, ThemeModes.Theme theme){
        Intent intent = new Intent(context,PictureDetailsActivity.class);
        intent.putExtra("PREFECTURE",theme);
        return intent;
    }

    @Override
    protected void initView() {
        mPrestener = new PictureDetailsPrestener(this);
        mTheme = (ThemeModes.Theme) getIntent().getSerializableExtra("PREFECTURE");
        themeId = mTheme.themeId;

        boolean isCacheinDisk = Fresco.getImagePipelineFactory().getMainDiskStorageCache().hasKey(new SimpleCacheKey(Uri.parse(mTheme.themeImgUrl).toString()));

        if (isCacheinDisk){
            setImg();
        }
        if (mAdapter != null && mAdapter.getItemCount() > 0){
            mAdapter.clear();
        }

        mPrestener.getData(LikeAgent.getInstance().getUid(),themeId,page,page_num);

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

    private void setImg() {
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
                                     float scale = (DensityUtils.mScreenWidth-DensityUtils.dip2px(20))/(float)bitmap.getWidth();
                                     LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPicture.getLayoutParams();
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
