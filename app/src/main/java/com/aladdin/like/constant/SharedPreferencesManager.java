package com.aladdin.like.constant;

import com.aladdin.utils.SharedPreferencesUtil;

/**
 * Description
 * Created by zxl on 2017/6/8 下午8:10.
 * Email:444288256@qq.com
 */
public class SharedPreferencesManager {
    public static void setLoginState(LoginType state) {
        SharedPreferencesUtil.INSTANCE.putInt(SharedPreferencesKey.LOGIN_STATE, state.getCode());
    }

    public static LoginType getLoginState() {
        return LoginType.valueOfCode(SharedPreferencesUtil.INSTANCE.getInt(SharedPreferencesKey.LOGIN_STATE, 0));
    }
}
