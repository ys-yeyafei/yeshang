package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Adapters.InvestRecordListAdapter;
import cn.ysgroup.ysdai.Beans.center.UserTender;
import cn.ysgroup.ysdai.Beans.center.UserTenderList;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LMessageDialog;
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

public class InvestRecordActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.invest_record_list_view)
    LoadMoreListView investRecordListView;
    @Bind(R.id.invest_record_swip_layout)
    SwipeRefreshLayout investRecordSwipLayout;
    @Bind(R.id.invest_record_toolbar_back)
    IconFontTextView investRecordToolbarBack;
    @Bind(R.id.invest_record_toolbar_title)
    TextView investRecordToolbarTitle;
    @Bind(R.id.invest_record_toolbar)
    Toolbar investRecordToolbar;
    @Bind(R.id.invest_record_nothing_img)
    ImageView investRecordNothingImg;

    private String TAG = "投资记录";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/tzjl";
    private List<UserTender> tenderList = new ArrayList<UserTender>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private InvestRecordListAdapter myAdapter;
    private LoadingDialog loadingDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    //刷新数据
                    investRecordNothingImg.setVisibility(View.GONE);
                    myAdapter = new InvestRecordListAdapter(InvestRecordActivity.this, tenderList);
                    investRecordListView.setAdapter(myAdapter);
                    if (investRecordSwipLayout.isShown()) {
                        investRecordSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    investRecordNothingImg.setVisibility(View.GONE);
                    investRecordListView.onLoadComplete();
                    myAdapter.setTenderList(tenderList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_record);
        ButterKnife.bind(this);

        initToolBar();

        myAdapter = new InvestRecordListAdapter(this, tenderList);
        investRecordListView.setAdapter(myAdapter);
        investRecordListView.setLoadMoreListen(this);
        investRecordSwipLayout.setOnRefreshListener(this);
        investRecordSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);


        RequestForListData(BASIC_URL, 1, PAGESIZE, true);
        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }

        investRecordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserTender userTender = tenderList.get(position);
                int a = userTender.getBorrowStatus();
                if (userTender.getBorrowStatus() == 3 ||
                        userTender.getBorrowStatus() == 7 || userTender.getIsNewbor().equals("1")) {
                    Intent intent = new Intent(InvestRecordActivity.this, RepayMentActivity.class);
                    if (tenderList != null) {
                        intent.putExtra("id", userTender.getTenderid());
                        intent.putExtra("projectId", userTender.getBorrowId());
                        intent.putExtra("name", userTender.getBorrowName());
                    }
                    startActivity(intent);
                } else {

                    final LMessageDialog dialog = new LMessageDialog(InvestRecordActivity.this, R.style.ActionSheetDialogStyle);
                    dialog.setMessage("等待项目复审");
                    dialog.setMtitle("我知道了");
                    dialog.show();
                    dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initToolBar() {
        investRecordToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(investRecordToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("pager.pageNumber", pageNumber + "").
                add("pager.pageSize", pageSize + "").
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            private UserTenderList resultBean;

            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (refreshing) {
                            if (investRecordSwipLayout.isShown()) {
                                investRecordSwipLayout.setRefreshing(false);
                            }
                        } else {
                            investRecordListView.onLoadComplete();
                        }
                        Toast.makeText(InvestRecordActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                resultBean = JSON.parseObject(s, UserTenderList.class);
                Log.i(TAG, s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            tenderList.addAll(resultBean.getUserTenderList());
                            if (refreshing) {
                                if (resultBean.getUserTenderList() != null && resultBean.getUserTenderList().size() > 0) {
                                    Message msg = new Message();
                                    msg.what = 0x111;
                                    mHandler.sendMessage(msg);
                                } else {
                                    investRecordNothingImg.setVisibility(View.VISIBLE);
                                    if (investRecordSwipLayout.isShown()) {
                                        investRecordSwipLayout.setRefreshing(false);
                                    }
                                }
                            } else {
                                Message msg = new Message();
                                msg.what = 0x222;
                                mHandler.sendMessage(msg);
                            }

                        } else {
                            if (refreshing) {
                                if (investRecordSwipLayout.isShown()) {
                                    investRecordSwipLayout.setRefreshing(false);
                                }
                            } else {
                                investRecordListView.onLoadComplete();
                            }
                            Toast.makeText(InvestRecordActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void loadMore() {
        if (currentBottomPageIndex < totalPageCount) {
            currentBottomPageIndex++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RequestForListData(BASIC_URL, currentBottomPageIndex, PAGESIZE, false);
                }
            }, 500);
        } else {
            investRecordListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        tenderList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(BASIC_URL, 1, PAGESIZE, true);
            }

        }, 500);
    }

    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("LoginContentBroadCast"));
        super.onDestroy();
    }


}
