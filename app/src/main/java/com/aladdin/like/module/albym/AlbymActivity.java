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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.arcmenu.ArcMenu;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.albym.adapter.AlbymAdapter;
import com.aladdin.like.module.albym.contract.AlbymContract;
import com.aladdin.like.module.albym.presenter.AlbymPrestener;
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
import butterknife.OnClick;


/**
 * Description 专辑页
 * Created by zxl on 2017/7/22 上午11:21.
 */
public class AlbymActivity extends BaseActivity implements AlbymContract.View, XRecyclerView.LoadingListener {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.share)
    ImageView mShare;
    @BindView(R.id.albym_list)
    XRecyclerView mAlbymList;
    @BindView(R.id.line)
    View mLine;
    @BindView(R.id.root_view)
    FrameLayout mRootView;

    @BindView(R.id.picture)
    GlideImageView mPicture;
    @BindView(R.id.type_name)
    TextView mTypeName;
    @BindView(R.id.parise_num)
    TextView mPariseNum;
    @BindView(R.id.click_praise)
    ImageView mClickPraise;

    AlbymAdapter mAlbymAdapter;

    AlbymContract.Prestener mPrestener;

    ThemeModes.Theme mTheme;

    AlbymModel.AlbymDetail mAlbymDetail;

    int page = 1, page_num = 10;

    private static final int[] ITEM_DRAWABLES = {R.drawable.compress_share, R.drawable.compress_collection, R.drawable.compress_download};

    @Override
    protected int getLayoutId() {
        return R.layout.activity_albym;
    }

    @Override
    protected void initView() {
        mTheme = (ThemeModes.Theme) getIntent().getSerializableExtra("THEME");

        mPrestener = new AlbymPrestener(AlbymActivity.this);
        mPrestener.getAlbymDetail(LikeAgent.getInstance().getOpenid(), mTheme.themeId, page, page_num);

        mAlbymList.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mAlbymList.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mAlbymList.setArrowImageView(R.drawable.icon_refresh);
        mAlbymList.setLoadingListener(this);
        mAlbymList.setPullRefreshEnabled(false);

        float scale = (DensityUtils.mScreenWidth-DensityUtils.dip2px(30))/(float)mTheme.width;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPicture.getLayoutParams();
        params.height = (int) (mTheme.height*scale);
        params.width = (int)(mTheme.width*scale);
        mPicture.setLayoutParams(params);
        mPicture.loadImage(mTheme.themeImgUrl,R.color.placeholder_color);
        mTypeName.setText(mTheme.themeName);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAlbymAdapter = new AlbymAdapter(AlbymActivity.this);
        mAlbymList.setLayoutManager(staggered);
        mAlbymList.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f), DensityUtils.dip2px(7.5f)));
        mAlbymList.setAdapter(mAlbymAdapter);

        mAlbymAdapter.setItemClickListener(new AlbymAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, GlideImageView mPrefectureBg, AlbymModel.AlbymDetail item) {
//                startCorrelationActivity(mPrefectureBg, item);
                Intent intent= AlbymDetailsActivity.getPhotoDetailIntent(AlbymActivity.this,item);
                startActivity(intent);
            }

            @Override
            public void onLongClickListener(View view, int position, AlbymModel.AlbymDetail item) {
//                int[] location = new int[2];
//                view.getLocationOnScreen(location);
//                int x = location[0];
//                int y = location[1];
//                mAlbymAdapter.setPressedPosition(position);
//                ArcMenu mArcMenu = new ArcMenu(AlbymActivity.this);
//                mArcMenu.setChildSize(DensityUtils.dip2px(AlbymActivity.this, 30));
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                if (x > DensityUtils.mScreenWidth / 2) {
//                    mArcMenu.setDrgess(90.0f, 180.0f);
//                    params.setMargins(DensityUtils.mScreenWidth / 2 - DensityUtils.dip2px(30), y + view.getHeight() - DensityUtils.dip2px(200), 0, 0);
//                } else {
//                    mArcMenu.setDrgess(0.0f, 90.0f);
//                    params.setMargins(x + view.getWidth() - DensityUtils.dip2px(100), y + view.getHeight() - DensityUtils.dip2px(200), 0, 0);
//                }
//                mArcMenu.setLayoutParams(params);
//                mRootView.addView(mArcMenu);
//
//                initArcMenu(mArcMenu, ITEM_DRAWABLES, item);
            }
        });
    }

    public static Intent getPhotoDetailIntent(Context context, ThemeModes.Theme theme) {
        Intent intent = new Intent(context, AlbymActivity.class);
        intent.putExtra("THEME", theme);
        return intent;
    }

    private void initArcMenu(ArcMenu menu, int[] itemDrawables, AlbymModel.AlbymDetail themeDetail) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(AlbymActivity.this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mRootView.removeView(menu);
                    mAlbymAdapter.setPressedPosition(-1);
                    if (position == 0) {
                        ShareDialog shareDialog = new ShareDialog();
                        shareDialog.setBitmapUrl(themeDetail.albymUrl);
                        shareDialog.show(getSupportFragmentManager(), "share");
                    } else if (position == 1) {
                        collectionPic(themeDetail);
                    } else if (position == 2) {
                        savePic(themeDetail.albymUrl);
                    }
                }
            });
        }
    }

    public void collectionPic(AlbymModel.AlbymDetail themeDetail) {
        HttpManager.INSTANCE.collectionImage(LikeAgent.getInstance().getOpenid(), themeDetail.albymUrl, new HttpResultCallback<String>() {
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
    public void setAlbymData(AlbymModel albymData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (page == 1) {
                    if (mAlbymAdapter != null && mAlbymAdapter.getCommonItemCount() > 0) {
                        mAlbymAdapter.clear();
                    }
                }
                mAlbymAdapter.addAll(albymData.albymList);
                mAlbymAdapter.notifyDataSetChanged();

                page = albymData.next_page;

            }
        });
    }

    //新添加
    public void startCorrelationActivity(GlideImageView mPrefectureBg, ThemeDetail.Theme item) {
        Intent intent = CorrelationActivity.getPhotoDetailIntent(AlbymActivity.this, item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Pair<View, String> imagePair = Pair.create(mPrefectureBg, Constant.TRANSITION_ANIMATION_NEWS_PHOTOS);
//            Pair<View, String> textPair = Pair.create(mWaterMark, Constant.TRANSITION_ANIMATION_NEWS_PHOTOS);
//            ActivityOptionsCompat compat = ActivityOptionsCompat
//                    .makeSceneTransitionAnimation(this, imagePair, textPair);
//            ActivityCompat.startActivity(this, intent,
//                    compat.toBundle());

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    AlbymActivity.this,
                    mPrefectureBg,
                    Constant.TRANSITION_ANIMATION_NEWS_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    mPrefectureBg,
                    mPrefectureBg.getWidth() / 2,
                    mPrefectureBg.getHeight() / 2,
                    0,
                    0);
            ActivityCompat.startActivity(AlbymActivity.this, intent, optionsCompat.toBundle());
        }
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.share)
    public void share(){
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.setBitmapUrl(mTheme.themeImgUrl);
        shareDialog.show(getSupportFragmentManager(),"share");
    }


}
