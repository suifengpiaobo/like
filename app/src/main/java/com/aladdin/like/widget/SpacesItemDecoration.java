package com.aladdin.like.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description recycleview分割线
 * Created by zxl on 2017/5/21 下午4:52.
 * Email:444288256@qq.com
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int vertical;
    private int horizontal;

    public SpacesItemDecoration(int vertical,int horizontal) {
        this.vertical=vertical;
        this.horizontal=horizontal;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left= horizontal;
        outRect.right=horizontal;
        outRect.bottom=vertical;
        if(parent.getChildAdapterPosition(view)==0){
            outRect.top=0;
        }
        else{
            outRect.top=vertical;
        }
    }
}
