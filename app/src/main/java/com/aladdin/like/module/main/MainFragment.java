package com.aladdin.like.module.main;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.download.PictureDetailsActivity;
import com.aladdin.like.module.main.adapter.MainAdapter;
import com.aladdin.like.module.main.contract.MainContract;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 主页
 * Created by zxl on 2017/4/29 上午10:21.
 */
public class MainFragment extends BaseFragment implements MainContract.View, XRecyclerView.LoadingListener {

    @BindView(R.id.main_recycle)
    XRecyclerView mMainRecycle;

    PrefecturePojo mPrefecture;

    MainAdapter mAdapter;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();

    private int cursor = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
        mAdapter = new MainAdapter(getActivity());
        mMainRecycle.setAdapter(mAdapter);

        mMainRecycle.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        mMainRecycle.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        mMainRecycle.setArrowImageView(R.mipmap.icon_refresh);
        mMainRecycle.setLoadingListener(this);

        mMainRecycle.setRefreshing(true);

        mAdapter.setOnItemClickListener(new MainAdapter.onItemClickListener() {
            @Override
            public void onItemClick(PrefecturePojo.Prefecture item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("PREFECTURE",item);
                startActivity(PictureDetailsActivity.class,bundle);
            }
        });
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void showLoading() {
        showLoading();
    }

    @Override
    public void stopLoading() {
        stopLoading();
    }

    @Override
    public void showErrorTip(String msg) {
        showErrorTip(msg);
    }

    @Override
    public void setData(PrefecturePojo data) {
        mPrefecture = data;
    }

    @Override
    public void onRefresh() {
        cursor = 0;
        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            mAdapter.clear();
        }
        for (int i = 0; i < 20; i++) {
            PrefecturePojo.Prefecture prefecture = new PrefecturePojo.Prefecture();
            prefecture.time = 10192;
            prefecture.typeName = "健身美图" + i;

            mPrefectures.add(prefecture);
        }

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mMainRecycle.setLayoutManager(staggered);
        mAdapter.addAll(mPrefectures);
        mAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainRecycle.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        for (int i = 0; i < 10; i++) {
            PrefecturePojo.Prefecture prefecture = new PrefecturePojo.Prefecture();
            prefecture.time = 10192;
            prefecture.typeName = "健身美图" + i;

            mPrefectures.add(prefecture);
        }

        mAdapter.addAll(mPrefectures);
        mAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainRecycle.loadMoreComplete();
            }
        }, 1000);
    }
}
