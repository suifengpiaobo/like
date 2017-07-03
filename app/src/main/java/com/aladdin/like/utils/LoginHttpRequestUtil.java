package com.aladdin.like.utils;

import com.aladdin.http.RequestType;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.http.HttpParamKey;
import com.aladdin.like.http.HttpUrl;
import com.aladdin.like.model.User2Pojo;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.util.HashMap;

/**
 * Description
 * Created by zxl on 2017/7/3 下午8:35.
 * Email:444288256@qq.com
 */
public class LoginHttpRequestUtil {

    public static void goWeiXinLogin(String provider ,String accessToken, String refreshToken,HttpResultCallback<User2Pojo> callback){

        HashMap<String, Object> parm = new HashMap<>();
        parm.put(HttpParamKey.WeiXinGetToKen.PROVIDER,provider);
        parm.put(HttpParamKey.WeiXinGetToKen.ACCESS_TOKEN,accessToken);
        parm.put(HttpParamKey.WeiXinGetToKen.REFRESH_TOKEN,refreshToken);
        HttpManager.INSTANCE.shortConnectRequest(HttpUrl.USER_LOGIN, parm, RequestType.POST, callback, User2Pojo.class);


    }
}
