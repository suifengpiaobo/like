package com.aladdin.like.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.aladdin.utils.LogUtil;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/4/27 下午5:29.
 * Email:444288256@qq.com
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private FragmentManager fm;

    List<BaseFragment> fragmentList;
    private String[] mTitles;

    public BaseFragmentAdapter(FragmentManager fm,List<BaseFragment> fragmentList) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
        LogUtil.i("fragmentList--->>>"+fragmentList);
    }

    public BaseFragmentAdapter(FragmentManager fm, List<BaseFragment> fragmentList, String[] mTitles) {
        super(fm);
        this.fm = fm;
        this.fragmentList = fragmentList;
        this.mTitles = mTitles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles == null ? "" : mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        if(fragmentList == null) return 0;
        return fragmentList.size();
    }

    /**
     * 这边并没有创建销毁过程，只创建一次
     * @param container
     * @param position
     * @return
     */
    @Override
    public Fragment instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        fm.beginTransaction().show(fragment).commit();
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Fragment fragment = fragmentList.get(position);
        fm.beginTransaction().hide(fragment).commit();
    }
}
