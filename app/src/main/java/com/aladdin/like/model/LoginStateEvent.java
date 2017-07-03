package com.aladdin.like.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Description
 * Created by zxl on 2017/7/3 下午8:46.
 * Email:444288256@qq.com
 */
public class LoginStateEvent {
    //begin success error
    public static final int BEGIN = 1;
    public static final int SUCCESS = 2;
    public static final int ERROR = 3;
    public static final int LOGIN_CANCEL = 4;
    public static final int WXLOGINERROR = 5;
    public  int  loginExceptionState;
    public  String loginExceptionMessage;



    public  int loginState;

    public LoginStateEvent(){

    }

    public LoginStateEvent(@LoginState int loginState) {
        this.loginState = loginState;
    }
    public LoginStateEvent(@LoginState int loginState,String loginExceptionMessage) {
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
