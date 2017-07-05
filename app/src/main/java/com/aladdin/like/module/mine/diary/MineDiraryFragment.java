package com.aladdin.like.module.mine.diary;


import android.support.v7.widget.GridLayoutManager;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.module.mine.diary.contract.DiaryContract;
import com.aladdin.like.module.mine.diary.prestener.DiaryPrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

/**
 * Description 我的  日记
 * Created by zxl on 2017/5/20 下午8:04.
 */
public class MineDiraryFragment extends BaseFragment implements DiaryContract.View,XRecyclerView.LoadingListener{
    DiaryContract.Presenter mPresenter;
    @BindView(R.id.mine_diary_viewpager)
    XRecyclerView mMineDiary;

    MineDiaryAdapter mDiaryAdapter;

    int page = 1;
    int page_num = 10;
    int total_page = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_dirary;
    }

    @Override
    protected void initView() {
        mPresenter = new DiaryPrestener(this);
        mPresenter.getUserDiary(LikeAgent.getInstance().getUserPojo().openid,page,page_num);

        mMineDiary.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mMineDiary.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mMineDiary.setArrowImageView(R.drawable.icon_refresh);
        mMineDiary.setLoadingListener(this);
        mMineDiary.setPullRefreshEnabled(false);

        GridLayoutManager staggered = new GridLayoutManager(getActivity(), 2);
        mDiaryAdapter = new MineDiaryAdapter(getActivity());
        mMineDiary.setLayoutManager(staggered);
        mMineDiary.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(7.5f),DensityUtils.dip2px(7.5f)));
        mMineDiary.setAdapter(mDiaryAdapter);
    }

    @Override
    protected void lazyFetchData() {
    }

    @Override
    protected void onvisible() {
        if (mDiaryAdapter != null && mDiaryAdapter.getCommonItemCount()>0){
            mDiaryAdapter.clear();
        }
        page = 1;
        mPresenter.getUserDiary(LikeAgent.getInstance().getUserPojo().openid,page,page_num);
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
                stopLoading();
                showErrorHint(msg);
            }
        });
    }

    @Override
    public void setUserDiary(DiaryDetail detail) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMineDiary.refreshComplete();
                mMineDiary.loadMoreComplete();
                mDiaryAdapter.addAll(detail.diaryList);
                page = detail.per_page;
                total_page = detail.total;
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        if (page < total_page){
            mPresenter.getUserDiary(LikeAgent.getInstance().getUserPojo().openid,page,page_num);
        }
    }
}
