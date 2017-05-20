package com.aladdin.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.aladdin.base.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Description 加载的dialog
 * Created by zxl on 2017/4/27 下午5:24.
 * Email:444288256@qq.com
 */
public class DialogTools {
    private static ProgressDialog mWaittingDialog;

    public static void showWaittingDialog(Context context) {
        try {
            if (mWaittingDialog != null) {
                mWaittingDialog.dismiss();
                mWaittingDialog = null;
            }

            mWaittingDialog = new ProgressDialog(context, R.style.CustomProgressDialog);
            mWaittingDialog.setCancelable(false);
            LayoutInflater mInflater = mWaittingDialog.getLayoutInflater();
            View mView = mInflater.inflate(R.layout.dialog_loading, null);
            mWaittingDialog.show();
            mWaittingDialog.setContentView(mView);
            // 3秒后还未完成任务，则设置为可取消
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    if (mWaittingDialog != null)
                        mWaittingDialog.setCancelable(true);
                }
            };
            Timer timer = new Timer(true);
            timer.schedule(task, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void closeWaittingDialog() {
        try {
            if (mWaittingDialog != null)
                mWaittingDialog.dismiss();
            mWaittingDialog = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
