package com.aladdin.like.module.atlas.contract;

import com.aladdin.base.BasePresenter;
import com.aladdin.base.BaseView;
import com.aladdin.like.model.AtlasPicturePojo;

/**
 * Description
 * Created by zxl on 2017/4/30 上午2:48.
 * Email:444288256@qq.com
 */
public interface AtlasContract {
    interface View extends BaseView{
        void setData(AtlasPicturePojo data);
    }

    interface Presenter extends BasePresenter{
        void loadData(String url);
    }
}
