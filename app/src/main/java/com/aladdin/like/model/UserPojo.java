package com.aladdin.like.model;

/**
 * Description 用户信息
 * Created by zxl on 2017/4/29 上午10:32.
 * Email:444288256@qq.com
 */
public class UserPojo {
//    "openid":"OPENID",
//            "nickname":"NICKNAME",
//            "sex":1,
//            "province":"PROVINCE",
//            "city":"CITY",
//            "country":"COUNTRY",
//            "headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
//            "privilege":[
//            "PRIVILEGE1",
//            "PRIVILEGE2"
//            ],
//            "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"

    public String openid;//微信openid
    public String nickname;
    public int sex;
    public String province;
    public String city;
    public String country;
    public String headimgurl;
    public String unionid;

    public int logtype; //登录方式, 1: 微信, 2: 微博, 3: qq
    public int IsFirstLogin;//是否首次登录，1 是，0 否
    public int device;//设备  0: 未知, 1: 安卓, 2: 苹果

    private String mauth;

//    class Privilege{
//    }
    public String getMauth() {
        return mauth;
    }

    @Override
    public String toString() {
        return "UserPojo{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", unionid='" + unionid + '\'' +
                ", logtype=" + logtype +
                ", IsFirstLogin=" + IsFirstLogin +
                ", device=" + device +
                ", mauth='" + mauth + '\'' +
                '}';
    }
}
