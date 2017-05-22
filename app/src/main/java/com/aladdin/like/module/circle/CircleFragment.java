package com.aladdin.like.module.circle;


import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.circle.adapter.CircleAdapter;
import com.aladdin.like.module.diary.PublishDiaryActivity;
import com.aladdin.like.module.download.PictureDetailsActivity;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Description 圈子
 * Created by zxl on 2017/4/29 下午9:32.
 */
public class CircleFragment extends BaseFragment implements XRecyclerView.LoadingListener {

    @BindView(R.id.circle_recycleview)
    RecyclerView mCircleRecycleview;

    CircleAdapter mAdapter;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    String[] name = {"日韩美图", "微信素材", "另类图集", "美食图集", "健美图片", "欧美情侣",
            "运动名将", "奢侈生活", "电影明星", "轻松搞笑", "夜生活", "专辑封面"};
    PrefecturePojo.Prefecture mPrefecture;
    @BindView(R.id.publish)
    TextView mPublish;
    Unbinder unbinder;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView() {
        for (int i = 0; i < 10; i++) {
            mPrefecture = new PrefecturePojo.Prefecture();
            mPrefecture.typeName = name[i];
            mPrefectures.add(mPrefecture);
        }

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mCircleRecycleview.setLayoutManager(staggered);
        mCircleRecycleview.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter = new CircleAdapter(getActivity());
        mCircleRecycleview.setAdapter(mAdapter);
        mAdapter.addAll(mPrefectures);


//        mCircleRecycleview.setRefreshProgressStyle(ProgressStyle.BallClipRotate);
//        mCircleRecycleview.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate);
//        mCircleRecycleview.setArrowImageView(R.mipmap.icon_refresh);
//        mCircleRecycleview.setLoadingListener(this);
//        mCircleRecycleview.setRefreshing(true);

        mAdapter.setItemClickListener(new CircleAdapter.onItemClickListener() {
            @Override
            public void onItemClick(PrefecturePojo.Prefecture item) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("PREFECTURE", item);
                startActivity(PictureDetailsActivity.class, bundle);
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

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mCircleRecycleview.refreshComplete();
//            }
//        }, 2000);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.publish)
    public void onViewClicked() {
//        startActivity(SendCircleActivity.class);
        startActivity(PublishDiaryActivity.class);
    }
}
