package com.aladdin.like.module.circle;


import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.circle.adapter.CircleAdapter;
import com.aladdin.like.module.circle.contract.CircleContract;
import com.aladdin.like.module.circle.prestener.Circlrprestener;
import com.aladdin.like.module.download.PictureDetailsActivity;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 圈子
 * Created by zxl on 2017/4/29 下午9:32.
 */
public class CircleFragment extends BaseFragment implements CircleContract.View,XRecyclerView.LoadingListener {
    CircleContract.Presenter mPresenter;

    @BindView(R.id.circle_recycleview)
    XRecyclerView mCircle;

    CircleAdapter mAdapter;

    @BindView(R.id.publish)
    TextView mPublish;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView() {
        mPresenter = new Circlrprestener(this);
        mPresenter.getData(LikeAgent.getInstance().getUid());

        mCircle.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mCircle.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mCircle.setArrowImageView(R.drawable.icon_refresh);
        mCircle.setLoadingListener(this);
        mCircle.setPullRefreshEnabled(false);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mCircle.setLayoutManager(staggered);
        mCircle.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter = new CircleAdapter(getActivity());
        mCircle.setAdapter(mAdapter);


        mAdapter.setItemClickListener(new CircleAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ThemeModes.Theme item) {
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
    }

    @Override
    public void onLoadMore() {

    }

    @OnClick(R.id.publish)
    public void onViewClicked() {
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCircle.refreshComplete();
                mCircle.loadMoreComplete();
                showToast(msg);
            }
        });
    }

    @Override
    public void setData() {

    }
}
