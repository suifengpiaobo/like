package com.aladdin.like.model;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/6/4 下午7:28.
 * Email:444288256@qq.com
 */
public class DiaryDetail {
    public int total;
    public int per_page;

    public List<Diary> recordList;

    class Diary{
        public String recordId;
        public String imageId;
        public String diarySubject;//日志题目
        public String diaryContent;//日志内容

        @Override
        public String toString() {
            return "Diary{" +
                    "recordId='" + recordId + '\'' +
                    ", imageId='" + imageId + '\'' +
                    ", diarySubject='" + diarySubject + '\'' +
                    ", diaryContent='" + diaryContent + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DiaryDetail{" +
                "total=" + total +
                ", per_page=" + per_page +
                ", recordList=" + recordList +
                '}';
    }
}
