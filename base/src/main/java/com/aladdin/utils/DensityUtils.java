package com.aladdin.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * Description
 * Created by zxl on 2017/4/27 下午5:58.
 * Email:444288256@qq.com
 */
public class DensityUtils {

    public static float mDensity;
    public static int mScreenWidth;
    public static int mScreenHeigth;

    public static int mStatusBarHeight;//手机状态栏

    private static Context mAppContext;

    private DensityUtils() {

    }

    public static void setAppContext(Context context) {
        mAppContext = context;
        mDensity = mAppContext.getResources().getDisplayMetrics().density;
        mScreenWidth = mAppContext.getResources().getDisplayMetrics().widthPixels;
        mScreenHeigth = mAppContext.getResources().getDisplayMetrics().heightPixels;
        //获取status_bar_height资源的ID
        int resourceId = mAppContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId >0){
            mStatusBarHeight = mAppContext.getResources().getDimensionPixelSize(resourceId);
        }
        if (mScreenWidth > mScreenHeigth) {
            mScreenWidth = mAppContext.getResources().getDisplayMetrics().heightPixels;
            mScreenHeigth = mAppContext.getResources().getDisplayMetrics().widthPixels;
        }
    }

    /**
     * dp converter to px
     *
     * @param dpValue before computing dp
     * @return after computing px
     */
    public static int dip2px(float dpValue) {
        float scale = mAppContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px converter to dp
     *
     * @param pxValue before computing px
     * @return after computing dp
     */
    public static int px2dip(float pxValue) {
        float scale = mAppContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px converter to sp, To ensure that the same text size
     *
     * @param pxValue before computing px
     * @return after computing sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = mAppContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp converter to px, To ensure that the same text size
     *
     * @param spValue before computing sp
     * @return after computing px
     */
    public static int sp2px(float spValue) {
        float fontScale = mAppContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * Gets the screen width and height, the unit is px
     *
     * @return point.x : width ,point.y : height
     */
    public static Point getScreenMetrics() {
        DisplayMetrics dm = mAppContext.getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        return new Point(w_screen, h_screen);

    }

    /**
     * Get screen aspect ratio
     *
     * @return screen aspect ratio
     */
    public static float getScreenRate() {
        Point P = getScreenMetrics();
        float H = P.y;
        float W = P.x;
        return (H / W);
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * context.getResources().getDisplayMetrics().density 1dp对应多少px，加0.5是为了四舍五入
     */
    public static int dip2px(Context context, float dpValue) {
        //这个得到的不应该叫做密度，应该是密度的一个比例，在160dpi手机上这个值是1,dpi屏幕像素密度（是基准）
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
