package com.aladdin.like.module.login;

import android.view.View;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.widget.NormalTitleBar;

import butterknife.BindView;

/**
 * Description 账号登录
 * Created by zxl on 2017/5/17 上午8:38.
 */
public class LoginAccountActivity extends BaseActivity {

    @BindView(R.id.title)
    NormalTitleBar mTitle;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_account;
    }

    @Override
    protected void initView() {
        mTitle.setTvTitleVisibility(false);
        mTitle.setSearchVisibility(false);
        mTitle.setTvLeft("");
        mTitle.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
