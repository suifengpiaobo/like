package com.aladdin.like.module.search.prestener;

import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.ThemeModes;
import com.aladdin.like.module.search.contract.SearchContract;
import com.zxl.network_lib.Inteface.HttpResultCallback;

/**
 * Description
 * Created by zxl on 2017/6/11 下午12:15.
 * Email:444288256@qq.com
 */
public class SearchPrestener implements SearchContract.Presenter {
    private SearchContract.View mView;

    public SearchPrestener(SearchContract.View mView){
        this.mView = mView;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadData(String openid) {
        HttpManager.INSTANCE.getTheme(openid, "",new HttpResultCallback<ThemeModes>() {
            @Override
            public void onSuccess(ThemeModes result) {
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

    @Override
    public void searchData(String openid, String themeName) {
        HttpManager.INSTANCE.getTheme(openid, themeName, new HttpResultCallback<ThemeModes>() {
            @Override
            public void onSuccess(ThemeModes result) {
                if (mView == null) return;

                mView.setResultData(result);
            }

            @Override
            public void onFailure(String code, String msg) {
                if (mView == null) return;
                mView.showErrorTip(msg);
            }
        });
    }
}
