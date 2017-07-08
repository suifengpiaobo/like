package com.aladdin.like.module.diary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.module.diary.contract.PublishContract;
import com.aladdin.like.module.diary.prestener.PublishPrestener;
import com.aladdin.like.module.publishcollection.ChooseCollectionActivity;
import com.aladdin.like.utils.FileUtils;
import com.aladdin.like.utils.WXUtils;
import com.aladdin.like.widget.PublishDialog;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
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
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.R.attr.width;

/**
 * Description 发布日记页面
 * Created by zxl on 2017/5/21 下午9:25.
 */
public class PublishDiaryFragment extends BaseActivity implements PublishContract.View {
    PublishContract.Presenter mPresenter;

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.finish)
    TextView mFinish;
    @BindView(R.id.shoose_picture)
    SimpleDraweeView mShoosePicture;
    @BindView(R.id.add_picture)
    ImageView mAddPicture;
    @BindView(R.id.description)
    EditText mDescription;
    @BindView(R.id.title_content)
    EditText mContent;
    @BindView(R.id.root_view)
    LinearLayout mRootView;


    @BindView(R.id.share_weixin)
    ImageView mShareWeixin;
    @BindView(R.id.share_friends)
    ImageView mShareFriends;
    @BindView(R.id.share_weixin_collection)
    ImageView mShareWeixinCollection;
    @BindView(R.id.share_info)
    LinearLayout mShareInfo;
    @BindView(R.id.publish_finish_pic)
    SimpleDraweeView mPublishFinishPic;
    @BindView(R.id.user_avatar)
    SimpleDraweeView mUserAvatar;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.publish_time)
    TextView mPublishTime;

    private int selectType = LocalMediaLoader.TYPE_IMAGE;
    private int copyMode = Constants.COPY_MODEL_DEFAULT;
    private int selectMode = Constants.MODE_SINGLE;

    String mPath;

    PublishDialog mDialog;

    int mFinishWidth,mFinishHeight;//生成的图片宽高

    @Override
    protected int getLayoutId() {
        return R.layout.activity_publish_diary;
    }

    @Override
    protected void initView() {
        mPresenter = new PublishPrestener(this);
        mTitle.setVisibility(View.GONE);
        mShareInfo.setVisibility(View.GONE);
        mPublishFinishPic.setVisibility(View.GONE);
        mRootView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case ImageGridActivity.REQUEST_IMAGE:
                    ArrayList<String> result = (ArrayList<String>) data.getSerializableExtra(ImageGridActivity.REQUEST_OUTPUT);
                    if (result != null) {
                        mAddPicture.setVisibility(View.GONE);
                        Bitmap bitmap = BitmapFactory.decodeFile(result.get(0));
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        float scale = (DensityUtils.mScreenWidth - DensityUtils.dip2px(30)) / (float) width;
                        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mShoosePicture.getLayoutParams();
                        params.height = (int) (height * scale);
                        params.width = (int) (width * scale);
                        mShoosePicture.setLayoutParams(params);
                        ImageLoaderUtils.loadLocalsPic(PublishDiaryFragment.this, mShoosePicture, result.get(0));
                    }
                    break;
                case ChooseCollectionActivity.CHOOSE_COLLECTION:
                    mAddPicture.setVisibility(View.GONE);
                    float scale = (DensityUtils.mScreenWidth - DensityUtils.dip2px(30)) / (float) data.getIntExtra("width", 0);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mShoosePicture.getLayoutParams();
                    params.height = (int) (data.getIntExtra("height", 0) * scale);
                    params.width = (int) (width * scale);
                    mShoosePicture.setLayoutParams(params);
                    mShoosePicture.setImageURI(data.getStringExtra("url"));
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

    @OnClick({R.id.back, R.id.finish, R.id.add_picture,R.id.share_weixin,
            R.id.share_friends, R.id.share_weixin_collection})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.finish:
                mFinish.requestFocus();
                mContent.setEnabled(false);
                mDescription.setEnabled(false);
                if (!TextUtils.isEmpty(LikeAgent.getInstance().getOpenid())){
                    new Handler().post(runnable);
                }else{
                    ToastUtil.showToast("请登录后发表日记");
                }
                break;
            case R.id.add_picture:
                showDialog();
                break;
            case R.id.share_weixin:
                Bitmap bitmap = BitmapFactory.decodeFile(mPath);
                WXUtils.shareBitmap(PublishDiaryFragment.this,bitmap, SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.share_friends:
                Bitmap bitmap1 = BitmapFactory.decodeFile(mPath);
                WXUtils.shareBitmap(PublishDiaryFragment.this,bitmap1, SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.share_weixin_collection:
                Bitmap bitmap2 = BitmapFactory.decodeFile(mPath);
                WXUtils.shareBitmap(PublishDiaryFragment.this,bitmap2, SendMessageToWX.Req.WXSceneFavorite);
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
        options.setEnableCrop(true);
        options.setPreviewVideo(false);
        AlbumDirectoryActivity.startPhoto(PublishDiaryFragment.this, options);
    }

    public void showDialog() {
        mDialog = new PublishDialog();
        mDialog.show(getSupportFragmentManager(), "share_dialog");
        mDialog.setDialogListener(new PublishDialog.onDialogListener() {
            @Override
            public void onLikeCollection() {
                mDialog.dismiss();
                startActivityForResult(ChooseCollectionActivity.class, ChooseCollectionActivity.CHOOSE_COLLECTION);
            }

            @Override
            public void onPhone() {
                mDialog.dismiss();
                choosePicture();
            }
        });
    }

    public void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        mFinishWidth = cachebmp.getWidth();
        mFinishHeight = cachebmp.getHeight();
        // 添加水印
//        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp,
//                "@ Zhang Phil"));

        FileOutputStream fos;
        File file = null;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
            File sdRoot;
            if (isHasSDCard) {
                // SD卡根目录
                sdRoot = new File(FileUtils.getImageRootPath());
                file = new File(sdRoot, UUID.randomUUID().toString().substring(0, 16) + ".jpeg");
                mPath = file.getAbsolutePath();
                fos = new FileOutputStream(file);
            } else
                throw new Exception("创建文件失败!");

            cachebmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.destroyDrawingCache();

//        if (LikeAgent.getInstance().getOpenid())
        Luban.get(this).load(file).putGear(Luban.THIRD_GEAR).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File file) {
                //插入相册
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file.getAbsoluteFile())));
                mPresenter.publishPic(LikeAgent.getInstance().getOpenid(), file.getAbsolutePath(), mContent.getText().toString(), mDescription.getText().toString());
            }

            @Override
            public void onError(Throwable e) {

            }
        }).launch();
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
                mTitle.setVisibility(View.VISIBLE);
                mFinish.setVisibility(View.GONE);
                mShareInfo.setVisibility(View.VISIBLE);
                mPublishFinishPic.setVisibility(View.VISIBLE);
                mRootView.setVisibility(View.GONE);

                mContent.setEnabled(true);
                mDescription.setEnabled(true);
                mShoosePicture.setImageURI("");

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mPublishFinishPic.getLayoutParams();
                params.height = mFinishHeight;
                params.width = mFinishWidth;
                mPublishFinishPic.setLayoutParams(params);

                ImageLoaderUtils.loadLocalsPic(PublishDiaryFragment.this, mPublishFinishPic,mPath);

//                mPath = "";
                mDescription.setText("");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPath = "";
    }
}
