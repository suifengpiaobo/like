package com.aladdin.like.module.download.contract;

import com.aladdin.base.BasePresenter;
import com.aladdin.base.BaseView;
import com.aladdin.like.model.PrefecturePojo;

/**
 * Description
 * Created by zxl on 2017/5/1 上午3:33.
 * Email:444288256@qq.com
 */
public interface PictureDetailsContract {
    interface View extends BaseView{
        void setData(PrefecturePojo prefecture);
    }

    interface Prestener extends BasePresenter{
        void getData(String url);
    }
}
