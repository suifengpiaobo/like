package com.aladdin.like.module.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.EditText;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.search.adapter.HorizontalAdapter;
import com.aladdin.like.module.search.adapter.SearchResultAdapter;
import com.aladdin.like.module.search.contract.SearchContract;
import com.aladdin.like.module.search.prestener.SearchPrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.LogUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 搜索
 * Created by zxl on 2017/4/28 下午2:56.
 */
public class SearchFragment extends BaseFragment implements SearchContract.View,XRecyclerView.LoadingListener{
    SearchContract.Presenter mPresenter;

    @BindView(R.id.search_horizontal)
    RecyclerView mSearchHorizontal;
    @BindView(R.id.search_result)
    XRecyclerView mSearchResult;
    @BindView(R.id.search)
    EditText mSearch;

    HorizontalAdapter mHorizontalAdapter;

    SearchResultAdapter mResultAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        mPresenter = new SearchPrestener(this);
        mPresenter.loadData(LikeAgent.getInstance().getUid());
        mPresenter.searchData(LikeAgent.getInstance().getUid(),"");

        mSearchResult.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mSearchResult.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mSearchResult.setArrowImageView(R.drawable.icon_refresh);
        mSearchResult.setLoadingListener(this);
        mSearchResult.setPullRefreshEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSearchHorizontal.setLayoutManager(linearLayoutManager);
        mHorizontalAdapter = new HorizontalAdapter(getActivity());
        mSearchHorizontal.setAdapter(mHorizontalAdapter);


        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mResultAdapter = new SearchResultAdapter(getActivity());
        mSearchResult.setLayoutManager(staggered);
        mSearchResult.addItemDecoration(new SpacesItemDecoration(10));
        mSearchResult.setAdapter(mResultAdapter);
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick(R.id.search)
    public void onViewClicked() {
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
        if (getActivity() ==null)
            return;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSearchResult.loadMoreComplete();
                mSearchResult.refreshComplete();
                showToast(msg);
            }
        });
    }

    @Override
    public void setData(ThemeModes data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mHorizontalAdapter.addAll(data.themeList);
                mHorizontalAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void setResultData(ThemeModes data) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mResultAdapter.addAll(data.themeList);
                mResultAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
