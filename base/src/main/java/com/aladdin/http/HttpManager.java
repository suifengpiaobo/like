package com.aladdin.http;

import com.zxl.network_lib.HttpUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.util.Map;

import okhttp3.HttpUrl;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:49.
 * Email:444288256@qq.com
 */
public enum HttpManager {
    INSTANCE;

    HttpManager() {
    }

    /**
     * 普通短连请求
     *
     * @param path               {@link HttpUrl}请求路径
     * @param param              请求参数
     * @param requestType        {@link RequestType}请求方式<em>post get</em>
     * @param httpResultCallback 结果回调
     */
    public <T> void shortConnectRequest(String path, String param, RequestType requestType, HttpResultCallback<T> httpResultCallback, Class<T> uClass) {

        StringBuilder url = new StringBuilder();
        if (path.contains("http") || path.contains("https")) {
            url.append(path);
        } else {
//            url.append(HttpUrl.API_URL).append(path);
        }
        if (requestType == RequestType.POST) {
            HttpUtil.getInstance().POST(url.toString(), param, HttpResultProcess.httpResultListener(httpResultCallback, uClass));

        } else {
            HttpUtil.getInstance().GET(url.toString(), param, HttpResultProcess.httpResultListener(httpResultCallback, uClass));

        }

    }

    public  <T> void shortConnectRequest(String path, Map param, RequestType requestType, HttpResultCallback<T> httpResultCallback, Class<T> uClass) {

        if(requestType == RequestType.POST){
            HttpUtil.getInstance().POST(path, param,HttpResultProcess.httpResultListener(httpResultCallback,uClass));

        }else{
            HttpUtil.getInstance().GET(path, param, HttpResultProcess.httpResultListener(httpResultCallback,uClass));
        }

    }
}
