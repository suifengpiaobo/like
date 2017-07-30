package com.aladdin.like.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description 收藏的图片
 * Created by zxl on 2017/6/4 下午7:50.
 * Email:444288256@qq.com
 */
public class CollectionImage implements Serializable {
    public int total;
    public int per_page;
    public List<Collection> recordList;

    public class Collection implements Serializable{
        public String nickName;
        public String avatar;
        public String collectTimes;
        public String recordId;
        public String imageId;
        public String resourceUrl;
        public String imageName;
        public String themeName;
        public String recordTimeStr;
        public int resourceType;//1、图片 2、日记
        public String resourceId;
        public int width;
        public int height;

        @Override
        public String toString() {
            return "Collection{" +
                    "recordId='" + recordId + '\'' +
                    ", imageId='" + imageId + '\'' +
                    ", imageUrl='" + resourceUrl + '\'' +
                    ", imageName='" + imageName + '\'' +
                    ", themeName='" + themeName + '\'' +
                    ", recordTimeStr='" + recordTimeStr + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "CollectionImage{" +
                "total=" + total +
                ", per_page=" + per_page +
                ", recordList=" + recordList +
                '}';
    }
}
