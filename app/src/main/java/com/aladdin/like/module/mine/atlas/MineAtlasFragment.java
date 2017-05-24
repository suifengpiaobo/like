package com.aladdin.like.module.mine.atlas;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.AtlasPicturePojo;
import com.aladdin.like.module.mine.atlas.adapter.AtlasAdapter;
import com.aladdin.like.module.mine.atlas.adapter.AtlasNotChooseAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 我的 主题
 * Created by zxl on 2017/5/20 下午7:26.
 */
public class MineAtlasFragment extends BaseFragment {

    @BindView(R.id.my_choose_atlas)
    RecyclerView mMyChooseAtlas;
    @BindView(R.id.my_not_atlas)
    RecyclerView mMyNotAtlas;

    AtlasAdapter mAtlasAdapter;
    AtlasNotChooseAdapter mNotChooseAdapter;

    //主题
    List<AtlasPicturePojo.AtlasPicture> mChooseAtlas = new ArrayList<>();
    String[] name = {"日韩美图","微信素材","另类图集","美食图集","健美图片","欧美情侣",
            "运动名将","奢侈生活","电影明星","轻松搞笑","夜生活","专辑封面"};
    AtlasPicturePojo.AtlasPicture picture;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_atlas;
    }

    @Override
    protected void initView() {

        for (int i = 0; i< 12;i++){
            picture = new AtlasPicturePojo.AtlasPicture();
            picture.name = name[i];
            mChooseAtlas.add(picture);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMyChooseAtlas.setLayoutManager(linearLayoutManager);
        mAtlasAdapter = new AtlasAdapter(getActivity());
        mMyChooseAtlas.setAdapter(mAtlasAdapter);
        mAtlasAdapter.addAll(mChooseAtlas);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMyNotAtlas.setLayoutManager(linearLayoutManager2);
        mNotChooseAdapter = new AtlasNotChooseAdapter(getActivity());
        mMyNotAtlas.setAdapter(mNotChooseAdapter);
        mNotChooseAdapter.addAll(mChooseAtlas);
    }

    @Override
    protected void lazyFetchData() {

    }
}
