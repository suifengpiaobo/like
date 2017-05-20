package com.zxl.network_lib.Inteface;

import org.json.JSONObject;

import java.io.File;
import java.util.Map;

/**
 * Description 该接口规定所有请求所需的常量和方法
 * Created by zxl on 2017/5/17 下午3:39.
 * Email:444288256@qq.com
 */
public interface IRequest {
    /**
     * 发送get请求
     * @param url URL
     * @param i responseInterface
     */
    void GET(String url, HttpResultListener i);

    /**
     * 发送get请求
     * @param url URL
     * @param map map
     * @param i responseInterface
     */
    void GET(String url, Map<String,Object> map, HttpResultListener i);

    /**
     * 发送get请求
     * @param url
     * @param params String
     * @param i
     * @param
     */
    void GET(String url,String params,HttpResultListener i);

    /**
     * 发送get请求
     * @param url URL
     * @param map map
     * @param time time_out
     * @param i responseInterface
     */
    void GET(String url, Map<String,Object> map, String time, HttpResultListener i);

    /**
     * 发送post请求
     * @param url
     * @param json
     * @param i
     */
    void POST(String url, JSONObject json, HttpResultListener i);

    void POST(String url,String params,HttpResultListener i);

    /**
     * 发送post请求
     * @param url URL
     * @param map map
     * @param i responseInterface
     */
    void POST(String url, Map<String,Object> map, HttpResultListener i);


    /**
     * 发送post请求
     * @param url URL
     * @param map map
     * @param time time_out
     * @param i responseInterface
     */
    void POST(String url, Map<String,Object> map, String time, HttpResultListener i);

    /**
     * 下载文件
     */
    void downloadFile(String url);

    /**
     * 图片
     */
    void upLoadPic(String url, String name, File file, HttpResultListener i);
    /**
     * 上传文件
     */
    void upLoadFile(String url,File file,HttpResultListener i);
}
