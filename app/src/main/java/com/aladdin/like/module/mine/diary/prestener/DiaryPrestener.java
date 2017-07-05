package com.aladdin.like.module.mine.diary.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.module.mine.diary.contract.DiaryContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/6/11 下午9:23.
 * Email:444288256@qq.com
 */
public class DiaryPrestener implements DiaryContract.Presenter {
    DiaryContract.View mView;

    public DiaryPrestener(DiaryContract.View mView){
        this.mView = mView;
    }
    @Override
    public void start() {

    }

    @Override
    public void getUserDiary(String openid, int page, int page_num) {
        HttpManager.INSTANCE.getUserDiary(openid, 0,page, page_num, new HttpResultCallback<DiaryDetail>() {
            @Override
            public void onSuccess(DiaryDetail result) {
                if (mView == null) return;

                mView.setUserDiary(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;

                mView.showErrorTip(msg);
            }
        });
    }
}
