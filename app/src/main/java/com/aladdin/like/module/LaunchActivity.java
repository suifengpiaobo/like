package com.aladdin.like.module;

import android.text.TextUtils;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.module.register.RegisterActivity;
import com.aladdin.utils.LogUtil;

public class LaunchActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initView() {
        LogUtil.i("---uid--->>>"+LikeAgent.getInstance().getUid());
        LikeAgent.getInstance().setUid("10000");
        if (!TextUtils.isEmpty(LikeAgent.getInstance().getUid())){
            startActivity(MainActivity.class);
        }else {
            startActivity(RegisterActivity.class);
        }

        finish();
    }
}
