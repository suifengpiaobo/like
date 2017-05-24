package com.aladdin.like.module.mine;


import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.widget.ScrollViewPager;
import com.aladdin.like.widget.SlidingTabLayout;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description
 * Created by zxl on 2017/5/24 下午8:18.
 */
public class MineFragment2 extends BaseFragment {

    @BindView(R.id.user_avatar)
    SimpleDraweeView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.mine_info_viewpager)
    ScrollViewPager mMineInfoViewpager;
    @BindView(R.id.root_view)
    LinearLayout mRootView;
    @BindView(R.id.set)
    ImageView mSet;


    MineAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_fragment2;
    }

    @Override
    protected void initView() {
        ViewTreeObserver vto = mRootView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int slidingTabLayoutHeight = DensityUtils.dip2px(47);
                int mScreenHeigth = mRootView.getHeight()-DensityUtils.dip2px(48);

                int height = mScreenHeigth - slidingTabLayoutHeight;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mMineInfoViewpager.getLayoutParams();
                layoutParams.width = DensityUtils.mScreenWidth;
                layoutParams.height = height;
                mMineInfoViewpager.setLayoutParams(layoutParams);
                LogUtil.i("height--->>>"+height);
            }
        });

        mSlidingTabLayout.setTabTitleTextSize(15);
        mSlidingTabLayout.setTabStripWidth(DensityUtils.dip2px(80));
        mSlidingTabLayout.setCustomTabView(R.layout.layout_tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.color_1F2427));
        mSlidingTabLayout.setSlidingTabStripCenter();
        mSlidingTabLayout.setTitleTextColor(getResources().getColor(R.color.color_1F2427), getResources().getColor(R.color.color_82879A));
        mSlidingTabLayout.setDistributeEvenly(true);
    }

    @Override
    protected void lazyFetchData() {
        mAdapter = new MineAdapter(getFragmentManager());
        mSlidingTabLayout.setVisibility(View.VISIBLE);
        mMineInfoViewpager.setVisibility(View.VISIBLE);

        mMineInfoViewpager.setAdapter(mAdapter);
        mSlidingTabLayout.setViewPager(mMineInfoViewpager);
//        mMineInfoViewpager.setOffscreenPageLimit(3);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.set)
    public void onViewClicked() {
    }
}
