package com.aladdin.base;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.aladdin.utils.ContextUtils;
import com.umeng.analytics.MobclickAgent;

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

        //友盟统计
        MobclickAgent.UMAnalyticsConfig  umAnalyticsConfig = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(),"59434c6f99f0c756230005a0",getChannel());
        MobclickAgent.startWithConfigure(umAnalyticsConfig);
        MobclickAgent.setCatchUncaughtExceptions(false);
        MobclickAgent.setDebugMode(false);
    }

    public static BaseApplication getInstance(){
        return instance;
    }

    public static String getChannel() {
        String CHANNELID = "000000";
        try {
            ApplicationInfo ai = ContextUtils.getInstance().getApplicationContext().getPackageManager()
                    .getApplicationInfo(
                            ContextUtils.getInstance().getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Object value = ai.metaData.get("CHANNEL");
            if (value != null) {
                CHANNELID = value.toString();
            }
        } catch (Exception e) {
        }

        return CHANNELID;
    }

    @Override
    public void onLowMemory() {
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onLowMemory();
    }
}
