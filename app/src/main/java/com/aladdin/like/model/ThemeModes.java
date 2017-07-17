package com.aladdin.like.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description 主题
 * Created by zxl on 2017/6/4 下午5:04.
 * Email:444288256@qq.com
 */
public class ThemeModes implements Serializable{
    public List<Theme> themeList;
    public class Theme implements Serializable{
        public String openid;
        public String themeId;
        public String themeName;
        public String themeImgUrl;//主题对应图片
        public String createTimeStr;//创建时间
        public int width;
        public int height;
        public float scale;

        public int followSign; //是否关注 1 关注 0 未关注 查询全部时 为空

        @Override
        public String toString() {
            return "Theme{" +
                    "openid='" + openid + '\'' +
                    ", themeId='" + themeId + '\'' +
                    ", themeName='" + themeName + '\'' +
                    ", themeImgUrl='" + themeImgUrl + '\'' +
                    ", createTimeStr='" + createTimeStr + '\'' +
                    ", followSign=" + followSign +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ThemeModes{" +
                "themeList=" + themeList +
                '}';
    }
}
