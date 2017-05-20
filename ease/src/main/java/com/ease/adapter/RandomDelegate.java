package com.ease.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ease.holder.BaseViewHolder;

/**
 * Created by zinmm on 16/7/21.
 */
public interface RandomDelegate {
    BaseViewHolder onCreateRandom(ViewGroup parent, int viewType);

    void onBindRandom(RecyclerView.ViewHolder holder, int position);

    int getRandomType();

    boolean isRandomType(int viewType);

    int getRandomPosition();

    boolean hasRandom();
}
