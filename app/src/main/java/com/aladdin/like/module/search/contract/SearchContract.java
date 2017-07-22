package com.aladdin.like.module.search.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeModes;

/**
 * Description
 * Created by zxl on 2017/6/11 下午12:13.
 * Email:444288256@qq.com
 */
public interface SearchContract {
    interface View extends BaseView {
        void setData(ThemeModes data);
        void setResultData(ThemeModes data);

        void setAlbymData(AlbymModel albymData);
    }
    interface Presenter extends BasePresenter {
        void loadData(String openid);
        void searchData(String openid,String themeName);

        void getAlbym(String openid, String themeId, int page, int page_num);
    }
}
