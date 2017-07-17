package com.aladdin.like.module.albym.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.ThemeDetail;

/**
 * Description
 * Created by zxl on 2017/7/17 下午6:59.
 * Email:444288256@qq.com
 */
public interface AlbymDetailsContract {
    interface View extends BaseView{
        void setAlbymData(ThemeDetail albymData);
    }
    interface Prestener extends BasePresenter{
        void getAlbymDetails(String openid, String albymId, int page, int page_num);
    }
}
