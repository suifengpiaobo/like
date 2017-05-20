package com.zxl.network_lib.Inteface;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:40.
 * Email:444288256@qq.com
 */
public interface HttpResultListener {
    void onSuccess(String str);

    void onFailure(String str);
}
