package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Activities.PreviewOneActivity;
import cn.ysgroup.ysdai.Adapters.BorrowListAdapter;
import cn.ysgroup.ysdai.Adapters.ListDropDownAdapter;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowItem;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowList;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.ActivityToFragmentMessage;
import cn.ysgroup.ysdai.EventBus.EventBus;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.DropDownMenu;
import cn.ysgroup.ysdai.UI.LoadMoreListView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.SpinnerData;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListFragment extends BaseFragment implements AdapterView.OnItemClickListener, LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {
    private final String TAG = "投资中心";
    private SwipeRefreshLayout investCenterSwipLayout;
    private LoadMoreListView investCenterListView;
    private DropDownMenu onDropDownMenu;
    private BorrowListAdapter mAdapter;
    private List<BorrowItem> borrowItemList;

    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/borrow";
    public int currentBottomPageIndex = 1;

    private LoadingDialog loadingDialog;

    private String peroidLinmit;//
    private String borrowType;//产品类型
    private String orderTime;//默认排序

    private String headers[] = {"产品类型", "项目期限", "时间排序"};
    private List<View> popupViews = new ArrayList<>();
    private ListDropDownAdapter areaAdapter;
    private ListDropDownAdapter typeAdapter;
    private ListDropDownAdapter sortAdapter;
    private Activity activity;
    public boolean isFirst = true;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    //刷新数据
                    mAdapter = new BorrowListAdapter(activity, borrowItemList);
                    investCenterListView.setAdapter(mAdapter);
                    if (investCenterSwipLayout.isShown()) {
                        investCenterSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    investCenterListView.onLoadComplete();
                    mAdapter.setBorrowItemList(borrowItemList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }


        }
    };
    private View rootView;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent = new Intent();
        intent.setClass(activity, PreviewOneActivity.class);
        intent.putExtra("itemId", borrowItemList.get(position).getId());
        activity.startActivityForResult(intent, 8090);
    }

    public ListFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public ListFragment(Activity activity) {
        this.activity = activity;
        borrowItemList = new ArrayList<BorrowItem>();
        rootView = View.inflate(activity, R.layout.fragment_list, null);
        onDropDownMenu = (DropDownMenu) rootView.findViewById(R.id.list_drop_menu);
        investCenterSwipLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.invest_center_swip_layout);
        investCenterListView = (LoadMoreListView) rootView.findViewById(R.id.invest_center_list_view);
        investCenterSwipLayout.setOnRefreshListener(this);
        investCenterSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        investCenterListView.setLoadMoreListen(this);
        mAdapter = new BorrowListAdapter(activity, new ArrayList<BorrowItem>() {
        });
        investCenterListView.setAdapter(mAdapter);
        investCenterListView.setOnItemClickListener(ListFragment.this);

        setSpinner();
