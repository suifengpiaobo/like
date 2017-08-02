package com.aladdin.like.module.diary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.constant.Constant;
import com.aladdin.like.module.diary.contract.PublishContract;
import com.aladdin.like.module.diary.prestener.PublishPrestener;
import com.aladdin.like.module.publishcollection.ChooseCollectionActivity;
import com.aladdin.like.utils.FileUtils;
import com.aladdin.like.utils.ImageTools;
import com.aladdin.like.utils.TimeUtils;
import com.aladdin.like.utils.WXUtils;
import com.aladdin.like.widget.PublishDialog;
import com.aladdin.utils.BitmapUtils;
import com.aladdin.utils.DensityUtils;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.utils.LogUtil;
import com.aladdin.utils.SharedPreferencesUtil;
import com.aladdin.utils.ToastUtil;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.R.attr.width;

//import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * Description 发布日记页面
 * Created by zxl on 2017/5/21 下午9:25.
 */
public class PublishDiaryFragment extends BaseActivity implements PublishContract.View {
    private static final int REQUEST_SELECT_PICTURE = 0x03;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "CropImage";
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

    String mPath;

    PublishDialog mDialog;

    int mFinishWidth, mFinishHeight;//生成的图片宽高

    Bitmap mUesrBitmap;

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
        getUserAvatar();
    }

    public void getUserAvatar() {
        Uri uri = Uri.parse(LikeAgent.getInstance().getUserPojo().headimgurl);
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);

        dataSource.subscribe(new BaseBitmapDataSubscriber() {

                                 @Override
                                 public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                     Bitmap bitmap1 = ImageTools.scaleWithWH(bitmap,DensityUtils.dip2px(40),DensityUtils.dip2px(40));//BitmapUtils.imageZoom(ImageTools.scaleWithWH(bitmap,DensityUtil.px2dp(40),DensityUtil.px2dp(40)),35);
                                     mUesrBitmap = ImageTools.getOvalBitmap(bitmap1);
                                 }

                                 @Override
                                 public void onFailureImpl(DataSource dataSource) {
                                 }
                             },
                CallerThreadExecutor.getInstance());
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
                    handleCropResult(data);
                    break;
                case ChooseCollectionActivity.CHOOSE_COLLECTION:
                    mAddPicture.setVisibility(View.GONE);
                    float scale = (DensityUtils.mScreenWidth - DensityUtils.dip2px(30)) / (float) data.getIntExtra("width", 0);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mShoosePicture.getLayoutParams();
                    params.height = (int) (data.getIntExtra("height", 0) * scale);
                    params.width = (int) (width * scale);
                    mShoosePicture.setLayoutParams(params);
                    mShoosePicture.setImageURI(data.getStringExtra("url"));
                    LogUtil.i("url--ABAB-->>>"+data.getStringExtra("url"));
//                    ImageLoaderUtils.loadLocalsPic(PublishDiaryFragment.this,mShoosePicture,data.getStringExtra("url"));
                    break;
            }
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            mAddPicture.setVisibility(View.GONE);
            Bitmap bitmap = getBitmapFromUri(resultUri);

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float scale = (DensityUtils.mScreenWidth - DensityUtils.dip2px(30)) / (float) width;
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mShoosePicture.getLayoutParams();
            params.height = (int) (height * scale);
            params.width = (int) (width * scale);

            mShoosePicture.setLayoutParams(params);
//            mShoosePicture.setImageURI(resultUri);
            ImageLoaderUtils.loadLocalsPic(PublishDiaryFragment.this, mShoosePicture, resultUri.toString());
        } else {
            Toast.makeText(PublishDiaryFragment.this, "Cannot retrieve cropped image", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            Log.e("[Android]", e.getMessage());
            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
    }


    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME + System.currentTimeMillis();
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

        uCrop.start(PublishDiaryFragment.this);
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

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewSaveToImage(mRootView);
        }
    };

    @OnClick({R.id.back, R.id.finish, R.id.add_picture, R.id.share_weixin,
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
                if (!TextUtils.isEmpty(LikeAgent.getInstance().getOpenid())) {
                    new Handler().post(runnable);
                } else {
                    ToastUtil.showToast("请登录后发表日记");
                }
                break;
            case R.id.add_picture:
                showDialog();
                break;
            case R.id.share_weixin:
                Bitmap bitmap = BitmapUtils.imageZoom(BitmapFactory.decodeFile(mPath),200);
                WXUtils.shareBitmap(PublishDiaryFragment.this, bitmap, SendMessageToWX.Req.WXSceneSession);
                break;
            case R.id.share_friends:
                Bitmap bitmap1 = BitmapUtils.imageZoom(BitmapFactory.decodeFile(mPath),200);
                WXUtils.shareBitmap(PublishDiaryFragment.this, bitmap1, SendMessageToWX.Req.WXSceneTimeline);
                break;
            case R.id.share_weixin_collection:
                Bitmap bitmap2 = BitmapUtils.imageZoom(BitmapFactory.decodeFile(mPath),200);
                WXUtils.shareBitmap(PublishDiaryFragment.this, bitmap2, SendMessageToWX.Req.WXSceneFavorite);
                break;
        }
    }

    void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_SELECT_PICTURE);
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

        boolean isWater = SharedPreferencesUtil.INSTANCE.getBoolean(Constant.PUBLISH_WATER,false);

        mFinishWidth = cachebmp.getWidth();
        mFinishHeight = cachebmp.getHeight();

        FileOutputStream fos;
        File file = null;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
            File sdRoot;
            if (isHasSDCard) {
                // SD卡根目录
                sdRoot = new File(FileUtils.getPhotoDirectory());
                file = new File(sdRoot, UUID.randomUUID().toString().substring(0, 16) + ".jpeg");
                mPath = file.getAbsolutePath();
                fos = new FileOutputStream(file);
            } else
                throw new Exception("创建文件失败!");

            if (!isWater){
                Bitmap waterBitmap=ImageTools.createWaterMaskLeftBottom(PublishDiaryFragment.this,cachebmp,mUesrBitmap,6,5);
                Bitmap nameBitmap = ImageTools.drawTextToLeftBottom(PublishDiaryFragment.this,waterBitmap,LikeAgent.getInstance().getUserPojo().nickname,
                        14,getResources().getColor(R.color.Black),(int)(12+DensityUtils.px2dip(mUesrBitmap.getWidth())),
                        (int)(3+DensityUtils.px2dip(mUesrBitmap.getHeight())/2));

                Bitmap timeBitmap = ImageTools.drawTextToRightBottom(PublishDiaryFragment.this,nameBitmap, TimeUtils.getCurrentTime(),14,
                        getResources().getColor(R.color.Black),6,(int)(3+DensityUtils.px2dip(mUesrBitmap.getHeight())/2));
                timeBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }else{
                cachebmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            }

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.destroyDrawingCache();

        Luban.get(this).load(file).putGear(Luban.THIRD_GEAR).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File file) {
                try {
                    MediaStore.Images.Media.insertImage(getContentResolver(),
                            file.getAbsolutePath(), file.getName(), null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
                ImageLoaderUtils.loadLocalsPic(PublishDiaryFragment.this, mPublishFinishPic, mPath);
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
