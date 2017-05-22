package com.aladdin.like.module.main.contract;

import com.aladdin.base.BasePresenter;
import com.aladdin.base.BaseView;
import com.aladdin.like.model.PrefecturePojo;

/**
 * Description
 * Created by zxl on 2017/4/30 下午4:04.
 * Email:444288256@qq.com
 */
public interface MainContract {
    interface View extends BaseView {
        void setData(PrefecturePojo.Prefecture data);
    }

    interface Presenter extends BasePresenter {
        void loadData(String url);
    }
}
