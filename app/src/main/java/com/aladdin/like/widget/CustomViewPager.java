package com.aladdin.like.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aladdin.utils.DensityUtils;

/**
 * Description
 * Created by zxl on 2017/5/20 上午10:28.
 * Email:444288256@qq.com
 */
public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int)ev.getX();
        int y = (int)ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
//                if (ev.getY() > DensityUtils.mScreenHeigth-DensityUtils.dip2px(170)){
//                    intercepted = true;
//                }
//                break;
            case MotionEvent.ACTION_MOVE:
                if (ev.getY() > DensityUtils.mScreenHeigth-DensityUtils.dip2px(170)){
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                break;
        }

        return intercepted;
    }
}
