package com.aladdin.like.module.register;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.LoginStateEvent;
import com.aladdin.like.model.UserPojo;
import com.aladdin.like.module.atlas.AtlasChooseActivity;
import com.aladdin.like.module.login.LoginAccountActivity;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.wxapi.WXEntryActivity;
import com.aladdin.utils.ToastUtil;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.experience_tv)
    TextView mExperience;

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
        EventBus.getDefault().register(this);
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

    @OnClick({R.id.wx_login, R.id.register_account, R.id.login_wx_login,R.id.experience_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.wx_login:
                loginWx();
                break;
            case R.id.register_account:
                startThenKill(LoginAccountActivity.class);
                break;
            case R.id.login_wx_login:
                if (mCurrent == 1) {
                    loginWx();
                }
                break;
            case R.id.experience_tv:
                startThenKill(MainActivity.class);

        }
    }

    public void loginWx() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startProgressDialog();
            }
        });
        IWXAPI api = WXAPIFactory.createWXAPI(getApplicationContext(), WCHAT_APPID, true);
        api.registerApp(WCHAT_APPID);
        if (!api.isWXAppInstalled()) {
            ToastUtil.showToast("未安装微信客户端");
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtil.showToast("当前的微信版本太低，请先更新微信");
            stopProgressDialog();
            return;
        }

        Constant.IS_AUTH_WCHAT = true;

        final SendAuth.Req req = new SendAuth.Req();
        req.scope = SCOPE;
        req.state = STATE;
        api.sendReq(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginCancel(LoginStateEvent loginStateEvent){
        if (loginStateEvent.loginState == LoginStateEvent.LOGIN_CANCEL){
            stopProgressDialog();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccess(LoginStateEvent loginStateEvent) {
        if (loginStateEvent.loginState == LoginStateEvent.SUCCESS){
            HttpManager.INSTANCE.login(1, LikeAgent.getInstance().getUserPojo().nickname,
                    LikeAgent.getInstance().getUserPojo().headimgurl, LikeAgent.getInstance().getOpenid(),
                    LikeAgent.getInstance().getUserPojo().unionid, new HttpResultCallback<UserPojo>() {
                        @Override
                        public void onSuccess(UserPojo result) {
                            UserPojo userPojo = LikeAgent.getInstance().getUserPojo();
                            if (!"".equals(result.userId) && result.userId != null) {
                                userPojo.userId = result.userId;
                            }
                            if (!"".equals(result.headimgurl) && result.headimgurl != null) {
                                userPojo.headimgurl = result.headimgurl;
                            }
                            if (!"".equals(result.nickname) && result.nickname != null) {
                                userPojo.nickname = result.nickname;
                            }
                            if (!TextUtils.isEmpty(result.openid)) {
                                userPojo.openid = result.openid;
                            }

                            if (result.IsFirstLogin == 1) {
                                userPojo.IsFirstLogin = 1;
                            } else {
                                userPojo.IsFirstLogin = 0;
                            }
                            LikeAgent.getInstance().updateUserInfo(userPojo);
                            stopProgressDialog();
                            startThenKill(AtlasChooseActivity.class);
                            if (!TextUtils.isEmpty(LikeAgent.getInstance().getOpenid())){
                                XGPushManager.registerPush(getApplicationContext(),LikeAgent.getInstance().getOpenid());
                            }
                        }

                        @Override
                        public void onFailure(String code, String msg) {

                        }
                    });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
