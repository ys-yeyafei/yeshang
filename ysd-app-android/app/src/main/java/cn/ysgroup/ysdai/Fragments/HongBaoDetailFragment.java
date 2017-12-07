package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HongBaoDetailFragment extends BaseFragment implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {
    private Activity activity;
    private String TAG = "我的红包列表";
    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListLooked";

    private List<UserHongbaoViews> hongbaoList = new ArrayList<UserHongbaoViews>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    public int lastPageNumber = 1;//已经加载的页数

    private HongBaoListAdapter myAdapter;
    private LoadingDialog loadingDialog;
    public int currentStatus;//红包类型
    //是否是点击红包类型
    public boolean isClick = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    isClick = false;
                    if (hongbaoList.size() > 0) {
                        no_hb.setVisibility(View.GONE);
                    } else {
                        no_hb.setVisibility(View.VISIBLE);
                    }
                    //刷新数据
                    myAdapter = new HongBaoListAdapter(activity, hongbaoList, activity.getLayoutInflater());
                    hbDetailListView.setAdapter(myAdapter);
                    if (hbDetailSwipeLayout.isShown()) {
                        hbDetailSwipeLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    isClick = false;
                    if (hongbaoList.size() > 0) {
                        no_hb.setVisibility(View.GONE);
                    } else {
                        no_hb.setVisibility(View.VISIBLE);
                    }
                    //直接添加数据
                    hbDetailListView.onLoadComplete();
                    myAdapter.setHongbaoList(hongbaoList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    private static final String ARG_PARAM = "hb_type";
    @Bind(R.id.hb_detail_list_view)
    LoadMoreListView hbDetailListView;
    @Bind(R.id.hb_detail_swipe_layout)
    SwipeRefreshLayout hbDetailSwipeLayout;
    private View rootView;
    private ImageView no_hb;


//    public static HongBaoDetailFragment newInstance(int param)
//    {
//        HongBaoDetailFragment fragment = new HongBaoDetailFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_PARAM, param);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public HongBaoDetailFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public HongBaoDetailFragment(Activity activity, int param) {
        this.activity = activity;
        rootView = View.inflate(activity, R.layout.fragment_hong_bao_detail, null);
        ButterKnife.bind(this, rootView);
        no_hb = (ImageView) rootView.findViewById(R.id.no_hb);
        myAdapter = new HongBaoListAdapter(activity, hongbaoList, activity.getLayoutInflater());
        hbDetailListView.setAdapter(myAdapter);
        hbDetailListView.setLoadMoreListen(this);
        hbDetailSwipeLayout.setOnRefreshListener(this);
        hbDetailSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        //红包状态：0未使用1使用2已过期
//        RequestForListData(basicUrl, lastPageNumber, PAGESIZE, param, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentStatus = getArguments().getInt(ARG_PARAM);
        }
    }


    @Override
    public View getmView() {
        return rootView;
    }

    /**
     * 请求红包数据
     *
     * @param basicUrl
     * @param pageNumber
     * @param pageSize
     * @param status
     * @param isRefreshing
     */
    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, int status, final boolean isRefreshing) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
                add("status", status + "").
                add("pager.pageSize", pageSize + "").
                add("pager.pageNumber", pageNumber + "").build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (isRefreshing) {
                            if (hbDetailSwipeLayout.isShown()) {
                                hbDetailSwipeLayout.setRefreshing(false);
                            }
                        } else {
                            hbDetailListView.onLoadComplete();
                        }
                        Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, s);
                final UserHongbaoBean resultBean = JSON.parseObject(s, UserHongbaoBean.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {

                            new Handler().post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            totalPageCount = resultBean.getPageBean().getPageCount();
                                            if (!isClick && resultBean.getUserHongbaoViews() != null) {
                                                hongbaoList.addAll(resultBean.getUserHongbaoViews());
                                            } else {
                                                hongbaoList.clear();
                                                if (resultBean.getUserHongbaoViews() != null)
                                                    hongbaoList.addAll(resultBean.getUserHongbaoViews());
                                            }
                                            if (isRefreshing) {
                                                Message msg = new Message();
                                                msg.what = 0x111;
                                                mHandler.sendMessage(msg);
                                            } else {
                                                Message msg = new Message();
                                                msg.what = 0x222;
                                                mHandler.sendMessage(msg);
                                            }
                                        }
                                    }
                            );
                        } else {
                            Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    @Override
    public void loadMore() {
        lastPageNumber++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(basicUrl, lastPageNumber, 10, currentStatus, false);
            }
        }, 500);
    }

    @Override
    public void onRefresh() {
        lastPageNumber = 1;
        hongbaoList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(basicUrl, 1, 10, currentStatus, true);
            }

        }, 500);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setCurrentState(int state) {
        currentStatus = state;
    }


}
