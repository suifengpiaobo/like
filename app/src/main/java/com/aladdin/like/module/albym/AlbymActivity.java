package com.aladdin.like.module.albym;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.albym.adapter.AlbymAdapter;
import com.aladdin.like.module.albym.adapter.AlbymPicAdapter;
import com.aladdin.like.module.albym.contract.AlbymContract;
import com.aladdin.like.module.albym.presenter.AlbymPrestener;
import com.aladdin.like.module.download.CorrelationActivity;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

public class AlbymActivity extends BaseActivity implements AlbymContract.View, XRecyclerView.LoadingListener {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.albym_horizontal)
    RecyclerView mAlbymHorizontal;
    @BindView(R.id.albym_name)
    TextView mAlbymName;
    @BindView(R.id.albym_list)
    XRecyclerView mAlbymList;
    @BindView(R.id.line)
    View mLine;

    AlbymAdapter mAlbymAdapter;

    AlbymPicAdapter mAlbymPicAdapter;

    AlbymContract.Prestener mPrestener;

    ThemeModes.Theme mTheme;

    AlbymModel.AlbymDetail mAlbymDetail;

    int page = 1, page_num = 10;

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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AlbymActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAlbymHorizontal.setLayoutManager(linearLayoutManager);
        mAlbymAdapter = new AlbymAdapter(AlbymActivity.this);
        mAlbymHorizontal.setAdapter(mAlbymAdapter);

        mAlbymAdapter.setItemClickListener(new AlbymAdapter.onItemClickListener() {
            @Override
            public void onItemClick(AlbymModel.AlbymDetail item) {
                if (mAlbymPicAdapter != null && mAlbymPicAdapter.getCommonItemCount()>0){
                    mAlbymPicAdapter.clear();
                }
                page = 1;
                mPrestener.getThemeDetail(LikeAgent.getInstance().getOpenid(), item.albymId + "", page, page_num);
                mAlbymName.setText(item.albymName);
            }
        });


        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAlbymPicAdapter = new AlbymPicAdapter(AlbymActivity.this);
        mAlbymList.setLayoutManager(staggered);
        mAlbymList.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f), DensityUtils.dip2px(7.5f)));
        mAlbymList.setAdapter(mAlbymPicAdapter);

        mAlbymPicAdapter.setItemClickListener(new AlbymPicAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, SimpleDraweeView mPrefectureBg, ThemeDetail.Theme item) {
                startCorrelationActivity(mPrefectureBg, item);
            }
        });
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
                if (albymData.albymList.size() > 0) {
                    mPrestener.getThemeDetail(LikeAgent.getInstance().getOpenid(), albymData.albymList.get(0).albymId + "", page, page_num);
                    mAlbymName.setText(albymData.albymList.get(0).albymName);
                    mLine.setVisibility(View.VISIBLE);
                }
                page = albymData.next_page;

            }
        });
    }

    //新添加
    public void startCorrelationActivity(SimpleDraweeView mPrefectureBg, ThemeDetail.Theme item) {
        Intent intent = CorrelationActivity.getPhotoDetailIntent(AlbymActivity.this, item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
    public void setAlbymPic(ThemeDetail themeDetail) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAlbymPicAdapter.addAll(themeDetail.imageList);
                mAlbymPicAdapter.notifyDataSetChanged();
            }
        });
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
}
