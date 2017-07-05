package com.aladdin.like.module;

import com.aladdin.like.LikeAgent;
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
//        if (!TextUtils.isEmpty(LikeAgent.getInstance().getUid())){
//            startActivity(MainActivity.class);
//        }else{
//            startActivity(RegisterActivity.class);
//        }
        if (SharedPreferencesManager.getLoginState() == LoginType.NOT_LOGIN){
            startActivity(RegisterActivity.class);
        }else{//已登陆
            if (LikeAgent.getInstance().isAuthed()){
                startActivity(MainActivity.class);
            }else {
                //登陆后进程被杀死走自动登陆
                LikeAgent.getInstance().autoLogin();
            }
        }
        finish();
    }
}
