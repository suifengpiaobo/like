package com.aladdin.like.constant;

/**
 * Description 登录类型
 * Created by zxl on 2017/6/8 下午7:54.
 * Email:444288256@qq.com
 */
public enum LoginType {
    NOT_LOGIN(0),
    BASE(1),
    WEIBO(2),
    WEIXIN(3),
    SHOUBO(4);

    private final int code;

    LoginType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static LoginType valueOfCode(int code) {
        for (LoginType loginType : LoginType.values()) {
            if (code == loginType.code)
                return loginType;
        }
        return NOT_LOGIN;
    }
}
