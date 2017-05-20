package com.aladdin.like.module.circle;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.widget.NormalTitleBar;
import com.yalantis.ucrop.ui.AlbumDirectoryActivity;
import com.yalantis.ucrop.ui.ImageGridActivity;
import com.yalantis.ucrop.util.Constants;
import com.yalantis.ucrop.util.LocalMediaLoader;
import com.yalantis.ucrop.util.Options;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description 发布到日记
 * Created by zxl on 2017/5/1 上午10:27.
 */
public class SendCircleActivity extends BaseActivity {

    @BindView(R.id.title)
    NormalTitleBar mTitle;
    @BindView(R.id.add_picture)
    ImageView mAddPicture;
    @BindView(R.id.title_des)
    EditText mTitleDes;
    @BindView(R.id.desctiption)
    EditText mDesctiption;
    @BindView(R.id.choose_picture_img)
    ImageView mChoosePictureImg;
    @BindView(R.id.choose_picture_rl)
    RelativeLayout mChoosePictureRl;

    private int selectType = LocalMediaLoader.TYPE_IMAGE;
    private int copyMode = Constants.COPY_MODEL_DEFAULT;
    private int selectMode = Constants.MODE_SINGLE;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_send_circle;
    }

    @Override
    protected void initView() {
        mAddPicture.requestFocus();
        initTitle();
    }

    private void initTitle() {
        mTitle.setBackVisibility(true);
        mTitle.setRightTitleVisibility(false);
        mTitle.setTvRightVisibility(true);

        mTitle.setOnBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitle.setOnRightTvListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SendCircleSucActivity.class);
            }
        });
    }

    @OnClick(R.id.add_picture)
    public void onViewClicked() {
//        PictureChooseDialog dialog = new PictureChooseDialog();
//        dialog.show(getSupportFragmentManager(),"picture_dialog");

        /**
         * type --> 1图片 or 2视频
         * copyMode -->裁剪比例，默认、1:1、3:4、3:2、16:9
         * maxSelectNum --> 可选择图片的数量
         * selectMode         --> 单选 or 多选
         * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
         * isPreview    --> 是否打开预览选项
         * isCrop       --> 是否打开剪切选项
         * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
         * 注意-->type为2时 设置isPreview or isCrop 无效
         * 注意：Options可以为空，默认标准模式
         */
        Options options = new Options();
        options.setType(selectType);
        options.setCopyMode(copyMode);
        options.setMaxSelectNum(1);
        options.setSelectMode(selectMode);
        options.setShowCamera(true);
        options.setEnablePreview(true);
        options.setEnableCrop(false);
        options.setPreviewVideo(false);
        AlbumDirectoryActivity.startPhoto(SendCircleActivity.this, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageGridActivity.REQUEST_IMAGE:
                    ArrayList<String> result = (ArrayList<String>) data.getSerializableExtra(ImageGridActivity.REQUEST_OUTPUT);
                    if (result != null) {
                        mAddPicture.setVisibility(View.GONE);
                        Log.e("SendCircleActivity", "result---->>>" + result);
                        ImageLoaderUtils.loadingImg(SendCircleActivity.this, mChoosePictureImg, result.get(0));
                    }
                    break;
            }
        }
    }

    //点击空白隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final View v = this.getWindow().peekDecorView();
        return hiddenInputMethodManager(v);
    }

    //隐藏键盘
    protected boolean hiddenInputMethodManager(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
