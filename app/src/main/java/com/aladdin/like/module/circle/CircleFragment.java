package com.aladdin.like.module.circle;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.arcmenu.ArcMenu;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.module.circle.adapter.CircleAdapter;
import com.aladdin.like.module.circle.contract.CircleContract;
import com.aladdin.like.module.circle.prestener.Circlrprestener;
import com.aladdin.like.module.diary.PublishDiaryFragment;
import com.aladdin.like.module.diarydetails.DiaryDetailsActivity;
import com.aladdin.like.widget.ShareDialog;
import com.aladdin.like.widget.SpacesItemDecoration;
import com.aladdin.utils.DensityUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 圈子
 * Created by zxl on 2017/4/29 下午9:32.
 */
public class CircleFragment extends BaseFragment implements CircleContract.View, XRecyclerView.LoadingListener {
    CircleContract.Presenter mPresenter;

    @BindView(R.id.circle_recycleview)
    XRecyclerView mCircle;

    CircleAdapter mAdapter;

    @BindView(R.id.publish)
    TextView mPublish;

    @BindView(R.id.root_view)
    FrameLayout mRootView;

    int total = 1;
    int page = 1;
    int page_num = 10;


    private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera,R.drawable.composer_place, R.drawable.composer_music};
          //  R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with};

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
        mCircle.addItemDecoration(new SpacesItemDecoration(DensityUtils.dip2px(7.5f), DensityUtils.dip2px(7.5f)));
        mAdapter = new CircleAdapter(getActivity());
        mCircle.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new CircleAdapter.onItemClickListener() {
            @Override
            public void onItemClick(View view, SimpleDraweeView simpleDraweeView, DiaryDetail.Diary item) {
                startDiaryDetailsActivity(simpleDraweeView, item);
            }

            @Override
            public void onLongClickListener(View view, int position, DiaryDetail.Diary item) {
                int[] location = new int[2];
                view.getLocationOnScreen(location);
                int x = location[0];
                int y = location[1];
                mAdapter.setPressedPosition(position);

                ArcMenu mArcMenu = new ArcMenu(getActivity());

                mArcMenu.setChildSize(DensityUtils.dip2px(getActivity(), 50));
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (x>DensityUtils.mScreenWidth/2){
                    mArcMenu.setDrgess(90.0f, 180.0f);
                    params.setMargins(DensityUtils.mScreenWidth/2-DensityUtils.dip2px(30),y+view.getHeight()-DensityUtils.dip2px(180),0,0);
                }else {
                    mArcMenu.setDrgess(0.0f, 90.0f);
                    params.setMargins(x+view.getWidth()-DensityUtils.dip2px(170),y+view.getHeight()-DensityUtils.dip2px(180),0,0);
                }
                mArcMenu.setLayoutParams(params);
                mRootView.addView(mArcMenu);
                initArcMenu(mArcMenu, ITEM_DRAWABLES,item);
            }
        });

    }

    private void initArcMenu(ArcMenu menu, int[] itemDrawables,DiaryDetail.Diary diary) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(getActivity());
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mRootView.removeView(menu);
                    mAdapter.setPressedPosition(-1);
                    if (position == 0){
                        ShareDialog shareDialog = new ShareDialog();
                        shareDialog.setBitmapUrl(diary.diaryImage);
                        shareDialog.show(getActivity().getSupportFragmentManager(),"share");
                    }else if(position == 1){

                    }else if (position == 2){
                        savePic(diary.diaryImage);
                    }
                }
            });
        }
    }

    private void savePic(String image) {

    }

    //新添加
    public void startDiaryDetailsActivity(SimpleDraweeView mPrefectureBg, DiaryDetail.Diary item) {
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
        mAdapter.setPressedPosition(0);
        mAdapter.notifyDataSetChanged();
        page = 1;
        if (mAdapter != null && mAdapter.getCommonItemCount() > 0) {
            mAdapter.clear();
        }
        mPresenter.getData(LikeAgent.getInstance().getOpenid(), 1, page, page_num);
    }

    @Override
    public void onRefresh() {
        if (mAdapter != null) {
            mAdapter.setPressedPosition(0);
            mAdapter.notifyDataSetChanged();
        }
        page = 1;
        mPresenter.getData(LikeAgent.getInstance().getOpenid(), 1, page, page_num);
    }

    @Override
    public void onLoadMore() {
        if (page < total) {
            mPresenter.getData(LikeAgent.getInstance().getOpenid(), 1, page, page_num);
        } else {
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
                if (page == 1) {
                    if (mAdapter != null && mAdapter.getItemCount() > 0) {
                        mAdapter.clear();
                    }
                }
                mCircle.refreshComplete();
                mCircle.loadMoreComplete();
                mAdapter.addAll(data.diaryList);
                mAdapter.setPressedPosition(-1);
                page = data.per_page;
                total = data.total;
            }
        });
    }
}
