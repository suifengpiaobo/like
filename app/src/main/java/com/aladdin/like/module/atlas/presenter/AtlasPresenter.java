package com.aladdin.like.module.atlas.presenter;

import com.aladdin.like.module.atlas.contract.AtlasContract;

/**
 * Description
 * Created by zxl on 2017/4/30 上午2:51.
 * Email:444288256@qq.com
 */
public class AtlasPresenter implements AtlasContract.Presenter {
    private AtlasContract.View mView;

    public AtlasPresenter(AtlasContract.View view){
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
//                AtlasPicturePojo atlas = JsonUtils.deserialize(response,AtlasPicturePojo.class);
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
