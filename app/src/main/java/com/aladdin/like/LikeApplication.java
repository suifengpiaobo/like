package com.aladdin.like;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.aladdin.like.utils.FontsOverrideUtil;
import com.aladdin.utils.ContextUtils;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Description
 * Created by zxl on 2017/4/28 下午5:16.
 * Email:444288256@qq.com
 */
public class LikeApplication extends Application {
    public static LikeApplication instance;
    private boolean isFirst = false;
    private static Context mAppContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this.getApplicationContext();
        if (!isFirst){
            isFirst = true;
            instance = this;
            initConfig();

            //友盟统计
            MobclickAgent.UMAnalyticsConfig  umAnalyticsConfig = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(),"59434c6f99f0c756230005a0",getChannel());
            MobclickAgent.startWithConfigure(umAnalyticsConfig);
            MobclickAgent.setCatchUncaughtExceptions(false);
            MobclickAgent.setDebugMode(false);
        }
    }

    public void initConfig() {
        //替换字体
        FontsOverrideUtil.init(mAppContext);

        //消息推送
        initPushMessage();

        ContextUtils.getInstance().setContext(this.getApplicationContext()); // Must!! First call this method.
        DensityUtils.setAppContext(this);
        Fresco.initialize(this);
    }

    //消息推送
    public void initPushMessage() {
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtil.i("+++ register push sucess. token:" + data+"flag" +flag);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.i("+++ register push fail. token:" + data
                        + ", errCode:" + errCode + ",msg:"
                        + msg);
            }
        });
    }


    public static LikeApplication getInstance(){
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

    public static String UDID() {
        SecureRandom random = new SecureRandom();
        return (new BigInteger(64, random)).toString(16);
    }

    public static String deviceType() {
        return android.os.Build.MODEL.replace("-", " ");
    }

    public static String getVersionName() {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = ContextUtils.getInstance().getApplicationContext().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    ContextUtils.getInstance().getApplicationContext().getPackageName(), 0);
            return packInfo.versionName;
        } catch (Exception ignored) {

        }
        return null;
    }
}
