package com.aladdin.http;

import com.aladdin.utils.LogUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;
import com.zxl.network_lib.Inteface.HttpResultListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:52.
 * Email:444288256@qq.com
 */
public class HttpResultProcess {

    public static <U> HttpResultListener httpResultListener(final HttpResultCallback<U> httpResultCallback, final Class<U> uClass) {
        return new HttpResultListener() {
            @Override
            public void onSuccess(String str) {
                try {
                    JSONObject response = new JSONObject(str);
                    LogUtil.i("---str--->>>"+str);
                    int code = response.optInt("code");
                    if (code == 200) {
                        try{
                            JSONObject result = response.getJSONObject("data");
                            httpResultCallback.onSuccess(RequestJsonUtils.getObj(uClass, result.toString()));
                        }catch (JSONException e){
                            httpResultCallback.onSuccess(RequestJsonUtils.getObj(uClass, "success"));
                            e.printStackTrace();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    } else {//网络请求不区分环境

                        String errorMsg = response.optString("errorMsg");
                        String errorCode = response.optString("errorCode");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    httpResultCallback.onFailure("400", "解析错误");
                }
            }

            @Override
            public void onFailure(String str) {
                httpResultCallback.onFailure("400", str);
            }
        };
    }
}
