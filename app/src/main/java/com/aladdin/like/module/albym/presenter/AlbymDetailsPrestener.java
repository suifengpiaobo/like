package com.aladdin.like.module.albym.presenter;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.ThemeDetail;
import com.aladdin.like.module.albym.contract.AlbymDetailsContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/7/17 下午7:01.
 * Email:444288256@qq.com
 */
public class AlbymDetailsPrestener implements AlbymDetailsContract.Prestener {
    AlbymDetailsContract.View mView;

    public AlbymDetailsPrestener(AlbymDetailsContract.View view){
        this.mView = view;
    }
    @Override
    public void start() {

    }

    @Override
    public void getAlbymDetails(String openid, String albymId, int page, int page_num) {
        HttpManager.INSTANCE.getThemeDetail(openid, albymId, page, page_num, new HttpResultCallback<ThemeDetail>() {
            @Override
            public void onSuccess(ThemeDetail result) {
                if (mView == null) return;

                mView.setAlbymData(result);
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        });
    }
}
