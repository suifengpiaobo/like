package com.aladdin.like.module.circle.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.DiaryDetail;

/**
 * Description
 * Created by zxl on 2017/6/11 下午9:48.
 * Email:444288256@qq.com
 */
public interface CircleContract {
    interface View extends BaseView {
        void setData(DiaryDetail data);
        void collectionSucc();
    }
    interface Presenter extends BasePresenter {
        void getData(String openid,int auditSign,int page,int page_num);

        void collectionPic(String openid,String imageId);
    }
}
