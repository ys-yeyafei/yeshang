package cn.ysgroup.ysdai.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Adapters.RechargeRecordListAdapter;
import cn.ysgroup.ysdai.Beans.funds.UserRecharge;
import cn.ysgroup.ysdai.Beans.funds.UserRechargeList;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
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

public class RechargeRecordActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.recharge_record_list_view)
    LoadMoreListView rechargeRecordListView;
    @Bind(R.id.recharge_record_swip_layout)
    SwipeRefreshLayout rechargeRecordSwipLayout;
    @Bind(R.id.recharge_record_toolbar_back)
    IconFontTextView rechargeRecordToolbarBack;
    @Bind(R.id.recharge_record_toolbar_title)
    TextView rechargeRecordToolbarTitle;
    @Bind(R.id.recharge_record_toolbar)
    Toolbar rechargeRecordToolbar;

    private String TAG = "充值记录";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/rechargeList";

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private RechargeRecordListAdapter myAdapter;
    private List<UserRecharge> rechargeList = new ArrayList<UserRecharge>();
    private LoadingDialog loadingDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    if (rechargeList.size() > 0) {
                        nothing_img.setVisibility(View.GONE);
                    }
                    //刷新数据
                    myAdapter = new RechargeRecordListAdapter(RechargeRecordActivity.this, rechargeList, getLayoutInflater());
                    rechargeRecordListView.setAdapter(myAdapter);
                    if (rechargeRecordSwipLayout.isShown()) {
                        rechargeRecordSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    if (rechargeList.size() > 0) {
                        nothing_img.setVisibility(View.GONE);
                    }
                    //直接添加数据
                    rechargeRecordListView.onLoadComplete();
                    myAdapter.setChargeList(rechargeList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private ImageView nothing_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_record);
        ButterKnife.bind(this);
        nothing_img = (ImageView) findViewById(R.id.recharge_record_nothing_img);
        initToolBar();


        myAdapter = new RechargeRecordListAdapter(this, rechargeList, getLayoutInflater());
        rechargeRecordListView.setAdapter(myAdapter);
        rechargeRecordListView.setLoadMoreListen(this);
        rechargeRecordSwipLayout.setOnRefreshListener(this);
        rechargeRecordSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        RequestForListData(BASIC_URL, 1, PAGESIZE, false);
        if (loadingDialog == null) {

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

    public void initToolBar() {
        rechargeRecordToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(rechargeRecordToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("pager.pageNumber", pageNumber + "").
                add("pager.pageSize", pageSize + "").
                add("type", "1").build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
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
                            if (rechargeRecordSwipLayout.isShown()) {
                                rechargeRecordSwipLayout.setRefreshing(false);
                            }
                        } else {
                            rechargeRecordListView.onLoadComplete();
                        }
                        Toast.makeText(RechargeRecordActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                final UserRechargeList resultBean = JSON.parseObject(s, UserRechargeList.class);
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
                            rechargeList.addAll(resultBean.getUserRechargesList());
                            if (refreshing) {
                                Message msg = new Message();
                                msg.what = 0x111;
                                mHandler.sendMessage(msg);
                            } else {
                                Message msg = new Message();
                                msg.what = 0x222;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Toast.makeText(RechargeRecordActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();

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
            rechargeRecordListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        rechargeList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(BASIC_URL, 1, PAGESIZE, true);
            }

        }, 500);
    }


}
