package com.aladdin.like.module.mine.pictures;


import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 我的 图片
 * Created by zxl on 2017/5/20 下午8:05.
 */
public class MinePictureFragment extends BaseFragment {

    @BindView(R.id.picture_recycleview)
    RecyclerView mPictureRecycleview;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    String[] mName = {"日韩美图","微信素材","另类图集","美食图集","欧美情侣",
            "运动名将","奢侈生活","轻松搞笑","夜生活","专辑封面"};
    PrefecturePojo.Prefecture mResultPrefecture;

    PictureAdapter mPictureAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_picture;
    }

    @Override
    protected void initView() {
        for (int i = 0; i< 10;i++){
            mResultPrefecture = new PrefecturePojo.Prefecture();
            mResultPrefecture.typeName = mName[i];
            mPrefectures.add(mResultPrefecture);
        }

        StaggeredGridLayoutManager staggered = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mPictureAdapter = new PictureAdapter(getActivity());
        mPictureRecycleview.setLayoutManager(staggered);
        mPictureRecycleview.addItemDecoration(new SpacesItemDecoration(10));
        mPictureRecycleview.setAdapter(mPictureAdapter);
        mPictureAdapter.addAll(mPrefectures);
    }

    @Override
    protected void lazyFetchData() {

    }
}
