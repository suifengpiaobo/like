package com.aladdin.like.module.mine.pictures;


import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.module.mine.pictures.adapter.PictureAdapter;
import com.aladdin.like.module.mine.pictures.contract.PictureContract;
import com.aladdin.like.module.mine.pictures.prestener.PicturePrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

/**
 * Description 我的 图片
 * Created by zxl on 2017/5/20 下午8:05.
 */
public class MinePictureFragment extends BaseFragment implements PictureContract.View,XRecyclerView.LoadingListener{
    PictureContract.Presenter mPresenter;

    @BindView(R.id.picture_recycleview)
    XRecyclerView mPicture;


    PictureAdapter mPictureAdapter;

    int page;
    int page_num;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_picture;
    }

    @Override
    protected void initView() {
        mPresenter = new PicturePrestener(this);
        mPresenter.getPicture(LikeAgent.getInstance().getUid(),page,page_num);

        mPicture.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mPicture.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mPicture.setArrowImageView(R.drawable.icon_refresh);
        mPicture.setLoadingListener(this);
        mPicture.setPullRefreshEnabled(false);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPictureAdapter = new PictureAdapter(getActivity());
        mPicture.setLayoutManager(staggered);
        mPicture.addItemDecoration(new SpacesItemDecoration(10));
        mPicture.setAdapter(mPictureAdapter);
    }

    @Override
    protected void lazyFetchData() {

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
                showToast(msg);
            }
        });
    }

    @Override
    public void setCollectImage(CollectionImage collectImage) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPicture.loadMoreComplete();
                mPicture.refreshComplete();
                mPictureAdapter.addAll(collectImage.recordList);
                mPictureAdapter.notifyDataSetChanged();
                page = collectImage.per_page;
            }
        });
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        mPresenter.getPicture(LikeAgent.getInstance().getUid(),page,page_num);
    }
}
