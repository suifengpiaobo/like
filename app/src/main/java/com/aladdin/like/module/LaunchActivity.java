package com.aladdin.like.module;

import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.LoginType;
import com.aladdin.like.constant.SharedPreferencesManager;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.module.register.RegisterActivity;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {
        if (SharedPreferencesManager.getLoginState() == LoginType.NOT_LOGIN){
            startThenKill(RegisterActivity.class);
        }else{//已登陆
            startThenKill(MainActivity.class);
        }
    }
}
