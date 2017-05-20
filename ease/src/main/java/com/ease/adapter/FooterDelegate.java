package com.ease.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by zinmm on 10/18/16.
 */
public interface FooterDelegate {

    int getFooterCount();

    RecyclerView.ViewHolder onCreateFooter(ViewGroup parent, int viewType);

    void onBindFooter(RecyclerView.ViewHolder holder, int position);
}