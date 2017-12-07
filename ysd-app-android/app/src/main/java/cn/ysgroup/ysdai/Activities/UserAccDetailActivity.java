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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Adapters.AccDetailListAdapter;
import cn.ysgroup.ysdai.Beans.funds.UserAccDetail;
import cn.ysgroup.ysdai.Beans.funds.UserAccDetailList;
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

public class UserAccDetailActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.acc_detail_list_view)
    LoadMoreListView accDetailListView;
    @Bind(R.id.acc_detail_swip_layout)
    SwipeRefreshLayout accDetailSwipLayout;
    @Bind(R.id.user_acc_detail_toolbar_back)
    IconFontTextView userAccDetailToolbarBack;
    @Bind(R.id.user_acc_detail_toolbar_title)
    TextView userAccDetailToolbarTitle;
    @Bind(R.id.user_acc_detail_toolbar)
    Toolbar userAccDetailToolbar;
    @Bind(R.id.acc_detail_nothing_img)
    ImageView accDetailNothingImg;
    private String TAG = "资金记录";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/accountDetailList";
    private List<UserAccDetail> accDetailsList = new ArrayList<UserAccDetail>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private AccDetailListAdapter myAdapter;
    private LoadingDialog loadingDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    //刷新数据
                    accDetailNothingImg.setVisibility(View.GONE);
                    myAdapter = new AccDetailListAdapter(accDetailsList, getLayoutInflater(), UserAccDetailActivity.this);
                    accDetailListView.setAdapter(myAdapter);
                    if (accDetailSwipLayout.isShown()) {
                        accDetailSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    accDetailNothingImg.setVisibility(View.GONE);
                    accDetailListView.onLoadComplete();
                    myAdapter.setAccDetailsList(accDetailsList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_acc_detail);
        ButterKnife.bind(this);

        initToolBar();

        myAdapter = new AccDetailListAdapter(accDetailsList, getLayoutInflater(), this);
        accDetailListView.setAdapter(myAdapter);
        accDetailListView.setLoadMoreListen(this);
        accDetailSwipLayout.setOnRefreshListener(this);
        accDetailSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        RequestForListData(BASIC_URL, 1, PAGESIZE, true);
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
        userAccDetailToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(userAccDetailToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("pager.pageNumber", pageNumber + "").
                add("pager.pageSize", pageSize + "").build();
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
                            if (accDetailSwipLayout.isShown()) {
                                accDetailSwipLayout.setRefreshing(false);
                            }
                        } else {
                            accDetailListView.onLoadComplete();
                        }
                        Toast.makeText(UserAccDetailActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, s);
                final UserAccDetailList resultBean = JSON.parseObject(s, UserAccDetailList.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            accDetailsList.addAll(resultBean.getUserAccDetailList());
                            if (refreshing) {
                                if (resultBean.getUserAccDetailList() != null && resultBean.getUserAccDetailList().size() > 0) {
                                    Message msg = new Message();
                                    msg.what = 0x111;
                                    mHandler.sendMessage(msg);
                                } else {
                                    accDetailNothingImg.setVisibility(View.VISIBLE);
                                    if (accDetailSwipLayout.isShown()) {
                                        accDetailSwipLayout.setRefreshing(false);
                                    }
                                }
                            } else {
                                Message msg = new Message();
                                msg.what = 0x222;
                                mHandler.sendMessage(msg);
                            }
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivityForResult(new Intent(UserAccDetailActivity.this, LoginActivity.class), 1400);
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(UserAccDetailActivity.this, "登录已过期，请重新登录!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            if (refreshing) {
                                if (accDetailSwipLayout.isShown()) {
                                    accDetailSwipLayout.setRefreshing(false);
                                }
                            } else {
                                accDetailListView.onLoadComplete();
                            }
                            Toast.makeText(UserAccDetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
            accDetailListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        accDetailsList.clear();
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
        Intent intent = new Intent("LoginContentBroadCast");
        intent.putExtra("Quickly","cash");
        sendBroadcast(intent);
        super.onDestroy();
    }


}
