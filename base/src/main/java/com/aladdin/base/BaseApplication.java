package com.aladdin.base;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDexApplication;

import com.aladdin.utils.ContextUtils;
import com.aladdin.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

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
        //消息推送
//        initPushMessage();
    }

    //消息推送
    public void initPushMessage() {
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtil.i("deviceToken--->>>"+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
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
