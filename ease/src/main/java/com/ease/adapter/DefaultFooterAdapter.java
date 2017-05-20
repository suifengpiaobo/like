package com.ease.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.ease.R;
import com.ease.holder.LoadViewHolder;

/**
 * Created by zinmm on 10/18/16.
 */
public class DefaultFooterAdapter implements FooterDelegate {

    @Override
    public int getFooterCount() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooter(ViewGroup parent, int viewType) {
        View footerLayout = View.inflate(parent.getContext(), R.layout.view_footer_load, null);
        return new LoadViewHolder(footerLayout);
    }

    @Override
    public void onBindFooter(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadViewHolder) {
            LoadViewHolder loadViewHolder = (LoadViewHolder) holder;

            loadViewHolder.noDataTextView.setVisibility(View.GONE);
            loadViewHolder.loadRelativeLayout.setVisibility(View.GONE);

            if (isVisibleLoad) {
                loadViewHolder.loadRelativeLayout.setVisibility(View.VISIBLE);
            } else {
                loadViewHolder.noDataTextView.setVisibility(View.VISIBLE);
            }
            if (isAllGone) {
                loadViewHolder.noDataTextView.setVisibility(View.GONE);
                loadViewHolder.loadRelativeLayout.setVisibility(View.GONE);
            }
        }
    }

    public boolean isAllGone;

    public boolean isVisibleLoad = true;
}
