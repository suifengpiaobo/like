package com.jcodecraeer.xrecyclerview.progressindicator.indicator;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jack on 2015/10/16.
 */
public class BallClipRotateIndicator extends BaseIndicatorController {

    float scaleFloat = 1, degrees;

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);

        float circleSpacing = 12;
        float x = (getWidth()) / 2;
        float y = (getHeight()) / 2;
        canvas.translate(x, y);
        canvas.scale(scaleFloat, scaleFloat);
        canvas.rotate(degrees);
        RectF rectF = new RectF(-x + circleSpacing, -y + circleSpacing, 0 + x - circleSpacing, 0 + y - circleSpacing);
        canvas.drawArc(rectF, -2, 350, false, paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators = new ArrayList<>();

        ValueAnimator rotateAnim = ValueAnimator.ofFloat(0, 180, 360);
        rotateAnim.setDuration(550);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                degrees = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        rotateAnim.start();
        animators.add(rotateAnim);
        return animators;
    }

}
