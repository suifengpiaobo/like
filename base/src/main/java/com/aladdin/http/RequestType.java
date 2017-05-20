package com.aladdin.http;

/**
 * Description
 * Created by zxl on 2017/5/17 下午3:48.
 * Email:444288256@qq.com
 */
public enum RequestType {
    POST(0),
    GET(1),
    MUTIPART(2);

    private final int value;

    private RequestType(int value) {
        this.value = value;
    }

    public int get() {
        return this.value;
    }

    public static RequestType valueOf(int value) {
        RequestType[] var4;
        int var3 = (var4 = values()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            RequestType type = var4[var2];
            if(value == type.value) {
                return type;
            }
        }

        return null;
    }
}
