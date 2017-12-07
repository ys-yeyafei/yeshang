package cn.ysgroup.ysdai.Activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

public class ProjectRepaymentActivity extends MyBaseActivity
{
    @Bind(R.id.project_repayment_total_account)
    TextView projectRepaymentTotalAccount;
    @Bind(R.id.project_repayment_total_capital)
    TextView projectRepaymentTotalCapital;
    @Bind(R.id.project_repayment_total_interest)
    TextView projectRepaymentTotalInterest;
    @Bind(R.id.project_repayment_list_view)
    NoScrollListView projectRepaymentListView;
    private String TAG = "还款计划";
    private BorrowRepaymentInfo resultBean;
    private ProjectRepayMentListAdapter myAdapter;
    private Handler mHandler = new Handler()
    {

        public void handleMessage(Message msg)
        {

            upateViews(resultBean);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_repayment);
        ButterKnife.bind(this);
        int borrowId = getIntent().getIntExtra("borrowId", 0);
        initActionBar("还款计划");
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/repaymentInfo";

        myAdapter = new ProjectRepayMentListAdapter(this, new ArrayList<BorrowRepaymentDetailList>(), getLayoutInflater());
        projectRepaymentListView.setAdapter(myAdapter);


        RequestForListData(basicUrl, borrowId);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initActionBar(String title)
    {

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.more_action_bar_layout);
        TextView moreBack = (TextView) actionBar.getCustomView().findViewById(R.id.more_actionbar_back);
        TextView moreTitle = (TextView) actionBar.getCustomView().findViewById(R.id.more_actionbar_textview);
        moreBack.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finish();
                    }
                }

        );
        moreTitle.setText(title);
    }


    public void RequestForListData(String basicUrl, int projectId)
    {

        String url = basicUrl + "/" + projectId;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
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
                resultBean = JSON.parseObject(s, BorrowRepaymentInfo.class);runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001"))
                        {
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);
                        } else
                        {
                            Toast.makeText(ProjectRepaymentActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void upateViews(BorrowRepaymentInfo resultBean)
    {

        projectRepaymentTotalAccount.setText("￥" + resultBean.getAccount());
        projectRepaymentTotalCapital.setText("￥" + resultBean.getCapital());
        projectRepaymentTotalInterest.setText("￥" + resultBean.getInterest());

        if (resultBean.getRepaymentDetailList() != null)
        {
            System.out.println("还款计划" + resultBean.getRepaymentDetailList().size());
            myAdapter.setRepayList(resultBean.getRepaymentDetailList());
            myAdapter.notifyDataSetChanged();
            projectRepaymentListView.setEnabled(false);
        }

    }

}
