package com.aladdin.like.module.mine;


import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.module.mine.atlas.MineAtlasFragment;
import com.aladdin.like.module.mine.diary.MineDiraryFragment;
import com.aladdin.like.module.mine.pictures.MinePictureFragment;
import com.aladdin.like.module.set.SettingActivity;
import com.aladdin.like.receiver.NotificationService;
import com.aladdin.like.widget.HViewPager;
import com.aladdin.utils.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description
 * Created by zxl on 2017/5/24 下午8:18.
 */
public class MineFragment2 extends BaseFragment {
    @BindView(R.id.user_avatar)
    SimpleDraweeView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.mine_info_viewpager)
    HViewPager mMineInfoViewpager;
    @BindView(R.id.set)
    ImageView mSet;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.new_message)
    ImageView mNewMessage;

    Mine2PagerAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_fragment2;
    }

    @Override
    protected void initView() {
        mAdapter = new Mine2PagerAdapter(getFragmentManager());
        mAdapter.addFragment(new MineAtlasFragment(), "主题");
        mAdapter.addFragment(new MinePictureFragment(), "图片");
        mAdapter.addFragment(new MineDiraryFragment(), "日记");
        mMineInfoViewpager.setAdapter(mAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("主题"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("图片"));
        mTabLayout.addTab(mTabLayout.newTab().setText("日记"));

        mTabLayout.setupWithViewPager(mMineInfoViewpager);
//        mMineInfoViewpager.CURRENT_PAGE = 0;

        if (LikeAgent.getInstance().getUserPojo() != null) {
            mUserAvatar.setImageURI(LikeAgent.getInstance().getUserPojo().headimgurl);
            mUserName.setText(LikeAgent.getInstance().getUserPojo().nickname);
            mDescription.setText("微信不给返回个性签名我也很无奈！");
        }

        int count = NotificationService.getInstance(getActivity()).getNewMessageCount();
        LogUtil.i("---message--count--->>>"+count);
        if (count >0){
            mNewMessage.setVisibility(View.VISIBLE);
        }else{
            mNewMessage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick(R.id.set)
    public void onViewClicked() {
        startActivity(SettingActivity.class);
    }
}
