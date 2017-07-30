package com.aladdin.like.utils;

import android.os.Environment;

import com.aladdin.like.LikeApplication;
import com.aladdin.utils.ContextUtils;

import java.io.File;
import java.io.IOException;

/**
 * Description
 * Created by zxl on 2017/6/13 上午8:58.
 * Email:444288256@qq.com
 */
public class FileUtils {

    /**
     * 获取跟目录
     * @return
     */
    public static String getAppRootPath() {
        String filePath = "/like";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            filePath = Environment.getExternalStorageDirectory() + filePath;
        } else {
            filePath = ContextUtils.getInstance().getApplicationContext().getCacheDir() + filePath;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = null;
        File nomedia = new File(filePath + "/.nomedia");
        if (!nomedia.exists())
            try {
                nomedia.createNewFile();
            } catch (IOException e) {
            }
        return filePath;
    }

    public static String getImageRootPath() {
        String filePath = getAppRootPath() + "/image/";
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        file = null;
        return filePath;
    }

    /**
     * 删除文件以及文件夹中所有内容
     *
     * @param file
     */
    public static void deleteFileAndDirs(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File file2 : files) {
                    deleteFileAndDirs(file2);
                }
                file.delete();
            }
        }
    }

    public static String getPhotoDirectory() {
        String path;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            path = LikeApplication.APP_EXT_PATH + "/photos/";
        } else {
            path = LikeApplication.APP_PATH +"/Like/" + "/photos/";
        }

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        return path;
    }

}
