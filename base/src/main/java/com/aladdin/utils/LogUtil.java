package com.aladdin.utils;

import com.orhanobut.logger.Logger;

/**
 * Description 日志打印工具
 * Created by zxl on 2017/4/27 下午6:10.
 * Email:444288256@qq.com
 */
public class LogUtil {
    private static boolean isDebug = true;
    private static final String TAG = "LogUtil";

    public static void init() {
        //改变默认TAG，Logger.t(TAG)可以局部改变TAG
        Logger.init(TAG);
    }

    /**
     * log.i
     *
     * @param msg
     */
    public static void i(String msg) {
        if (isDebug) {
            Logger.i(msg);
        }
    }

    /**
     * log.d
     *
     * @param msg
     */
    public static void d(String msg) {
        if (isDebug) {
            Logger.d(msg);
        }
    }

    /**
     * log.w
     *
     * @param msg
     */
    public static void w(String msg) {
        if (isDebug) {
            Logger.w(msg);
        }
    }

    /**
     * log.e
     *
     * @param msg
     */
    public static void e(String msg) {
        if (isDebug) {
            Logger.e(msg);
        }
    }
}
