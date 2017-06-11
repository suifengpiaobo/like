package com.aladdin.like.widget.loadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Description 下拉刷新的listView
 * Created by zxl on 2017/6/7 下午8:13.
 * Email:444288256@qq.com
 */
public class LoadListView extends ListView implements AbsListView.OnScrollListener{
    private Context context;
    LoadListViewCallBack mLoadListViewCallBack;
    /**
     * 记录第一行Item的数值
     */
    private int firstVisibleItem;

    private View mFootView;

    public LoadListView(Context context) {
        super(context);
        this.context = context;
        initListView();
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initListView();
    }

    /**
     * 初始化ListView
     */
    private void initListView() {

        // 为ListView设置滑动监听
        setOnScrollListener(this);
        // 去掉底部分割线
        setFooterDividersEnabled(false);

        LoadingListViewMoreFooter footView = new LoadingListViewMoreFooter(getContext());
        mFootView = footView;
        mFootView.setVisibility(GONE);
        initBottomView();
    }

    /**
     * 初始化话底部页面
     */
    public void initBottomView() {

        addFooterView(mFootView);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当滑动到底部时
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && firstVisibleItem != 0) {
            mLoadListViewCallBack.scrollBottomState();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    public void loadMoreComplete() {
        ((LoadingListViewMoreFooter) mFootView).setState(LoadingListViewMoreFooter.STATE_COMPLETE);
    }

    public void setNoMore(boolean noMore) {
        ((LoadingListViewMoreFooter) mFootView).setState(noMore ? LoadingListViewMoreFooter.STATE_NOMORE : LoadingListViewMoreFooter.STATE_COMPLETE);
    }

    public void setLoadMore() {
        ((LoadingListViewMoreFooter) mFootView).setState(LoadingListViewMoreFooter.STATE_LOADING);
    }

    public LoadListViewCallBack getLoadListViewCallBack() {
        return mLoadListViewCallBack;
    }

    public void setLoadListViewCallBack(LoadListViewCallBack loadListViewCallBack) {
        mLoadListViewCallBack = loadListViewCallBack;
    }

    public interface LoadListViewCallBack{
        void scrollBottomState();
    }
}
