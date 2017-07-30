package com.aladdin.like;

import android.support.annotation.CheckResult;
import android.text.TextUtils;

import com.aladdin.like.constant.Constant;
import com.aladdin.like.constant.LoginType;
import com.aladdin.like.constant.SharedPreferencesManager;
import com.aladdin.like.model.LoginStateEvent;
import com.aladdin.like.model.User2Pojo;
import com.aladdin.like.model.UserPojo;
import com.aladdin.like.module.register.entity.WeiXinResult;
import com.aladdin.like.utils.LoginHttpRequestUtil;
import com.aladdin.like.utils.UserSettingHelper;
import com.aladdin.utils.GsonUtils;
import com.aladdin.utils.LogUtil;
import com.aladdin.utils.SharedPreferencesUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description 用户控制类
 * Created by zxl on 2017/6/8 下午7:53.
 * Email:444288256@qq.com
 */
public class LikeAgent {
    private final static String TAG = LikeAgent.class.getSimpleName();
    public static LoginType signInState;
    public static Map<String, UserPojo> userCache = new ConcurrentHashMap<>();

    public static volatile LikeAgent instance;

    private volatile boolean authed = false;//是否认证过

    private volatile String code;//微信授权时返回的code

    private String uid;
    private String openid;
    private volatile UserPojo mUserPojo;
    private volatile WeiXinResult weiXinpojo;

    private LikeAgent() {
    }

    public static LikeAgent getInstance() {
        if (instance == null) {
            synchronized (LikeAgent.class) {
                if (instance == null) {
                    instance = new LikeAgent();
                }
            }
        }
        return instance;
    }

    public void setCode(String code){
        SharedPreferencesUtil.INSTANCE.putString(Constant.User.CODE, code);
    }

    public String getCode(){
        String code = SharedPreferencesUtil.INSTANCE.getString(Constant.User.CODE,"");
        return code;
    }

    /**
     * 获取认证状态
     *
     * @return 是否认证成功
     */
    public boolean isAuthed() {
        return authed;
    }

    /**
     * 保存认证状态
     *
     * @param authed 是否认证成功
     */
    public void setAuthed(boolean authed) {
        this.authed = authed;
    }

    public void setUid(String uid) {
        this.uid = uid;
        SharedPreferencesUtil.INSTANCE.putString(Constant.User.LAST_ID, uid);
    }

    public String getUid() {
        //防止程序异常 uid返回null的情况
        if (TextUtils.isEmpty(uid)) {
            UserPojo userPojo = getUserPojo();
            if (null != userPojo) {
                return userPojo.userId;
            }
        }
        return uid;
//
//        uid = SharedPreferencesUtil.INSTANCE.getString(Constant.User.LAST_ID, "");
//        return uid;
    }

    public String getOpenid(){
        String userPojo = SharedPreferencesUtil.INSTANCE.getString(Constant.User.USER_INFO, null);
        if (!TextUtils.isEmpty(userPojo)){
            this.mUserPojo = GsonUtils.jsonToObject(userPojo, UserPojo.class);
            openid = mUserPojo.openid;
        }else{
            openid = "";
        }
        return openid;
    }

    @CheckResult
    public UserPojo getUserPojo() {
        if (mUserPojo == null) {
            String userPojo = SharedPreferencesUtil.INSTANCE.getString(Constant.User.USER_INFO, null);
            if (!TextUtils.isEmpty(userPojo)) {
                this.mUserPojo = GsonUtils.jsonToObject(userPojo, UserPojo.class);
            }
        }
        return mUserPojo;
    }

    public synchronized void saveUserInfo(UserPojo user) {
        if (null != user) {
            SharedPreferencesUtil.INSTANCE.putString(Constant.User.USER_INFO, GsonUtils.objectToJson(user));
        }
    }

    public synchronized void saveWeiXinInfo(WeiXinResult weiXin) {
        if (null != weiXin) {
            SharedPreferencesUtil.INSTANCE.putString(Constant.User.WEIXIN_INFO, GsonUtils.objectToJson(weiXin));
        }
    }

    @CheckResult
    public WeiXinResult getWeiXinPojo() {
        if (weiXinpojo == null) {
            String weiXinpojo = SharedPreferencesUtil.INSTANCE.getString(Constant.User.WEIXIN_INFO, null);
            if (!TextUtils.isEmpty(weiXinpojo)) {
                this.weiXinpojo = GsonUtils.jsonToObject(weiXinpojo, WeiXinResult.class);
            }
        }
        return weiXinpojo;
    }

    // 必须只在更新个人资料或者其它特殊情况的时候用
    public synchronized void updateUserInfo(UserPojo user) {
        saveUserInfo(user);
        mUserPojo = user;
    }

    // 必须只在注销或者其它特殊情况的时候用
    public synchronized void clearUserInfo() {
        SharedPreferencesUtil.INSTANCE.remove(Constant.User.USER_INFO);
        mUserPojo = null;
    }

    public synchronized void clearWeiXinInfo() {
        SharedPreferencesUtil.INSTANCE.remove(Constant.User.WEIXIN_INFO);
        weiXinpojo = null;
    }

    /**
     * 微信登陆
     *
     * @param accessToken
     * @param refreshToken
     * @param openId
     */
    public void weixinLogin(String accessToken, String refreshToken, String openId) {
        WeiXinLoginRequtst("weixin", accessToken, refreshToken);
        //  singleThreadExecutor.execute(new WeixinLoginHandler(accessToken, refreshToken, openId, isBackground));
    }

    private void WeiXinLoginRequtst(String provider, String accessToken, String refreshToken) {

        LoginHttpRequestUtil.goWeiXinLogin(provider, accessToken, refreshToken, new HttpResultCallback<User2Pojo>() {
            @Override
            public void onSuccess(User2Pojo result) {
//                if (provider.equals("weibo")) {
//                    loginResult(LoginType.WEIBO, result);
//                } else {
                    loginResult(LoginType.WEIXIN, result);
//                }
            }

            @Override
            public void onFailure(String code, String msg) {
                LogUtil.i("");
            }
        });
    }

    private  void loginResult(LoginType state, User2Pojo userPojo) {
        if (userPojo != null) {
            getInstance().saveUserInfo(userPojo.user);
            getInstance().setUid(userPojo.uid);
            SharedPreferencesManager.setLoginState(state);
            UserSettingHelper.getInstance().setMauth(userPojo.user.getMauth());
            getInstance().setAuthed(true);
            signInState = state;

            EventBus.getDefault().post(new LoginStateEvent(LoginStateEvent.SUCCESS));
        }

    }
}
