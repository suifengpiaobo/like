package com.aladdin.like.module.atlas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.model.AtlasPicturePojo;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/5/20 上午11:56.
 * Email:444288256@qq.com
 */
public class ChooseAdapter extends BaseAdapter {
    Context mContext;
    List<AtlasPicturePojo.AtlasPicture> mAtlas = new ArrayList<>();
    List<AtlasPicturePojo.AtlasPicture> mChoose = new ArrayList<>(); //存放选中的图集
    LayoutInflater mInflater;

    private Integer[] imgs = {
            R.drawable.picture_1, R.drawable.picture_2, R.drawable.picture_3,
            R.drawable.picture_4, R.drawable.picture_5, R.drawable.picture_6,
            R.drawable.picture_7, R.drawable.picture_8, R.drawable.picture_9,
            R.drawable.picture_10, R.drawable.picture_11, R.drawable.picture_12,
    };

    public ChooseAdapter(Context context,List<AtlasPicturePojo.AtlasPicture> choose){
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mAtlas.clear();
        this.mAtlas.addAll(choose);
    }

    public void setChoose(List<AtlasPicturePojo.AtlasPicture> choose){
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
        AtlasPicturePojo.AtlasPicture item;
        if (convertView == null){
            convertView = mInflater.inflate(R.layout.layout_atlas_choose,null);
            viewHolder = new ViewHolder();
            viewHolder.mAtlasItemBg = (ImageView) convertView.findViewById(R.id.atlas_item_bg);
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
            viewHolder.mAtlasItemBg.setBackgroundResource(imgs[position]);
//            ImageLoaderUtils.displayRoundNative(mContext,viewHolder.mAtlasItemBg,imgs[position]);

            viewHolder.mAtlasTypeName.setText(item.name);

        }

        return convertView;
    }

    public AtlasPicturePojo.AtlasPicture getItemObject(int position) {
        if (position < 0 || position > mChoose.size() - 1) return null;
        return mAtlas.get(position);
    }

    class ViewHolder {
        ImageView mAtlasItemBg;
        TextView mAtlasTypeName;
        public ImageView mAtlasChoose;
    }
}
