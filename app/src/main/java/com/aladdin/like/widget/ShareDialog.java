package com.aladdin.like.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.aladdin.base.R;
import com.aladdin.utils.ContextUtils;
import com.aladdin.like.utils.WXUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

/**
 * Description 分享dialog
 * Created by zxl on 2017/4/28 下午5:40.
 * Email:444288256@qq.com
 */
public class ShareDialog extends DialogFragment {

    LinearLayout mWeixin;
    LinearLayout mFriends;
    LinearLayout mCollect;

    private Bitmap mBitmap;
    private Context mContext;

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
        view = View.inflate(mContext, R.layout.share_dialog, null);
        dialog = new Dialog(mContext, R.style.style_dialog_fragment_two);

        mWeixin = (LinearLayout) view.findViewById(R.id.share_weixin);
        mFriends = (LinearLayout) view.findViewById(R.id.share_weixin_friends);
        mCollect = (LinearLayout) view.findViewById(R.id.share_weixin_collect);

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
        mWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXUtils.shareBitmap(mContext,mBitmap, SendMessageToWX.Req.WXSceneSession);
            }
        });

        mFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXUtils.shareBitmap(mContext,mBitmap, SendMessageToWX.Req.WXSceneTimeline);
            }
        });

        mCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXUtils.shareBitmap(mContext,mBitmap, SendMessageToWX.Req.WXSceneFavorite);
            }
        });
    }
}
