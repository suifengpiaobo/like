package com.aladdin.like.module.circle;


import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.download.PictureDetailsActivity;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.module.main.adapter.MainAdapter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 圈子
 * Created by zxl on 2017/4/29 下午9:32.
 */
public class CircleFragment extends BaseFragment implements XRecyclerView.LoadingListener{

    @BindView(R.id.circle_recycleview)
    XRecyclerView mCircleRecycleview;

    MainAdapter mAdapter;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView() {
        mAdapter = new MainAdapter(getActivity());
        mCircleRecycleview.setAdapter(mAdapter);

        mCircleRecycleview.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
        mCircleRecycleview.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
        mCircleRecycleview.setArrowImageView(R.mipmap.icon_refresh);
        mCircleRecycleview.setLoadingListener(this);

        mCircleRecycleview.setRefreshing(true);

        mAdapter.setOnItemClickListener(new MainAdapter.onItemClickListener() {
            @Override
            public void onItemClick(PrefecturePojo.Prefecture item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("PREFECTURE",item);
                startActivity(PictureDetailsActivity.class,bundle);
            }
        });
        
        bindEvent();
    }

    private void bindEvent() {
        ((MainActivity)getActivity()).setOnCircleClickListener(new MainActivity.onCircleClickListener() {
            @Override
            public void onClick() {
                startActivity(SendCircleActivity.class);
            }

        });
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void onRefresh() {
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
        mCircleRecycleview.setLayoutManager(staggered);
        mAdapter.addAll(mPrefectures);
        mAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCircleRecycleview.refreshComplete();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {

    }
}
