package com.aladdin.like.module.download.presenter;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.AlbymModel;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.module.download.contract.PictureDetailsContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/5/1 上午3:36.
 * Email:444288256@qq.com
 */
public class PictureDetailsPrestener implements PictureDetailsContract.Prestener {
    PictureDetailsContract.View mView;

    public PictureDetailsPrestener(PictureDetailsContract.View view){
        this.mView = view;
    }

    @Override
    public void start() {
        mView.showLoading();
    }

    @Override
    public void getData(String openid, String themeId, int page, int page_num) {
        HttpManager.INSTANCE.getThemeDetail(openid, themeId, page, page_num, new HttpResultCallback<ThemeDetail>() {
            @Override
            public void onSuccess(ThemeDetail result) {
                if (mView == null) return;

                mView.setData(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;
                mView.showErrorTip(msg);
            }
        });
    }

    @Override
    public void collectionImage(String openid, String imageId) {
        HttpManager.INSTANCE.collectionImage(openid, imageId, new HttpResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (mView == null) return;

                mView.collectionResult(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;

                mView.collectionResult(msg);
            }
        });
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
}
