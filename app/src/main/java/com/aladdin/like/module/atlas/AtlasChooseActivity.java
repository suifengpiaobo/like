package com.aladdin.like.module.atlas;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.like.model.AtlasPicturePojo;
import com.aladdin.like.module.atlas.adapter.ChooseAdapter;
import com.aladdin.like.module.atlas.contract.AtlasContract;
import com.aladdin.like.module.atlas.presenter.AtlasPresenter;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.widget.CustomGridView;
import com.aladdin.widget.NormalTitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 感兴趣的图集选择
 * Created by zxl on 2017/4/28 下午5:39.
 */
public class AtlasChooseActivity extends BaseActivity implements AtlasContract.View{

    @BindView(R.id.title)
    NormalTitleBar mTitle;
    @BindView(R.id.atlas_choose)
    CustomGridView mAtlasChoose;
    @BindView(R.id.enter)
    TextView mEnter;

    AtlasContract.Presenter mPresenter;
    List<AtlasPicturePojo.AtlasPicture> mPicturePojo = new ArrayList<>();
    List<AtlasPicturePojo.AtlasPicture> mChoose = new ArrayList<>(); //存放选中的图集
    String[] name = {"日韩美图","微信素材","另类图集","美食图集","健美图片","欧美情侣",
            "运动名将","奢侈生活","电影明星","轻松搞笑","夜生活","专辑封面"};
    AtlasPicturePojo.AtlasPicture picture;
    ChooseAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_atlas_choose;
    }

    @Override
    protected void initView() {
        mTitle.setVisibility(View.VISIBLE);
        mTitle.setBackVisibility(false);

        for (int i = 0; i< 12;i++){
            picture = new AtlasPicturePojo.AtlasPicture();
            picture.name = name[i];
            mPicturePojo.add(picture);

        }

        mAdapter = new ChooseAdapter(AtlasChooseActivity.this,mPicturePojo);
        mAdapter.setChoose(mChoose);

        mAtlasChoose.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mPresenter = new AtlasPresenter(this);

        mAtlasChoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mChoose.contains(mPicturePojo.get(position))){
                    mChoose.remove(mPicturePojo.get(position));
                }else{
                    mChoose.add(mPicturePojo.get(position));
                }

                mAdapter.setChoose(mChoose);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick(R.id.enter)
    public void onViewClicked() {
        startActivity(MainActivity.class);
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
        showErrorHint(msg);
    }

    @Override
    public void setData(AtlasPicturePojo data) {
        for (int i = 0;i < data.atlas.size();i++){
            mPicturePojo.add(data.atlas.get(i));
        }
        mAdapter.notifyDataSetChanged();
    }
}
