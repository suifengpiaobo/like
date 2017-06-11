package com.aladdin.like.model;

/**
 * Description 用户信息
 * Created by zxl on 2017/4/29 上午10:32.
 * Email:444288256@qq.com
 */
public class UserPojo {
    public int logtype; //登录方式, 1: 微信, 2: 微博, 3: qq
    public String userId;
    public String nickName;
    public String avatar;
    public String openid;//微信openid
    public boolean IsFirstLogin;//是否首次登录，1 是，0 否
    public int device;//设备  0: 未知, 1: 安卓, 2: 苹果

    @Override
    public String toString() {
        return "UserPojo{" +
                "nickName='" + nickName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", openid='" + openid + '\'' +
                ", IsFirstLogin=" + IsFirstLogin +
                '}';
    }
}
