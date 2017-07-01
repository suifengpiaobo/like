package com.aladdin.like.module.mine.atlas.contract;

import com.aladdin.like.base.BasePresenter;
import com.aladdin.like.base.BaseView;
import com.aladdin.like.model.ThemeModes;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/6/11 下午8:55.
 * Email:444288256@qq.com
 */
public interface MineThemeContract {
    interface View extends BaseView {
        void setThemeData(ThemeModes theme);

        void addThemeSuc();
    }

    interface Presenter extends BasePresenter {
        void getTheme(String openid);
        void addUserTheme(String openid, List<String> themeId,int operateType);
    }
}
