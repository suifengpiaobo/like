package com.aladdin.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aladdin.dialog.DialogTools;
import com.aladdin.utils.AppManager;
import com.aladdin.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * Description activity基类
 * Created by zxl on 2017/4/27 下午5:16.
 * Email:444288256@qq.com
 */
public abstract class BaseActivity extends AppCompatActivity {
    //    private CompositeSubscription mSubscriptions;
    protected Context mContext;
    protected Context mApplicationContext;

    private View baseView;
    private View contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        }
        mContext = this;
        mApplicationContext = this.getApplicationContext();

        AppManager.instance.addActivity(this);
        baseView = View.inflate(mContext, R.layout.base_activity_container, null);
        setContentView(baseView);
        setupContentView();

        ButterKnife.bind(this);
        this.initView();
    }
    // 加载子类布局文件
    private void setupContentView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.root);
        if (getLayoutId() != 0) {
            contentView = View.inflate(mContext, getLayoutId(), null);
            frameLayout.addView(contentView);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //当模式为singleTop和SingleInstance会回调到这里
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.instance.removeActivity(this);
//        if (mSubscriptions != null) {
//            mSubscriptions.clear();
//        }
    }

    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转界面并关闭当前界面
     *
     * @param clazz 目标Activity
     */
    protected void startThenKill(Class<?> clazz) {
        startThenKill(clazz, null);
    }

    /**
     * @param clazz  目标Activity
     * @param bundle 数据
     */
    protected void startThenKill(Class<?> clazz, Bundle bundle) {
        startActivity(clazz, bundle);
        finish();
    }

    /**
     * 5.0以上系统状态栏透明,国产手机默认透明
     */
    protected void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 读取状态栏的高度
     *
     * @param context
     * @return
     */

    protected int getStatusBarHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            return resourceId > 0 ? context.getResources().getDimensionPixelSize(resourceId) : 0;
        } else {
            return 0;
        }
    }

    /**
     * 开启加载效果
     */
    public void startProgressDialog() {
        DialogTools.showWaittingDialog(this);
    }

    /**
     * 关闭加载
     */
    public void stopProgressDialog() {
        DialogTools.closeWaittingDialog();
    }

    /**
     * 显示错误的dialog
     */
    public void showErrorHint(String errorContent) {
        View errorView = LayoutInflater.from(this).inflate(R.layout.app_error_tip, null);
        TextView tvContent = (TextView) errorView.findViewById(R.id.content);
        tvContent.setText(errorContent);
        new ToastUtil(errorView);
    }

    /**
     * 显示普通的toast
     *
     * @return
     */
    public void showToast(String str) {
        ToastUtil.sToastUtil.shortDuration(str).setToastBackground(Color.WHITE, R.drawable.toast_radius).show();
    }

    //获取布局
    protected abstract int getLayoutId();

    //初始化布局和监听
    protected abstract void initView();

    /**
     * 点击空白隐藏软键盘
     */
    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        final View v = this.getWindow().peekDecorView();
        return hiddenInputMethodManager(v);
    }

    protected boolean hiddenInputMethodManager(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        return false;
    }

    protected boolean showInputMethodManager(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        }
        return false;
    }
}
