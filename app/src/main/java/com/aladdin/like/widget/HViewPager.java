package com.aladdin.like.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aladdin.utils.LogUtil;

/**
 * Description
 * Created by zxl on 2017/6/21 上午10:08.
 * Email:444288256@qq.com
 */
public class HViewPager extends ViewPager {
    private float lastX;
    private float lastY;

    public static int CURRENT_PAGE = 0;

    public HViewPager(Context context) {
        super(context);
    }

    public HViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogUtil.i("CURRENT_PAGE--->>"+CURRENT_PAGE);
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (CURRENT_PAGE == 0){
                    if (Math.abs(y - lastY) > Math.abs(x - lastX)) {
                        intercepted = true;
                    }else{
                        intercepted = false;
                    }
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

        lastX = x;
        lastY = y;
        LogUtil.i("intercepted--->>>"+intercepted);
        return intercepted;
    }
}
