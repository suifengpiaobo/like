package com.aladdin.like.model;

import java.util.List;

/**
 * Description 收藏的图片
 * Created by zxl on 2017/6/4 下午7:50.
 * Email:444288256@qq.com
 */
public class CollectionImage {
    public int total;
    public int per_page;
    public List<Collection> recordList;

    public class Collection{
        public String recordId;
        public String imageId;
        public String imageUrl;
        public String imageName;
        public String themeName;
        public String recordTimeStr;

        @Override
        public String toString() {
            return "Collection{" +
                    "recordId='" + recordId + '\'' +
                    ", imageId='" + imageId + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
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
