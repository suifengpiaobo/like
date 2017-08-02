package com.aladdin.like;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import com.aladdin.like.receiver.NotificationService;
import com.aladdin.like.utils.FontsOverrideUtil;
import com.aladdin.utils.ContextUtils;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.LogUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.analytics.MobclickAgent;

import java.math.BigInteger;
import java.security.SecureRandom;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

/**
 * Description
 * Created by zxl on 2017/4/28 下午5:16.
 * Email:444288256@qq.com
 */
public class LikeApplication extends Application {
//    public static LikeApplication instance;
    private boolean isFirst = false;
//    private static Context mAppContext;
    private Message m;
    public static String APP_PATH;
    // 应用SD卡扩展路径
    public final static String APP_EXT_PATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/Like/";

    private RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        ContextUtils.getInstance().setContext(this.getApplicationContext()); // Must!! First call this method.
        DensityUtils.setAppContext(this);
        APP_PATH = this.getFilesDir().getAbsolutePath() + "/";
//        initImageLoader(getApplicationContext());
        if (!isFirst){
            isFirst = true;
            initConfig();

            //友盟统计
            MobclickAgent.UMAnalyticsConfig  umAnalyticsConfig = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(),"59434c6f99f0c756230005a0",getChannel());
            MobclickAgent.startWithConfigure(umAnalyticsConfig);
            MobclickAgent.setCatchUncaughtExceptions(false);
            MobclickAgent.setDebugMode(false);
        }
        enabledStrictMode();
        mRefWatcher = LeakCanary.install(this);
    }
    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }

//    public void initImageLoader(Context context){
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(10 * 1024 * 1024); // 10 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//
//        // Initialize ImageLoader with configuration.
//        ImageLoader.getInstance().init(config.build());
//    }

    public void initConfig() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //替换字体
                FontsOverrideUtil.init(getApplicationContext());
                //初始化消息数据库
                NotificationService.getInstance(getApplicationContext());
                //消息推送
                initPushMessage();
            }
        });

        Fresco.initialize(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                ContextUtils.getInstance().setContext(activity); // Must!! First call this method.
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
//                ContextUtils.getInstance().onDestroyContext();
            }
        });

    }

    //消息推送
    public void initPushMessage() {
        XGPushManager.registerPush(getApplicationContext(), new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                LogUtil.i("+++ register push sucess. token:" + data+"flag" +flag);
                // 使用ApplicationContext
//                Context context = getApplicationContext();
//                Intent service = new Intent(context, XGPushService.class);
//                context.startService(service);

            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                LogUtil.i("+++ register push fail. token:" + data
                        + ", errCode:" + errCode + ",msg:"
                        + msg);
            }
        });

        initCustomPushNotificationBuilder(getApplicationContext());
    }

    private void initCustomPushNotificationBuilder(Context context) {
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setSound(
                RingtoneManager.getActualDefaultRingtoneUri(
                        getApplicationContext(), RingtoneManager.TYPE_ALARM)) // 设置声音
                // setSound(
                // Uri.parse("android.resource://" + getPackageName()
                // + "/" + R.raw.wind)) 设定Raw下指定声音文件
                .setDefaults(Notification.DEFAULT_VIBRATE) // 振动
                .setFlags(Notification.FLAG_NO_CLEAR); // 是否可清除
        // 设置自定义通知layout,通知背景等可以在layout里设置
        build.setLayoutId(R.layout.notification);
        // 设置自定义通知内容id
        build.setLayoutTextId(R.id.content);
        // 设置自定义通知标题id
        build.setLayoutTitleId(R.id.title);
        // 设置自定义通知图片id
        build.setLayoutIconId(R.id.icon);
        // 设置自定义通知图片资源
        build.setLayoutIconDrawableId(R.mipmap.ic_launcher);
        // 设置状态栏的通知小图标
        build.setIcon(R.mipmap.ic_launcher);
        // 设置时间id
        build.setLayoutTimeId(R.id.time);
        // 若不设定以上自定义layout，又想简单指定通知栏图片资源（注：自定义layout和setNotificationLargeIcon两种方式指定图片资源只能选其一，不能同时使用）
        // build.setNotificationLargeIcon(R.drawable.logo);
        // 客户端保存build_id
//         XGPushManager.setPushNotificationBuilder(this, build_id, build);
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
//        android.os.Process.killProcess(android.os.Process.myPid());
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
