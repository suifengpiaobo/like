package com.ease.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.ease.holder.BaseViewHolder;

/**
 * Created by zinmm on 16/7/21.
 */
public interface HeaderDelegate {

    BaseViewHolder onCreateHeader(ViewGroup parent, int viewType);

    void onBindHeader(RecyclerView.ViewHolder holder, int position);

    int getHeaderCount();
}
