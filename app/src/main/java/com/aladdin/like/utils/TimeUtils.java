package com.aladdin.like.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description
 * Created by zxl on 2017/7/26 下午4:45.
 * Email:444288256@qq.com
 */
public class TimeUtils {
    public final static String FORMAT_DATE = "yyyy.MM.dd";

    //获取当前时间
    public static String getCurrentTime(){
        String current;
        long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DATE);
        Date date = new Date(time);
        current = format.format(date);
        return current;
    }
}
