package com.aladdin.like.module.download.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.ThemeDetail;

/**
 * Description
 * Created by zxl on 2017/5/1 上午3:33.
 * Email:444288256@qq.com
 */
public interface PictureDetailsContract {
    interface View extends BaseView {
        void setData(ThemeDetail themeDetail);

        void collectionResult(String result);
    }

    interface Prestener extends BasePresenter {
        void getData(String openid,String themeId, int page, int page_num);

        void collectionImage(String openid,String imageId);
    }
}
