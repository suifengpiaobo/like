package com.aladdin.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:53.
 * Email:444288256@qq.com
 */
public class GsonUtils {
    private GsonUtils() {

    }

    /**
     * object converter String
     *
     * @param t   t
     * @param <T> t
     * @return json
     */
    public static <T> String objectToJson(T t) {
        if (t == null) {
            throw new IllegalArgumentException("t can not null");
        }
        Gson gson = new Gson();
        return gson.toJson(t);
    }


    /**
     * string converter object
     *
     * @param json   json
     * @param clazzT converter to class
     * @param <T>    t
     * @return object
     */
    public static <T> T jsonToObject(String json, Class<T> clazzT) {
        if (TextUtils.isEmpty(json) || clazzT == null) {
            throw new IllegalArgumentException("json can not null");
        }
        Gson gson = new Gson();
        return gson.fromJson(json, clazzT);
    }

    /**
     * json converter list
     *
     * @param json   json
     * @param classT List of generic type needs such as: List T, classT parameters in place, fill T.class
     * @param <T>    t
     * @return List T t
     */
    public static <T> List<T> jsonToObjectList(String json, Class<T> classT) {
        if (TextUtils.isEmpty(json) || classT == null) {
            throw new IllegalArgumentException("json can not null");
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new ListOfSomething<T>(classT));
    }
}
