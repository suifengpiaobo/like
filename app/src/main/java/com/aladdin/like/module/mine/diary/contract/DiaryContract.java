package com.aladdin.like.module.mine.diary.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.DiaryDetail;

/**
 * Description
 * Created by zxl on 2017/6/11 下午9:22.
 * Email:444288256@qq.com
 */
public interface DiaryContract {
    interface View extends BaseView {
        void setUserDiary(DiaryDetail detail);
    }
    interface Presenter extends BasePresenter {
        void getUserDiary(String openid,int page,int page_num);
    }
}
