package com.aladdin.http;

import com.aladdin.utils.GsonUtils;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:53.
 * Email:444288256@qq.com
 */
public class RequestJsonUtils {
    private RequestJsonUtils() {

    }

    public static synchronized <T> T getObj(Class<T> clazz, String json) {
        if (clazz.equals(String.class)) {
            return (T) json;
        } else {
            if (json.startsWith("[")) { // is list
                return (T) GsonUtils.jsonToObjectList(json, clazz);
            } else if (json.startsWith("{")) { // is object
                return GsonUtils.jsonToObject(json, clazz);
            } else {
                return (T) json;
            }
        }
    }
}
