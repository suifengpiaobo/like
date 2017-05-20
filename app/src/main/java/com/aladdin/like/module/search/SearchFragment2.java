package com.aladdin.like.module.search;

import android.content.Context;
import android.support.v4.widget.Space;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aladdin.base.BaseFragment;
import com.aladdin.like.R;
import com.aladdin.like.model.PrefecturePojo;
import com.aladdin.like.widget.MyGridView;
import com.aladdin.utils.ImageLoaderUtils;
import com.aladdin.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Description
 * Created by zxl on 2017/5/1 上午8:54.
 * Email:444288256@qq.com
 */
public class SearchFragment2 extends BaseFragment {

    @BindView(R.id.horizontal_gridview)
    MyGridView mHorizontalGridview;
    @BindView(R.id.search_tip)
    TextView mSearchTip;
    @BindView(R.id.search_grid)
    MyGridView mSearchGrid;

    List<PrefecturePojo.Prefecture> mPrefectures = new ArrayList<>();
    HorizontalGridViewAdapter mHorizontalAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.layout_search_fragment2;
    }

    @Override
    protected void initView() {
        for (int i = 0;i<5;i++){
            PrefecturePojo.Prefecture prefecture = new PrefecturePojo.Prefecture();
            prefecture.typeName = "欧美图集"+i;
            mPrefectures.add(prefecture);
        }
        mHorizontalAdapter = new HorizontalGridViewAdapter();
        mHorizontalAdapter.setData(getActivity(),mPrefectures);
        mHorizontalGridview.setAdapter(mHorizontalAdapter);
        mHorizontalAdapter.notifyDataSetChanged();

    }

    @Override
    protected void lazyFetchData() {

    }

    class HorizontalGridViewAdapter extends BaseAdapter {
        List<PrefecturePojo.Prefecture> mPrefectures;
        private Context mContext;
        private LayoutInflater mInflater;

        public void setData(Context context,List<PrefecturePojo.Prefecture> prefectures){
            mPrefectures = new ArrayList<>();
            this.mContext = context;
            mPrefectures.addAll(prefectures);
            mInflater = LayoutInflater.from(mContext);
        }
        @Override
        public int getCount() {
            return mPrefectures.size();
        }

        @Override
        public PrefecturePojo.Prefecture getItem(int position) {
            return mPrefectures.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HorizontalViewHolder viewHolder = null;
            PrefecturePojo.Prefecture item =mPrefectures.get(position);
            if (convertView ==null){
                viewHolder = new HorizontalViewHolder();
                convertView = mInflater.inflate(R.layout.layout_search_horizontal,null);
                viewHolder.mHorizontalTypeName = (TextView) convertView.findViewById(R.id.horizontal_type_name);
                viewHolder.mSearchHorizontalBg = (ImageView) convertView.findViewById(R.id.search_horizontal_bg);
                viewHolder.mSearchSpace1 = (Space) convertView.findViewById(R.id.search_space_1);

                convertView.setTag(viewHolder);
            }else{
                viewHolder = (HorizontalViewHolder) convertView.getTag();
            }

            viewHolder.mHorizontalTypeName.setText("欧美图集"+position);
            ImageLoaderUtils.displayRoundNative(mContext,viewHolder.mSearchHorizontalBg,R.mipmap.ic_github);
            LogUtil.e("item--->>>"+item);

            return convertView;
        }
        class HorizontalViewHolder{
            Space mSearchSpace1;
            ImageView mSearchHorizontalBg;
            TextView mHorizontalTypeName;
        }
    }

}
