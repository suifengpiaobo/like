package com.aladdin.like.module.atlas.presenter;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.module.atlas.contract.AtlasContract;
import com.aladdin.like.model.ThemeModes;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/4/30 上午2:51.
 * Email:444288256@qq.com
 */
public class AtlasPresenter implements AtlasContract.Presenter {
    private AtlasContract.View mView;

    public AtlasPresenter(AtlasContract.View view){
        this.mView = view;

    }
    @Override
    public void start() {

    }

    @Override
    public void loadData(String openid,String themeName) {
        HttpManager.INSTANCE.getTheme(openid, themeName, new HttpResultCallback<ThemeModes>() {
            @Override
            public void onSuccess(ThemeModes result) {
                mView.setData(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                mView.showErrorTip(msg);
            }
        });
    }

    @Override
    public void addUserTheme(String openid, List<String> themeId,int operateType) {
        HttpManager.INSTANCE.addUserTheme(openid, themeId,operateType,new HttpResultCallback<ThemeModes>() {
            @Override
            public void onSuccess(ThemeModes result) {
                if (mView == null) return;

                mView.addThemeSuc();
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;
                mView.showErrorTip(msg);
            }
        });
    }


}
