package com.zxl.downloader.helper;

import com.zxl.downloader.DownloadListener;
import com.zxl.downloader.entity.FileInfo;
import com.zxl.downloader.model.DownloadStatus;
import com.zxl.downloader.service.DownloadThreadPool;

/**
 * Description
 * Created by zxl on 2017/5/8 下午2:59.
 * Email:444288256@qq.com
 */
public class ListenerDecorator implements DownloadListener {
    private final DownloadListener mListener;
    private final boolean mIsUiThread;

    public ListenerDecorator(DownloadListener listener, boolean isUiThread) {
        this.mListener = listener;
        this.mIsUiThread = isUiThread;
    }


    @Override
    public void onStart(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.START);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onStart(fileInfo);
                }
            });
        } else {
            mListener.onStart(fileInfo);
        }
    }

    @Override
    public void onUpdate(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.DOWNLOADING);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onUpdate(fileInfo);
                }
            });
        } else {
            mListener.onUpdate(fileInfo);
        }
    }

    @Override
    public void onStop(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.STOP);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onStop(fileInfo);
                }
            });
        } else {
            mListener.onStop(fileInfo);
        }
    }

    @Override
    public void onComplete(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.COMPLETE);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onComplete(fileInfo);
                }
            });
        } else {
            mListener.onComplete(fileInfo);
        }
        DownloadThreadPool.getInstance().cancel(fileInfo.getUrl(), false);
    }

    @Override
    public void onCancel(final FileInfo fileInfo) {
        fileInfo.setStatus(DownloadStatus.CANCEL);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onCancel(fileInfo);
                }
            });
        } else {
            mListener.onCancel(fileInfo);
        }
        DownloadThreadPool.getInstance().cancel(fileInfo.getUrl(), true);
    }

    @Override
    public void onError(final FileInfo fileInfo, final String errorMsg) {
        fileInfo.setStatus(DownloadStatus.ERROR);
        if (mIsUiThread) {
            MainHandler.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    mListener.onError(fileInfo, errorMsg);
                }
            });
        } else {
            mListener.onError(fileInfo, errorMsg);
        }
        DownloadThreadPool.getInstance().cancel(fileInfo.getUrl(), false);
    }
}
