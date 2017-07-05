package com.aladdin.like.http;

import android.text.TextUtils;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.constant.LoginType;
import com.aladdin.like.constant.SharedPreferencesManager;
import com.aladdin.like.model.LoginStateEvent;
import com.aladdin.like.model.UserPojo;
import com.aladdin.like.model.WeiXinFailPojo;
import com.aladdin.like.module.register.entity.WeiXinResult;
import com.aladdin.like.wxapi.WXEntryActivity;
import com.aladdin.utils.GsonUtils;
import com.zxl.network_lib.HttpUtil;
import com.zxl.network_lib.Inteface.HttpResultListener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

/**
 * Description
 * Created by zxl on 2017/6/3 下午7:19.
 * Email:444288256@qq.com
 */
public class WChatHttpClient {
    public static final String WEIXIN_GET_CODE_FROM_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static final String WEIXIN_GET_REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
    public static final String WEIXIN_GET_USERINFO = "https://api.weixin.qq.com/sns/userinfo";

    public static void requestGetWChatToken(String code) {
        String appId = WXEntryActivity.WX_APPID;
        String secret = WXEntryActivity.WX_SECRET;

        //https://api.weixin.qq.com/sns/oauth2/access_token
        // ?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
        String param = WEIXIN_GET_CODE_FROM_TOKEN + "?" +
                HttpParamKey.WeiXinGetToKen.APP_ID + "=" + appId + "&" +
                HttpParamKey.WeiXinGetToKen.SECRET + "=" + secret + "&" +
                HttpParamKey.WeiXinGetToKen.CODE + "=" + code + "&" +
                HttpParamKey.WeiXinGetToKen.GRANT_TYPE + "=" + "authorization_code";

        try {
            httpClientGetRequest(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestGetRefreshWChatToken() {
        String appId = WXEntryActivity.WX_APPID;
        String secret = WXEntryActivity.WX_SECRET;

        //https://api.weixin.qq.com/sns/oauth2/refresh_token
        // ?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN
        String param = WEIXIN_GET_REFRESH_TOKEN + "?" +
                HttpParamKey.WeiXinGetToKen.APP_ID + "=" + appId + "&" +
                HttpParamKey.WeiXinGetToKen.GRANT_TYPE + "=" + "refresh_token" +
                HttpParamKey.WeiXinGetToKen.REFRESH_TOKEN_PARAM + "=" + LikeAgent.getInstance().getWeiXinPojo().getRefresh_token();


        try {
            httpClientGetRequest(param);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void httpClientGetRequest(String param) throws IllegalStateException, IOException {
        HttpUtil.getInstance().GET(param, new HttpResultListener() {
            @Override
            public void onSuccess(String str) {
                WeiXinResult weiXinResult = GsonUtils.jsonToObject(str, WeiXinResult.class);
                if (weiXinResult == null || TextUtils.isEmpty(weiXinResult.getAccess_token()) || TextUtils.isEmpty(weiXinResult.getRefresh_token()) || TextUtils.isEmpty(weiXinResult.getOpenid())) {
//                    EventBus.getDefault().post(new LoginStateEvent(LoginStateEvent.WXLOGINERROR, "微信返回的数据有空"));
                    return;
                }

                LikeAgent.getInstance().clearWeiXinInfo();
                LikeAgent.getInstance().saveWeiXinInfo(weiXinResult);

                getUserInfo(weiXinResult.getAccess_token(),weiXinResult.getOpenid());
            }

            @Override
            public void onFailure(String str) {
                WeiXinFailPojo fail = GsonUtils.jsonToObject(str, WeiXinFailPojo.class);
                if (fail.errcode == 40029) {
                    requestGetRefreshWChatToken();
                }

            }
        });
    }

    public static void getUserInfo(String access_token, String openid) {
        //https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID
        String param = WEIXIN_GET_USERINFO + "?" + HttpParamKey.WeiXinGetToKen.ACCESS_TOKEN_PARAM+"="
                + access_token + "&openid=" + openid;
        HttpUtil.getInstance().GET(param, new HttpResultListener() {
            @Override
            public void onSuccess(String str) {
                UserPojo userPojo = GsonUtils.jsonToObject(str, UserPojo.class);
                LikeAgent.getInstance().saveUserInfo(userPojo);
                SharedPreferencesManager.setLoginState(LoginType.WEIXIN);
                EventBus.getDefault().post(new LoginStateEvent(LoginStateEvent.SUCCESS));

                LikeAgent.getInstance().setAuthed(true);
            }

            @Override
            public void onFailure(String str) {

            }
        });
    }
}
