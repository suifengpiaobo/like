package com.aladdin.base;

import android.support.multidex.MultiDexApplication;

/**
 * Description
 * Created by zxl on 2017/4/27 下午5:23.
 * Email:444288256@qq.com
 */
public abstract class BaseApplication extends MultiDexApplication {
    public static BaseApplication instance;
    public abstract void initConfig();

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        initConfig();
    }

    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }
}
