package com.aladdin.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;
import android.widget.ImageView;

import com.aladdin.base.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.drawee.view.SimpleDraweeView;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Description 加载图片工具类
 * Created by zxl on 2017/4/27 下午6:06.
 * Email:444288256@qq.com
 */
public class ImageLoaderUtils {
    private static int placeholderId = R.drawable.ic_image_loading;
    private static int errorId = R.drawable.ic_empty_picture;

    /**
     * 加载图片
     */
    public static void loadingImg(Context context, ImageView iv, String picUrl) {
        Glide.with(context)
                .load(picUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholderId)
                .error(errorId)
                .dontAnimate()
                .into(iv);
    }

    /**
     * 加载gif图
     */
    public static void loadGif(Context context, ImageView iv, String picUrl) {
        Glide.with(context)
                .load(picUrl)
                .asGif()
                .into(iv);
    }

    public static void loadingImg(Context context, ImageView iv, String picUrl, int placeholderAvatar) {
        Glide.with(context)
                .load(picUrl)
                .placeholder(placeholderAvatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorId)
                .dontAnimate()
                .into(iv);
    }

    public static void loadingImgWithError(Context context, ImageView iv, String picUrl, int errorAvatar) {
        Glide.with(context)
                .load(picUrl)
                .error(errorAvatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(iv);
    }

    public static void loadingImgWithBlur(Context context, ImageView iv, String picUrl) {
        Glide.with(context)
                .load(picUrl)
                .error(errorId)
                .bitmapTransform(new BlurTransformation(context, Glide.get(context).getBitmapPool()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(iv);
    }

    public static void loadingImgWithBlur(Context context, ImageView iv, String picUrl, int error) {
        Glide.with(context)
                .load(picUrl)
                .error(error)
                .bitmapTransform(new BlurTransformation(context, Glide.get(context).getBitmapPool()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(iv);
    }

    public static void displayRound(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorId)
                .centerCrop()
                .bitmapTransform(new RoundedCornersTransformation(context, 20, 0, RoundedCornersTransformation.CornerType.RIGHT))
                .dontAnimate()
                .crossFade(30)
                .into(imageView);
    }

    public static void displayRoundNative(Context context, ImageView imageView, int res) {
        Glide.with(context)
                .load(res)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorId)
                .centerCrop()
                .bitmapTransform(new RoundedCornersTransformation(context, 30, 0, RoundedCornersTransformation.CornerType.ALL))
                .dontAnimate()
                .crossFade(10)
                .into(imageView);
    }

    public static void displayCircle(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorId)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(context))
                .dontAnimate()
                .into(imageView);
    }

    public static void clear(Context context) {
        clearImageDiskCache(context);
        Glide.get(context).clearDiskCache();
    }

    /**
     * 清除图片磁盘缓存
     */
    private static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载本地图片（drawable图片）
     *
     * @param context
     * @param simpleDraweeView
     * @param id
     */
    public static void loadResPic(Context context, SimpleDraweeView simpleDraweeView, int id) {
        Uri uri = Uri.parse("res://" +
                context.getPackageName() +
                "/" + id);
        simpleDraweeView.setImageURI(uri);
    }

    /**
     * 加载本地图片（assets图片）
     *
     * @param context
     * @param simpleDraweeView
     * @param nameWithSuffix   带后缀的名称
     */
    public static void loadAssetsPic(Context context, SimpleDraweeView simpleDraweeView, String nameWithSuffix) {
        Uri uri = Uri.parse("asset:///" +
                nameWithSuffix);
        simpleDraweeView.setImageURI(uri);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param simpleDraweeView
     * @param nameWithSuffix
     */
    public static void loadLocalsPic(Context context, SimpleDraweeView simpleDraweeView, String nameWithSuffix) {
        Uri uri = Uri.parse("file://" +
                nameWithSuffix);
        simpleDraweeView.setImageURI(uri);
    }
}
