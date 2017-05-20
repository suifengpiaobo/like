package com.ease.holder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ease.R;

/**
 * Created by zinmm on 16/7/20.
 */
public class LoadViewHolder extends BaseViewHolder {

    public TextView noDataTextView;
    public RelativeLayout loadRelativeLayout;

    public LoadViewHolder(View itemView) {
        super(itemView);

        loadRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.loadRelativeLayout);
        noDataTextView = (TextView) itemView.findViewById(R.id.noDataTextView);
    }
}
