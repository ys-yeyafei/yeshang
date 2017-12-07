package cn.ysgroup.ysdai.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Adapters.HongBaoListAdapter;
import cn.ysgroup.ysdai.Beans.user.UserHongbaoBean;
import cn.ysgroup.ysdai.Beans.user.UserHongbaoViews;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadMoreListView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HongBaoListActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener
{
    @Bind(R.id.hong_bao_list_view)
    LoadMoreListView hongBaoListView;
    @Bind(R.id.hong_bao_swip_layout)
    SwipeRefreshLayout hongBaoSwipLayout;

    private String TAG = "我的红包列表";
    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbList";

    private List<UserHongbaoViews> hongbaoList = new ArrayList<UserHongbaoViews>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int lastPageNumber = 1;//已经加载的页数
    private int currentStatus = 0;//当前红包类型

    private HongBaoListAdapter myAdapter;
    private PopupWindow popupWindow;
    private TextView titleText;
    private LoadingDialog loadingDialog;

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x111:
                    //刷新数据
                    myAdapter = new HongBaoListAdapter(HongBaoListActivity.this, hongbaoList,getLayoutInflater());
                    hongBaoListView.setAdapter(myAdapter);
                    if (hongBaoSwipLayout.isShown())
                    {
                        hongBaoSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    hongBaoListView.onLoadComplete();
                    myAdapter.setHongbaoList(hongbaoList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hong_bao_list);
        ButterKnife.bind(this);

        initActionBar("我的红包列表");

        myAdapter = new HongBaoListAdapter(this, hongbaoList, getLayoutInflater());
        hongBaoListView.setAdapter(myAdapter);
        hongBaoListView.setLoadMoreListen(this);
        hongBaoSwipLayout.setOnRefreshListener(this);
        hongBaoSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        //红包状态：0未使用1使用2已过期
        RequestForListData(basicUrl, lastPageNumber, PAGESIZE, currentStatus, false);
        if (loadingDialog == null)
        {

            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initActionBar(String title)
    {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.hb_action_bar_layout);
        TextView moreBack = (TextView) actionBar.getCustomView().findViewById(R.id.hb_actionbar_back);
        titleText = (TextView) actionBar.getCustomView().findViewById(R.id.hb_actionbar_title);

        LinearLayout titleLayout = (LinearLayout) actionBar.getCustomView().findViewById(R.id.hb_actionbar_title_layout);
        moreBack.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finish();
                    }
                }

        );
        titleText.setText(title);
        titleLayout.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (popupWindow != null && popupWindow.isShowing())
                        {
                            popupWindow.dismiss();
                            popupWindow = null;
                        } else
                        {
                            initPopuptWindow();
                            int xPos = getActionBar().getCustomView().getWidth() / 2 - 225;
                            popupWindow.showAsDropDown(getActionBar().getCustomView(), xPos, 0);
                        }

                    }
                }
        );
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, int status, final boolean isRefreshing)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("pager.pageNumber", pageNumber+"").
                add("pager.pageSize",pageSize+"").
                add("status",status+"").build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing())
                        {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (isRefreshing)
                        {
                            if (hongBaoSwipLayout.isShown())
                            {
                                hongBaoSwipLayout.setRefreshing(false);
                            }
                        } else
                        {
                            hongBaoListView.onLoadComplete();
                        }
                        Toast.makeText(HongBaoListActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                final UserHongbaoBean resultBean = JSON.parseObject(s, UserHongbaoBean.class);
                Log.i(TAG, s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing())
                        {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001"))
                        {

                            new Handler().post(
                                    new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            totalPageCount = resultBean.getPageBean().getPageCount();

                                            hongbaoList.addAll(resultBean.getUserHongbaoViews());
                                            if (isRefreshing)
                                            {
                                                Message msg = new Message();
                                                msg.what = 0x111;
                                                mHandler.sendMessage(msg);
                                            } else
                                            {
                                                Message msg = new Message();
                                                msg.what = 0x222;
                                                mHandler.sendMessage(msg);
                                            }
                                        }
                                    }
                            );
                        } else
                        {
                            Toast.makeText(HongBaoListActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }


    protected void initPopuptWindow()
    {
        if (popupWindow == null)
        {
            // 获取自定义布局文件activity_popupwindow_left.xml的视图
            View popupWindow_view = getLayoutInflater().inflate(R.layout.hb_poupup_window_layout, null,
                    false);
            popupWindow_view.setAlpha(0.7f);
            LinearLayout newLayout = (LinearLayout) popupWindow_view.findViewById(R.id.popup_hb_not_used);
            LinearLayout usedLayout = (LinearLayout) popupWindow_view.findViewById(R.id.popup_hb_used);
            LinearLayout guoqiLayout = (LinearLayout) popupWindow_view.findViewById(R.id.popup_hb_guoqi);

            newLayout.setOnClickListener(
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            titleText.setText("我的红包-未使用");
                            popupWindow.dismiss();
                            popupWindow = null;
                            lastPageNumber = 1;
                            currentStatus = 0;
                            hongbaoList.clear();
                            myAdapter.notifyDataSetInvalidated();
                            RequestForListData(basicUrl, 1, 10, currentStatus, true);
                        }
                    }
            );
            usedLayout.setOnClickListener(
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            titleText.setText("我的红包-已使用");
                            popupWindow.dismiss();
                            popupWindow = null;
                            lastPageNumber = 1;
                            currentStatus = 1;
                            hongbaoList.clear();
                            myAdapter.notifyDataSetInvalidated();
                            RequestForListData(basicUrl, 1, 10, currentStatus, true);
                        }
                    }
            );
            guoqiLayout.setOnClickListener(
                    new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            titleText.setText("我的红包-已过期");
                            popupWindow.dismiss();
                            popupWindow = null;
                            lastPageNumber = 1;
                            currentStatus = 2;
                            hongbaoList.clear();
                            myAdapter.notifyDataSetInvalidated();
                            RequestForListData(basicUrl, 1, 10, currentStatus, true);
                        }
                    }
            );


            // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
            popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // 设置动画效果
            //popupWindow.setAnimationStyle(R.style.AnimationFade);
            // 点击其他地方消失
            popupWindow_view.setOnTouchListener(new View.OnTouchListener()
            {
                @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    // TODO Auto-generated method stub
                    if (popupWindow != null && popupWindow.isShowing())
                    {
                        popupWindow.dismiss();
                        popupWindow = null;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void loadMore()
    {
        lastPageNumber++;
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                RequestForListData(basicUrl, lastPageNumber, 10, currentStatus, false);
            }
        }, 500);
    }

    @Override
    public void onRefresh()
    {
        lastPageNumber = 1;
        hongbaoList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                RequestForListData(basicUrl, 1, 10, currentStatus, true);
            }

        }, 500);
    }


}
