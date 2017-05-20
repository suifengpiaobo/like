package com.aladdin.like;

import com.aladdin.base.BaseApplication;
import com.aladdin.utils.ContextUtils;
import com.aladdin.utils.DensityUtils;

/**
 * Description
 * Created by zxl on 2017/4/28 下午5:16.
 * Email:444288256@qq.com
 */
public class LikeApplication extends BaseApplication {

    @Override
    public void initConfig() {
        ContextUtils.getInstance().setContext(this.getApplicationContext()); // Must!! First call this method.
        DensityUtils.setAppContext(this);
    }
}
