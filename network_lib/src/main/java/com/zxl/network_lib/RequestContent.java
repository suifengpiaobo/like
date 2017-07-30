package com.zxl.network_lib;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.zxl.network_lib.Inteface.HttpResultListener;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:42.
 * Email:444288256@qq.com
 */
public class RequestContent {
    private static final MediaType TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType TYPE_STREAM = MediaType.parse("application/octet-stream");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static OkHttpClient mOkHttpClient = null;
    private static Headers headers = null;

    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(60, TimeUnit.SECONDS);  //设置连接超时时间
        okHttpClientBuilder.readTimeout(60, TimeUnit.SECONDS);    //设置读取超时时间
        okHttpClientBuilder.writeTimeout(60, TimeUnit.SECONDS);  //设置写入超时时间
        mOkHttpClient = okHttpClientBuilder.build();
    }

    /**
     * get不带参数
     *
     * @param url
     * @param i
     */
    public static void get(String url, HttpResultListener i) {
        logRequestInfo(url, "");
        Request request = createGetRequest(url, "");
        getResponse(request, i);
    }

    /**
     * get带参数 String
     *
     * @param url
     * @param params
     * @param i
     */
    public static void get(String url, String params, HttpResultListener i) {
        logRequestInfo(url, params);
        Request getRequest = createGetRequest(url, params);
        getResponse(getRequest, i);
    }

    /**
     * get带参数 Map
     *
     * @param url
     * @param map
     * @param i
     * @param
     */
    public static void get(String url, Map<String, Object> map, HttpResultListener i) {
        if (null == map || map.isEmpty()) throw new NullPointerException("map == null");
        logRequestInfo(url, map.toString());
        Request getRequest = createGetRequest(url, map);
        getResponse(getRequest, i);
    }

    /**
     * post 参数Map封装
     *
     * @param url
     * @param map
     * @param i
     * @param
     */
    public static void post(String url, Map<String, Object> map, HttpResultListener i) {
        if (null == map || map.isEmpty()) throw new NullPointerException("map == null");
        logRequestInfo(url, map.toString());
        Request getRequest = createPostRequest(url, map);
        getResponse(getRequest, i);
    }

    /**
     * post 参数json类型
     *
     * @param url
     * @param jsonStr
     * @param i
     * @param
     */
    public static void post(String url, JSONObject jsonStr, HttpResultListener i) {
        if (null == jsonStr) throw new NullPointerException("jsonStr == null");
        logRequestInfo(url, jsonStr.toString());
        Request getRequest = createPostJsonRequest(url, jsonStr);
        getResponse(getRequest, i);
    }

    /**
     * post 参数是String类型
     *
     * @param url
     * @param params
     * @param i
     * @param
     */
    public static void post(String url, String params, HttpResultListener i) {
        logRequestInfo(url, params);
        Request postStringRequest = createPostStringRequest(url, params);
        getResponse(postStringRequest, i);
    }


    /**
     * post 参数是文件 图片
     *
     * @param url
     * @param file
     * @param i
     * @param
     */
    public static void post(String url, String name, Headers header, File file, HttpResultListener i, String... params) {
        if (null == file) throw new NullPointerException("file == null");
        logRequestInfo(url, file.getName());
        Request getRequest = createPostPicRequest(url, name, file, header,params);
        getResponse(getRequest, i);
    }

    /**
     * post 上传文件
     *
     * @param url
     * @param file
     * @return
     */
    public static Request createPostPicRequest(String url, String name, File file, Headers header, String... params) {
        checkUrl(url);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        Log.i("path--->>>","file"+file.getName());
        if (params != null && params.length % 2 == 0) {
            for (int i = 0; i < params.length; i += 2){
                builder.addFormDataPart(params[i], params[i+1]);
            }
        }
        MultipartBody requestBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);
        if (header != null)
            requestBuilder.headers(header);
        else
            requestBuilder.headers(headers);

        return requestBuilder.build();
    }
    public static Request createPostPicRequest(String url, String name,String params, File file,Headers header) {
        checkUrl(url);
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart(name, file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file));
        MultipartBody requestBody = builder.build();
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        requestBuilder.post(requestBody);
        if (header != null)
            requestBuilder.headers(header);
        else
            requestBuilder.headers(headers);

        return requestBuilder.build();
    }


    /**
     * 创建 Post json 请求，
     *
     * @param url
     * @param jsonStr
     * @return
     */
    public static Request createPostJsonRequest(String url, JSONObject jsonStr) {
        checkUrl(url);
        Request.Builder requestBuilder = new Request.Builder();
        RequestBody requestBody = RequestBody.create(TYPE_JSON, jsonStr.toString());
        Request request = requestBuilder.url(url).post(requestBody).build();
        return request;
    }

    /**
     * post 请求
     *
     * @param url
     * @param params String
     * @return
     */
    public static Request createPostStringRequest(String url, String params) {
        checkUrl(url);
        Request.Builder request = new Request.Builder();
        if (headers != null)
            request.headers(headers);
        request.post(postBody(params));
        request.url(url);
        return request.build();
    }

    /**
     * post 请求
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static Request createPostRequest(String url, Map<String, Object> paramMap) {
        checkUrl(url);
        Request.Builder request = new Request.Builder();
        if (headers != null)
            request.headers(headers);
        request.post(postBody(paramMap));
        request.url(url);
        return request.build();

    }

    /**
     * get请求
     *
     * @param url
     * @param paramMap
     * @return
     */
    public static Request createGetRequest(String url, Map<String, Object> paramMap) {
        checkUrl(url);
        String realUrl = url;
        Request.Builder requestBuilder = new Request.Builder();
        StringBuffer sb = new StringBuffer(url);
        if (paramMap != null && !paramMap.isEmpty()) {
            sb.append("?");
            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            realUrl = sb.substring(0, sb.length() - 1);
        }
        if (headers != null)
            requestBuilder.headers(headers);
        requestBuilder.url(realUrl);
        requestBuilder.get();
        return requestBuilder.build();

    }

    /**
     * get请求数据是 String类型
     *
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url, String params) {
        checkUrl(url);
        String realUrl = url;
        Request.Builder requestBuilder = new Request.Builder();
        if (!TextUtils.isEmpty(params)) {
            StringBuffer sb = new StringBuffer(url);
            sb.append("?").append(params);
            realUrl = sb.toString();
        }
        if (headers != null)
            requestBuilder.headers(headers);
        requestBuilder.url(realUrl);
        requestBuilder.get();
        return requestBuilder.build();
    }

    private static void checkUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new NullPointerException("request url is null....");
        }
        //okHttp 也有检测是否为有效的，
//        if(!url.startsWith("http")||!url.startsWith("https")){
//            throw new IllegalArgumentException("unexpected url: " + url);
//        }
    }

    private static void logRequestInfo(String url, String params) {
        Log.d("RequestContent", "url-->" + url);
        Log.d("RequestContent", "url-->" + url + "params-->" + params);
    }

    /**
     *
     *
     * @param request
     * @param i
     * @param
     */
    private static void getResponse(final Request request, final HttpResultListener i) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                i.onFailure(" 网络不佳，请检查网络设置后再试!");
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.body() == null) {
                    i.onFailure("response.body() == null");
                    return;
                }
                if (response.isSuccessful()) {
                    String str = null;
                    try {
                        response.code();
                        str = response.body().string();
                        Log.i("RequestContent", "response.nody()-->" + "url-->" + request.toString() + "data-->" + str);
//                        if (TextUtils.isEmpty(str)) {
//                            i.onFailure("返回值为空！");
//                        } else {
//                            i.onSuccess(str);
//                        }
                        i.onSuccess(str);
                    } catch (Exception e) {
                        i.onFailure(e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    if (response.code() <= 500 && response.code() >= 400) {
                        i.onFailure(response.toString());
                    }
                }
            }
        });
    }

    /**
     * 请求参数
     *
     * @param map
     * @return
     */
    private static RequestBody postBody(Map<String, Object> map) {
        if (null == map || map.isEmpty()) throw new NullPointerException("map == null");
        FormBody.Builder b = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            b.add(entry.getKey(), (String) entry.getValue());
        }

        return b.build();
    }

    /**
     * 请求参数String
     *
     * @param params
     * @return
     */
    private static RequestBody postBody(String params) {
        FormBody.Builder b = new FormBody.Builder();
        if (!TextUtils.isEmpty(params)) {
            String[] split = params.split("&");
            for (String str : split) {
                String[] s = str.split("=");
                b.add(s[0], s[1]);
            }
        }
        return b.build();
    }

    /**
     * 新方法头部谁都可以去拼装
     *
     * @param str
     */
    public static void initOkHttpHearder(@Nullable String... str) {
        if (str.length % 2 != 0) {
            throw new IllegalArgumentException("String... str不合法！");
        }
        headers = Headers.of(str);
    }
}
