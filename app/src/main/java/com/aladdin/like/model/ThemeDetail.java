package com.aladdin.like.model;

import java.io.Serializable;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/6/4 下午6:54.
 * Email:444288256@qq.com
 */
public class ThemeDetail implements Serializable{

//    {
//        "code": 200,
//            "message": "success",
//            "data": {
//        "total": 3,
//                "per_page": 10,
//                "imageList": [
//        {
//            "pageSize": 10,
//                "pageIndex": 0,
//                "orderBy": " null ",
//                "imageId": 5,
//                "themeId": 11,
//                "imageName": "美女",
//                "imageUrl": "http://103.228.131.203/image/upload/20176/1497628391020.jpg",
//                "createTime": 1497542400000,
//                "createTimeStr": "2017-06-16 00:00:00",
//                "collectTimes": 0,
//                "mySqlLimit": "  null  limit -10, 10"
//        }
//        ]
//    }
//    }
    public int total; //查询总条数
    public int per_page; //分页条数
    public List<Theme> imageList;

    public class Theme implements Serializable{
        public String imageId;
        public String themeId;
        public String imageUrl;
        public int collectionTimes;//收藏次数
        public String imageName;
        public String createTime;
        public String createTimeStr;

        @Override
        public String toString() {
            return "Theme{" +
                    "imageId='" + imageId + '\'' +
                    ", themeId='" + themeId + '\'' +
                    ", imageUrl='" + imageUrl + '\'' +
                    ", collectionTimes='" + collectionTimes + '\'' +
                    ", imageName='" + imageName + '\'' +
                    ", createTime='" + createTime + '\'' +
                    ", createTimeStr='" + createTimeStr + '\'' +
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
