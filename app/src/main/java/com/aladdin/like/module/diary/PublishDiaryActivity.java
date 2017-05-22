package com.aladdin.like.module.diary;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.R;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.utils.ToastUtil;
import com.yalantis.ucrop.ui.AlbumDirectoryActivity;
import com.yalantis.ucrop.ui.ImageGridActivity;
import com.yalantis.ucrop.util.Constants;
import com.yalantis.ucrop.util.LocalMediaLoader;
import com.yalantis.ucrop.util.Options;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 发布日记页面
 * Created by zxl on 2017/5/21 下午9:25.
 */
public class PublishDiaryActivity extends BaseActivity {

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.finish)
    TextView mFinish;
    @BindView(R.id.shoose_picture)
    ImageView mShoosePicture;
    @BindView(R.id.add_picture)
    ImageView mAddPicture;
    @BindView(R.id.description)
    EditText mDescription;

    private int selectType = LocalMediaLoader.TYPE_IMAGE;
    private int copyMode = Constants.COPY_MODEL_DEFAULT;
    private int selectMode = Constants.MODE_SINGLE;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_diary;
    }

    @Override
    protected void initView() {

    }

    @OnClick({R.id.back, R.id.finish, R.id.add_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.finish:
                ToastUtil.sToastUtil.shortDuration("发布成功!");
                break;
            case R.id.add_picture:
                choosePicture();
                break;
        }
    }

    void choosePicture(){
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
        AlbumDirectoryActivity.startPhoto(PublishDiaryActivity.this, options);
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
                        ImageLoaderUtils.loadingImg(PublishDiaryActivity.this, mShoosePicture, result.get(0));
                    }
                    break;
            }
        }
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
