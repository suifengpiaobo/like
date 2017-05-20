package com.aladdin.base;

/**
 * Description mvp基础View
 * Created by zxl on 2017/4/27 下午5:32.
 * Email:444288256@qq.com
 */
public interface BaseView {
    void showLoading();
    void stopLoading();
    void showErrorTip(String msg);
}
