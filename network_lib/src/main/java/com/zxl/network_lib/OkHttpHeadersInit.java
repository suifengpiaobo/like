package com.zxl.network_lib;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:42.
 * Email:444288256@qq.com
 */
public class OkHttpHeadersInit {
    /**
     * 初始化请求头信息
     * @param str
     */
    public static void initOkHttpHeaders(String... str){
        RequestContent.initOkHttpHearder(str);
    }
}
