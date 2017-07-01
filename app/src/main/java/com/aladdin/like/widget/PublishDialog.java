package com.aladdin.like.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.utils.ContextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Description
 * Created by zxl on 2017/6/25 上午10:49.
 * Email:444288256@qq.com
 */
public class PublishDialog extends DialogFragment {
    @BindView(R.id.from_like_collection)
    TextView mFromLikeCollection;
    @BindView(R.id.from_phone)
    TextView mFromPhone;
    @BindView(R.id.cancel)
    TextView mCancel;
    private Context mContext;

    private onDialogListener mDialogListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity() != null ? getActivity() : ContextUtils.getInstance().getContext();

        setStyle(DialogFragment.STYLE_NORMAL, R.style.style_dialog_fragment_two);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = null;
        Dialog dialog = null;
        view = View.inflate(mContext, R.layout.layout_public_dialog, null);
        dialog = new Dialog(mContext, R.style.style_dialog_fragment_two);
        ButterKnife.bind(this, view);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距

        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.BOTTOM);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        return dialog;
    }

    @OnClick({R.id.from_like_collection, R.id.from_phone, R.id.cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.from_like_collection:
                if (mDialogListener != null){
                    mDialogListener.onLikeCollection();
                }
                break;
            case R.id.from_phone:
                if (mDialogListener != null){
                    mDialogListener.onPhone();
                }
                break;
            case R.id.cancel:
                dismiss();
                break;
        }
    }

    public onDialogListener getDialogListener() {
        return mDialogListener;
    }

    public void setDialogListener(onDialogListener dialogListener) {
        mDialogListener = dialogListener;
    }

    public interface onDialogListener{
        void onLikeCollection();
        void onPhone();
    }
}
