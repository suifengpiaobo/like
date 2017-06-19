package com.aladdin.like.module.diary.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.module.diary.contract.PublishContract;
import com.zxl.network_lib.Inteface.HttpResultListener;

import java.io.File;

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

        HttpManager.INSTANCE.addUserDiary(openid, image, new File(image), diaryTitle, diaryContent, new HttpResultListener() {
            @Override
            public void onSuccess(String str) {
                if (mView == null) return;

                mView.publishSuc(str);
            }

            @Override
            public void onFailure(String str) {
                if (mView == null) return;
                mView.publishFail(str);
            }
        });
    }
}
