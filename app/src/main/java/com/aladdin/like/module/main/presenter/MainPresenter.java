package com.aladdin.like.module.main.presenter;

import com.aladdin.like.module.main.contract.MainContract;
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

    }

    @Override
    public void loadData(String url) {
//        OkHttpUtils.get(url, new OkHttpUtils.ResultCallback<String>() {
//            @Override
//            public void onSuccess(String response) {
//                PrefecturePojo atlas = JsonUtils.deserialize(response,PrefecturePojo.class);
//                mView.setData(atlas);
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                mView.showErrorTip(e.getMessage());
//            }
//        });
    }
}
