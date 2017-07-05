package com.aladdin.like.module.circle.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.DiaryDetail;
import com.aladdin.like.module.circle.contract.CircleContract;
import com.aladdin.utils.LogUtil;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/6/11 下午9:48.
 * Email:444288256@qq.com
 */
public class Circlrprestener implements CircleContract.Presenter{
    CircleContract.View mView;
    public Circlrprestener(CircleContract.View mView){
        this.mView = mView;
    }
    @Override
    public void start() {

    }

    @Override
    public void getData(String openid,int auditSign,int page,int page_num) {
        LogUtil.i(""+page+"----"+page_num);
        HttpManager.INSTANCE.getUserDiary(openid,auditSign, page, page_num, new HttpResultCallback<DiaryDetail>() {
            @Override
            public void onSuccess(DiaryDetail result) {
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
}
