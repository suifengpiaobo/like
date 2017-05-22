package com.aladdin.like.module.search;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.EditText;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.module.search.adapter.HorizontalAdapter;
import com.aladdin.like.module.search.adapter.SearchResultAdapter;
import com.aladdin.like.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 搜索
 * Created by zxl on 2017/4/28 下午2:56.
 */
public class SearchFragment extends BaseFragment {
    @BindView(R.id.search_horizontal)
    RecyclerView mSearchHorizontal;
    @BindView(R.id.search_result)
    RecyclerView mSearchResult;
    @BindView(R.id.search)
    EditText mSearch;

    List<PrefecturePojo.Prefecture> mSearchPrefectures = new ArrayList<>();
    String[] name = {"日韩美图","微信素材","另类图集","美食图集","健美图片","欧美情侣",
            "运动名将","奢侈生活","电影明星","轻松搞笑","夜生活","专辑封面"};
    PrefecturePojo.Prefecture mPrefecture;
    HorizontalAdapter mHorizontalAdapter;

    List<PrefecturePojo.Prefecture> mResultPrefectures = new ArrayList<>();
    String[] mResultName = {"日韩美图","微信素材","另类图集","美食图集","欧美情侣",
            "运动名将","奢侈生活","轻松搞笑","夜生活","专辑封面"};
    PrefecturePojo.Prefecture mResultPrefecture;
    SearchResultAdapter mResultAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView() {
        for (int i = 0; i< 12;i++){
            mPrefecture = new PrefecturePojo.Prefecture();
            mPrefecture.typeName = name[i];
            mSearchPrefectures.add(mPrefecture);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mSearchHorizontal.setLayoutManager(linearLayoutManager);
        mHorizontalAdapter = new HorizontalAdapter(getActivity());
        mSearchHorizontal.setAdapter(mHorizontalAdapter);
        mHorizontalAdapter.addAll(mSearchPrefectures);

        for (int i = 0; i< 10;i++){
            mResultPrefecture = new PrefecturePojo.Prefecture();
            mResultPrefecture.typeName = mResultName[i];
            mResultPrefectures.add(mResultPrefecture);
        }

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mResultAdapter = new SearchResultAdapter(getActivity());
        mSearchResult.setLayoutManager(staggered);
        mSearchResult.addItemDecoration(new SpacesItemDecoration(10));
        mSearchResult.setAdapter(mResultAdapter);
        mResultAdapter.addAll(mResultPrefectures);
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick(R.id.search)
    public void onViewClicked() {
    }
}