//        RequestForListData(basicUrl, currentBottomPageIndex, 10, true);
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }


    /**
     * 设置下拉列表
     */
    private void setSpinner() {
        final List<SpinnerData> typeDataList = new ArrayList<SpinnerData>();
        typeDataList.add(new SpinnerData("", "类型不限"));
        typeDataList.add(new SpinnerData("16", "新手标"));
        final ListView typeView = new ListView(activity);
        //设置分隔符高度
        typeView.setDividerHeight(0);
        typeAdapter = new ListDropDownAdapter(activity, typeDataList);
        typeView.setAdapter(typeAdapter);

        final List<SpinnerData> peroidDataList = new ArrayList<SpinnerData>();
        peroidDataList.add(new SpinnerData("0", "期限不限"));
        peroidDataList.add(new SpinnerData("1", "0-30天"));
        peroidDataList.add(new SpinnerData("2", "30-90天"));
        peroidDataList.add(new SpinnerData("3", "90-180天"));
        peroidDataList.add(new SpinnerData("4", "180-360天"));
        peroidDataList.add(new SpinnerData("5", "360天以上"));
        final ListView peroidView = new ListView(activity);
        peroidView.setDividerHeight(0);
        areaAdapter = new ListDropDownAdapter(activity, peroidDataList);
        peroidView.setAdapter(areaAdapter);

        final List<SpinnerData> sortDataList = new ArrayList<SpinnerData>();
        sortDataList.add(new SpinnerData("", "默认排序"));
        sortDataList.add(new SpinnerData("1", "时间降序"));
        sortDataList.add(new SpinnerData("0", "时间升序"));
        final ListView sortView = new ListView(activity);
        sortView.setDividerHeight(0);
        sortAdapter = new ListDropDownAdapter(activity, sortDataList);
        sortView.setAdapter(sortAdapter);

        //产品类型列表点击事件
        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = typeDataList.get(position).getValue();
                //刷新adapter  变色选中状态
                typeAdapter.setCheckItem(position);
                onDropDownMenu.setTabText(position == 0 ? headers[0] : typeDataList.get(position).getText());
                onDropDownMenu.closeMenu();
                if (!clicked.equals(borrowType)) {
                    borrowType = clicked;
                    onRefresh();
                }
            }
        });

        //期限区域点击事件
        peroidView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String clicked = peroidDataList.get(position).getValue();
                areaAdapter.setCheckItem(position);
                onDropDownMenu.setTabText(position == 0 ? headers[1] : peroidDataList.get(position).getText());
                onDropDownMenu.closeMenu();
                if (!clicked.equals(peroidLinmit)) {
                    peroidLinmit = clicked;
                    onRefresh();
                }
            }
        });

        //排序点击事件
        sortView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clicked = sortDataList.get(position).getValue();
                sortAdapter.setCheckItem(position);
                onDropDownMenu.setTabText(position == 0 ? headers[2] : sortDataList.get(position).getText());
                onDropDownMenu.closeMenu();
                if (!clicked.equals(orderTime)) {
                    orderTime = clicked;
                    onRefresh();
                }
            }

        });
        popupViews.add(typeView);
        popupViews.add(peroidView);
        popupViews.add(sortView);
        onDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews);
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing) {
// 初始化第一页数据
        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(activity);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();


        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder();
        formEncodingBuilder.add("pager.pageNumber", "" + pageNumber);
        formEncodingBuilder.add("pager.pageSize", "" + pageSize);
        if (borrowType != null && !borrowType.equals("")) {
            formEncodingBuilder.add("businessType", borrowType);
        }
        if (peroidLinmit != null && !peroidLinmit.equals("")) {
            formEncodingBuilder.add("limitLevel", peroidLinmit);
        }
        if (orderTime != null && !orderTime.equals("")) {
            formEncodingBuilder.add("desc", "" + orderTime);
        }
        RequestBody body = formEncodingBuilder.build();

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
                        if (refreshing) {
                            if (investCenterSwipLayout.isShown()) {
                                investCenterSwipLayout.setRefreshing(false);
                            }
                        } else {
                            investCenterListView.onLoadComplete();
                        }
                        Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);
                try {
                    final BorrowList borrowList = new Gson().fromJson(s, BorrowList.class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            if (borrowList.getRcd().equals("R0001")) {
                                isFirst = false;
                                borrowItemList.addAll(borrowList.getBorrowItemList());
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
                                Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show();
                                if (investCenterSwipLayout.isShown()) {
                                    investCenterSwipLayout.setRefreshing(false);
                                } else {
                                    investCenterListView.onLoadComplete();
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    Log.e("list",e.toString());
                }
            }
        });

    }


    public void onEventMainThread(ActivityToFragmentMessage mfe) {
        if (mfe.getTag() == 2003) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(activity);
                loadingDialog.show();
            }
            RequestForListData(basicUrl, currentBottomPageIndex, 10, true);
        }
    }

    @Override
    public void loadMore() {
        currentBottomPageIndex++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(basicUrl, currentBottomPageIndex, 10, false);
            }
        }, 500);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        borrowItemList.clear();
        mAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(basicUrl, 1, 10, true);
            }

        }, 500);

    }

    public View getmView() {
        return rootView;
    }
}

