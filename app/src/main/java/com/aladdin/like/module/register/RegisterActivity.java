package com.aladdin.like.module.register;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.like.module.atlas.AtlasChooseActivity;
import com.aladdin.like.module.login.LoginAccountActivity;
import com.aladdin.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.wx_login)
    TextView mWxLogin;
    @BindView(R.id.register_account)
    TextView mRegisterAccount;
    @BindView(R.id.register_buttom)
    LinearLayout mRegisterButtom;

    RegisterLoginAdapter mAdapter;

    RegisterFragment mRegisterFragment;
    RegisterLoginFragment mRegisterLoginFragment;
    private List<Fragment> mList = new ArrayList<>();
    private int mCurrent = 0;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        mRegisterFragment = new RegisterFragment();
        mRegisterLoginFragment = new RegisterLoginFragment();
        mList.add(mRegisterFragment);
        mList.add(mRegisterLoginFragment);

        mAdapter = new RegisterLoginAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(new RegisterLoginAdapter(getSupportFragmentManager(), mList));

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrent = position;
                if (position == 0) {
                    mRegisterButtom.setVisibility(View.VISIBLE);
                } else {
                    mRegisterButtom.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.wx_login, R.id.register_account,R.id.login_wx_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wx_login:
                startActivity(AtlasChooseActivity.class);
                break;
            case R.id.register_account:
//                ToastUtil.sToastUtil.shortDuration("注册账号").show();
                startActivity(LoginAccountActivity.class);
                break;
            case R.id.login_wx_login:
                if (mCurrent == 1){
                    ToastUtil.sToastUtil.shortDuration("微信登录").show();
                }
                break;
        }
    }

    public class RegisterLoginAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> mList;

        public RegisterLoginAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mList = list;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
