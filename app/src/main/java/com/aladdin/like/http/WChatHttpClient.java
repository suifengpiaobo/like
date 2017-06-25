package com.aladdin.like.http;

import android.text.TextUtils;

import com.aladdin.like.module.register.entity.WeiXinResult;
import com.aladdin.like.wxapi.WXEntryActivity;
import com.aladdin.utils.GsonUtils;
import com.aladdin.utils.LogUtil;
import com.zxl.network_lib.HttpUtil;
import com.zxl.network_lib.Inteface.HttpResultListener;

import java.io.IOException;

/**
 * Description
 * Created by zxl on 2017/6/3 下午7:19.
 * Email:444288256@qq.com
 */
public class WChatHttpClient {
    public static final String WEIXIN_GET_CODE_FROM_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";
    public static void requestGetWChatToken(String code) {
        String appId = WXEntryActivity.WX_APPID;
        String secret = WXEntryActivity.WX_SECRET;

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

    private static void httpClientGetRequest(String param) throws IllegalStateException, IOException {
        LogUtil.i("param--->>>"+param);
        HttpUtil.getInstance().GET(param, new HttpResultListener() {
            @Override
            public void onSuccess(String str) {
                WeiXinResult weiXinResult = GsonUtils.jsonToObject(str, WeiXinResult.class);
                if (weiXinResult == null || TextUtils.isEmpty(weiXinResult.getAccess_token()) || TextUtils.isEmpty(weiXinResult.getRefresh_token()) || TextUtils.isEmpty(weiXinResult.getOpenid())) {
//                    EventBus.getDefault().post(new LoginStateEvent(LoginStateEvent.WXLOGINERROR, "微信返回的数据有空"));
                    return;
                }
                LogUtil.i(weiXinResult.getAccess_token()+"  --  "+weiXinResult.getRefresh_token()+"  --  "+weiXinResult.getOpenid());
//                LiveAgent.getInstance().weixinLogin(weiXinResult.getAccess_token(), weiXinResult.getRefresh_token(), weiXinResult.getOpenid(), false);
            }

            @Override
            public void onFailure(String str) {
//                EventBus.getDefault().post(new LoginStateEvent(LoginStateEvent.WXLOGINERROR, "请求网络异常"));
            }
        });
    }
}
