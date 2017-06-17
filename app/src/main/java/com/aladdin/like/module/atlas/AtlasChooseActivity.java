package com.aladdin.like.module.atlas;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.atlas.adapter.ChooseAdapter;
import com.aladdin.like.module.atlas.contract.AtlasContract;
import com.aladdin.like.module.atlas.presenter.AtlasPresenter;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.widget.CustomGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 感兴趣的图集选择
 * Created by zxl on 2017/4/28 下午5:39.
 */
public class AtlasChooseActivity extends BaseActivity implements AtlasContract.View{

    @BindView(R.id.atlas_choose)
    CustomGridView mAtlasChoose;
    @BindView(R.id.enter)
    ImageView mEnter;

    AtlasContract.Presenter mPresenter;
    ChooseAdapter mAdapter;

    List<ThemeModes.Theme> mChoose = new ArrayList<>();//存放选中的图集
    List<ThemeModes.Theme> mPicturePojo = new ArrayList<>();
    List<String> mChooseId = new ArrayList<>();
    ThemeModes.Theme mTheme;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_atlas_choose;
    }

    @Override
    protected void initView() {
        mPresenter = new AtlasPresenter(this);
        mPresenter.loadData(LikeAgent.getInstance().getUid(),"");
        showLoading();

        mAdapter = new ChooseAdapter(AtlasChooseActivity.this);
        mAdapter.setChoose(mChoose);
        mAtlasChoose.setAdapter(mAdapter);

        mAtlasChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mChoose.contains(mPicturePojo.get(position))){
                    mChoose.remove(mPicturePojo.get(position));
                    mChooseId.remove(mPicturePojo.get(position).themeId);
                }else{
                    mChoose.add(mPicturePojo.get(position));
                    mChooseId.add(mPicturePojo.get(position).themeId);
                }

                mAdapter.setChoose(mChoose);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick(R.id.enter)
    public void onViewClicked() {
        mPresenter.addUserTheme(LikeAgent.getInstance().getUid(),mChooseId,1);
        startThenKill(MainActivity.class);

//        startActivity(TestActivity.class);
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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopLoading();
                showErrorHint(msg);
            }
        });

    }

    @Override
    public void setData(ThemeModes data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stopLoading();
                if (data != null && data.themeList != null){
                    mPicturePojo.addAll(data.themeList);
                    mAdapter.setAtlas(mPicturePojo);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    public void addThemeSuc() {
        startActivity(MainActivity.class);
    }
}
