package com.aladdin.like.module.circle;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.widget.CircleImageView;
import com.aladdin.widget.NormalTitleBar;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SendCircleSucActivity extends BaseActivity {
    @BindView(R.id.title)
    NormalTitleBar mTitle;
    @BindView(R.id.share_weixin)
    LinearLayout mShareWeixin;
    @BindView(R.id.share_weixin_friends)
    LinearLayout mShareWeixinFriends;
    @BindView(R.id.share_weixin_collect)
    LinearLayout mShareWeixinCollect;
    @BindView(R.id.save_img)
    ImageView mSaveImg;
    @BindView(R.id.save_title)
    TextView mSaveTitle;
    @BindView(R.id.save_desc)
    TextView mSaveDesc;
    @BindView(R.id.user_avatar)
    CircleImageView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.user_time)
    TextView mUserTime;
    @BindView(R.id.save_rl)
    RelativeLayout mSaveRl;


    private String mPicUrl;
    private String mTitles;
    private String mDesc;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_circle_suc;
    }

    @Override
    protected void initView() {
        initTitle();

        ImageLoaderUtils.displayRoundNative(SendCircleSucActivity.this,mSaveImg,R.mipmap.ic_github);
//        ImageLoaderUtils.displayRound(SendCircleSucActivity.this,mUserAvatar,R.mipmap.ic_github);
        Glide.with(this).load(R.mipmap.ic_github).bitmapTransform(new CropCircleTransformation(SendCircleSucActivity.this)).crossFade(10).into(mUserAvatar);
    }

    private void initTitle() {
        mTitle.setBackVisibility(true);
        mTitle.setTvTitleVisibility(true);
        mTitle.setTitleText("保存完成");
        mTitle.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.share_weixin, R.id.share_weixin_friends, R.id.share_weixin_collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.share_weixin:
                break;
            case R.id.share_weixin_friends:
                break;
            case R.id.share_weixin_collect:
                break;
        }
    }
}
