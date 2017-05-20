package com.aladdin.like.module.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.main.adapter.MainAdapter;
import com.aladdin.like.module.search.adapter.HorizontalAdapter;
import com.aladdin.like.widget.ExStaggeredGridLayoutManager;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 搜索
 * Created by zxl on 2017/4/28 下午2:56.
 */
public class SearchFragment extends BaseFragment {

    RecyclerView mSearchHorizontal;
    TextView mSearchTip;

    @BindView(R.id.search_result)
    XRecyclerView mSearchResult;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    HorizontalAdapter mHorizontalAdapter;

    MainAdapter mAdapter;
    List<PrefecturePojo.Prefecture> mSearchPrefectures = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {

        for (int i = 0;i<5;i++){
            PrefecturePojo.Prefecture prefecture = new PrefecturePojo.Prefecture();
            prefecture.typeName = "欧美图集"+i;
            mPrefectures.add(prefecture);
        }
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.layout_search_header, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
        mSearchResult.addHeaderView(header);
        mSearchHorizontal = (RecyclerView) header.findViewById(R.id.search_horizontal);
        mSearchTip = (TextView) header.findViewById(R.id.search_tip);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSearchHorizontal.setLayoutManager(linearLayoutManager);
        mHorizontalAdapter = new HorizontalAdapter(getActivity());
        mHorizontalAdapter.addAll(mPrefectures);
        Log.e("SearchFragment",""+mPrefectures.size());
        mSearchHorizontal.setAdapter(mHorizontalAdapter);
        mHorizontalAdapter.notifyDataSetChanged();


        mAdapter = new MainAdapter(getActivity());
        mSearchResult.setAdapter(mAdapter);

        if (mAdapter != null && mAdapter.getItemCount() > 0) {
            mAdapter.clear();
        }
        for (int i = 0; i < 20; i++) {
            PrefecturePojo.Prefecture prefecture = new PrefecturePojo.Prefecture();
            prefecture.time = 10192;
            prefecture.typeName = "健身美图" + i;

            mPrefectures.add(prefecture);
        }

        ExStaggeredGridLayoutManager staggered = new ExStaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mSearchResult.setLayoutManager(staggered);
        mAdapter.addAll(mPrefectures);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void lazyFetchData() {

    }
}
