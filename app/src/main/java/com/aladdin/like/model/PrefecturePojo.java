package com.aladdin.like.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description 专区
 * Created by zxl on 2017/4/30 上午2:44.
 * Email:444288256@qq.com
 */
public class PrefecturePojo implements Serializable{

    List<Prefecture> mPrefectures;

    public static class Prefecture implements Serializable{
        public String url;
        public long time;
        public int type;
        public String typeName;

        @Override
        public String toString() {
            return "PrefecturePojo{" +
                    "url='" + url + '\'' +
                    ", time=" + time +
                    ", type=" + type +
                    ", typeName='" + typeName + '\'' +
                    '}';
        }
    }
}
