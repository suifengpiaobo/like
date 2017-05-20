package com.aladdin.like.module.download.presenter;

import com.aladdin.like.module.download.contract.PictureDetailsContract;

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

    }

    @Override
    public void getData(String url) {

    }
}
