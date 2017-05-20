package com.aladdin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aladdin.base.R;


/**
 * Description 通用的头部
 * Created by zxl on 2017/4/27 下午5:51.
 * Email:444288256@qq.com
 */
public class NormalTitleBar extends RelativeLayout {
    private TextView ivBack,tvTitle,tvRight;
    private ImageView imgRight;
    private RelativeLayout mSearch;
    private EditText mEditText;
    RelativeLayout headBg;

    public NormalTitleBar(Context context) {
        this(context, null);
    }

    public NormalTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NormalTitleBar(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NormalTitleBar, defStyleAttr, 0);
        String title = a.getString(R.styleable.NormalTitleBar_content);
        String rightTitle = a.getString(R.styleable.NormalTitleBar_rightContent);
        Boolean leftState = a.getBoolean(R.styleable.NormalTitleBar_leftState,true);
        int color = a.getColor(R.styleable.NormalTitleBar_backgroudColor, Color.parseColor("#FFFFFF"));
        a.recycle();
        View.inflate(context, R.layout.view_head, this);
        headBg = (RelativeLayout)findViewById(R.id.head_bg);
        ivBack = (TextView) findViewById(R.id.tv_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        imgRight = (ImageView) findViewById(R.id.img_right);
        mSearch = (RelativeLayout) findViewById(R.id.search_rl);
        mEditText = (EditText) findViewById(R.id.seach_edit);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvTitle.setText(title);
//        tvRight.setText(rightTitle);
        ivBack.setVisibility(leftState == true?View.VISIBLE:View.INVISIBLE);
        headBg.setBackgroundColor(color);
    }

    public void setTitleBg(boolean isVisibe){
        if (isVisibe){
            headBg.setBackgroundColor(getResources().getColor(R.color.white));
        }else{
            headBg.setBackgroundColor(0);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
        //view有id才会保存状态
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 管理返回按钮
     */
    public void setBackVisibility(boolean visible) {
        ivBack.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setSearchVisibility(boolean visible){
        mSearch.setVisibility(visible ? View.VISIBLE : View.GONE);
        if (visible){
            ivBack.setVisibility(GONE);
            tvTitle.setVisibility(GONE);
            imgRight.setVisibility(GONE);
            tvRight.setVisibility(GONE);
        }
    }

    public EditText getEditText(){
        return mEditText;
    }
    /**
     * 设置标题栏左侧字符串
     * @param tvLeftText
     */
    public void setTvLeft(String tvLeftText){
        ivBack.setText(tvLeftText);
    }

    public void setTvRight(String tvLeftText){
        tvRight.setText(tvLeftText);
    }

    public void setTvRightVisibility(boolean visible){
        tvRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTvTitleVisibility(boolean visible){
        tvTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitleText(String string) {
        tvTitle.setText(string);
    }

    public void setTitleText(int string) {
        tvTitle.setText(string);
    }

    public void setTitleColor(int color) {
        tvTitle.setTextColor(color);
    }

    /**
     * 右标题
     */
    public void setRightTitleVisibility(boolean visible) {
        imgRight.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setRightTitle(int bg) {
        imgRight.setBackgroundResource(bg);
    }

    /*
     * 点击事件
     */
    public void setOnBackListener(OnClickListener listener) {
        ivBack.setOnClickListener(listener);
    }

    public void setOnRightTextListener(OnClickListener listener) {
        imgRight.setOnClickListener(listener);
    }

    public void setOnRightTvListener(OnClickListener listener){
        tvRight.setOnClickListener(listener);
    }
}
