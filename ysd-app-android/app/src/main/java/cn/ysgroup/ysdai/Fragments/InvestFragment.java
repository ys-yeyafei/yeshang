package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Adapters.ProjectTenderRecordListAdapter;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowTenderItem;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowTenderList;
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

/**
 * Created by linyujie on 16/10/14.
 */

public class InvestFragment extends Fragment implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener {

    private int id;
    private View view;
    private ImageView tenderRecordNothingImg;
    private SwipeRefreshLayout tenderRecordSwipLayout;
    private LoadMoreListView tenderRecordListView;
    private String TAG = "投资记录";
    String basicUrl = AppConstants.URL_SUFFIX + "/rest/borrowTenderList";

    private BorrowTenderList resultBean;
    private ProjectTenderRecordListAdapter mAdapter;
    private List<BorrowTenderItem> tenderList = new ArrayList<BorrowTenderItem>();
    private LoadingDialog loadingDialog;

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0x111:
                    //刷新数据
                    tenderRecordNothingImg.setVisibility(View.GONE);
                    mAdapter = new ProjectTenderRecordListAdapter(getActivity(), tenderList, getActivity().getLayoutInflater(), "0");
                    tenderRecordListView.setAdapter(mAdapter);
                    if (tenderRecordSwipLayout.isShown()) {
                        tenderRecordSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    tenderRecordNothingImg.setVisibility(View.GONE);
                    tenderRecordListView.onLoadComplete();
                    mAdapter.setTenderList(tenderList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public InvestFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public InvestFragment(int id) {
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = View.inflate(getActivity(), R.layout.activity_project_tender_record, null);
        initView();
        initDate();
        return view;

    }

    private void initView() {
        tenderRecordListView = (LoadMoreListView) view.findViewById(R.id.tender_record_list_view);
        tenderRecordSwipLayout = (SwipeRefreshLayout) view.findViewById(R.id.tender_record_swip_layout);
        tenderRecordNothingImg = (ImageView) view.findViewById(R.id.tender_record_nothing_img);
    }

    private void initDate() {
        mAdapter = new ProjectTenderRecordListAdapter(getActivity(), new ArrayList<BorrowTenderItem>(), getActivity().getLayoutInflater(), "0");
        tenderRecordListView.setAdapter(mAdapter);
        tenderRecordSwipLayout.setOnRefreshListener(this);
        tenderRecordListView.setLoadMoreListen(this);
        tenderRecordSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        RequestForListData(basicUrl, id, 1, PAGESIZE, true);
        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.show();
        }
    }

    public void RequestForListData(String basicUrl, int projectId, int pageNumber, int pageSize, final boolean refreshing) {
        if(getActivity()==null){
            return;
        }
        String url = basicUrl + "/" + projectId;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
                add("pager.pageNumber", pageNumber + "").
                add("pager.pageSize", pageSize + "").build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }

                        if (tenderRecordSwipLayout.isShown()) {
                            tenderRecordSwipLayout.setRefreshing(false);
                        }

                        Toast.makeText(getActivity(), "连接网络失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, "response json:" + s);
                resultBean = JSON.parseObject(s, BorrowTenderList.class);
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }

                            if (tenderRecordSwipLayout.isShown()) {
                                tenderRecordSwipLayout.setRefreshing(false);
                            }
                            if (resultBean.getRcd().equals("R0001")) {
                                totalPageCount = resultBean.getPageBean().getPageCount();
                                tenderList.addAll(resultBean.getBorrowTenderItemList());
                                if (refreshing) {
                                    if (resultBean.getBorrowTenderItemList() != null && resultBean.getBorrowTenderItemList().size() > 0) {
                                        Message msg = new Message();
                                        msg.what = 0x111;
                                        mHandler.sendMessage(msg);
                                    } else {
                                        tenderRecordNothingImg.setVisibility(View.VISIBLE);
                                        if (tenderRecordSwipLayout.isShown()) {
                                            tenderRecordSwipLayout.setRefreshing(false);
                                        }
                                    }
                                } else {
                                    Message msg = new Message();
                                    msg.what = 0x222;
                                    mHandler.sendMessage(msg);
                                }
                            } else {
                                if (refreshing) {
                                    if (tenderRecordSwipLayout.isShown()) {
                                        tenderRecordSwipLayout.setRefreshing(false);
                                    }
                                } else {
                                    tenderRecordListView.onLoadComplete();
                                }
                                Toast.makeText(getActivity(), resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.e("InvestFragment",e.toString());

                }
            }
        });
    }


    @Override
    public void onRefresh() {
        currentBottomPageIndex = 1;
        tenderList.clear();

        mAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RequestForListData(basicUrl, id, 1, PAGESIZE, true);
            }

        }, 500);
    }


    @Override
    public void loadMore() {
        if (currentBottomPageIndex < totalPageCount) {
            currentBottomPageIndex++;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    RequestForListData(basicUrl, id, currentBottomPageIndex, PAGESIZE, false);
                }
            }, 500);
        } else {
            tenderRecordListView.onLoadComplete();
            return;
        }
    }
}
