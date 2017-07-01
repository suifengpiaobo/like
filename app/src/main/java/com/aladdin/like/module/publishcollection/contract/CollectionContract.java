package com.aladdin.like.module.publishcollection.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.CollectionImage;

/**
 * Description 发布日记时选择收藏图片
 * Created by zxl on 2017/6/25 上午11:09.
 * Email:444288256@qq.com
 */
public interface CollectionContract {
    interface View extends BaseView {
        void setData(CollectionImage collectionImage);
    }

    interface Presenter extends BasePresenter {
        void getUserCollectionImage(String openid, int page, int page_num);
    }
}
