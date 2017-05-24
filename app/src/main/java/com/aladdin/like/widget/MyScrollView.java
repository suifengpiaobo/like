package com.aladdin.like.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aladdin.like.constant.Constant;
import com.jcodecraeer.xrecyclerview.XScrollView;

/**
 * Description
 * Created by zxl on 2017/5/24 下午8:21.
 * Email:444288256@qq.com
 */
public class MyScrollView extends XScrollView {
    private float xDistance, yDistance, xLast, yLast;
    float y1 = 0;
    float y2 = 0;
    private int t;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {

            case MotionEvent.ACTION_DOWN:
                y1 = ev.getY();

                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;

            case MotionEvent.ACTION_MOVE:

                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if(xDistance > yDistance){
                    return false;
                }

                y2 = ev.getY();

                int scrollY = getScrollY();

                int height = getHeight();
                int scrollViewMeasuredHeight = getChildAt(0).getMeasuredHeight();
                if (scrollY == 0) {
//                    LoggerUtils.error("滑动到了顶端 view.getScrollY()=" + scrollY);
                }
                if ((scrollY + height) == scrollViewMeasuredHeight) {
                    if (Constant.isScroll && y1 < y2){
                        return super.onInterceptTouchEvent(ev);
                    }
                    return false;
                }

                return super.onInterceptTouchEvent(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void upSmoothScroll() {
        smoothScrollTo(0, t);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (t - oldt < 50) {
            this.t = t;
        }
    }
}
