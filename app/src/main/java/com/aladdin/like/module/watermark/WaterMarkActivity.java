package com.aladdin.like.module.watermark;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 关闭水印
 * Created by zxl on 2017/5/20 下午8:38.
 */
public class WaterMarkActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.invite_friends)
    TextView mInviteFriends;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_water_mark;
    }

    @Override
    protected void initView() {

    }


    @OnClick({R.id.back, R.id.invite_friends})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.invite_friends:
                break;
        }
    }
}
