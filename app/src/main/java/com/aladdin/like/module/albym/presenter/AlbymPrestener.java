package com.aladdin.like.module.albym.presenter;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.module.albym.contract.AlbymContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/7/4 下午10:59.
 * Email:444288256@qq.com
 */
public class AlbymPrestener implements AlbymContract.Prestener{
    AlbymContract.View mView;

    public AlbymPrestener(AlbymContract.View mView){
        this.mView = mView;
    }
    @Override
    public void start() {

    }

    @Override
    public void getAlbymDetail(String openid, String themeId, int page, int page_num) {
        HttpManager.INSTANCE.getAlbymDetail(openid, themeId, page, page_num, new HttpResultCallback<AlbymModel>() {
            @Override
            public void onSuccess(AlbymModel result) {
                if (mView == null) return;

                mView.setAlbymData(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;
            }
        });
    }

    @Override
    public void getThemeDetail(String openid, String albymId, int page, int page_num) {
        HttpManager.INSTANCE.getThemeDetail(openid, albymId, page, page_num, new HttpResultCallback<ThemeDetail>() {
            @Override
            public void onSuccess(ThemeDetail result) {
                if (mView == null) return;
                mView.setAlbymPic(result);
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        });
    }
}
