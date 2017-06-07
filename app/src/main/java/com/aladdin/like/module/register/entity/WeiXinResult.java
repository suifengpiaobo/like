package com.aladdin.like.module.register.entity;

/**
 * Description
 * Created by zxl on 2017/6/3 下午7:25.
 * Email:444288256@qq.com
 */
public class WeiXinResult {
    private String access_token;

    private int expires_in;

    private String refresh_token;

    private String openid;

    private String scope;

    private String unionid;

    public void setAccess_token(String access_token){
        this.access_token = access_token;
    }
    public String getAccess_token(){
        return this.access_token;
    }
    public void setExpires_in(int expires_in){
        this.expires_in = expires_in;
    }
    public int getExpires_in(){
        return this.expires_in;
    }
    public void setRefresh_token(String refresh_token){
        this.refresh_token = refresh_token;
    }
    public String getRefresh_token(){
        return this.refresh_token;
    }
    public void setOpenid(String openid){
        this.openid = openid;
    }
    public String getOpenid(){
        return this.openid;
    }
    public void setScope(String scope){
        this.scope = scope;
    }
    public String getScope(){
        return this.scope;
    }
    public void setUnionid(String unionid){
        this.unionid = unionid;
    }
    public String getUnionid(){
        return this.unionid;
    }

    @Override
    public String toString() {
        return "WeiXinResult{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", refresh_token='" + refresh_token + '\'' +
                ", openid='" + openid + '\'' +
                ", scope='" + scope + '\'' +
                ", unionid='" + unionid + '\'' +
                '}';
    }
}
