package com.aladdin.like.module.mine.diary;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.module.diarydetails.DiaryDetailsActivity;
import com.aladdin.like.module.mine.diary.contract.DiaryContract;
import com.aladdin.like.module.mine.diary.prestener.DiaryPrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sunfusheng.glideimageview.GlideImageView;

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
    int page_num = 200;
//    int total_page = 1;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_dirary;
    }

    @Override
    protected void initView() {
        mPresenter = new DiaryPrestener(this);
//        mPresenter.getUserDiary(LikeAgent.getInstance().getOpenid(),page,page_num);
        mMineDiary.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mMineDiary.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mMineDiary.setLoadingListener(this);
        mMineDiary.setRefreshing(true);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mDiaryAdapter = new MineDiaryAdapter(getActivity());
        mMineDiary.setLayoutManager(staggered);
        mMineDiary.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(7.5f),DensityUtils.dip2px(7.5f)));
        mMineDiary.setAdapter(mDiaryAdapter);

        mDiaryAdapter.setItemClickListener(new MineDiaryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, GlideImageView pic, DiaryDetail.Diary item) {
                startDiaryDetailsActivity(pic,item);
            }
        });
    }

    //新添加
    public void startDiaryDetailsActivity(GlideImageView mPrefectureBg, DiaryDetail.Diary item) {
        Intent intent = new Intent(getActivity(), DiaryDetailsActivity.class);
        intent.putExtra("DIARY", item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(),
                    mPrefectureBg,
                    Constant.TRANSITION_ANIMATION_CIRCLE_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    mPrefectureBg,
                    mPrefectureBg.getWidth() / 2,
                    mPrefectureBg.getHeight() / 2,
                    0,
                    0);
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        }
    }

    @Override
    protected void lazyFetchData() {
    }

    @Override
    protected void onvisible() {
        page = 1;
        mPresenter.getUserDiary(LikeAgent.getInstance().getOpenid(),page,page_num);
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
                if (page == 1){
                    if (mDiaryAdapter != null && mDiaryAdapter.getCommonItemCount()>0){
                        mDiaryAdapter.clear();
                    }
                }
                mMineDiary.refreshComplete();
                mMineDiary.loadMoreComplete();
                mDiaryAdapter.addAll(detail.diaryList);
                mDiaryAdapter.notifyDataSetChanged();
                page = page+1;
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        mPresenter.getUserDiary(LikeAgent.getInstance().getOpenid(),page,page_num);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getUserDiary(LikeAgent.getInstance().getOpenid(),page,page_num);
    }
}
