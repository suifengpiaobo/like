package com.aladdin.like.module.set;

import android.os.Build;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.module.about.AboutActivity;
import com.aladdin.like.module.message.MineMessageActivity;
import com.aladdin.like.module.watermark.WaterMarkActivity;
import com.aladdin.like.receiver.NotificationService;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.SharedPreferencesUtil;

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
    @BindView(R.id.new_message)
    ImageView mNewMessage;
    @BindView(R.id.close_water)
    ToggleButton mToggleButton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        int count = NotificationService.getInstance(SettingActivity.this).getNewMessageCount();
        if (count >0){
            mNewMessage.setVisibility(View.VISIBLE);
        }else{
            mNewMessage.setVisibility(View.GONE);
        }

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mToggleButton.getLayoutParams();
        if (Build.VERSION.SDK_INT >= 21) {
            params.setMarginEnd(DensityUtils.dip2px(-30));
        } else {
            params.setMarginEnd(DensityUtils.dip2px(-10));
        }
        mToggleButton.setLayoutParams(params);

        mToggleButton.setChecked(SharedPreferencesUtil.INSTANCE.getBoolean(Constant.PUBLISH_WATER,false));

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mToggleButton.setChecked(true);
                    SharedPreferencesUtil.INSTANCE.putBoolean(Constant.PUBLISH_WATER,true);
                }else{
                    mToggleButton.setChecked(false);
                    SharedPreferencesUtil.INSTANCE.putBoolean(Constant.PUBLISH_WATER,false);
                }
            }
        });
    }

    @OnClick({R.id.back, R.id.mine_message, R.id.mine_picture, R.id.mine_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.mine_message:
                NotificationService.getInstance(SettingActivity.this).updateMessage();
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
