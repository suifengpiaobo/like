package com.aladdin.like.module.main;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.base.BaseFragment;
import com.aladdin.like.base.BaseFragmentAdapter;
import com.aladdin.like.module.circle.CircleFragment;
import com.aladdin.like.module.mine.MineFragment2;
import com.aladdin.like.module.search.SearchFragment;
import com.aladdin.like.widget.NoScrollViewPager;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 主页
 * Created by zxl on 2017/4/29 下午9:48.
 */
public class MainActivity extends BaseActivity {
    @BindView(R.id.main_view_pager)
    NoScrollViewPager mMainViewPager;
    @BindView(R.id.main_page)
    ImageView mMainPage;
    @BindView(R.id.search_page)
    ImageView mSearchPadge;
    @BindView(R.id.circle_page)
    ImageView mCirclePage;
    @BindView(R.id.mine_page)
    ImageView mMinePage;
    @BindView(R.id.buttom_ll)
    LinearLayout mLayout;

    @BindView(R.id.main_page_rl)
    RelativeLayout mMainPageRl;
    @BindView(R.id.search_page_rl)
    RelativeLayout mSearchPageRl;
    @BindView(R.id.circle_page_rl)
    RelativeLayout mCirclePageRl;
    @BindView(R.id.mine_page_rl)
    RelativeLayout mMinePageRl;

    private List<BaseFragment> mFragments;
    private MainFragment mMainFragment;
    private SearchFragment mSearchFragment;
    private CircleFragment mCircleFragment;
    private MineFragment2 mMineFragment;
    private BaseFragmentAdapter mAdapter;

    onSearchClickListener mSearchClickListener;
    onCircleClickListener mOnCircleClickListener;

    onChoosePicListener mChoosePicListener;

    public static final int REQUEST_SELECT_PICTURE = 0x03;
    public static final String SAMPLE_CROPPED_IMAGE_NAME = "CropImage_";

    int currentTabPosition = 0;
    public static final String CURRENT_TAB_POSITION = "HOME_CURRENT_TAB_POSITION";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        mFragments = new ArrayList<>();
        mMainFragment = new MainFragment();
        mSearchFragment = new SearchFragment();
        mCircleFragment = new CircleFragment();
        mMineFragment = new MineFragment2();

        mFragments.add(mMainFragment);
        mFragments.add(mSearchFragment);
        mFragments.add(mCircleFragment);

        if (TextUtils.isEmpty(LikeAgent.getInstance().getOpenid())){
            mMinePageRl.setVisibility(View.GONE);
        }else{
            mMinePageRl.setVisibility(View.VISIBLE);
            mFragments.add(mMineFragment);
        }

//        resetTab();
        mAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), mFragments);
        mMainViewPager.setAdapter(mAdapter);
        mMainViewPager.setCurrentItem(currentTabPosition);
        mMainPage.setSelected(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //崩溃前保存位置
        outState.putInt(CURRENT_TAB_POSITION, currentTabPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentTabPosition = savedInstanceState.getInt(CURRENT_TAB_POSITION);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_SELECT_PICTURE:
                    final Uri selectedUri = data.getData();
                    if (selectedUri != null) {
                        startCropActivity(data.getData());
                    }
                    break;
                case UCrop.REQUEST_CROP:
                    final Uri resultUri = UCrop.getOutput(data);
                    if (mChoosePicListener != null){
                        mChoosePicListener.onFile(resultUri);
                    }
//                    handleCropResult(data);
                    break;
            }
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME+System.currentTimeMillis();
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(MainActivity.this);
    }

    private UCrop basisConfig(@NonNull UCrop uCrop) {
        uCrop = uCrop.useSourceImageAspectRatio();
//        uCrop = uCrop.withAspectRatio(ratioX, ratioY);
        return uCrop;
    }

    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(90);
        options.setHideBottomControls(true);
        options.setFreeStyleCropEnabled(true);
        return uCrop.withOptions(options);
    }

    private void resetTab() {
        mMainPage.setSelected(false);
        mSearchPadge.setSelected(false);
        mCirclePage.setSelected(false);
        mMinePage.setSelected(false);
    }

    @OnClick({R.id.main_page_rl, R.id.search_page_rl, R.id.circle_page_rl, R.id.mine_page_rl})
    public void onViewClicked(View view) {
        resetTab();
        switch (view.getId()) {
            case R.id.main_page_rl:
                currentTabPosition = 0;
                mMainPage.setSelected(true);
                break;
            case R.id.search_page_rl:
                currentTabPosition = 1;
                mSearchPadge.setSelected(true);
                break;
            case R.id.circle_page_rl:
                currentTabPosition = 2;
                mCirclePage.setSelected(true);
                break;
            case R.id.mine_page_rl:
                currentTabPosition = 3;
                mMinePage.setSelected(true);
                break;
            default:
//                mLayout.setBackgroundColor(getResources().getColor(R.color.color_e6ffffff));
                break;
        }
        mMainViewPager.setCurrentItem(currentTabPosition, false);
    }

    public void setSearchClickListener(onSearchClickListener searchClickListener) {
        mSearchClickListener = searchClickListener;
    }

    public void setOnCircleClickListener(onCircleClickListener onCircleClickListener) {
        mOnCircleClickListener = onCircleClickListener;
    }

    public void setChoosePicListener(onChoosePicListener choosePicListener) {
        mChoosePicListener = choosePicListener;
    }

    public interface onSearchClickListener {
        void onSearch(String s);
    }

    public interface onCircleClickListener {
        void onClick();
    }

    public interface onChoosePicListener{
        void onFile(Uri path);
    }

    //隐藏键盘
    protected boolean hiddenInputMethodManager(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    //点击空白隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final View v = this.getWindow().peekDecorView();
        return hiddenInputMethodManager(v);
    }


}
