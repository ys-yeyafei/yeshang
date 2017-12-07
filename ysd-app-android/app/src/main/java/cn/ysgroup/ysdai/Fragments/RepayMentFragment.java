package cn.ysgroup.ysdai.Fragments;


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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import cn.ysgroup.ysdai.Adapters.RepayMentListAdapter;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.Beans.center.UserRepayment;
import cn.ysgroup.ysdai.Beans.center.UserRepaymentList;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadMoreListView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PostStringRequest;
import cn.ysgroup.ysdai.Util.PreferenceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RepayMentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepayMentFragment extends Fragment implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener
{
    private static final String ARG_PARAM = "param";
    @Bind(R.id.repay_ment_list_view)
    LoadMoreListView repayMentListView;
    @Bind(R.id.repay_ment_swip_layout)
    SwipeRefreshLayout repayMentSwipLayout;
    @Bind(R.id.repay_ment_nothing_img)
    ImageView repayMentNothingImg;

    // TODO: Rename and change types of parameters
    private String TAG = "回款明细";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/hkmx";
    private String status;
    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private List<UserRepayment> repayList = new ArrayList<UserRepayment>();
    private RepayMentListAdapter myAdapter;
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
                    repayMentNothingImg.setVisibility(View.GONE);
                    myAdapter = new RepayMentListAdapter(getActivity(), repayList);
                    repayMentListView.setAdapter(myAdapter);
                    if (repayMentSwipLayout.isShown())
                    {
                        repayMentSwipLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    repayMentNothingImg.setVisibility(View.GONE);
                    repayMentListView.onLoadComplete();
                    myAdapter.setRepaymentsList(repayList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };

    public static RepayMentFragment newInstance(String param1)
    {
        RepayMentFragment fragment = new RepayMentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public RepayMentFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            status = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_repay_ment, container, false);
        ButterKnife.bind(this, rootView);

        myAdapter = new RepayMentListAdapter(getActivity(), repayList);
        repayMentListView.setAdapter(myAdapter);
        repayMentListView.setLoadMoreListen(this);
        repayMentSwipLayout.setOnRefreshListener(this);
        repayMentSwipLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        RequestForListData(BASIC_URL, 1, PAGESIZE, true);
        if (loadingDialog == null)
        {

            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.show();
        }


        return rootView;
    }

    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing)
    {
        HashMap<String ,String > map =new HashMap<>();
        map.put("token", PreferenceUtil.getPrefString(getActivity(),"loginToken",""));
        map.put("pager.pageNumber", pageNumber+"");
        map.put("pager.pageSize", pageSize+"");
        map.put("status", status);
        PostStringRequest jr = new PostStringRequest( basicUrl, map, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.i(TAG, response.toString());
                if (loadingDialog != null && loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
                final UserRepaymentList resultBean = JSON.parseObject(response.toString(), UserRepaymentList.class);
                if (resultBean.getRcd().equals("R0001"))
                {
                    totalPageCount = resultBean.getPageBean().getPageCount();
                    repayList.addAll(resultBean.getUserRepaymentList());
                    if (refreshing)
                    {
                        if (resultBean.getUserRepaymentList() != null && resultBean.getUserRepaymentList().size() > 0)
                        {
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);
                        } else
                        {
                            repayMentNothingImg.setVisibility(View.VISIBLE);
                            if (repayMentSwipLayout.isShown())
                            {
                                repayMentSwipLayout.setRefreshing(false);
                            }
                        }
                    } else
                    {
                        Message msg = new Message();
                        msg.what = 0x222;
                        mHandler.sendMessage(msg);
                    }

                } else
                {
                    if (refreshing)
                    {
                        if (repayMentSwipLayout.isShown())
                        {
                            repayMentSwipLayout.setRefreshing(false);
                        }
                    } else
                    {
                        repayMentListView.onLoadComplete();
                    }
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

                if (loadingDialog != null && loadingDialog.isShowing())
                {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
                if (refreshing)
                {
                    if (repayMentSwipLayout.isShown())
                    {
                        repayMentSwipLayout.setRefreshing(false);
                    }
                } else
                {
                    repayMentListView.onLoadComplete();
                }
                Toast.makeText(getActivity(), "连接网络失败", Toast.LENGTH_SHORT).show();
            }
        });
        CustomApplication.newInstance().getRequestQueue().add(jr);
    }

    @Override
    public void loadMore()
    {
        if (currentBottomPageIndex < totalPageCount)
        {
            currentBottomPageIndex++;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    RequestForListData(BASIC_URL, currentBottomPageIndex, PAGESIZE, false);
                }
            }, 500);
        } else
        {
            repayMentListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onRefresh()
    {
        currentBottomPageIndex = 1;
        repayList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                RequestForListData(BASIC_URL, 1, PAGESIZE, true);
            }

        }, 500);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
