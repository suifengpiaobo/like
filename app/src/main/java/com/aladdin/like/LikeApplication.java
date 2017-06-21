package com.aladdin.like;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.aladdin.base.BaseApplication;
import com.aladdin.like.utils.FontsOverrideUtil;
import com.aladdin.utils.ContextUtils;
import com.aladdin.utils.DensityUtils;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Description
 * Created by zxl on 2017/4/28 下午5:16.
 * Email:444288256@qq.com
 */
public class LikeApplication extends BaseApplication {

    @Override
    public void initConfig() {
        //替换字体
        FontsOverrideUtil.init(this);
        ContextUtils.getInstance().setContext(this.getApplicationContext()); // Must!! First call this method.
        DensityUtils.setAppContext(this);
        Fresco.initialize(this);
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
}
