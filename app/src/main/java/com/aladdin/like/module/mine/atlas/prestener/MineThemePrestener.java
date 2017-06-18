package com.aladdin.like.module.mine.atlas.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.mine.atlas.contract.MineThemeContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;

import java.util.List;

/**
 * Description
 * Created by zxl on 2017/6/11 下午8:56.
 * Email:444288256@qq.com
 */
public class MineThemePrestener implements MineThemeContract.Presenter {
    MineThemeContract.View mView;

    public MineThemePrestener(MineThemeContract.View mView){
        this.mView = mView;
    }
    @Override
    public void start() {

    }

    @Override
    public void getTheme(String openid) {
        HttpManager.INSTANCE.getUserTheme(openid,new HttpResultCallback<ThemeModes>() {
            @Override
            public void onSuccess(ThemeModes result) {
                if (mView == null) return;

                mView.setThemeData(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;

                mView.showErrorTip(msg);
            }
        });
    }

    @Override
    public void addUserTheme(String openid, List<String> themeId, int operateType) {
        HttpManager.INSTANCE.addUserTheme(openid, themeId,operateType,new HttpResultCallback<String>() {
            @Override
            public void onSuccess(String result) {
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
