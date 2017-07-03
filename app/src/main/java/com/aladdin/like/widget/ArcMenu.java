package com.aladdin.like.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.aladdin.like.R;

/**
 * Description 星菜单
 * Created by zxl on 2017/7/2 下午11:39.
 * Email:444288256@qq.com
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener{
    private static final  int LEFT_TOP=0;
    private static final int LEFT_BOTTOM=1;
    private static final int CENTER=2;
    private static final int RIGHT_TOP=3;
    private  static final int RIGHT_BOTTOM=4;

    private State mMenuAnimationState=State.OPEN;

    private AnimationSet mMenuAnimation;

    private State mState=State.CLOSE;

    private int mCenterX,mCenterY;

    private onMenuItemClickListner mOnMenuItemClickListner;

    private View mCButton;

    private int mRadius;

    private  Position mPosition=Position.RIGHT_BOTTOM;

    private enum  State{
        OPEN,CLOSE
    }

    private  enum  Position{
        LEFT_TOP,LEFT_BOTTOM,CENTER,RIGHT_TOP,RIGHT_BOTTOM
    }
    public ArcMenu(Context context) {
        this(context, null);
    }
    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int defaultRadius= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,100, getResources().getDisplayMetrics());

        TypedArray typedArray=context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu,defStyleAttr,0);
        int pos=typedArray.getInt(R.styleable.ArcMenu_position, 4);
        switch (pos){
            case LEFT_TOP:
                mPosition=Position.LEFT_TOP;
                break;
            case LEFT_BOTTOM:
                mPosition=Position.LEFT_BOTTOM;
                break;
            case CENTER:
                mPosition=Position.CENTER;
                break;
            case RIGHT_TOP:
                mPosition=Position.RIGHT_TOP;
                break;
            case RIGHT_BOTTOM:
                mPosition=Position.RIGHT_BOTTOM;
                break;
        }
        mRadius= (int) typedArray.getDimension(R.styleable.ArcMenu_radius,defaultRadius);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount=getChildCount();
        for (int i=0;i<childCount;i++){
            View child=getChildAt(i);
            //²âÁ¿child
            measureChild(child,widthMeasureSpec,heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed){
            layoutCButton();
            int childCount=getChildCount()-1;

            for(int i=0;i<childCount;i++){
                View child=getChildAt(i+1);
                child.setVisibility(View.GONE);
                int left=0;
                int top=0;
                if(mPosition==Position.CENTER){
                    left= (int) (mRadius*Math.cos(Math.PI*2 / (childCount) * i));
                    top= (int) (mRadius*Math.sin(Math.PI*2 / (childCount) * i));
                }else{
                    left= (int) (mRadius*Math.sin(Math.PI/2/(childCount - 1) * i));
                    top= (int) (mRadius*Math.cos(Math.PI/2 / (childCount - 1) * i));
                }

                int width=child.getMeasuredWidth();
                int height=child.getMeasuredHeight();

                if(mPosition==Position.LEFT_BOTTOM||mPosition==Position.RIGHT_BOTTOM){
                    top=getMeasuredHeight()-height-top;
                }

                if(mPosition==Position.RIGHT_BOTTOM||mPosition==Position.RIGHT_TOP){
                    left=getMeasuredWidth()-width-left;
                }

                if(mPosition==Position.CENTER){
                    left=mCenterX+left;
                    top=mCenterY+top;
                }
                child.layout(left,top,left+child.getMeasuredWidth(),top+child.getMeasuredHeight());
            }
            startMenuAnimation(mCButton);
        }
    }

    private void layoutCButton() {
        if(getChildCount()>0){
            mCButton=getChildAt(0);
            mCButton.setOnClickListener(this);
            int left=0;
            int top=0;
            int width=mCButton.getMeasuredWidth();
            int height=mCButton.getMeasuredHeight();
            switch (mPosition){
                case LEFT_TOP:
                    left=0;
                    top=0;
                    break;
                case LEFT_BOTTOM:
                    left=0;
                    top=getMeasuredHeight()-height;
                    break;
                case CENTER:
                    left=(getMeasuredWidth()-width)/2;
                    top=(getMeasuredHeight()-height) / 2;
                    mCenterX=left+width/10;
                    mCenterY=top+height/10;
                    break;
                case RIGHT_TOP:
                    left=getMeasuredWidth()-width;
                    top=0;
                    break;
                case RIGHT_BOTTOM:
                    left=getMeasuredWidth()-width;
                    top=getMeasuredHeight()-height;
                    break;
            }
            mCButton.layout(left,top,left+width,top+height);

        }
    }


    public void setOnMenuItemClickListner(onMenuItemClickListner mOnMenuItemClickListner) {
        this.mOnMenuItemClickListner = mOnMenuItemClickListner;
    }


    public interface  onMenuItemClickListner{
        public void onClick(View childView,int position);
    }

    @Override
    public void onClick(View v) {
        rotateButton(v, 0f, 360f, 300);
        toggleMenu(300);

        startMenuAnimation(v);


    }

    public void toggleMenu(final int duration) {
        int childCount=getChildCount()-1;
        for(int i=0;i<childCount;i++){
            final View child=getChildAt(i+1);
            child.setVisibility(View.VISIBLE);
            int left=0;
            int top=0;
            if(mPosition==Position.CENTER){
                left= (int) (mRadius*Math.cos(Math.PI*2 / (childCount) * i));
                top= (int) (mRadius*Math.sin(Math.PI*2 / (childCount) * i));
            }else{
                left= (int) (mRadius*Math.sin(Math.PI/2/(childCount - 1) * i))-(mCButton.getMeasuredWidth()-child.getMeasuredWidth())/2;
                top= (int) (mRadius*Math.cos(Math.PI / 2 / (childCount - 1) * i))-(mCButton.getMeasuredHeight()-child.getMeasuredHeight())/2;
            }
            int xFlag=1;
            int yFlag=1;
            if(mPosition==Position.LEFT_BOTTOM||mPosition==Position.LEFT_TOP){
                xFlag=-1;
            }
            if(mPosition==Position.LEFT_TOP||mPosition==Position.RIGHT_TOP){
                yFlag=-1;
            }
            int startX=left*xFlag;
            int startY=top*yFlag;
            int endX=0;
            int endY=0;

            if(mPosition==Position.CENTER){
                startX=-left;
                startY=-top;
                endX=0;
                endY=0;
            }

            AnimationSet animationSet=new AnimationSet(true);
            TranslateAnimation transAnimation;
            if(mState==State.CLOSE){
                transAnimation=new TranslateAnimation(startX,endX,startY,endY);
                child.setClickable(true);
                child.setFocusable(true);

            }else{
                transAnimation=new TranslateAnimation(endX,startX,endY,startY);
                child.setClickable(false);
                child.setFocusable(false);

            }

            RotateAnimation roateAnimation=new RotateAnimation(0,720,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            roateAnimation.setDuration(duration);
            roateAnimation.setFillAfter(true);
            transAnimation.setDuration(duration);
            transAnimation.setFillAfter(true);
            animationSet.addAnimation(roateAnimation);
            animationSet.addAnimation(transAnimation);
            animationSet.setStartOffset((i * 100) / childCount);
            child.startAnimation(animationSet);

            transAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    if (mState == State.CLOSE) {
                        child.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            final int poisition=i;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnMenuItemClickListner!=null){
                        mOnMenuItemClickListner.onClick(child,poisition);
                    }
                    childClickAnimation(poisition, duration);
                    changeMenuState();
                    startMenuAnimation(mCButton);
                }
            });

        }

        changeMenuState();
    }

    private void childClickAnimation(int poisition,int duration) {
        int childCount=getChildCount()-1;
        for(int i=0;i<childCount;i++){
            View child=getChildAt(i+1);
            if(i==poisition){
                child.startAnimation(childBigAnimation(duration,4.0f));
            }else{
                child.startAnimation(childSmallAnimation(duration,1.0f));
            }

        }
    }

    private AnimationSet childSmallAnimation(int duration,float scale) {
        AnimationSet animationSet=new AnimationSet(true);
        ScaleAnimation scaleAnimation=new ScaleAnimation(scale,0.0f,scale,0.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        animationSet.setFillAfter(true);
        animationSet.setDuration(duration);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        return  animationSet;
    }

    private AnimationSet childBigAnimation(int duration,float scale) {
        AnimationSet animationSet=new AnimationSet(true);

        ScaleAnimation scaleAnimation=new ScaleAnimation(1.0f,scale,1.0f,scale,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        animationSet.setFillAfter(true);
        animationSet.setDuration(duration);
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        return  animationSet;
    }


    private void changeMenuState() {
        mState=mState==State.OPEN?State.CLOSE:State.OPEN;
    }

    public boolean isMenuOpen(){
        return mState==State.OPEN?true:false;
    }

    public void setmMenuAnimationState(boolean isOpen){
        mMenuAnimationState=isOpen?State.OPEN:State.CLOSE;
    }

    public void startMenuAnimation(View v){
        if(mMenuAnimationState==State.OPEN){
            menuAnimation(v,1000);
            if(mState==State.CLOSE){
                v.startAnimation(mMenuAnimation);
            }else {
                v.clearAnimation();
            }
        }
    }

    public void rotateButton(View v, float start, float end, int duration) {
        RotateAnimation roteAnimation=new RotateAnimation(start,end, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        roteAnimation.setDuration(duration);
        roteAnimation.setFillAfter(true);
        v.startAnimation(roteAnimation);
    }

    private void menuAnimation(final View v,int duration){
        if(mMenuAnimation==null){
            mMenuAnimation=childBigAnimation(duration, 2.0f);

            mMenuAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }
                @Override
                public void onAnimationEnd(Animation animation) {
                    v.startAnimation(mMenuAnimation);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }
}
