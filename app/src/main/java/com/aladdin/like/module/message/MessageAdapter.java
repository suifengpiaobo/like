package com.aladdin.like.module.message;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.receiver.XGNotification;
import com.aladdin.utils.LogUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Description
 * Created by zxl on 2017/7/8 上午11:01.
 * Email:444288256@qq.com
 */
public class MessageAdapter extends BaseAdapter{
    private Activity mActivity;
    private LayoutInflater mInflater;
    List<XGNotification> adapterData;

    public MessageAdapter(Activity activity){
        mActivity = activity;
        mInflater = LayoutInflater.from(mActivity);
        adapterData = new ArrayList<>();
    }
    public List<XGNotification> getData() {
        return adapterData;
    }

    public void setData(List<XGNotification> pushInfoList) {
        adapterData.clear();
        adapterData.addAll(pushInfoList);
        LogUtil.i("adapterData-->>"+adapterData);
    }

    public void addData(List<XGNotification> pushInfoList){
        adapterData.addAll(pushInfoList);
    }

    @Override
    public int getCount() {
        return (null == adapterData ? 0 : adapterData.size());
    }

    @Override
    public Object getItem(int position) {
        return (null == adapterData ? null : adapterData.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        pushViewHolder aholder = null;
        XGNotification item = adapterData.get(position);
        if (convertView == null) {
            aholder = new pushViewHolder();
            convertView = mInflater.inflate(R.layout.item_push, null);
            aholder.contentv = (TextView) convertView
                    .findViewById(R.id.push_content);
            aholder.timev = (TextView) convertView
                    .findViewById(R.id.push_time);
            aholder.titlev = (TextView) convertView
                    .findViewById(R.id.push_title);
            convertView.setTag(aholder);
        } else {
            aholder = (pushViewHolder) convertView.getTag();
        }
        aholder.titlev.setText(item.getTitle());
        aholder.contentv.setText(item.getContent());
        if (item.getUpdate_time() != null
                && item.getUpdate_time().length() > 18) {
            String notificationdate = item.getUpdate_time()
                    .substring(0, 10);
            String notificationtime = item.getUpdate_time().substring(11);
            if (new SimpleDateFormat("yyyy-MM-dd").format(
                    Calendar.getInstance().getTime()).equals(
                    notificationdate)) {
                aholder.timev.setText(notificationtime);
            } else {
                aholder.timev.setText(notificationdate);
            }
        } else {
            aholder.timev.setText("未知");
        }
        return convertView;
    }

    private class pushViewHolder {
        TextView titlev;
        TextView timev;
        TextView contentv;
    }
}
