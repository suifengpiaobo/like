package com.aladdin.like.module.albym;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aladdin.arcmenu.ArcMenu;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.module.albym.adapter.AlbymDetailsAdapter;
import com.aladdin.like.module.albym.contract.AlbymDetailsContract;
import com.aladdin.like.module.albym.presenter.AlbymDetailsPrestener;
import com.aladdin.like.module.download.CorrelationActivity;
import com.aladdin.like.utils.FileUtils;
import com.aladdin.like.widget.ShareDialog;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.ToastUtil;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sunfusheng.glideimageview.GlideImageView;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;

/**
 * Description 专辑详情
 * Created by zxl on 2017/7/17 下午6:53.
 */
public class AlbymDetailsActivity extends BaseActivity implements AlbymDetailsContract.View, XRecyclerView.LoadingListener {
    AlbymDetailsContract.Prestener mPrestener;
    @BindView(R.id.albym_details_recycle)
    XRecyclerView mAlbymRecycle;

    @BindView(R.id.albym_item)
    FrameLayout mRootView;

    AlbymDetailsAdapter mDetailsAdapter;

    private int page = 1;
    private int page_num = 10;

    AlbymModel.AlbymDetail mAlbymDetail;

    private static final int[] ITEM_DRAWABLES = {R.drawable.compress_share, R.drawable.compress_collection, R.drawable.compress_download};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_albym_details;
    }

    @Override
    protected void initView() {
        mPrestener = new AlbymDetailsPrestener(this);
        mAlbymDetail = (AlbymModel.AlbymDetail) getIntent().getSerializableExtra("ALBYM");

        mPrestener.getAlbymDetails(LikeAgent.getInstance().getOpenid(), mAlbymDetail.albymId + "", page, page_num);

        mAlbymRecycle.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mAlbymRecycle.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mAlbymRecycle.setArrowImageView(R.drawable.icon_refresh);
        mAlbymRecycle.setLoadingListener(this);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mDetailsAdapter = new AlbymDetailsAdapter(AlbymDetailsActivity.this);
        mAlbymRecycle.setLayoutManager(staggered);
        mAlbymRecycle.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f), DensityUtils.dip2px(7.5f)));
        mAlbymRecycle.setAdapter(mDetailsAdapter);

        mDetailsAdapter.setItemClickListener(new AlbymDetailsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, GlideImageView mMainImg, ThemeDetail.Theme item) {
                startCorrelationActivity(mMainImg, item);
            }

            @Override
            public void onLongClickListener(View view, int position, ThemeDetail.Theme item) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                mDetailsAdapter.setPressedPosition(position);

                ArcMenu mArcMenu = new ArcMenu(AlbymDetailsActivity.this);

                mArcMenu.setChildSize(DensityUtils.dip2px(AlbymDetailsActivity.this, 30));
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (x > DensityUtils.mScreenWidth / 2) {
                    mArcMenu.setDrgess(90.0f, 180.0f);
                    params.setMargins(DensityUtils.mScreenWidth / 2 - DensityUtils.dip2px(30), y + view.getHeight() - DensityUtils.dip2px(150), 0, 0);
                } else {
                    mArcMenu.setDrgess(0.0f, 90.0f);
                    params.setMargins(x + view.getWidth() - DensityUtils.dip2px(100), y + view.getHeight() - DensityUtils.dip2px(150), 0, 0);
                }
                mArcMenu.setLayoutParams(params);
                mRootView.addView(mArcMenu);

                initArcMenu(mArcMenu, ITEM_DRAWABLES, item);
            }
        });
    }

    private void initArcMenu(ArcMenu menu, int[] itemDrawables, ThemeDetail.Theme themeDetail) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(AlbymDetailsActivity.this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mRootView.removeView(menu);
                    mDetailsAdapter.setPressedPosition(-1);
                    if (position == 0) {
                        ShareDialog shareDialog = new ShareDialog();
                        shareDialog.setBitmapUrl(themeDetail.imageUrl);
                        shareDialog.show(getSupportFragmentManager(), "share");
                    } else if (position == 1) {
                        collectionPic(themeDetail);
                    } else if (position == 2) {
                        savePic(themeDetail.imageUrl);
                    }
                }
            });
        }
    }

    public void collectionPic(ThemeDetail.Theme themeDetail) {
        HttpManager.INSTANCE.collectionImage(LikeAgent.getInstance().getOpenid(), themeDetail.imageId, new HttpResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast("收藏成功");
                    }
                });
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        });
    }

    private void savePic(String image) {
        Uri uri = Uri.parse(image);
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
                                     ToastUtil.showToast("下载成功");
                                     saveMyBitmap(bitmap, System.currentTimeMillis() + "");
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
    }

    public void saveMyBitmap(Bitmap mBitmap, String bitName) {
        File f = new File(FileUtils.getImageRootPath() + bitName + ".jpeg");
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


    public static Intent getPhotoDetailIntent(Context context, AlbymModel.AlbymDetail theme) {
        Intent intent = new Intent(context, AlbymDetailsActivity.class);
        intent.putExtra("ALBYM", theme);
        return intent;
    }

    //新添加
    public void startCorrelationActivity(GlideImageView mPrefectureBg, ThemeDetail.Theme item) {
        Intent intent = CorrelationActivity.getPhotoDetailIntent(AlbymDetailsActivity.this, item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    AlbymDetailsActivity.this,
                    mPrefectureBg,
                    Constant.TRANSITION_ANIMATION_ALBYM_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    mPrefectureBg,
                    mPrefectureBg.getWidth() / 2,
                    mPrefectureBg.getHeight() / 2,
                    0,
                    0);
            ActivityCompat.startActivity(AlbymDetailsActivity.this, intent, optionsCompat.toBundle());
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void setAlbymData(ThemeDetail albymData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAlbymRecycle.refreshComplete();
                if (page == 1) {
                    if (mDetailsAdapter != null && mDetailsAdapter.getCommonItemCount() > 0) {
                        mDetailsAdapter.clear();
                    }
                }
                mDetailsAdapter.addAll(albymData.imageList);
                mDetailsAdapter.notifyDataSetChanged();

                page = albymData.per_page;

            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPrestener.getAlbymDetails(LikeAgent.getInstance().getOpenid(), mAlbymDetail.albymId + "", page, page_num);
    }

    @Override
    public void onLoadMore() {

    }
}
