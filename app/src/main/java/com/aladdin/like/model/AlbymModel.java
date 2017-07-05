package com.aladdin.like.model;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/7/4 下午11:54.
 * Email:444288256@qq.com
 */
public class AlbymModel {
    /**
     * code : 200
     * message : success
     * data : {"total":2,"albymList":[{"albymId":1,"themeId":20,"themeName":null,"albymName":"上海夜生活","albymUrl":"http://103.228.131.203/image/upload/20177/1499016902049.png","createTimeStr":"2017-06-26","width":"800","height":"800","collectTimes":0,"albymStatus":0},{"albymId":2,"themeId":20,"themeName":null,"albymName":"北京夜生活","albymUrl":"http://103.228.131.203/image/upload/20177/1499016928675.png","createTimeStr":"2017-06-26","width":"1024","height":"683","collectTimes":0,"albymStatus":0}],"per_page":10,"next_page":-1}
     */
    /**
     * total : 2
     * albymList : [{"albymId":1,"themeId":20,"themeName":null,"albymName":"上海夜生活","albymUrl":"http://103.228.131.203/image/upload/20177/1499016902049.png","createTimeStr":"2017-06-26","width":"800","height":"800","collectTimes":0,"albymStatus":0},{"albymId":2,"themeId":20,"themeName":null,"albymName":"北京夜生活","albymUrl":"http://103.228.131.203/image/upload/20177/1499016928675.png","createTimeStr":"2017-06-26","width":"1024","height":"683","collectTimes":0,"albymStatus":0}]
     * per_page : 10
     * next_page : -1
     */

    public int total;
    public int per_page;
    public int next_page;
    public List<AlbymDetail> albymList;

    public static class AlbymDetail {
        /**
         * albymId : 1
         * themeId : 20
         * themeName : null
         * albymName : 上海夜生活
         * albymUrl : http://103.228.131.203/image/upload/20177/1499016902049.png
         * createTimeStr : 2017-06-26
         * width : 800
         * height : 800
         * collectTimes : 0
         * albymStatus : 0
         */

        public int albymId;
        public int themeId;
        public Object themeName;
        public String albymName;
        public String albymUrl;
        public String createTimeStr;
        public String width;
        public String height;
        public int collectTimes;
        public int albymStatus;
    }
}
