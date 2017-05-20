package com.zxl.downloader.exception;

/**
 * Description 下载异常
 * Created by zxl on 2017/5/8 下午2:58.
 * Email:444288256@qq.com
 */
public class DownloadException extends RuntimeException{
    public DownloadException(String detailMessage) {
        super(detailMessage);
    }
}
