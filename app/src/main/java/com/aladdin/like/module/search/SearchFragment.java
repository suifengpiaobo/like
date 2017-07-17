package com.aladdin.like.module.search;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.albym.AlbymActivity;
import com.aladdin.like.module.search.adapter.HorizontalAdapter;
import com.aladdin.like.module.search.adapter.SearchResultAdapter;
import com.aladdin.like.module.search.contract.SearchContract;
import com.aladdin.like.module.search.prestener.SearchPrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sunfusheng.glideimageview.GlideImageView;

import butterknife.BindView;

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
                    mPresenter.searchData(LikeAgent.getInstance().getOpenid(), mSearch.getText().toString());
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
        mPresenter = new SearchPrestener(this);
        mPresenter.loadData(LikeAgent.getInstance().getOpenid());

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
        mSearchResult.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f),DensityUtils.dip2px(7.5f)));
        mSearchResult.setAdapter(mResultAdapter);

        mSearch.setOnEditorActionListener(onEditorActionListener);


        mResultAdapter.setItemClickListener(new SearchResultAdapter.onItemClickListener() {
            @Override
            public void onItemClick(GlideImageView imageView,ThemeModes.Theme item) {
//                Intent intent = new Intent(getActivity(),AlbymActivity.class);
//                intent.putExtra("THEME",item);
//                startActivity(intent);
                LogUtil.i("--onItemClick-->>"+item);
                startCorrelationActivity(imageView,item);
            }
        });
    }

    //新添加
    public void startCorrelationActivity(GlideImageView mPrefectureBg, ThemeModes.Theme item){
        Intent intent = AlbymActivity.getPhotoDetailIntent(getActivity(),item);

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
    public void onResume() {
        super.onResume();

    }

    @Override
    protected void lazyFetchData() {
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
                if (mHorizontalAdapter != null && mHorizontalAdapter.getCommonItemCount()>0){
                    mHorizontalAdapter.clear();
                }
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
                if (mResultAdapter != null && mResultAdapter.getCommonItemCount()>0){
                    mResultAdapter.clear();
                }
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
