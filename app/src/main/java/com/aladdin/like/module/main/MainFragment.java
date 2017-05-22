package com.aladdin.like.module.main;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.download.PictureDetailsActivity;
import com.aladdin.like.module.main.adapter.MainAdapter;
import com.aladdin.like.module.main.contract.MainContract;
import com.aladdin.like.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 主页
 * Created by zxl on 2017/4/29 上午10:21.
 */
public class MainFragment extends BaseFragment implements MainContract.View{//, XRecyclerView.LoadingListener {

    @BindView(R.id.main_recycle)
    RecyclerView mMainRecycle;

    PrefecturePojo.Prefecture mPrefecture;

    MainAdapter mAdapter;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    String[] name = {"日韩美图","微信素材","另类图集","美食图集","健美图片","欧美情侣",
            "运动名将","奢侈生活","电影明星","轻松搞笑","夜生活","专辑封面"};

    private int cursor = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initView() {
//        mMainRecycle.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
//        mMainRecycle.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
//        mMainRecycle.setArrowImageView(R.mipmap.icon_refresh);
//        mMainRecycle.setLoadingListener(this);

        for (int i = 0; i< 10;i++){
            mPrefecture = new PrefecturePojo.Prefecture();
            mPrefecture.typeName = name[i];
            mPrefectures.add(mPrefecture);
        }

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mMainRecycle.setLayoutManager(staggered);
        mMainRecycle.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter = new MainAdapter(getActivity());
        mMainRecycle.setAdapter(mAdapter);
        mAdapter.addAll(mPrefectures);
//        mMainRecycle.setRefreshing(true);

        mAdapter.setItemClickListener(new MainAdapter.onItemClickListener() {
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
    public void setData(PrefecturePojo.Prefecture data) {
        mPrefecture = data;
    }
/*
    @Override
    public void onRefresh() {
        cursor = 0;
        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            mAdapter.clear();
        }
        for (int i = 0; i< 10;i++){
            mPrefecture = new PrefecturePojo.Prefecture();
            mPrefecture.typeName = name[i];
            mPrefectures.add(mPrefecture);
        }
        LogUtil.i("mPrefectures--->>>"+mPrefectures);
        mAdapter.addAll(mPrefectures);
        mAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mMainRecycle.refreshComplete();
            }
        }, 1500);
    }

    @Override
    public void onLoadMore() {
        for (int i = 0; i< 10;i++){
            mPrefecture = new PrefecturePojo.Prefecture();
            mPrefecture.typeName = name[i];
            mPrefectures.add(mPrefecture);
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
    */
}
