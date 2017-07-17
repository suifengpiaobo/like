package com.aladdin.like.module.publishcollection.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.module.publishcollection.contract.CollectionContract;
import com.aladdin.utils.LogUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/6/25 上午11:13.
 * Email:444288256@qq.com
 */
public class CollectionPrestener implements CollectionContract.Presenter {
    CollectionContract.View mView;

    public CollectionPrestener(CollectionContract.View mView){
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public void getUserCollectionImage(String openid, int page, int page_num,int operateType) {
        HttpManager.INSTANCE.getUserCollectionImage(openid, page, page_num, operateType,new HttpResultCallback<CollectionImage>() {
            @Override
            public void onSuccess(CollectionImage result) {
                if (mView == null) return;

                LogUtil.i("---CollectionImage--->>>"+result);
                mView.setData(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;

                mView.showErrorTip(msg);
            }
        });
    }
}
