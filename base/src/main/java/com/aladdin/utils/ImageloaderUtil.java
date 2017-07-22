package com.aladdin.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * Description imageloader封装
 * Created by zxl on 2017/6/28 上午11:34.
 * Email:444288256@qq.com
 */
public class ImageloaderUtil {

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
     * 加载圆角图
     * @param imgUrl
     * @param img
     * @param drawableId
     */
    public void loadRoundImaFromUrl(String imgUrl, ImageView img,int drawableId){
        ImageLoader loder = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId) // 设置正在下载是显示的图片
                .showImageForEmptyUri(drawableId)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(drawableId)// 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)// 是否緩存都內存中
                .cacheOnDisk(false)// 是否緩存到sd卡上
                .considerExifParams(true) // 启用EXIF和JPEG图像格式
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
        loder.displayImage(imgUrl, img, options);
    }

    //圆角图片
//    options2 = new DisplayImageOptions.Builder()
//            .showStubImage(R.drawable.ic_stub)
//                .showImageForEmptyUri(R.mipmap.ic_launcher)
//                .showImageOnFail(R.drawable.ic_error)
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
//                .displayer(new RoundedBitmapDisplayer(20))
//            .build();
    //圆形图片
//    options3 = new DisplayImageOptions.Builder()
//            .showStubImage(R.drawable.ic_stub)
//                .showImageForEmptyUri(R.mipmap.ic_launcher)
//                .showImageOnFail(R.drawable.ic_error)
//                .cacheInMemory(true)
//                .cacheOnDisc(true)
//                .bitmapConfig(Bitmap.Config.ARGB_8888)   //设置图片的解码类型
//                .displayer(new Displayer(0))
//            .build();

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
    public void loadFromContent(String uri, ImageView imageView,int drawableId,int radius){
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
