package com.aladdin.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Description
 * Created by zxl on 2017/6/29 下午10:21.
 * Email:444288256@qq.com
 */
public class ImageloaderUtil {

    ImageLoader loader;

    static ImageloaderUtil instance;

    public static ImageloaderUtil getInstance(){
        if (instance == null){
            synchronized (ImageloaderUtil.class){
                if (instance == null){
                    instance = new ImageloaderUtil();
                }
            }
        }
        return instance;
    }

    /**
     *
     * @param imgUrl
     * @param img
     * @param drawableId 默认图片
     * @return
     */
    public void loadImaFromUrl(String imgUrl, ImageView img,int drawableId){
        ImageLoader loder = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId) // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 是否緩存都內存中
                .cacheOnDisk(false)// 是否緩存到sd卡上
                .considerExifParams(true) // 启用EXIF和JPEG图像格式
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        loder.displayImage(imgUrl, img, options);
    }

    /**
     * 下载原图
     * @param imgUrl
     * @param img
     * @param drawableId
     * @param radius
     */
    public void loadRadiusImgForUrl(String imgUrl, ImageView img,
                                    int drawableId, int radius){
        ImageLoader loder = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                // 是否緩存都內存中
                .cacheOnDisk(false)
                // 是否緩存到sd卡上
                .considerExifParams(true)
                // 启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(radius))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        loder.displayImage(imgUrl, img, options);
    }

    /**
     * 从SDCard加载圆角图片
     * @param uri
     * @param imageView
     * @param drawableId
     * @param radius
     */
    public void loadFromSDCard(String uri, ImageView imageView,int drawableId,int radius){
        ImageLoader loder = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                // 是否緩存都內存中
                .cacheOnDisk(false)
                // 是否緩存到sd卡上
                .considerExifParams(true)
                // 启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(radius))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        loder.displayImage("file://" + uri, imageView, options);
    }

    /**
     * 从assets文件夹读取图片
     * @param uri
     * @param imageView
     * @param drawableId
     * @param radius
     */
    public void loadFromAssets(String uri, ImageView imageView,int drawableId,int radius){
        ImageLoader loder = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                // 是否緩存都內存中
                .cacheOnDisk(false)
                // 是否緩存到sd卡上
                .considerExifParams(true)
                // 启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(radius))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        loder.displayImage("assets://" + uri, imageView, options);
    }

    /**
     * 从drawable中加载图片
     * @param uri
     * @param imageView
     * @param drawableId
     * @param radius
     */
    public void loadFromDrawable(String uri, ImageView imageView,int drawableId,int radius){
        ImageLoader loder = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                // 是否緩存都內存中
                .cacheOnDisk(false)
                // 是否緩存到sd卡上
                .considerExifParams(true)
                // 启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(radius))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        loder.displayImage("drawable://" + uri, imageView, options);
    }

    /**
     * 从 ContentProvider 中加载图片
     * @param uri
     * @param imageView
     * @param drawableId
     * @param radius
     */
    public void loadFromContent(String uri, ImageView imageView, int drawableId, int radius){
        ImageLoader loder = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)
                // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)
                // 是否緩存都內存中
                .cacheOnDisk(false)
                // 是否緩存到sd卡上
                .considerExifParams(true)
                // 启用EXIF和JPEG图像格式
                .displayer(new RoundedBitmapDisplayer(radius))
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        loder.displayImage("content://" + uri, imageView, options);
    }
}
