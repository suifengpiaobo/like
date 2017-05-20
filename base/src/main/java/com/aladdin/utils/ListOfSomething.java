package com.aladdin.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:54.
 * Email:444288256@qq.com
 */
public class ListOfSomething<X> implements ParameterizedType {
    private Class<?> wrapped;

    public ListOfSomething(Class<X> wrapped) {
        this.wrapped = wrapped;
    }

    public Type[] getActualTypeArguments() {
        return new Type[]{wrapped};
    }

    public Type getRawType() {
        return List.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
