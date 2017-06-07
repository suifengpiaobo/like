package com.aladdin.like.event;

/**
 * Description 分享事件
 * Created by zxl on 2017/6/3 下午7:18.
 * Email:444288256@qq.com
 */
public class ShareEvent {
    public static final int SHARE_UNKNOW = 0;
    public static final int SHARE_SUCCESS = 1;
    public static final int SHARE_FAILD = 2;
    public static final int SHARE_CANCEL = 3;

    public int status = SHARE_UNKNOW;

    public ShareEvent(int status) {
        this.status = status;
    }

    public static class StartLiveShareEvent {

    }
}
