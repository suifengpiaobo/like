package com.aladdin.like.module.main.presenter;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.main.contract.MainContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;
//import com.aladdin.utils.OkHttpUtils;

/**
 * Description
 * Created by zxl on 2017/4/30 下午4:05.
 * Email:444288256@qq.com
 */
public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenter(MainContract.View view){
        this.mView = view;
    }

    @Override
    public void start() {
        mView.showLoading();
    }

    @Override
    public void loadData(String openid) {
        HttpManager.INSTANCE.getUserTheme(openid, new HttpResultCallback<ThemeModes>() {
            @Override
            public void onSuccess(ThemeModes result) {
                if (mView == null) return;

                mView.stopLoading();
                mView.setData(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;
                mView.stopLoading();
                mView.showErrorTip(msg);
            }
        });
    }
}
