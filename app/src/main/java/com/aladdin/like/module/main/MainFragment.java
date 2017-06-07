package com.aladdin.like.module.main;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.download.PictureDetailsActivity;
import com.aladdin.like.module.main.adapter.MainAdapter;
import com.aladdin.like.module.main.contract.MainContract;
import com.aladdin.like.module.main.presenter.MainPresenter;
import com.aladdin.like.widget.SpacesItemDecoration;
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

        mMainRecycle.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        mMainRecycle.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        mMainRecycle.setArrowImageView(R.drawable.icon_refresh);
        mMainRecycle.setLoadingListener(this);
        mMainRecycle.setFootViewVisible(false);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mMainRecycle.setLayoutManager(staggered);
        mMainRecycle.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter = new MainAdapter(getActivity());
        mMainRecycle.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new MainAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ThemeModes.Theme item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("PREFECTURE",item);
                startActivity(PictureDetailsActivity.class,bundle);
            }
        });
    }

    @Override
    protected void lazyFetchData() {
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
                mMainRecycle.refreshComplete();
                mAdapter.addAll(data.themeList);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (mAdapter != null && mAdapter.getItemCount() > 0){
            mAdapter.clear();
        }
        mPresenter.start();
        mPresenter.loadData("");
    }

    @Override
    public void onLoadMore() {
        mMainRecycle.loadMoreComplete();
    }
}
