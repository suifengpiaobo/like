package com.aladdin.like.module.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aladdin.like.R;
import com.aladdin.like.base.BaseActivity;
import com.aladdin.like.receiver.NotificationService;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

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

    private LinearLayout bloadLayout;// 加载提示的布局
    private LinearLayout tloadLayout;// 加载提示的布局
    private TextView bloadInfo;// 加载提示
    private TextView tloadInfo;// 加载提示

    private int currentPage = 1;// 默认第一页
    private static final int lineSize = 10;// 每次显示数
    private int allRecorders = 0;// 全部记录数
    private int pageSize = 1;// 默认共1页
    private boolean isLast = false;// 是否最后一条
    private int firstItem;// 第一条显示出来的数据的游标
    private int lastItem;// 最后显示出来数据的游标
    private String id = "";// 查询条件
    private boolean isUpdate = false;
    private MsgReceiver updateListViewReceiver;
    private NotificationService notificationService;
    private Context context;

    MessageAdapter mAdapter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine_message;
    }

    @Override
    protected void initView() {
        context=this;
        //绑定列表展示
        notificationService = NotificationService.getInstance(this);

        //注册数据更新监听器
        updateListViewReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.aladdin.like.activity.UPDATE_LISTVIEW");
        registerReceiver(updateListViewReceiver, intentFilter);


        //点击事件
        mPushList.setOnItemClickListener(this);
        //滑动事件
        mPushList.setOnScrollListener(this);
        //创建一个角标线性布局来显示正在加载
        bloadLayout = new LinearLayout(this);
        bloadLayout.setMinimumHeight(100);
        bloadLayout.setGravity(Gravity.CENTER);
        //定义一个文本显示"正在加载文本"
        bloadInfo = new TextView(this);
        bloadInfo.setTextSize(16);
        bloadInfo.setTextColor(Color.parseColor("#858585"));
        bloadInfo.setText("加载更多...");
        bloadInfo.setGravity(Gravity.CENTER);
        //绑定组件
        bloadLayout.addView(bloadInfo, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        bloadLayout.getBottom();
        //绑定提示到列表底部
        mPushList.addFooterView(bloadLayout);

        // 4. 创建一个角标线性布局来显示正在加载
        tloadLayout = new LinearLayout(this);
        tloadLayout.setGravity(Gravity.CENTER);
        tloadLayout.setBackgroundResource(R.color.white);
        // 定义一个文本显示"正在加载文本"
        tloadInfo = new TextView(this);
        tloadInfo.setTextSize(14);
        tloadInfo.setTextColor(Color.parseColor("#858585"));
        // tloadInfo.setBackgroundResource(R.color.gray);
        tloadInfo.setText("更多新消息...");
        tloadInfo.setGravity(Gravity.CENTER);
        tloadInfo.setHeight(0);

        // 綁定組件
        tloadLayout.addView(tloadInfo, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        // 绑定提示到列表底部
        mPushList.addHeaderView(tloadLayout);
        tloadLayout.setVisibility(View.GONE);
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
        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            Toast.makeText(this, "通知被点击:" + click.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(updateListViewReceiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    private void getNotificationswithouthint(String id) {
        if (allRecorders != 0) {
            mNoMessageTip.setVisibility(View.GONE);
        }

        // 计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;

        // 创建适配器
        mAdapter = new MessageAdapter(this);
        mAdapter.setData(NotificationService.getInstance(this).getScrollData(
                currentPage = 1, lineSize, id));
        if (allRecorders <= lineSize) {
            bloadLayout.setVisibility(View.GONE);
            bloadInfo.setHeight(0);
            bloadLayout.setMinimumHeight(0);
        } else {
            if (mPushList.getFooterViewsCount() < 1) {
                bloadLayout.setVisibility(View.VISIBLE);
                bloadInfo.setHeight(50);
                bloadLayout.setMinimumHeight(100);
            }
        }
        mPushList.setAdapter(mAdapter);
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
            } else if (firstItem == 0) {
                if (isUpdate && tloadInfo.getHeight() >= 50) {
                    isUpdate = false;
                    updateNotifications(id);
                    TranslateAnimation alp = new TranslateAnimation(0, 0, 80, 0);
                    alp.setDuration(1000);
                    alp.setRepeatCount(1);
                    tloadLayout.setAnimation(alp);
                    alp.setAnimationListener(new Animation.AnimationListener() {

                        @Override
                        public void onAnimationStart(Animation animation) {
                            tloadInfo.setText("正在更新...");
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            tloadInfo.setText("更多新消息...");
                            tloadLayout.setVisibility(View.GONE);
                            tloadInfo.setHeight(0);
                            tloadLayout.setMinimumHeight(0);
                        }
                    });
                }
            }
        } else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
                && firstItem == 0) {
            if (tloadInfo.getHeight() < 50) {
                isUpdate = true;
                tloadInfo.setHeight(50);
                tloadLayout.setMinimumHeight(100);
                tloadLayout.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateNotifications(String id) {
        // 计算总数据条数
        int oldAllRecorders = allRecorders;
        allRecorders = notificationService.getCount();
        getNotificationswithouthint(id);
        Toast.makeText(
                this,
                "共" + allRecorders + "条信息,更新了"
                        + (allRecorders - oldAllRecorders) + "条新信息",
                Toast.LENGTH_SHORT).show();
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
        // 计算总数据条数
        allRecorders = notificationService.getCount();
        // 计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;
        int oldsize = mAdapter.getData().size();
        // 更新适配器
        mAdapter.getData().addAll(
                NotificationService.getInstance(this).getScrollData(
                        currentPage, lineSize, id));
        // 如果到了最末尾则去掉"正在加载"
        if (allRecorders == mAdapter.getCount()) {
            bloadInfo.setHeight(0);
            bloadLayout.setMinimumHeight(0);
            bloadLayout.setVisibility(View.GONE);
        } else {
            bloadInfo.setHeight(50);
            bloadLayout.setMinimumHeight(100);
            bloadLayout.setVisibility(View.VISIBLE);
        }
        Toast.makeText(
                this,
                "共" + allRecorders + "条信息,加载了"
                        + (mAdapter.getData().size() - oldsize) + "条信息",
                Toast.LENGTH_SHORT).show();
        // 通知改变
        mAdapter.notifyDataSetChanged();
    }

    public class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            allRecorders = notificationService.getCount();
            getNotificationswithouthint(id);
        }
    }
}
