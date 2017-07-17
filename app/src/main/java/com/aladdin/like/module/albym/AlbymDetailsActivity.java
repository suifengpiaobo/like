package com.aladdin.like.module.albym;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.module.albym.adapter.AlbymDetailsAdapter;
import com.aladdin.like.module.albym.contract.AlbymDetailsContract;
import com.aladdin.like.module.albym.presenter.AlbymDetailsPrestener;
import com.aladdin.like.module.download.CorrelationActivity;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sunfusheng.glideimageview.GlideImageView;

import butterknife.BindView;

/**
 * Description 专辑详情
 * Created by zxl on 2017/7/17 下午6:53.
 */
public class AlbymDetailsActivity extends BaseActivity implements AlbymDetailsContract.View,XRecyclerView.LoadingListener{
    AlbymDetailsContract.Prestener mPrestener;
    @BindView(R.id.albym_details_recycle)
    XRecyclerView mAlbymRecycle;

    AlbymDetailsAdapter mDetailsAdapter;

    private int page=1;
    private int page_num = 10;

    AlbymModel.AlbymDetail mAlbymDetail;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_albym_details;
    }

    @Override
    protected void initView() {
        mPrestener = new AlbymDetailsPrestener(this);
        mAlbymDetail = (AlbymModel.AlbymDetail) getIntent().getSerializableExtra("ALBYM");

        mPrestener.getAlbymDetails(LikeAgent.getInstance().getOpenid(), mAlbymDetail.albymId+"", page, page_num);

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
            public void onItemClick(View v, GlideImageView mMainImg, RelativeLayout mMainItem, ThemeDetail.Theme item) {
                startCorrelationActivity(mMainImg,item);
            }
        });
    }

    public static Intent getPhotoDetailIntent(Context context, AlbymModel.AlbymDetail theme) {
        Intent intent = new Intent(context, AlbymDetailsActivity.class);
        intent.putExtra("ALBYM", theme);
        return intent;
    }

    //新添加
    public void startCorrelationActivity(GlideImageView mPrefectureBg, ThemeDetail.Theme item){
        Intent intent = CorrelationActivity.getPhotoDetailIntent(AlbymDetailsActivity.this,item);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    AlbymDetailsActivity.this,
                    mPrefectureBg,
                    Constant.TRANSITION_ANIMATION_ALBYM_PHOTOS);
            startActivity(intent,options.toBundle());
        }else{
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    mPrefectureBg,
                    mPrefectureBg.getWidth() / 2,
                    mPrefectureBg.getHeight() / 2,
                    0,
                    0);
            ActivityCompat.startActivity(AlbymDetailsActivity.this,intent,optionsCompat.toBundle());
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
        mPrestener.getAlbymDetails(LikeAgent.getInstance().getOpenid(), mAlbymDetail.albymId+"", page, page_num);
    }

    @Override
    public void onLoadMore() {

    }
}
