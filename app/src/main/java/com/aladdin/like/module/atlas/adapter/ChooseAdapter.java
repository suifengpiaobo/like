package com.aladdin.like.module.atlas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.ThemeModes;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/5/20 上午11:56.
 * Email:444288256@qq.com
 */
public class ChooseAdapter extends BaseAdapter {
    Context mContext;
    List<ThemeModes.Theme> mAtlas = new ArrayList<>();
    List<ThemeModes.Theme> mChoose = new ArrayList<>(); //存放选中的图集
    LayoutInflater mInflater;

    public ChooseAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);

    }

    public void setAtlas(List<ThemeModes.Theme> atlas) {
        mAtlas.clear();
        this.mAtlas.addAll(atlas);
    }

    public void setChoose(List<ThemeModes.Theme> choose){
        mChoose.clear();
        this.mChoose.addAll(choose);
    }

    @Override
    public int getCount() {
        return mAtlas.size();
    }

    @Override
    public Object getItem(int position) {
        return mChoose.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        ThemeModes.Theme item;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.layout_atlas_choose,null);
            viewHolder = new ViewHolder();
            viewHolder.mAtlasItemBg = (SimpleDraweeView) convertView.findViewById(R.id.atlas_item_bg);
            viewHolder.mAtlasTypeName = (TextView) convertView.findViewById(R.id.atlas_type_name);
            viewHolder.mAtlasChoose = (ImageView) convertView.findViewById(R.id.atlas_choose);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        item = mAtlas.get(position);

        if (item != null){
            viewHolder.mAtlasChoose.setSelected(false);
            if (mChoose.contains(item)){
                viewHolder.mAtlasChoose.setSelected(true);
            }else{
                viewHolder.mAtlasChoose.setSelected(false);
            }
            viewHolder.mAtlasItemBg.setImageURI(item.themeImgUrl);

            viewHolder.mAtlasTypeName.setText(item.themeName);

        }

        return convertView;
    }

    public ThemeModes.Theme getItemObject(int position) {
        if (position < 0 || position > mChoose.size() - 1) return null;
        return mAtlas.get(position);
    }

    class ViewHolder {
        SimpleDraweeView mAtlasItemBg;
        TextView mAtlasTypeName;
        public ImageView mAtlasChoose;
    }
}
