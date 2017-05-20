package com.zxl.downloader;

import com.zxl.downloader.entity.FileInfo;

/**
 * Description 下载监听器
 * Created by zxl on 2017/5/8 下午2:40.
 * Email:444288256@qq.com
 */
public interface DownloadListener {
    /**
     * 开始下载
     * @param fileInfo
     */
    void onStart(FileInfo fileInfo);
    /**
     * 更新下载进度
     * @param fileInfo
     */
    void onUpdate(FileInfo fileInfo);
    /**
     * 停止下载
     * @param fileInfo
     */
    void onStop(FileInfo fileInfo);
    /**
     * 下载成功
     * @param fileInfo
     */
    void onComplete(FileInfo fileInfo);
    /**
     * 取消下载
     * @param fileInfo
     */
    void onCancel(FileInfo fileInfo);
    /**
     * 下载失败
     * @param fileInfo
     */
    void onError(FileInfo fileInfo, String errorMsg);
}
