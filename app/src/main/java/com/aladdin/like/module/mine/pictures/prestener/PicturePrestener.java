package com.aladdin.like.module.mine.pictures.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.CollectionImage;
import com.aladdin.like.module.mine.pictures.contract.PictureContract;
import com.aladdin.utils.LogUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/6/11 下午9:17.
 * Email:444288256@qq.com
 */
public class PicturePrestener implements PictureContract.Presenter {
    PictureContract.View mView;

    public PicturePrestener(PictureContract.View mView){
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public void getPicture(String openid, int page, int page_num,int operateType) {
        HttpManager.INSTANCE.getUserCollectionImage(openid, page, page_num,operateType, new HttpResultCallback<CollectionImage>() {
            @Override
            public void onSuccess(CollectionImage result) {
                if (mView == null) return;
                LogUtil.i("---result--->>>"+result);
                mView.setCollectImage(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;

                mView.showErrorTip(msg);
            }
        });
    }
}
