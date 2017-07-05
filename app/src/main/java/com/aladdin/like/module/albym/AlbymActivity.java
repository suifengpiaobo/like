package com.aladdin.like.module.albym;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.albym.adapter.AlbymAdapter;
import com.aladdin.like.module.albym.adapter.AlbymPicAdapter;
import com.aladdin.like.module.albym.contract.AlbymContract;
import com.aladdin.like.module.albym.presenter.AlbymPrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
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

    AlbymAdapter mAlbymAdapter;

    AlbymPicAdapter mAlbymPicAdapter;

    AlbymContract.Prestener mPrestener;

    ThemeModes.Theme mTheme;

    AlbymModel.AlbymDetail mAlbymDetail;

    int page = 1,page_num = 10;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_albym;
    }

    @Override
    protected void initView() {
        mTheme = (ThemeModes.Theme) getIntent().getSerializableExtra("THEME");

        mPrestener = new AlbymPrestener(AlbymActivity.this);
        mPrestener.getAlbymDetail(LikeAgent.getInstance().getUserPojo().openid,mTheme.themeId,page,page_num);

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


        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAlbymPicAdapter = new AlbymPicAdapter(AlbymActivity.this);
        mAlbymList.setLayoutManager(staggered);
        mAlbymList.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f), DensityUtils.dip2px(7.5f)));
        mAlbymList.setAdapter(mAlbymPicAdapter);
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
                if (page == 1){
                    if (mAlbymAdapter != null && mAlbymAdapter.getCommonItemCount()>0){
                        mAlbymAdapter.clear();
                    }
                }

                mAlbymAdapter.addAll(albymData.albymList);
                mAlbymAdapter.notifyDataSetChanged();
                if (albymData.albymList.size()>0){
                    mPrestener.getThemeDetail(LikeAgent.getInstance().getUserPojo().openid,albymData.albymList.get(0).albymId+"",page,page_num);
                    mAlbymName.setText(albymData.albymList.get(0).albymName);
                }
                page = albymData.next_page;

            }
        });
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
    }
}
