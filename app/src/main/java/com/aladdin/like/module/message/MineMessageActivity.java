package com.aladdin.like.module.message;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.receiver.NotificationService;
import com.aladdin.like.receiver.XGNotification;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Description 消息
 * Created by zxl on 2017/5/20 下午8:38.
 */
public class MineMessageActivity extends BaseActivity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

    @BindView(R.id.back)
    ImageView mBack;
    @BindView(R.id.no_message_tip)
    TextView mNoMessageTip;
    @BindView(R.id.push_list)
    ListView mPushList;

    private int currentPage = 1;// 默认第一页
    private static final int lineSize = 10;// 每次显示数
    private int allRecorders = 0;// 全部记录数
    private int pageSize = 1;// 默认共1页
    private boolean isLast = false;// 是否最后一条
    private int firstItem;// 第一条显示出来的数据的游标
    private int lastItem;// 最后显示出来数据的游标
    private String id = "";// 查询条件

    List<XGNotification> mMessage;

    MessageAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_message;
    }

    @Override
    protected void initView() {
        int count = NotificationService.getInstance(MineMessageActivity.this).getNewMessageCount();
        mMessage = NotificationService.getInstance(this).getScrollData(
                currentPage, lineSize, id);
        allRecorders = NotificationService.getInstance(this).getCount();

        if (allRecorders != 0) {
            mNoMessageTip.setVisibility(View.GONE);
            mPushList.setVisibility(View.VISIBLE);
        }else{
            mNoMessageTip.setVisibility(View.VISIBLE);
            mPushList.setVisibility(View.GONE);
        }

        // 计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;
        // 创建适配器
        mAdapter = new MessageAdapter(this);
        mPushList.setAdapter(mAdapter);
        mAdapter.setData(mMessage);
        mAdapter.notifyDataSetChanged();
        //点击事件
        mPushList.setOnItemClickListener(this);
        //滑动事件
        mPushList.setOnScrollListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 是否到最底部并且数据还没读完
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            if (isLast && currentPage < pageSize) {
                currentPage++;
                // 设置显示位置
                mPushList.setSelection(lastItem);
                // 增加数据
                appendNotifications(id);
            }
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
                && firstItem == 0) {

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        firstItem = firstVisibleItem;
        lastItem = totalItemCount - 1;
        if (firstVisibleItem + visibleItemCount == totalItemCount) {
            isLast = true;
        } else {
            isLast = false;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int index, long id) {
//        Intent ait = new Intent(this, MsgInfoActivity.class);
//        if (index > 0 && index <= lastItem) {
//            XGNotification xgnotification = mAdapter.getData().get(index - 1);
//            ait.putExtra("msg_id", xgnotification.getMsg_id());
//            ait.putExtra("title", xgnotification.getTitle());
//            ait.putExtra("content", xgnotification.getContent());
//            ait.putExtra("activity", xgnotification.getActivity());
//            ait.putExtra("notificationActionType",
//                    xgnotification.getNotificationActionType());
//            ait.putExtra("update_time", xgnotification.getUpdate_time());
//            this.startActivity(ait);
//        }
    }

    private void appendNotifications(String id) {

        // 更新适配器
        mAdapter.addData(
                NotificationService.getInstance(this).getScrollData(
                        currentPage, lineSize, id));

        // 通知改变
        mAdapter.notifyDataSetChanged();
    }
}
