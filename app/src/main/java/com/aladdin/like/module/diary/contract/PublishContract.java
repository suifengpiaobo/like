package com.aladdin.like.module.diary.contract;


import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;

/**
 * Description
 * Created by zxl on 2017/6/11 下午10:31.
 * Email:444288256@qq.com
 */
public interface PublishContract {
    interface View extends BaseView {
        void publishSuc(String str);
        void publishFail(String str);
    }

    interface Presenter extends BasePresenter {
        void publishPic(String openid, String image, String diaryTitle, String diaryContent);
    }
}
