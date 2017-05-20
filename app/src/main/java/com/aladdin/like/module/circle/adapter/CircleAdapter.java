package com.aladdin.like.module.circle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.aladdin.like.model.PrefecturePojo;
import com.ease.adapter.BaseAdapter;

/**
 * Description
 * Created by zxl on 2017/5/1 上午9:47.
 * Email:444288256@qq.com
 */
public class CircleAdapter extends BaseAdapter<PrefecturePojo.Prefecture> {
    public CircleAdapter(Context mContext) {
        super(mContext);
    }

    @Override
    public void onBindCommon(RecyclerView.ViewHolder holder, PrefecturePojo.Prefecture item) {

    }

    @Override
    public int getCommonType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType) {
        return null;
    }
}
