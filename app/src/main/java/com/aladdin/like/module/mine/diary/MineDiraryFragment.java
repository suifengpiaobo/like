package com.aladdin.like.module.mine.diary;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 我的  日记
 * Created by zxl on 2017/5/20 下午8:04.
 */
public class MineDiraryFragment extends BaseFragment {

    @BindView(R.id.mine_diary_viewpager)
    RecyclerView mMineDiaryViewpager;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    String[] mName = {"日韩美图","微信素材","另类图集","美食图集","欧美情侣",
            "运动名将","奢侈生活","轻松搞笑","夜生活","专辑封面"};
    PrefecturePojo.Prefecture mResultPrefecture;

    MineDiaryAdapter mDiaryAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_dirary;
    }

    @Override
    protected void initView() {
        for (int i = 0; i< 10;i++){
            mResultPrefecture = new PrefecturePojo.Prefecture();
            mResultPrefecture.typeName = mName[i];
            mPrefectures.add(mResultPrefecture);
        }

        GridLayoutManager staggered = new GridLayoutManager(getActivity(), 2);
        mDiaryAdapter = new MineDiaryAdapter(getActivity());
        mMineDiaryViewpager.setLayoutManager(staggered);
        mMineDiaryViewpager.addItemDecoration(new SpacesItemDecoration(10));
        mMineDiaryViewpager.setAdapter(mDiaryAdapter);
        mDiaryAdapter.addAll(mPrefectures);
    }

    @Override
    protected void lazyFetchData() {

    }
}
