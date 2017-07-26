package com.zxl.network_lib.Inteface;

import org.json.JSONException;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:41.
 * Email:444288256@qq.com
 */
public interface HttpResultCallback<T> {
    void onSuccess(T result) throws JSONException;

    void onFailure(String code, String msg);
}
