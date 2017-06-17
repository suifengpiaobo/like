package com.aladdin.like.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description
 * Created by zxl on 2017/6/17 下午6:56.
 * Email:444288256@qq.com
 */
public class SearEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {

    private Drawable mSearchDrawable;
    /**
     * 控件是否有焦点
     */
    private boolean hasFoucs;

    public SearEditText(Context context) {
        super(context);
    }

    public SearEditText(Context context, AttributeSet attrs) {
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SearEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
//        mSearchDrawable = getCompoundDrawables()[2];
//        if (mSearchDrawable == null) {
            // throw new
            // NullPointerException("You can add drawableRight attribute in XML");
//            mSearchDrawable = getResources().getDrawable(R.drawable.search_icon);
//        }
//        mSearchDrawable.setBounds( mSearchDrawable.getIntrinsicWidth(),0, mSearchDrawable.getIntrinsicHeight(),0);
        // 默认设置隐藏图标
//        setSearchIconVisible(true);
        // 设置焦点改变的监听
        setOnFocusChangeListener(this);
        // 设置输入框里面内容发生改变的监听
        addTextChangedListener(this);
    }

    protected void setSearchIconVisible(boolean visible) {
        Drawable right = visible ? mSearchDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (hasFoucs) {
//            setClearIconVisible(s.length() > 0);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
}
