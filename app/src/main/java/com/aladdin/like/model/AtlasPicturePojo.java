package com.aladdin.like.model;

import java.util.List;

/**
 * Description 感兴趣的图集
 * Created by zxl on 2017/4/29 上午10:36.
 * Email:444288256@qq.com
 */
public class AtlasPicturePojo {
    public List<AtlasPicture> atlas;

    public static class AtlasPicture{
        public String name;
        public String image;
        public int type;

        @Override
        public String toString() {
            return "AtlasPicturePojo{" +
                    "name='" + name + '\'' +
                    ", image='" + image + '\'' +
                    ", type=" + type +
                    '}';
        }
    }

}
