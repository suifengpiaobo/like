package com.aladdin.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description 网络相关的工具类
 * Created by zxl on 2017/4/27 下午6:11.
 * Email:444288256@qq.com
 */
public class NetWorkUtil {
    public static final int NETWORK_WIFI = 1;    // wifi network
    public static final int NETWORK_4G = 4;    // "4G" networks
    public static final int NETWORK_3G = 3;    // "3G" networks
    public static final int NETWORK_2G = 2;    // "2G" networks
    public static final int NETWORK_UNKNOWN = 5;    // unknown network
    public static final int NETWORK_NO = -1;   // no network

    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;


    /**
     * 判断网络是否连接
     *
     * @param context 上下文
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    /**
     * 获取活动网络信息
     *
     * @param context 上下文
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * 打开网络设置界面
     *
     * @param context 上下文
     */
    public static void openWirelessSettings(Context context) {
        context.startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
    }

    /**
     * 判断网络是否可用
     *
     * @param context 上下文
     * @return {@code true}: 可用{@code false}: 不可用
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }

    /**
     * 判断网络是否是4G
     *
     * @param context 上下文
     * @return {@code true}: 是{@code false}: 不是
     */
    public static boolean is4G(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
    }

    /**
     * 判断wifi是否连接状态
     *
     * @param context 上下文
     * @return {@code true}: 连接{@code false}: 未连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取移动网络运营商名称
     * 如中国联通、中国移动、中国电信
     *
     * @param context 上下文
     * @return 移动网络运营商名称
     */
    public static String getNetworkOperatorName(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 获取移动终端类型
     *
     * @param context 上下文
     * @return 手机制式
     * <p>
     * {@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知
     * {@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为GSM，移动和联通
     * {@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为CDMA，电信
     * {@link TelephonyManager#PHONE_TYPE_SIP  } : 3
     */
    public static int getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }


    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     *
     * @param context 上下文
     * @return 网络类型
     * {@link #NETWORK_WIFI   } = 1;
     * {@link #NETWORK_4G     } = 4;
     * {@link #NETWORK_3G     } = 3;
     * {@link #NETWORK_2G     } = 2;
     * {@link #NETWORK_UNKNOWN} = 5;
     * {@link #NETWORK_NO     } = -1;
     */
    public static int getNetWorkType(Context context) {
        int netType = NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo(context);
        if (info != null && info.isAvailable()) {

            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = NETWORK_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = NETWORK_3G;
                        } else {
                            netType = NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 获取当前的网络类型(WIFI,2G,3G,4G)
     * 依赖上面的方法
     *
     * @param context 上下文
     * @return 网络类型名称
     */
    public static String getNetWorkTypeName(Context context) {
        switch (getNetWorkType(context)) {
            case NETWORK_WIFI:
                return "NETWORK_WIFI";
            case NETWORK_4G:
                return "NETWORK_4G";
            case NETWORK_3G:
                return "NETWORK_3G";
            case NETWORK_2G:
                return "NETWORK_2G";
            case NETWORK_NO:
                return "NETWORK_NO";
            default:
                return "NETWORK_UNKNOWN";
        }
    }

    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /**
     * 判断网址是否有效
     */
    public static boolean isLinkAvailable(String link) {
        Pattern pattern = Pattern.compile("^(http://|https://)?((?:[A-Za-z0-9]+-[A-Za-z0-9]+|[A-Za-z0-9]+)\\.)+([A-Za-z]+)[/\\?\\:]?.*$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(link);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
}
