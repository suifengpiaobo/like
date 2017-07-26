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
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.like.module.mine.atlas.MineAtlasFragment;
import com.aladdin.like.module.mine.diary.MineDiraryFragment;
import com.aladdin.like.module.mine.pictures.MinePictureFragment;
import com.aladdin.like.module.set.SettingActivity;
import com.aladdin.like.receiver.NotificationService;
import com.aladdin.like.utils.UriUtils;
import com.aladdin.like.widget.HViewPager;
import com.aladdin.utils.LogUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sunfusheng.glideimageview.GlideImageView;
import com.zxl.network_lib.Inteface.HttpResultCallback;
import com.zxl.network_lib.Inteface.HttpResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

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
//        AppBarLayout appBar = (AppBarLayout) findViewById(R.id.bar_layout);
//        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setBehavior(new AppBarLayoutOverScrollViewBehavior());
//        appBar.setLayoutParams(params);

        mAdapter = new Mine2PagerAdapter(getFragmentManager());
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
//                    Bitmap bitmap = getBitmapFromUri(path);
//                    int width = bitmap.getWidth();
//                    int height = bitmap.getHeight();
//                    float scale = (DensityUtils.mScreenWidth / (float) width;
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mShoosePicture.getLayoutParams();
//                    params.height = (int) (height * scale);
//                    params.width = (int) (width * scale);
//                    mShoosePicture.setLayoutParams(params);
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
        LogUtil.i("---selectedUri--fragment-->>"+selectedUri);
    }

    @Override
    protected void lazyFetchData() {
        getUserBackground();
    }

    public void getUserBackground(){
        HttpManager.INSTANCE.getUserInfo(LikeAgent.getInstance().getOpenid(), new HttpResultCallback<String>() {
            @Override
            public void onSuccess(String result){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.i("---userinfo--result-->>"+result);
                        JSONObject object= null;
                        try {
                            object = new JSONObject(result);
                            String url = object.getString("data");
                            if (!TextUtils.isEmpty(url)){
                                mImageView.loadLocalImage(R.drawable.mine_title,0);
                            }else{
                                mImageView.loadImage(url,0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
        String path = UriUtils.getImageAbsolutePath(getActivity(),url);
        File file = new File(path);
        HttpManager.INSTANCE.addUserImg(LikeAgent.getInstance().getOpenid(), file, new HttpResultListener() {
            @Override
            public void onSuccess(String str) {
                mImageView.loadLocalImage(path,0);
            }

            @Override
            public void onFailure(String str) {

            }
        });
    }

}
