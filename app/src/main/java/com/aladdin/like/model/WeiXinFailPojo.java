package com.aladdin.like.model;

/**
 * Description
 * Created by zxl on 2017/7/3 下午9:25.
 * Email:444288256@qq.com
 */
public class WeiXinFailPojo {
    //{"errcode":40029,"errmsg":"invalid code"}
    public int errcode;
    public String errmsg;

    @Override
    public String toString() {
        return "WeiXinFailPojo{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                '}';
    }
}
