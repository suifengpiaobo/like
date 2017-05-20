package com.aladdin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aladdin.base.R;
import com.aladdin.utils.ContextUtils;

/**
 * Description
 * Created by zxl on 2017/5/1 上午9:53.
 * Email:444288256@qq.com
 */
public class PictureChooseDialog extends DialogFragment {
    private Context mContext;
    private TextView mChoose,mTakePhoto,mCancel;
    private onItemClickListener mOnItemClickLisner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity() != null ? getActivity() : ContextUtils.getInstance().getContext();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = null;
        Dialog dialog = null;
        view = View.inflate(mContext, R.layout.layout_picture_dialog, null);
        dialog = new Dialog(mContext, R.style.style_dialog_fragment_two);

        mChoose = (TextView) view.findViewById(R.id.alumb_choose);
        mTakePhoto = (TextView) view.findViewById(R.id.take_photo);
        mCancel = (TextView) view.findViewById(R.id.cancel);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(view);

        Window window = dialog.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        WindowManager.LayoutParams lp = window.getAttributes();

        window.setGravity(Gravity.BOTTOM);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.alpha = 0.98f;
        window.setAttributes(lp);

        bindEvent();
        return dialog;
    }

    private void bindEvent() {
        mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLisner != null){
                    mOnItemClickLisner.onChoose();
                }
            }
        });

        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickLisner != null){
                    mOnItemClickLisner.onTakePhoto();
                }
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setOnItemClickLisner(onItemClickListener onItemClickLisner) {
        mOnItemClickLisner = onItemClickLisner;
    }

    public interface onItemClickListener{
        void onChoose();
        void onTakePhoto();
    }

}
