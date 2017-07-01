package com.aladdin.like.module.main;


import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.model.ThemeModes;
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
            public void onItemClick(View v, SimpleDraweeView mMainImg, LinearLayout mMainItem, ThemeModes.Theme item) {
                scaleUpAnimation(mMainItem,item);
            }
        });
    }

    private void scaleUpAnimation(View view,ThemeModes.Theme item) {
        //让新的Activity从一个小的范围扩大到全屏
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeScaleUpAnimation(view, //The View that the new activity is animating from
                        (int)view.getWidth()/2, (int)view.getHeight()/2, //拉伸开始的坐标
                        0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        startNewAcitivity(options,item);
    }

    private void startNewAcitivity(ActivityOptionsCompat options,ThemeModes.Theme item) {
        Intent intent = new Intent(getActivity(),PictureDetailsActivity.class);
        intent.putExtra("PREFECTURE",item);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
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
        mPresenter.loadData(LikeAgent.getInstance().getUid());
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
