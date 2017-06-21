package com.aladdin.like.module.circle;


import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.module.circle.adapter.CircleAdapter;
import com.aladdin.like.module.circle.contract.CircleContract;
import com.aladdin.like.module.circle.prestener.Circlrprestener;
import com.aladdin.like.module.diary.PublishDiaryFragment;
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

    int total = 1;
    int page = 1;
    int page_num = 10;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_circle;
    }

    @Override
    protected void initView() {
        mPresenter = new Circlrprestener(this);

        mCircle.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mCircle.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mCircle.setArrowImageView(R.drawable.icon_refresh);
        mCircle.setLoadingListener(this);
        mCircle.setRefreshing(true);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mCircle.setLayoutManager(staggered);
        mCircle.addItemDecoration(new SpacesItemDecoration(10));
        mAdapter = new CircleAdapter(getActivity());
        mCircle.setAdapter(mAdapter);

//        mAdapter.setItemClickListener(new CircleAdapter.onItemClickListener() {
//            @Override
//            public void onItemClick(DiaryDetail.Diary item) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("PREFECTURE", item);
//                startActivity(PictureDetailsActivity.class, bundle);
//            }
//        });

    }

    @Override
    protected void lazyFetchData() {
    }

    @Override
    protected void onvisible() {
        page = 1;
        if (mAdapter != null && mAdapter.getCommonItemCount()>0){
            mAdapter.clear();
        }
        mPresenter.getData(LikeAgent.getInstance().getUid(),page,page_num);
    }

    @Override
    public void onRefresh() {
        mPresenter.getData(LikeAgent.getInstance().getUid(),1,page_num);
    }

    @Override
    public void onLoadMore() {
        if (page<total){
            mPresenter.getData(LikeAgent.getInstance().getUid(),page,page_num);
        }else{
            mCircle.loadMoreComplete();
        }

    }

    @OnClick(R.id.publish)
    public void onViewClicked() {
        startActivity(PublishDiaryFragment.class);
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
    public void setData(DiaryDetail data) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (page == 1){
                    if (mAdapter != null && mAdapter.getItemCount() > 0){
                        mAdapter.clear();
                    }
                }
                mCircle.refreshComplete();
                mCircle.loadMoreComplete();
                mAdapter.addAll(data.diaryList);
                page = data.per_page;
                total = data.total;
            }
        });
    }
}
