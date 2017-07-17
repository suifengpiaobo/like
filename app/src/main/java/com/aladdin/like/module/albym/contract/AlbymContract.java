package com.aladdin.like.module.albym.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.AlbymModel;

/**
 * Description
 * Created by zxl on 2017/7/4 下午10:59.
 * Email:444288256@qq.com
 */
public interface AlbymContract {
    interface View extends BaseView{
        void setAlbymData(AlbymModel albymData);
//        void setAlbymPic(ThemeDetail themeDetail);
    }

    interface Prestener extends BasePresenter{
        void getAlbymDetail(String openid, String themeId, int page, int page_num);

//        void getThemeDetail(String openid, String albymId, int page, int page_num);
    }
}
