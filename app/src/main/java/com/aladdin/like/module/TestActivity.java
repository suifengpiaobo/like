package com.aladdin.like.module;

import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.like.widget.MyScrollView;
import com.aladdin.like.widget.SlidingTabLayout;
import com.aladdin.utils.DensityUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.jcodecraeer.xrecyclerview.XScrollView;

import butterknife.BindView;

public class TestActivity extends BaseActivity implements XScrollView.onRefreshAbleStatusChangedListener, XScrollView.onPullToRefreshListener, XScrollView.onRefreshCompletedDone{

    @BindView(R.id.user_avatar)
    SimpleDraweeView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.mine_title)
    RelativeLayout mMineTitle;
    @BindView(R.id.slidingTabLayout)
    SlidingTabLayout mSlidingTabLayout;
    @BindView(R.id.plugInUnitViewPager)
    ViewPager mPlugInUnitViewPager;
    @BindView(R.id.scrollView)
    MyScrollView mScrollView;
    @BindView(R.id.set)
    ImageView mSet;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.root);
        ViewTreeObserver vto2 = frameLayout.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                frameLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int slidingTabLayoutHeight = DensityUtils.dip2px(48);
                int mScreenHeigth = frameLayout.getHeight();

                int height = mScreenHeigth - slidingTabLayoutHeight;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPlugInUnitViewPager.getLayoutParams();
                layoutParams.width = DensityUtils.mScreenWidth;
                layoutParams.height = height;
                mPlugInUnitViewPager.setLayoutParams(layoutParams);
            }
        });

        mSlidingTabLayout.setTabTitleTextSize(15);
        mSlidingTabLayout.setTabStripWidth(DensityUtils.dip2px(80));
        mSlidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.color_1F2427));
        mSlidingTabLayout.setSlidingTabStripCenter();
        mSlidingTabLayout.setTitleTextColor(getResources().getColor(R.color.color_1F2427), getResources().getColor(R.color.color_82879A));
        mSlidingTabLayout.setDistributeEvenly(true);

        mSlidingTabLayout.setViewPager(mPlugInUnitViewPager);
    }

    @Override
    public void onPullToRefresh() {

    }

    @Override
    public void onRefreshAble(boolean refreshAble) {

    }

    @Override
    public void onRefreshCompletedDone() {

    }
}
