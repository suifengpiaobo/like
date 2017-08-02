package com.aladdin.like.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aladdin.dialog.DialogTools;
import com.aladdin.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
//import rx.Subscription;
//import rx.subscriptions.CompositeSubscription;

/**
 * Description
 * Created by zxl on 2017/4/27 下午5:26.
 * Email:444288256@qq.com
 */
public abstract class BaseFragment extends Fragment {
    protected BaseActivity mActivity;

    private boolean hasCreateView; //rootView是否初始化标志
    private boolean isFragmentVisible; // 当前Fragment是否处于可见状态标志
    private volatile boolean isFirst; // 是否初次可见

    private View rootView; //根布局 父类
    private View contentView; // 根布局 子类

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    // 初始状态
    private void initVariable() {
        hasCreateView = false;
        isFragmentVisible = false;
        isFirst = true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(com.aladdin.base.R.layout.base_activity_container, container, false);
        setupContentView();
        ButterKnife.bind(this, rootView);
        this.initView();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        Log.d("BaseFragment", this.getClass().getSimpleName() + "--> onViewCreated: View-创建成功");
        super.onViewCreated(view, savedInstanceState);
        if (!hasCreateView && getUserVisibleHint()) {
            isFirst = true;
            onVisibleChange(true);
            isFragmentVisible = true;
        }
    }

    /* 在这里实现Fragment数据的缓加载.
*
        * @param isVisibleToUser
*/
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("BaseFragment", this.getClass().getSimpleName() + "--> isVisibleToUser: " + isVisibleToUser);
        if (rootView == null) {
            return;
        }

        hasCreateView = true;
//        if (isVisibleToUser) {
        if (getUserVisibleHint()) {
            onVisibleChange(true);
            isFragmentVisible = true;
            return;
        }
        if (isFragmentVisible) {
            onVisibleChange(false);
            isFragmentVisible = false;
        }
    }

    /**
     * @param isVisible true 不可见-->可见
     *                  false 可见-->不可见
     */
    protected void onVisibleChange(boolean isVisible) {
        Log.d("BaseFragment", this.getClass().getSimpleName() + "--> isVisible: " + isVisible);
        if (isVisible) {
            if (isFirst) {
                Log.d("BaseFragment", this.getClass().getSimpleName() + "--> 首次可见 懒加载");
                lazyFetchData();
                isFirst = false;
                return;
            }
            Log.d("BaseFragment", this.getClass().getSimpleName() + "--> 非首次 不加载");
            onvisible();
        } else {
            onInvisible();
        }
    }

    // 子类根据需求 选择实现
    protected void onvisible() {
        if (getActivity() == null)
            return;
        MobclickAgent.onResume(getActivity());
        // Log.e("BaseFragment", this.getClass().getSimpleName() + "--> 可见 onvisible");
    }

    // 子类根据需求 选择实现
    protected void onInvisible() {
        if (getActivity() == null)
            return;
        MobclickAgent.onPause(getActivity());
        // Log.e("BaseFragment", this.getClass().getSimpleName() + "--> 不可见 onInvisible");
    }

    // 加载子类布局文件
    private void setupContentView() {
        FrameLayout frameLayout = (FrameLayout) findViewById(com.aladdin.base.R.id.root);
        if (getLayoutId() != 0) {
            contentView = View.inflate(mActivity, getLayoutId(), null);
            frameLayout.addView(contentView);
        }
    }

    // 工具方法 用于获取某个View
    public View findViewById(int resId) {
        return rootView.findViewById(resId);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
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
        getActivity().finish();
    }

    /**
     * 开启加载效果
     */
    public void startProgressDialog() {
        DialogTools.showWaittingDialog(getActivity());
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
        View errorView = LayoutInflater.from(getActivity()).inflate(com.aladdin.base.R.layout.app_error_tip, null);
        TextView tvContent = (TextView) errorView.findViewById(com.aladdin.base.R.id.content);
        tvContent.setText(errorContent);
//        new ToastUtil(errorView);
    }

    /**
     * 显示普通的toast
     *
     * @return
     */
    public void showToast(String str) {
        ToastUtil.showToast(str);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void lazyFetchData();

    protected boolean hiddenInputMethodManager(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

        return false;
    }

    protected boolean showInputMethodManager(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
        }

        return false;
    }
}
