package cn.ysgroup.ysdai.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Activities.ArticleActivity;
import cn.ysgroup.ysdai.Adapters.CashRewardListAdapter;
import cn.ysgroup.ysdai.Beans.user.AwawdCashBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadMoreListView;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class RewardFragment extends Fragment implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.cash_award_list_view)
    LoadMoreListView cashAwardListView;
    @Bind(R.id.cash_award_swipe_layout)
    SwipeRefreshLayout cashAwardSwipeLayout;
    @Bind(R.id.cash_award_nothing_img)
    ImageView cashAwardNothingImg;

    private String TAG = "现金奖励";
    private CashRewardListAdapter myAdapter;
    private List<AwawdCashBean.CashListBean> awardList = new ArrayList<AwawdCashBean.CashListBean>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/awardCash";


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    //刷新数据
                    cashAwardNothingImg.setVisibility(View.GONE);
                    myAdapter = new CashRewardListAdapter(getActivity(), awardList, getActivity().getLayoutInflater());
                    cashAwardListView.setAdapter(myAdapter);
                    if (cashAwardSwipeLayout.isShown()) {
                        cashAwardSwipeLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    cashAwardNothingImg.setVisibility(View.GONE);
                    cashAwardListView.onLoadComplete();
                    myAdapter.setAwardList(awardList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private TextView explanation;

    public static RewardFragment newInstance() {
        RewardFragment fragment = new RewardFragment();
        return fragment;
    }

    public RewardFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reward, container, false);
        ButterKnife.bind(this, rootView);
        explanation = (TextView) rootView.findViewById(R.id.reward_explanation);
        myAdapter = new CashRewardListAdapter(getActivity(), awardList, getActivity().getLayoutInflater());
        cashAwardListView.setAdapter(myAdapter);
        cashAwardListView.setLoadMoreListen(RewardFragment.this);
        cashAwardSwipeLayout.setOnRefreshListener(RewardFragment.this);
        cashAwardSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        RequestForListData(basicUrl, 1, PAGESIZE, true);


        //奖励说明
        explanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("id", "653");
                intent.putExtra("header", "奖励说明");
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
                add("pager.pageNumber", pageNumber + "").
                add("pager.pageSize", pageSize + "").
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, s);
                final AwawdCashBean resultBean = JSON.parseObject(s, AwawdCashBean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (resultBean.getRcd().equals("R0001")) {
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            awardList.addAll(resultBean.getCashList());
                            if (refreshing) {
                                if (resultBean.getCashList() != null && resultBean.getCashList().size() > 0) {
                                    Message msg = new Message();
                                    msg.what = 0x111;
                                    mHandler.sendMessage(msg);
                                } else {
                                    cashAwardNothingImg.setVisibility(View.VISIBLE);
                                    if (cashAwardSwipeLayout.isShown()) {
                                        cashAwardSwipeLayout.setRefreshing(false);
                                    }
                                }
                            } else {
                                Message msg = new Message();
                                msg.what = 0x222;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();

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
                    RequestForListData(basicUrl, currentBottomPageIndex, PAGESIZE, false);
                }
            }, 500);
        } else {
            cashAwardListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        awardList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(basicUrl, 1, PAGESIZE, true);
            }

        }, 500);
    }
}
