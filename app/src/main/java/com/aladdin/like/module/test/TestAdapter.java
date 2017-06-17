package com.aladdin.like.module.test;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/6/16 下午3:05.
 * Email:444288256@qq.com
 */
public class TestAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener{
    private static List<String> titles;

    public TestAdapter(FragmentManager fm) {
        super(fm);
        titles = new ArrayList<>();

    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
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
