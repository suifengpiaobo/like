package com.aladdin.like.module.set;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.like.module.about.AboutActivity;
import com.aladdin.like.module.message.MineMessageActivity;
import com.aladdin.like.module.watermark.WaterMarkActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description设置
 * Created by zxl on 2017/5/20 下午8:42.
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.mine_message)
    RelativeLayout mMineMessage;
    @BindView(R.id.mine_picture)
    LinearLayout mMinePicture;
    @BindView(R.id.mine_about)
    LinearLayout mMineAbout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.back, R.id.mine_message, R.id.mine_picture, R.id.mine_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.mine_message:
                startActivity(MineMessageActivity.class);
                break;
            case R.id.mine_picture:
                startActivity(WaterMarkActivity.class);
                break;
            case R.id.mine_about:
                startActivity(AboutActivity.class);
                break;
        }
    }
}
