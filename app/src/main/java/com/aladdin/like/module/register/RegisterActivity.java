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
import com.aladdin.like.constant.Constant;
import com.aladdin.like.module.atlas.AtlasChooseActivity;
import com.aladdin.like.module.login.LoginAccountActivity;
import com.aladdin.like.wxapi.WXEntryActivity;
import com.aladdin.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 登录
 * Created by zxl on 2017/6/3 下午7:07.
 */
public class RegisterActivity extends BaseActivity {
    public final static String WCHAT_APPID = WXEntryActivity.WX_APPID;
    public final static String SCOPE = "snsapi_userinfo,snsapi_base";
    public final static String STATE = "wechat_sdk_demo_test";
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
//                loginWx();
                break;
            case R.id.register_account:
                startActivity(LoginAccountActivity.class);
                break;
            case R.id.login_wx_login:
                if (mCurrent == 1){
                    loginWx();
                }
                break;
        }
    }

    public void loginWx(){
        IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), WCHAT_APPID);
        api.registerApp(WCHAT_APPID);

        if (!api.isWXAppInstalled()) {
            ToastUtil.sToastUtil.shortDuration("未安装微信客户端");
            return;
        }

        if (!api.isWXAppSupportAPI()) {
            ToastUtil.sToastUtil.shortDuration("当前的微信版本太低，请先更新微信");
            return;
        }

        Constant.IS_AUTH_WCHAT = true;

        startProgressDialog();
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = SCOPE;
        req.state = STATE;
        api.sendReq(req);
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
