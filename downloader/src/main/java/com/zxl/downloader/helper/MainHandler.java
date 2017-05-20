package com.zxl.downloader.helper;

import android.os.Handler;
import android.os.Looper;

/**
 * Description 主线程处理工具
 * Created by zxl on 2017/5/8 下午2:59.
 * Email:444288256@qq.com
 */
public class MainHandler {
    private static final Handler mHandler = new Handler(Looper.getMainLooper());


    private MainHandler() {
        throw new RuntimeException("MainHandler cannot be initialized!");
    }

    /**
     * 在主线程执行
     * @param runnable
     */
    public static void runInMainThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
