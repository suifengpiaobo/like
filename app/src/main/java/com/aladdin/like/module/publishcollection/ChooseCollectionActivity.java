package com.aladdin.like.module.publishcollection;

import android.content.Intent;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.module.publishcollection.adapter.CollectionAdapter;
import com.aladdin.like.module.publishcollection.contract.CollectionContract;
import com.aladdin.like.module.publishcollection.prestener.CollectionPrestener;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;

public class ChooseCollectionActivity extends BaseActivity implements CollectionContract.View,XRecyclerView.LoadingListener {
    @BindView(R.id.collection_picture)
    XRecyclerView mCollectionPicture;

    CollectionAdapter mAdapter;
    int page = 1;
    int page_num = 10;
    CollectionContract.Presenter mPresenter;

    public static final int CHOOSE_COLLECTION = 0x01;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_collection;
    }

    @Override
    protected void initView() {
        mPresenter = new CollectionPrestener(this);

        mAdapter = new CollectionAdapter(this);

        mCollectionPicture.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mCollectionPicture.setLoadingMoreProgressStyle(ProgressStyle.BallSpinFadeLoader);
//        mCollectionPicture.setArrowImageView(R.drawable.icon_refresh);
        mCollectionPicture.setLoadingListener(this);
        mCollectionPicture.setRefreshing(true);

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mCollectionPicture.setLayoutManager(staggered);
        mCollectionPicture.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(7.5f), DensityUtils.dip2px(7.5f)));
        mCollectionPicture.setAdapter(mAdapter);

        mAdapter.setClickListener(new CollectionAdapter.onItemClickListener() {
            @Override
            public void onChoose(String url, int width, int height) {
                Intent intent = new Intent();
                intent.putExtra("url",url);
                intent.putExtra("width",width);
                intent.putExtra("height",height);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void setData(CollectionImage collectionImage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCollectionPicture.refreshComplete();
                if(collectionImage != null && collectionImage.recordList.size()>0){
                    mAdapter.addAll(collectionImage.recordList);
                    mAdapter.notifyDataSetChanged();

                    page = collectionImage.per_page;
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        if (mAdapter != null &&mAdapter.getCommonItemCount()>0){
            mAdapter.clear();
        }
        mPresenter.getUserCollectionImage(LikeAgent.getInstance().getUid(),page,page_num);
    }

    @Override
    public void onLoadMore() {
        mPresenter.getUserCollectionImage(LikeAgent.getInstance().getUid(),page,page_num);
    }
}
