package com.aladdin.like.module.mine.atlas;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.mine.atlas.adapter.AtlasAdapter;
import com.aladdin.like.module.mine.atlas.adapter.AtlasNotChooseAdapter;
import com.aladdin.like.module.mine.atlas.contract.MineThemeContract;
import com.aladdin.like.module.mine.atlas.prestener.MineThemePrestener;
import com.aladdin.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description 我的 主题
 * Created by zxl on 2017/5/20 下午7:26.
 */
public class MineAtlasFragment extends BaseFragment implements MineThemeContract.View{
    MineThemeContract.Presenter mPresenter;

    @BindView(R.id.my_choose_atlas)
    RecyclerView mMyChooseAtlas;
    @BindView(R.id.my_not_atlas)
    RecyclerView mMyNotAtlas;

    AtlasAdapter mAtlasAdapter;
    AtlasNotChooseAdapter mNotChooseAdapter;

    List<ThemeModes.Theme> mFollowThemes = new ArrayList<>();
    List<ThemeModes.Theme> mNotFollowThemes = new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_atlas;
    }

    @Override
    protected void initView() {
        mPresenter = new MineThemePrestener(this);
        mPresenter.getTheme(LikeAgent.getInstance().getUid());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMyChooseAtlas.setLayoutManager(linearLayoutManager);
        mAtlasAdapter = new AtlasAdapter(getActivity());
        mMyChooseAtlas.setAdapter(mAtlasAdapter);


        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMyNotAtlas.setLayoutManager(linearLayoutManager2);
        mNotChooseAdapter = new AtlasNotChooseAdapter(getActivity());
        mMyNotAtlas.setAdapter(mNotChooseAdapter);

        bindEvent();
    }

    private void bindEvent() {
        mAtlasAdapter.setItemClickListener(new AtlasAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ThemeModes.Theme item) {
                List<String> tempList = new ArrayList<String>();
                tempList.add(item.themeId);
                mPresenter.addUserTheme(LikeAgent.getInstance().getUid(),tempList,2);
                tempList.clear();
            }
        });

        mNotChooseAdapter.setItemClickListener(new AtlasNotChooseAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ThemeModes.Theme item) {
                List<String> tempList = new ArrayList<String>();
                tempList.add(item.themeId);
                mPresenter.addUserTheme(LikeAgent.getInstance().getUid(),tempList,1);
                tempList.clear();
            }
        });
    }

    @Override
    protected void lazyFetchData() {

    }

    @Override
    public void showLoading() {
        startProgressDialog();
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        if (getActivity() == null){
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(msg);
                showToast(msg);
            }
        });
    }

    @Override
    public void setThemeData(ThemeModes theme) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (theme != null){
                    for (ThemeModes.Theme themes : theme.themeList){
                        if (themes.followSign == 1){
                            mFollowThemes.add(themes);
                        }else{
                            mNotFollowThemes.add(themes);
                        }
                    }
                    if (mFollowThemes != null && mFollowThemes.size()>0){
                        mAtlasAdapter.addAll(mFollowThemes);
                        mAtlasAdapter.notifyDataSetChanged();
                    }
                    if (mNotFollowThemes != null && mNotFollowThemes.size()>0){
                        mNotChooseAdapter.addAll(mNotFollowThemes);
                        mNotChooseAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

    @Override
    public void addThemeSuc() {

    }
}
