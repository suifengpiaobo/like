package com.aladdin.like.module.mine.pictures.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.CollectionImage;

/**
 * Description
 * Created by zxl on 2017/6/11 下午9:09.
 * Email:444288256@qq.com
 */
public interface PictureContract {
    interface View extends BaseView {
        void setCollectImage(CollectionImage collectImage);
    }

    interface Presenter extends BasePresenter {
        void getPicture(String openid, int page, int page_num);
    }
}
