package com.aladdin.like.module.mine;


import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.module.mine.atlas.MineAtlasFragment;
import com.aladdin.like.module.mine.diary.MineDiraryFragment;
import com.aladdin.like.module.mine.pictures.MinePictureFragment;
import com.aladdin.like.module.set.SettingActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 我的
 * Created by zxl on 2017/4/28 下午1:53.
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.user_avatar)
    SimpleDraweeView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.mine_title)
    RelativeLayout mMineTitle;
    //    @BindView(R.id.mine_info_viewpager)
//    ScrollViewPager mViewpager;
    @BindView(R.id.set)
    ImageView mSet;

//    List<BaseFragment> mFragments;
    MineAtlasFragment mAtlasFragment;
    MinePictureFragment mPictureFragment;
    MineDiraryFragment mDiraryFragment;
//    BaseFragmentAdapter mAdapter;

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
        setMineTitle();
        initAtlasFragment();
//        mFragments = new ArrayList<>();
//        mAtlasFragment = new MineAtlasFragment();
//        mPictureFragment = new MinePictureFragment();
//        mDiraryFragment = new MineDiraryFragment();
//        mFragments.add(mAtlasFragment);
//        mFragments.add(mPictureFragment);
//        mFragments.add(mDiraryFragment);

//        mAdapter = new BaseFragmentAdapter(getFragmentManager(), mFragments);
//        mViewpager.setAdapter(mAdapter);

//        mViewpager.setCurrentItem(mCurrent);
//        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                mCurrent = position;
//                setMineTitle();
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }

    @Override
    protected void lazyFetchData() {

    }

    void setMineTitle() {
        mMineAtlas.setTextColor(getResources().getColor(R.color.Gray));
        mMinePicture.setTextColor(getResources().getColor(R.color.Gray));
        mMineDirary.setTextColor(getResources().getColor(R.color.Gray));
        if (mCurrent == 0) {
            mMineAtlas.setTextColor(getResources().getColor(R.color.Red));
        } else if (mCurrent == 1) {
            mMinePicture.setTextColor(getResources().getColor(R.color.Red));
        } else if (mCurrent == 2) {
            mMineDirary.setTextColor(getResources().getColor(R.color.Red));
        }
    }

    @OnClick({R.id.mine_atlas, R.id.mine_picture, R.id.mine_dirary, R.id.set})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_atlas:
                mCurrent = 0;
//                mViewpager.setCurrentItem(mCurrent);
                setMineTitle();
                initAtlasFragment();
                break;
            case R.id.mine_picture:
                mCurrent = 1;
//                mViewpager.setCurrentItem(mCurrent);
                setMineTitle();
                initPictureFragment();
                break;
            case R.id.mine_dirary:
                mCurrent = 2;
//                mViewpager.setCurrentItem(mCurrent);
                setMineTitle();
                initMineDiraryFragment();
                break;
            case R.id.set:
                startActivity(SettingActivity.class);
                break;
        }
    }

    private void initAtlasFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (mAtlasFragment == null){
            mAtlasFragment = new MineAtlasFragment();
            transaction.add(R.id.mine_fragment,mAtlasFragment);
        }
        hideFragment(transaction);
        transaction.show(mAtlasFragment);
        transaction.commit();
    }

    private void initPictureFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (mPictureFragment == null){
            mPictureFragment = new MinePictureFragment();
            transaction.add(R.id.mine_fragment,mPictureFragment);
        }
        hideFragment(transaction);
        transaction.show(mPictureFragment);
        transaction.commit();
    }

    private void initMineDiraryFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (mDiraryFragment == null){
            mDiraryFragment = new MineDiraryFragment();
            transaction.add(R.id.mine_fragment,mDiraryFragment);
        }
        hideFragment(transaction);
        transaction.show(mDiraryFragment);
        transaction.commit();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction){
        if(mAtlasFragment != null){
            transaction.hide(mAtlasFragment);
        }
        if(mPictureFragment != null){
            transaction.hide(mPictureFragment);
        }
        if(mDiraryFragment != null){
            transaction.hide(mDiraryFragment);
        }
    }

}
