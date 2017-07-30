package com.aladdin.like.module.mine;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.http.HttpManager;
import com.aladdin.like.model.UserInfo;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.module.mine.atlas.MineAtlasFragment;
import com.aladdin.like.module.mine.diary.MineDiraryFragment;
import com.aladdin.like.module.mine.pictures.MinePictureFragment;
import com.aladdin.like.module.set.SettingActivity;
import com.aladdin.like.receiver.NotificationService;
import com.aladdin.like.utils.UriUtils;
import com.aladdin.like.widget.HViewPager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sunfusheng.glideimageview.GlideImageView;
import com.zxl.network_lib.Inteface.HttpResultCallback;
import com.zxl.network_lib.Inteface.HttpResultListener;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Description
 * Created by zxl on 2017/5/24 下午8:18.
 */
public class MineFragment2 extends BaseFragment {
    @BindView(R.id.bg)
    GlideImageView mImageView;
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

    Uri mUri;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine_fragment2;
    }

    @Override
    protected void initView() {
        mAdapter = new Mine2PagerAdapter(getChildFragmentManager());
        mAdapter.addFragment(new MineAtlasFragment(), "主题");
        mAdapter.addFragment(new MinePictureFragment(), "图片");
        mAdapter.addFragment(new MineDiraryFragment(), "日记");
        mMineInfoViewpager.setAdapter(mAdapter);

        mTabLayout.addTab(mTabLayout.newTab().setText("主题"));//给TabLayout添加Tab
        mTabLayout.addTab(mTabLayout.newTab().setText("图片"));
        mTabLayout.addTab(mTabLayout.newTab().setText("日记"));

        mTabLayout.setupWithViewPager(mMineInfoViewpager);

        int count = NotificationService.getInstance(getActivity()).getNewMessageCount();
        if (count >0){
            mNewMessage.setVisibility(View.VISIBLE);
        }else{
            mNewMessage.setVisibility(View.GONE);
        }

        mUserAvatar.setImageURI(LikeAgent.getInstance().getUserPojo().headimgurl);
        mUserName.setText(LikeAgent.getInstance().getUserPojo().nickname);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        ((MainActivity)getActivity()).setChoosePicListener(new MainActivity.onChoosePicListener() {
            @Override
            public void onFile(Uri path) {
                if (path != null) {
                    mUri = path;
                    new Handler().post(runnable);
                } else {
                    Toast.makeText(getActivity(), "Cannot retrieve cropped image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        getActivity().startActivityForResult(intent, MainActivity.REQUEST_SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final Uri selectedUri = data.getData();
    }

    @Override
    protected void lazyFetchData() {
        getUserBackground();
    }

    public void getUserBackground(){
        HttpManager.INSTANCE.getUserInfo(LikeAgent.getInstance().getOpenid(), new HttpResultCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo result){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result != null){
                            if (!TextUtils.isEmpty(result.backgroudUrl)){
                                mImageView.loadImage(result.backgroudUrl,R.drawable.mine_title);
                            }else{
                                mImageView.loadLocalImage(R.drawable.mine_title,0);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(String code, String msg) {

            }
        });
    }

    @OnClick(R.id.set)
    public void onViewClicked() {
        startActivity(SettingActivity.class);
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            changeBg(mUri);
        }
    };

    public void changeBg(Uri url){
        String path = UriUtils.getRealFilePath(getActivity(),url);
        File file1 = new File(path);
        Luban.get(getActivity()).load(file1).putGear(Luban.THIRD_GEAR).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(File file) {
                HttpManager.INSTANCE.addUserImg(LikeAgent.getInstance().getOpenid(),file.getName(), file, new HttpResultListener() {
                    @Override
                    public void onSuccess(String str) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mImageView.loadLocalImage(file.getAbsolutePath(),0);
                            }
                        });
                    }

                    @Override
                    public void onFailure(String str) {

                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        }).launch();
    }



}
