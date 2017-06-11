package com.aladdin.like.module.circle.prestener;

import com.aladdin.like.module.circle.contract.CircleContract;

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
    public void getData(String openid) {

    }
}
