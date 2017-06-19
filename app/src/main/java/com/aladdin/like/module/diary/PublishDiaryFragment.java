package com.aladdin.like.module.diary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aladdin.base.BaseActivity;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.module.diary.contract.PublishContract;
import com.aladdin.like.module.diary.prestener.PublishPrestener;
import com.aladdin.like.utils.FileUtils;
import com.aladdin.utils.ImageLoaderUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yalantis.ucrop.ui.AlbumDirectoryActivity;
import com.yalantis.ucrop.ui.ImageGridActivity;
import com.yalantis.ucrop.util.Constants;
import com.yalantis.ucrop.util.LocalMediaLoader;
import com.yalantis.ucrop.util.Options;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 发布日记页面
 * Created by zxl on 2017/5/21 下午9:25.
 */
public class PublishDiaryFragment extends BaseActivity implements PublishContract.View {
    PublishContract.Presenter mPresenter;

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.finish)
    TextView mFinish;
    @BindView(R.id.shoose_picture)
    SimpleDraweeView mShoosePicture;
    @BindView(R.id.add_picture)
    ImageView mAddPicture;
    @BindView(R.id.description)
    EditText mDescription;
    @BindView(R.id.root_view)
    LinearLayout mRootView;

    private int selectType = LocalMediaLoader.TYPE_IMAGE;
    private int copyMode = Constants.COPY_MODEL_DEFAULT;
    private int selectMode = Constants.MODE_SINGLE;

    String mPath;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_diary;
    }

    @Override
    protected void initView() {
        mPresenter = new PublishPrestener(this);


//        ((MainActivity) getActivity()).setOnChoosePictureListener(new MainActivity.onChoosePictureListener() {
//            @Override
//            public void onChooseListener(String path) {
//                mAddPicture.setVisibility(View.GONE);
//                ImageLoaderUtils.loadLocalsPic(getActivity(), mShoosePicture, path);
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageGridActivity.REQUEST_IMAGE:
                    ArrayList<String> result = (ArrayList<String>) data.getSerializableExtra(ImageGridActivity.REQUEST_OUTPUT);
                    if (result != null) {
                        mAddPicture.setVisibility(View.GONE);
                        ImageLoaderUtils.loadLocalsPic(PublishDiaryFragment.this, mShoosePicture, result.get(0));
                    }
                    break;
            }
        }
    }


    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewSaveToImage(mRootView);
        }
    };

    @OnClick({R.id.back, R.id.finish, R.id.add_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.finish:
                new Handler().post(runnable);
                break;
            case R.id.add_picture:
                choosePicture();
                break;
        }
    }

    void choosePicture() {
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
        AlbumDirectoryActivity.startPhoto(PublishDiaryFragment.this, options);
    }

    public void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        // 添加水印
//        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp,
//                "@ Zhang Phil"));

        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = new File(FileUtils.getImageRootPath());
                File file = new File(sdRoot, UUID.randomUUID().toString().substring(0,16)+".png");
                mPath = file.getAbsolutePath();
                fos = new FileOutputStream(file);
            } else
                throw new Exception("创建文件失败!");

            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.destroyDrawingCache();
        mPresenter.publishPic(LikeAgent.getInstance().getUid(), mPath, "", mDescription.getText().toString());


        mPath = "";
        mDescription.setText("");
        mShoosePicture.setImageURI("");
        mAddPicture.setVisibility(View.VISIBLE);
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        });
    }

    @Override
    public void publishSuc(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PublishDiaryFragment.this,"发布成功",Toast.LENGTH_SHORT).show();
                mPath = "";
                mDescription.setText("");
                mShoosePicture.setImageURI("");
                mAddPicture.setVisibility(View.VISIBLE);
                finish();
            }
        });
    }

    @Override
    public void publishFail(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(str);
            }
        });
    }
}
