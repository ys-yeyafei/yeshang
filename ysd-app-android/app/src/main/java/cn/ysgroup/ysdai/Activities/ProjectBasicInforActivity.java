package cn.ysgroup.ysdai.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProjectBasicInforActivity extends MyBaseActivity {

    //    @Bind(R.id.project_basic_infor_toolbar_back)
//    IconFontTextView projectBasicInforToolbarBack;
//    @Bind(R.id.project_basic_infor_toolbar_title)
//    TextView projectBasicInforToolbarTitle;
//    @Bind(R.id.project_basic_infor_toolbar)
    Toolbar projectBasicInforToolbar;
    @Bind(R.id.project_basic_infor_debt)
    TextView projectBasicInforDebt;
    @Bind(R.id.project_basic_infor_desc)
    TextView projectBasicInforDesc;
    @Bind(R.id.project_basic_infor_use)
    TextView projectBasicInforUse;
    @Bind(R.id.project_basic_infor_source)
    TextView projectBasicInforSource;
    @Bind(R.id.project_repayment_list_view)
    NoScrollListView projectRepaymentListView;
    @Bind(R.id.project_repayment_total_capital)
    TextView projectRepaymentTotalCapital;
    @Bind(R.id.project_repayment_total_interest)
    TextView projectRepaymentTotalInterest;

    private String TAG = "项目基本信息";
    private BorrowRepaymentInfo resultBean;
    private ProjectRepayMentListAdapter myAdapter;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            upateViews(resultBean);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_basic_infor);
        ButterKnife.bind(this);
//        initToolBar();
        int borrowId = getIntent().getIntExtra("borrowId", 0);
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/repaymentInfo/" + borrowId;

        myAdapter = new ProjectRepayMentListAdapter(this, new ArrayList<BorrowRepaymentDetailList>(), getLayoutInflater());
        projectRepaymentListView.setAdapter(myAdapter);
        RequestForListData(basicUrl);


    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

//    public void initToolBar()
//    {
//        projectBasicInforToolbarBack.setOnClickListener(
//                new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        finish();
//                    }
//                }
//        );
//        setSupportActionBar(projectBasicInforToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//    }


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
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
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
                Log.i(TAG, "response json:" + s);
                resultBean = JSON.parseObject(s, BorrowRepaymentInfo.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);
                        } else {
                            Toast.makeText(ProjectBasicInforActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }



}
