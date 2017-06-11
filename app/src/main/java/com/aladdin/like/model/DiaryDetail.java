package com.aladdin.like.model;

import java.util.List;

/**
 * Description 日记
 * Created by zxl on 2017/6/4 下午7:28.
 * Email:444288256@qq.com
 */
public class DiaryDetail {
    public int total;
    public int per_page;

    public List<Diary> diaryList;

    public class Diary{
        public String diaryId;
        public String diaryImage;
        public String diaryTimeStr;
        public String diaryTitle;//日志题目
        public String diaryContent;//日志内容

        @Override
        public String toString() {
            return "Diary{" +
                    "diaryId='" + diaryId + '\'' +
                    ", diaryImage='" + diaryImage + '\'' +
                    ", diaryTimeStr='" + diaryTimeStr + '\'' +
                    ", diaryTitle='" + diaryTitle + '\'' +
                    ", diaryContent='" + diaryContent + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DiaryDetail{" +
                "total=" + total +
                ", per_page=" + per_page +
                ", diaryList=" + diaryList +
                '}';
    }
}
