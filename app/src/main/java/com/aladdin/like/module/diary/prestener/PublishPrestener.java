package com.aladdin.like.module.diary.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.module.diary.contract.PublishContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/6/11 下午10:31.
 * Email:444288256@qq.com
 */
public class PublishPrestener implements PublishContract.Presenter{
    PublishContract.View mView;

    public PublishPrestener(PublishContract.View mView){
        this.mView = mView;
    }
    @Override
    public void start() {

    }

    @Override
    public void publishPic(String openid, String image, String diaryTitle, String diaryContent) {
        HttpManager.INSTANCE.addUserDiary(openid, image, diaryTitle, diaryContent, new HttpResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (mView == null) return;

                mView.showErrorTip(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;

                mView.showErrorTip(msg);
            }
        });
    }
}
