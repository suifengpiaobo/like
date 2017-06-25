package com.aladdin.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Description
 * Created by zxl on 2017/4/27 下午5:23.
 * Email:444288256@qq.com
 */
public class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context mContext, String text) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showToast(@NonNull String content) {
        Toast.makeText(ContextUtils.getInstance().getApplicationContext(), content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(@NonNull String content) {
        Toast.makeText(ContextUtils.getInstance().getApplicationContext(), content, Toast.LENGTH_LONG).show();
    }

    public static void showToast(@StringRes int stringResId) {
        Toast.makeText(ContextUtils.getInstance().getApplicationContext(), stringResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToastOnSubThread(@NonNull final Fragment fragment, @NonNull final String content) {
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(fragment.getActivity().getBaseContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToastOnSubThread(@NonNull final Fragment fragment, @StringRes final int stringResId) {
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(fragment.getActivity().getBaseContext(), stringResId, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToastOnSubThread(@NonNull final  Activity activity, @NonNull final  String content) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getBaseContext(), content, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showToastOnSubThread(@NonNull final Activity activity, @StringRes final  int stringResId) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getBaseContext(), stringResId, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
