package com.aladdin.like.module.circle.contract;

import com.aladdin.base.BasePresenter;
import com.aladdin.base.BaseView;

/**
 * Description
 * Created by zxl on 2017/6/11 下午9:48.
 * Email:444288256@qq.com
 */
public interface CircleContract {
    interface View extends BaseView {
        void setData();
    }
    interface Presenter extends BasePresenter {
        void getData(String openid);
    }
}
