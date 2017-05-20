package com.aladdin.like.module.about;

import android.widget.ImageView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 关于
 * Created by zxl on 2017/4/28 下午5:11.
 */
public class AboutActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
