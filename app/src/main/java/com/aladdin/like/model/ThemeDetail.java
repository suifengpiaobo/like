package com.aladdin.like.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/6/4 下午6:54.
 * Email:444288256@qq.com
 */
public class ThemeDetail implements Serializable{
    public int total; //查询总条数
    public int per_page; //分页条数
    public List<Theme> imageList;

    public class Theme implements Serializable{
        public String imageId;
        public String imgeUrl;
        public String collectionTimes;//收藏次数
        public String imageName;

        @Override
        public String toString() {
            return "Theme{" +
                    "imageId='" + imageId + '\'' +
                    ", imgeUrl='" + imgeUrl + '\'' +
                    ", collectionTimes='" + collectionTimes + '\'' +
                    ", imageName='" + imageName + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ThemeDetail{" +
                "total=" + total +
                ", per_page=" + per_page +
                ", imageList=" + imageList +
                '}';
    }
}
