package com.aladdin.like.module.diary;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.LikeAgent;
import com.aladdin.like.R;
import com.aladdin.like.module.diary.contract.PublishContract;
import com.aladdin.like.module.diary.prestener.PublishPrestener;
import com.aladdin.like.module.main.MainActivity;
import com.aladdin.utils.ImageLoaderUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yalantis.ucrop.ui.AlbumDirectoryActivity;
import com.yalantis.ucrop.util.Constants;
import com.yalantis.ucrop.util.LocalMediaLoader;
import com.yalantis.ucrop.util.Options;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 发布日记页面
 * Created by zxl on 2017/5/21 下午9:25.
 */
public class PublishDiaryFragment extends BaseFragment implements PublishContract.View{
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


        ((MainActivity) getActivity()).setOnChoosePictureListener(new MainActivity.onChoosePictureListener() {
            @Override
            public void onChooseListener(String path) {
                mPath = path;
                mAddPicture.setVisibility(View.GONE);
                ImageLoaderUtils.loadLocalsPic(getActivity(), mShoosePicture, path);
            }
        });
    }

    @Override
    protected void lazyFetchData() {

    }

    @OnClick({R.id.back, R.id.finish, R.id.add_picture})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.finish:
                mPresenter.publishPic(LikeAgent.getInstance().getUid(),mPath,"","");
//                ToastUtil.sToastUtil.shortDuration("发布成功!");
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
        AlbumDirectoryActivity.startPhoto(getActivity(), options);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(msg);
            }
        });
    }
}
