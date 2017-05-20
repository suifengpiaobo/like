package com.aladdin.like.model;

/**
 * Description 用户信息
 * Created by zxl on 2017/4/29 上午10:32.
 * Email:444288256@qq.com
 */
public class UserPojo {
    public String uid;
    public String name;
    public String avatar;
    public int sex;
    public String token;

    @Override
    public String toString() {
        return "UserPojo{" +
                "uid='" + uid + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sex=" + sex +
                ", token='" + token + '\'' +
                '}';
    }
}
