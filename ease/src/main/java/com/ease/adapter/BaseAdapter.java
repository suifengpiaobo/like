/*
 * Copyright (C) 2016 AdvancingPainters (https://github.com/AdvancingPainters).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ease.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.ease.data.LoadMoreDelegate;

import java.util.ArrayList;
import java.util.List;

//    ┏┓       ┏┓
//   ┏┛┻━━━━━━━┛┻┓
//   ┃     ━     ┃
//   ┃   ┳┛ ┗┳   ┃
//   ┃           ┃
//   ┃     ┻     ┃
//   ┗━┓       ┏━┛
//     ┃       ┃   神兽保佑
//     ┃       ┃   代码无BUG！
//     ┃       ┗━━━┓
//     ┃           ┣┓
//     ┃          ┏┛
//     ┗┓┓┏━━━━┳┓┏┛
//      ┃┫┫    ┃┫┫
//      ┗┻┛    ┗┻┛

/**
 * base adapter
 * Created by zinmm on 15/10/28.
 */
public abstract class BaseAdapter<M> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_COMMON = 1;

    public int position;
    public String key;
    protected Context mContext;
    protected List<M> mDatas;
    private HeaderDelegate mHeaderDelegate;
    private LoadMoreDelegate mLoadMoreDelegate;

    public BaseAdapter(Context mContext) {
        this.mContext = mContext;
        this.mDatas = new ArrayList<>();
    }

    public void setHeaderDelegate(HeaderDelegate delegate) {
        mHeaderDelegate = delegate;
        notifyDataSetChanged();
    }

    public void setLoadMoreDelegate(LoadMoreDelegate loadMoreDelegate) {
        mLoadMoreDelegate = loadMoreDelegate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int itemType) {
        if (itemType == TYPE_HEADER && hasHeader()) {
            return mHeaderDelegate.onCreateHeader(parent, position);
        } else {
            return onCreateCommon(parent, position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemType(position)) {
            case HEADER:
                mHeaderDelegate.onBindHeader(holder, position);
                break;
            case COMMON:
                int p = position - getHeaderCount();
                onBindCommon(holder, mDatas.get(p));
                break;
            default:
//                new IllegalArgumentException("BaseAdapter.class， 超出条目类型");
        }

        if (position == getItemCount() - 2 && mLoadMoreDelegate != null) {
            loadMore();
        }
    }

    public void loadMore() {
        mLoadMoreDelegate.onLoadMore();
    }

    public HOLDER_TYPE getItemType(int position) {
        int type = getItemViewType(position);

        switch (type) {
            case TYPE_HEADER:
                return HOLDER_TYPE.HEADER;
            case TYPE_COMMON:
                return HOLDER_TYPE.COMMON;
            default:
//                new IllegalArgumentException(this.getClass().getName() + "， 多余的TYPE");
        }

        return HOLDER_TYPE.ERROR;
    }

    @Override
    public int getItemCount() {
        return getCommonItemCount() + getHeaderCount();
    }

    public int getAdapterCount() {
        return getCommonItemCount() + getHeaderCount();
    }

    public abstract void onBindCommon(RecyclerView.ViewHolder holder, M item);

    public abstract int getCommonType(int position);

    public abstract RecyclerView.ViewHolder onCreateCommon(ViewGroup parent, int viewType);

    public int getCommonItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public int getHeaderCount() {
        return mHeaderDelegate != null ? mHeaderDelegate.getHeaderCount() : 0;
    }

    public boolean hasHeader() {
        return mHeaderDelegate != null && mHeaderDelegate.getHeaderCount() != 0;
    }

    @Override
    public int getItemViewType(int position) {

        this.position = position;
        if (position < getHeaderCount() && hasHeader()) {
            return TYPE_HEADER;
        } else {
            return TYPE_COMMON;
        }
    }

    public M getItemData(int position) {
        return mDatas.get(position);
    }

    public List<M> getDatas() {
        return mDatas;
    }

    public void addItem(M data) {
        this.mDatas.add(data);
    }

    public void addAll(List<M> data) {
        this.mDatas.addAll(data);
    }

    public void setDatas(List<M> data) {
        mDatas = data;
    }

    public void setItems(List<M> data) {
        setItems(data, true);
    }
    List<M> datas ;
    public void setItems(List<M> data, boolean needClear) {
        datas = data;
        Log.e("List","list:"+data.size());
            if (needClear)
                this.mDatas.clear();
        Log.e("List","list:"+datas.size());
            this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        mDatas.remove(position);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void clear() {
        mDatas.clear();
    }

    private enum HOLDER_TYPE {
        HEADER, COMMON, ERROR
    }
}