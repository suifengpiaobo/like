package com.aladdin.like.event;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description 登录事件
 * Created by zxl on 2017/6/3 下午7:15.
 * Email:444288256@qq.com
 */
public class WXLoginEvent {
    public static final int BEGIN = 1;
    public static final int SUCCESS = 2;
    public static final int ERROR = 3;
    public static final int LOGIN_CANCEL = 4;
    public static final int WXLOGINERROR = 5;
    public  int  loginExceptionState;
    public  String loginExceptionMessage;



    public  int loginState;

    public WXLoginEvent(){

    }

    public WXLoginEvent(@LoginState int loginState) {
        this.loginState = loginState;
    }
    public WXLoginEvent(@LoginState int loginState,String loginExceptionMessage) {
        this.loginState = loginState;
        this.loginExceptionMessage = loginExceptionMessage;
    }

    @LoginState
    public int getSingInState() {
        return loginState;
    }

    @IntDef({BEGIN, SUCCESS, ERROR, LOGIN_CANCEL,WXLOGINERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoginState {
    }
}
