package com.aladdin.like.model;

/**
 * Description 用户信息
 * Created by zxl on 2017/4/29 上午10:32.
 * Email:444288256@qq.com
 */
public class UserPojo {
    public String user_id;
    public String nickName;
    public String avatar;
    public String openid;//微信openid
    public boolean IsFirstLogin;//是否首次登录，1 是，0 否

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
