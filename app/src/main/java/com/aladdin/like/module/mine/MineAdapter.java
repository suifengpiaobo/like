package com.aladdin.like.module.mine;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.aladdin.like.module.mine.atlas.MineAtlasFragment;
import com.aladdin.like.module.mine.diary.MineDiraryFragment;
import com.aladdin.like.module.mine.pictures.MinePictureFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/5/24 下午8:39.
 * Email:444288256@qq.com
 */
public class MineAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{
    private static final String THEME = "主题";
    private static final String PICTURE = "图片";
    private static final String DIARY = "日记";
    private static List<String> titles = new ArrayList<>();

    private Fragment ft;

    MineAtlasFragment mAtlasFragment;
    MinePictureFragment mPictureFragment;
    MineDiraryFragment mDiraryFragment;

    public MineAdapter(FragmentManager fm) {
        super(fm);
        titles.add(THEME);
        titles.add(PICTURE);
        titles.add(DIARY);
    }

    @Override
    public Fragment getItem(int position) {
        switch (titles.get(position)) {
            case THEME:
                if (mAtlasFragment == null){
                    mAtlasFragment = new MineAtlasFragment();
                }
                ft = mAtlasFragment;
                break;
            case PICTURE:
                if (mPictureFragment == null){
                    mPictureFragment = new MinePictureFragment();
                }
                ft = mPictureFragment;
                break;
            case DIARY:
                if (mDiraryFragment == null){
                    mDiraryFragment = new MineDiraryFragment();
                }
                ft = mDiraryFragment;
                break;
        }
        return ft;
    }

    @Override
    public int getCount() {
        return titles == null ? 0:titles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
