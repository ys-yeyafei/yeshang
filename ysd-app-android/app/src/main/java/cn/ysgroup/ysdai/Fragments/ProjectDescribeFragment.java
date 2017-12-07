package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Adapters.ProjectRepayMentListAdapter;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowRepaymentDetailList;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowRepaymentInfo;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.NoScrollListView;
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

/**
 * Created by linyujie on 16/10/14.
 */

public class ProjectDescribeFragment extends Fragment {
    private View view;
    private String TAG = "项目基本信息";
    private BorrowRepaymentInfo resultBean;
    private ProjectRepayMentListAdapter myAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            upateViews(resultBean);

        }
    };
    private int id;
    private TextView projectBasicInforDebt;
    private TextView projectBasicInforDesc;
    private TextView projectBasicInforUse;
    private TextView projectBasicInforSource;
    private NoScrollListView projectRepaymentListView;
    private TextView projectRepaymentTotalCapital;
    private TextView projectRepaymentTotalInterest;

    public ProjectDescribeFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public ProjectDescribeFragment(int id) {
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.activity_project_basic_infor, null);
        initView();
        inteDate();
        return view;
    }

    private void initView() {
        projectBasicInforDebt = (TextView) view.findViewById(R.id.project_basic_infor_debt);
        projectBasicInforDesc = (TextView) view.findViewById(R.id.project_basic_infor_desc);
        projectBasicInforUse = (TextView) view.findViewById(R.id.project_basic_infor_use);
        projectBasicInforSource = (TextView) view.findViewById(R.id.project_basic_infor_source);
        projectRepaymentListView = (NoScrollListView) view.findViewById(R.id.project_repayment_list_view);
        projectRepaymentTotalCapital = (TextView) view.findViewById(R.id.project_repayment_total_capital);
        projectRepaymentTotalInterest = (TextView) view.findViewById(R.id.project_repayment_total_interest);

    }


    private void inteDate() {
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/repaymentInfo/" + id;

        myAdapter = new ProjectRepayMentListAdapter(getActivity(), new ArrayList<BorrowRepaymentDetailList>(), getActivity().getLayoutInflater());
        projectRepaymentListView.setAdapter(myAdapter);
        RequestForListData(basicUrl);
    }

    public void upateViews(BorrowRepaymentInfo resultBean) {


        projectBasicInforDebt.setText(resultBean.getDebtMess());
        projectBasicInforDesc.setText(resultBean.getContent());
        projectBasicInforUse.setText(resultBean.getUseReason());
        projectBasicInforSource.setText(resultBean.getPaymentSource());

        projectRepaymentTotalCapital.setText("￥" + resultBean.getCapital());
        projectRepaymentTotalInterest.setText("￥" + resultBean.getInterest());

        if (resultBean.getRepaymentDetailList() != null) {
            System.out.println("还款计划" + resultBean.getRepaymentDetailList().size());
            myAdapter.setRepayList(resultBean.getRepaymentDetailList());
            myAdapter.notifyDataSetChanged();
            projectRepaymentListView.setEnabled(false);
        }

    }

    public void RequestForListData(String basicUrl) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    });
                }catch (Exception e1){
                    Log.e("ProjectDescribeFragment",e1.toString());
                }
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                try{
                    String s=response.body().string();
                    Log.i(TAG, "response json:" + s);
                    resultBean = JSON.parseObject(s, BorrowRepaymentInfo.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (resultBean.getRcd().equals("R0001")) {
                                Message msg = new Message();
                                msg.what = 0x111;
                                mHandler.sendMessage(msg);
                            } else {
                                Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("ProjectDescribeFragment",e.toString());
                }
            }
        });
    }

    public void setId(int id) {
        this.id = id;
    }
}
