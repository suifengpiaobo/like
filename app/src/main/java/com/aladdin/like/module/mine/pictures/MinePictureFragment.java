package com.aladdin.like.module.mine.pictures;


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
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.module.collectiondetails.CollectionDetailsActivity;
import com.aladdin.like.module.mine.pictures.adapter.PictureAdapter;
import com.aladdin.like.module.mine.pictures.contract.PictureContract;
import com.aladdin.like.module.mine.pictures.prestener.PicturePrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.sunfusheng.glideimageview.GlideImageView;

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

    int page = 1;
    int page_num =10;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_picture;
    }

    @Override
    protected void initView() {
        mPresenter = new PicturePrestener(this);
        mPresenter.getPicture(LikeAgent.getInstance().getOpenid(),page,page_num,1);

        mPicture.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mPicture.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mPicture.setArrowImageView(R.drawable.icon_refresh);
        mPicture.setLoadingListener(this);
        mPicture.setPullRefreshEnabled(false);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPictureAdapter = new PictureAdapter(getActivity());
        mPicture.setLayoutManager(staggered);
        mPicture.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(11.5f),DensityUtils.dip2px(7.5f)));
        mPicture.setAdapter(mPictureAdapter);

        mPictureAdapter.setItemClickListener(new PictureAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, GlideImageView pic, CollectionImage.Collection item) {
                startCollectionDetailsActivity(pic,item);
            }
        });
    }

    private void startCollectionDetailsActivity(GlideImageView pic, CollectionImage.Collection item) {
        Intent intent = new Intent(getActivity(), CollectionDetailsActivity.class);
        intent.putExtra("COLLECTION", item);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                    getActivity(),
                    pic,
                    Constant.TRANSITION_ANIMATION_CIRCLE_PHOTOS);
            startActivity(intent, options.toBundle());
        } else {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                    pic,
                    pic.getWidth() / 2,
                    pic.getHeight() / 2,
                    0,
                    0);
            ActivityCompat.startActivity(getActivity(), intent, optionsCompat.toBundle());
        }
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
        mPresenter.getPicture(LikeAgent.getInstance().getOpenid(),page,page_num,1);
    }
}
