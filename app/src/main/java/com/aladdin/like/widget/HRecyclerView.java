package com.aladdin.like.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aladdin.utils.LogUtil;

/**
 * Description 解决横向滑动冲突
 * Created by zxl on 2017/6/20 下午4:50.
 * Email:444288256@qq.com
 */
public class HRecyclerView extends RecyclerView {
    private float startY;
    private float startX;
    private int mScrollPointerId;
    public HRecyclerView(Context context) {
        super(context);
    }

    public HRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;

        int x = (int) ev.getX();
        int y = (int) ev.getY();
        final int action = MotionEventCompat.getActionMasked(ev);
        final int actionIndex = MotionEventCompat.getActionIndex(ev);


        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                mScrollPointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(y-startY)>Math.abs(x-startX)){
                    intercepted = true;
                }else{
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }
        startX = x;
        startY = y;
        LogUtil.i("x--->>>"+x+"  ---y--->>>"+y+"  intercepted-->>"+intercepted);
        return intercepted;
    }
}
