package com.aladdin.like.module.main;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.RelativeLayout;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.albym.AlbymActivity;
import com.aladdin.like.module.download.PictureDetailsActivity;
import com.aladdin.like.module.main.adapter.MainAdapter;
import com.aladdin.like.module.main.contract.MainContract;
import com.aladdin.like.module.main.presenter.MainPresenter;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

/**
 * Description 主页
 * Created by zxl on 2017/4/29 上午10:21.
 */
public class MainFragment extends BaseFragment implements MainContract.View, XRecyclerView.LoadingListener {

    @BindView(R.id.main_recycle)
    XRecyclerView mMainRecycle;
    MainAdapter mAdapter;

    MainContract.Presenter mPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mPresenter = new MainPresenter(this) ;

        mMainRecycle.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mMainRecycle.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mMainRecycle.setArrowImageView(R.drawable.icon_refresh);
        mMainRecycle.setLoadingListener(this);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mMainRecycle.setLayoutManager(staggered);
        mMainRecycle.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f),DensityUtils.dip2px(7.5f)));
        mAdapter = new MainAdapter(getActivity());
        mMainRecycle.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new MainAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View v, SimpleDraweeView mMainImg, RelativeLayout mMainItem, ThemeModes.Theme item) {
//                startCorrelationActivity(mMainImg,item);
                Intent intent = new Intent(getActivity(),AlbymActivity.class);
                intent.putExtra("THEME",item);
                startActivity(intent);
            }
        });
    }

    //新添加
    public void startCorrelationActivity(SimpleDraweeView mPrefectureBg, ThemeModes.Theme item){
        Intent intent = PictureDetailsActivity.getPhotoDetailIntent(getActivity(),item);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(),
                    mPrefectureBg,
                    Constant.TRANSITION_ANIMATION_MAIN_PHOTOS);
            startActivity(intent,options.toBundle());
        }else{
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    mPrefectureBg,
                    mPrefectureBg.getWidth() / 2,
                    mPrefectureBg.getHeight() / 2,
                    0,
                    0);
            ActivityCompat.startActivity(getActivity(),intent,optionsCompat.toBundle());
        }
    }

    @Override
    protected void lazyFetchData() {
        mMainRecycle.setRefreshing(true);
    }

    @Override
    protected void onvisible() {
        mMainRecycle.setRefreshing(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainRecycle.refreshComplete();
                mMainRecycle.loadMoreComplete();
                showToast(msg);
            }
        });

    }

    @Override
    public void setData(ThemeModes data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mAdapter != null && mAdapter.getItemCount() > 0){
                    mAdapter.clear();
                }
                mMainRecycle.refreshComplete();
                for (ThemeModes.Theme theme :data.themeList){
                    if (theme.followSign == 1){
                        mAdapter.addItem(theme);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefresh() {
        mPresenter.loadData(LikeAgent.getInstance().getUserPojo().openid);
    }

    @Override
    public void onLoadMore() {
//        mPresenter.loadData(LikeAgent.getInstance().getUid());
        if (getActivity() == null) return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainRecycle.loadMoreComplete();
            }
        });
    }
}
