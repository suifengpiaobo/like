package com.aladdin.like.module.atlas.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.ThemeModes;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/4/30 上午2:48.
 * Email:444288256@qq.com
 */
public interface AtlasContract {
    interface View extends BaseView {
        void setData(ThemeModes data);

        void addThemeSuc();
    }

    interface Presenter extends BasePresenter {
        void loadData(String openid,String themeName);
        void addUserTheme(String openid, List<String> themeId, int operateType);
    }
}
