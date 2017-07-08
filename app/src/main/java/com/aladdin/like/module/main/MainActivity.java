package com.aladdin.like.module.main;


import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.base.BaseFragmentAdapter;
import com.aladdin.like.module.circle.CircleFragment;
import com.aladdin.like.module.mine.MineFragment2;
import com.aladdin.like.module.search.SearchFragment;
import com.aladdin.like.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 主页
 * Created by zxl on 2017/4/29 下午9:48.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.main_view_pager)
    NoScrollViewPager mMainViewPager;
    @BindView(R.id.main_page)
    ImageView mMainPage;
    @BindView(R.id.search_page)
    ImageView mSearchPadge;
    @BindView(R.id.circle_page)
    ImageView mCirclePage;
    @BindView(R.id.mine_page)
    ImageView mMinePage;
    @BindView(R.id.buttom_ll)
    LinearLayout mLayout;

    private List<BaseFragment> mFragments;
    private MainFragment mMainFragment;
    private SearchFragment mSearchFragment;
    private CircleFragment mCircleFragment;
    private MineFragment2 mMineFragment;
    private BaseFragmentAdapter mAdapter;

    onSearchClickListener mSearchClickListener;
    onCircleClickListener mOnCircleClickListener;

    int currentTabPosition = 0;
    public static final String CURRENT_TAB_POSITION = "HOME_CURRENT_TAB_POSITION";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        resetTab();
        mFragments = new ArrayList<>();
        mMainFragment = new MainFragment();
        mSearchFragment = new SearchFragment();
        mCircleFragment = new CircleFragment();
        mMineFragment = new MineFragment2();

        mFragments.add(mMainFragment);
        mFragments.add(mSearchFragment);
        mFragments.add(mCircleFragment);
        mFragments.add(mMineFragment);

        mAdapter = new BaseFragmentAdapter(getSupportFragmentManager(),mFragments);
        mMainViewPager.setAdapter(mAdapter);
        mMainViewPager.setCurrentItem(currentTabPosition);
        mLayout.setBackgroundColor(getResources().getColor(R.color.color_e6ffffff));
        mMainPage.setSelected(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //奔溃前保存位置
        outState.putInt(CURRENT_TAB_POSITION, currentTabPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentTabPosition = savedInstanceState.getInt(CURRENT_TAB_POSITION);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void resetTab() {
        mMainPage.setSelected(false);
        mSearchPadge.setSelected(false);
        mCirclePage.setSelected(false);
        mMinePage.setSelected(false);
    }

    @OnClick({R.id.main_page, R.id.search_page, R.id.circle_page, R.id.mine_page})
    public void onViewClicked(View view) {
        resetTab();
        switch (view.getId()) {
            case R.id.main_page:
                currentTabPosition = 0;
                mMainPage.setSelected(true);
                break;
            case R.id.search_page:
                currentTabPosition = 1;
                mSearchPadge.setSelected(true);
                break;
            case R.id.circle_page:
                currentTabPosition = 2;
                mCirclePage.setSelected(true);
                break;
            case R.id.mine_page:
                currentTabPosition = 3;
                mMinePage.setSelected(true);
                break;
            default:
                mLayout.setBackgroundColor(getResources().getColor(R.color.color_e6ffffff));
                break;
        }
        mMainViewPager.setCurrentItem(currentTabPosition, false);
    }

    public void setSearchClickListener(onSearchClickListener searchClickListener) {
        mSearchClickListener = searchClickListener;
    }

    public void setOnCircleClickListener(onCircleClickListener onCircleClickListener) {
        mOnCircleClickListener = onCircleClickListener;
    }

    public interface onSearchClickListener{
        void onSearch(String s);
    }

    public interface onCircleClickListener{
        void onClick();
    }

    //隐藏键盘
    protected boolean hiddenInputMethodManager(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    //点击空白隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final View v = this.getWindow().peekDecorView();
        return hiddenInputMethodManager(v);
    }
}
