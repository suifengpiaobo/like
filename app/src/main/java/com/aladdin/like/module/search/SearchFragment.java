package com.aladdin.like.module.search;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.search.adapter.HorizontalAdapter;
import com.aladdin.like.module.search.adapter.SearchResultAdapter;
import com.aladdin.like.module.search.contract.SearchContract;
import com.aladdin.like.module.search.prestener.SearchPrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 搜索
 * Created by zxl on 2017/4/28 下午2:56.
 */
public class SearchFragment extends BaseFragment implements SearchContract.View, XRecyclerView.LoadingListener {
    SearchContract.Presenter mPresenter;

    @BindView(R.id.search_horizontal)
    RecyclerView mSearchHorizontal;
    @BindView(R.id.search_result)
    XRecyclerView mSearchResult;
    @BindView(R.id.search)
    EditText mSearch;

    HorizontalAdapter mHorizontalAdapter;

    SearchResultAdapter mResultAdapter;

    private TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if ((actionId == EditorInfo.IME_ACTION_SEARCH || actionId == 0) && event != null) {
                /*隐藏软键盘*/
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                }
                if (mSearch.getText().toString().length() > 0) {
                    mPresenter.searchData(LikeAgent.getInstance().getUid(), mSearch.getText().toString());
                }
                return true;
            }
            return false;
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mPresenter = new SearchPrestener(this);
        mPresenter.loadData(LikeAgent.getInstance().getUid());

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
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void lazyFetchData() {
        mSearch.setOnEditorActionListener(onEditorActionListener);
//        mSearch.requestFocus();
    }


    @OnClick(R.id.search)
    public void onViewClicked() {
        mSearch.setFocusable(true);
        mSearch.requestFocus();
//        showInputMethodManager(mSearch);
        forceOpenSoftKeyboard(getActivity());
    }

    public void forceOpenSoftKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
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
        if (getActivity() == null)
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
