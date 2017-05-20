package com.zxl.network_lib;

import com.zxl.network_lib.Inteface.HttpResultListener;
import com.zxl.network_lib.Inteface.IRequest;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

import okhttp3.Headers;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:40.
 * Email:444288256@qq.com
 */
public class HttpUtil implements IRequest {
    public HttpUtil() {
    }

    private static class HttpUtilHolder {
        static final HttpUtil h = new HttpUtil();
    }

    public static HttpUtil getInstance() {
        return HttpUtilHolder.h;
    }

    @Override
    public  void GET(String url, HttpResultListener i) {
        RequestContent.get(url, i);
    }

    @Override
    public  void GET(String url, Map<String, Object> map, HttpResultListener i) {
        RequestContent.get(url, map, i);
    }

    @Override
    public  void GET(String url, String params, HttpResultListener i) {
        RequestContent.get(url,params,i);
    }

    @Override
    public  void GET(String url, Map<String, Object> map, String time, HttpResultListener i) {

    }

    @Override
    public  void POST(String url, JSONObject json, HttpResultListener i) {
        RequestContent.post(url,json,i);
    }

    @Override
    public  void POST(String url, String params, HttpResultListener i) {

        RequestContent.post(url,params,i);
    }

    @Override
    public  void POST(String url, Map<String, Object> map, HttpResultListener i) {
        RequestContent.post(url, map, i);
    }


    @Override
    public  void POST(String url, Map<String, Object> map, String time, HttpResultListener i) {

    }

    @Override
    public void downloadFile(String url) {

    }

    @Override
    public void upLoadPic(String url, String name, File file, HttpResultListener i) {
        RequestContent.post(url,name,null,file,i);
    }

    /**
     * need to build yourself header
     * @param url url
     * @param name name
     * @param headers header
     * @param file file
     * @param i listener
     */
    public void upLoadPic(String url, String name, Headers headers, File file, HttpResultListener i, String... params){
        RequestContent.post(url,name,headers,file,i,params);
    }

    @Override
    public void upLoadFile(String url, File file, HttpResultListener i) {

    }
}
