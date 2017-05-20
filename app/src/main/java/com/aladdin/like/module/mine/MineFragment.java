package com.aladdin.like.module.mine;


import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.base.BaseFragmentAdapter;
import com.aladdin.like.R;
import com.aladdin.like.module.mine.atlas.MineAtlasFragment;
import com.aladdin.like.module.mine.diary.MineDiraryFragment;
import com.aladdin.like.module.mine.pictures.MinePictureFragment;
import com.aladdin.like.module.set.SettingActivity;
import com.aladdin.like.widget.ScrollViewPager;
import com.aladdin.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 我的
 * Created by zxl on 2017/4/28 下午1:53.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.user_avatar)
    CircleImageView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.mine_title)
    RelativeLayout mMineTitle;
    @BindView(R.id.mine_info_viewpager)
    ScrollViewPager mViewpager;
    @BindView(R.id.set)
    ImageView mSet;

    List<BaseFragment> mFragments;
    MineAtlasFragment mAtlasFragment;
    MinePictureFragment mPictureFragment;
    MineDiraryFragment mDiraryFragment;

    BaseFragmentAdapter mAdapter;

    int mCurrent = 0;
    @BindView(R.id.mine_atlas)
    TextView mMineAtlas;
    @BindView(R.id.mine_picture)
    TextView mMinePicture;
    @BindView(R.id.mine_dirary)
    TextView mMineDirary;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        mFragments = new ArrayList<>();
        mAtlasFragment = new MineAtlasFragment();
        mPictureFragment = new MinePictureFragment();
        mDiraryFragment = new MineDiraryFragment();
        mFragments.add(mAtlasFragment);
        mFragments.add(mPictureFragment);
        mFragments.add(mDiraryFragment);

        mAdapter = new BaseFragmentAdapter(getFragmentManager(), mFragments);
        mViewpager.setAdapter(mAdapter);

        mViewpager.setCurrentItem(mCurrent);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mCurrent = position;
                setMineTitle();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void lazyFetchData() {

    }

    void setMineTitle(){
        mMineAtlas.setTextColor(getResources().getColor(R.color.Gray));
        mMinePicture.setTextColor(getResources().getColor(R.color.Gray));
        mMineDirary.setTextColor(getResources().getColor(R.color.Gray));
        if (mCurrent == 0){
            mMineAtlas.setTextColor(getResources().getColor(R.color.Red));
        }else if (mCurrent == 1){
            mMinePicture.setTextColor(getResources().getColor(R.color.Red));
        }else if (mCurrent == 2){
            mMineDirary.setTextColor(getResources().getColor(R.color.Red));
        }
    }
    @OnClick({R.id.mine_atlas, R.id.mine_picture, R.id.mine_dirary,R.id.set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_atlas:
                mCurrent = 0;
                setMineTitle();
                break;
            case R.id.mine_picture:
                mCurrent = 1;
                setMineTitle();
                break;
            case R.id.mine_dirary:
                mCurrent = 2;
                setMineTitle();
                break;
            case R.id.set:
                startActivity(SettingActivity.class);
                break;
        }
    }
}
