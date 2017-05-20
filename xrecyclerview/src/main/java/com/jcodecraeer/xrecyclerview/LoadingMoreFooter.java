package com.jcodecraeer.xrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.progressindicator.AVLoadingIndicatorView;

public class LoadingMoreFooter extends LinearLayout {

    private SimpleViewSwitcher progressCon;
    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private TextView mText;
    private ImageView mImageView;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView();
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public void initView() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        int dip10 = (int) getResources().getDimension(R.dimen.textandiconmargin);
        int dip30 = (int) getResources().getDimension(com.jcodecraeer.xrecyclerview.R.dimen.bottomTopmargin);
        setPadding(0, dip30, 0, dip10);
//        setPadding(0, dip10, 0, dip10);
        setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        progressCon = new SimpleViewSwitcher(getContext());
        progressCon.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
        progressView.setIndicatorColor(0xff4A4A4A);
        progressView.setIndicatorId(ProgressStyle.BallSpinFadeLoader);
        progressCon.setView(progressView);
        addView(progressCon);
        mText = new TextView(getContext());
        mText.setText("正在加载...");

        mImageView = new ImageView(getContext());
        mImageView.setImageResource(R.drawable.icon_fooder_logo);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins((int) getResources().getDimension(R.dimen.textandiconmargin), 0, 0, 0);

        mText.setLayoutParams(layoutParams);
        addView(mText);
        addView(mImageView);
    }

    public void setProgressStyle(int style) {
        if (style == ProgressStyle.SysProgress) {
            progressCon.setView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyle));
        } else {
            AVLoadingIndicatorView progressView = new AVLoadingIndicatorView(this.getContext());
            progressView.setIndicatorColor(0xff4A4A4A);
            progressView.setIndicatorId(style);
            progressCon.setView(progressView);
        }
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mText.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                progressCon.setVisibility(View.VISIBLE);

                mText.setText(getContext().getText(R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);

                mText.setText(getContext().getText(R.string.listview_loading));

                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setVisibility(View.GONE);
                progressCon.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }
}
