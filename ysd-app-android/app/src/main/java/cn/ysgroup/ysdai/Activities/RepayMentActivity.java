package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Beans.userInfo.RepaBeen;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.MyListView;
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

public class RepayMentActivity extends MyBaseActivity {


    private IconFontTextView repaymentToolbarBack;
    private Intent intent;
    private int id;//号码
    private String name;
    private TextView textName;
    private TextView hb_play;
    private IconFontTextView icon;
    private TextView money;
    private TextView profit;
    private TextView state;
    private TextView time;
    private TextView time1;
    private TextView limit;
    private Gson gson = new Gson();
    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (repaBeen != null) {
                textName.setText(name);
                if(repaBeen.getHongbaoAmount()!=null){
                    hb_play.setText(repaBeen.getHongbaoAmount());
                }
                if(repaBeen.getBorrowStatus()==3){
                    icon.setText(getResources().getString(R.string.clock));
                    icon.setTextColor(getResources().getColor(R.color.colorLightYellow));
                    state.setText(repaBeen.getBorrowStatusVal());
                }else{
                    icon.setText(getResources().getString(R.string.checkbox));
                    icon.setTextColor(getResources().getColor(R.color.colorDarkGrey));
                    state.setText(repaBeen.getBorrowStatusVal());
                }
                time.setText(repaBeen.getCreateTime());
                time1.setText("账户余额");
                money.setText(repaBeen.getAccount());
                profit.setText(repaBeen.getInterest());
                limit.setText(repaBeen.getTimeLimit());
                ment_year.setText(repaBeen.getApr()+"");

                listView.setAdapter(new RepayMentAdapter());
            }
        }
    };
    private RepaBeen repaBeen;
    private TextView ment_year;
    private MyListView listView;
    private LinearLayout detail;
    private LinearLayout negotiate;
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repay_ment);
        initView();
        initNetWork();
        initListener();
    }

    private void initListener() {
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(RepayMentActivity.this, PreviewOneActivity.class);
                    intent.putExtra("itemId", projectId);
                    RepayMentActivity.this.startActivity(intent);
            }
        });

        negotiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RepayMentActivity.this,MoneyNegotiateActivity.class);
                intent.putExtra("itemId",id);
                intent.putExtra("projectId",projectId);
                startActivity(intent);
            }
        });
    }


    private void initView() {
        intent = getIntent();
        id = intent.getIntExtra("id", 0);
        projectId = intent.getIntExtra("projectId", 0);
        name = intent.getStringExtra("name");
        repaymentToolbarBack = (IconFontTextView) findViewById(R.id.repayment_toolbar_back);
        textName = (TextView) findViewById(R.id.repay_ment_name);
        hb_play = (TextView) findViewById(R.id.repay_ment_hb_play);
        icon = (IconFontTextView) findViewById(R.id.repay_ment_icon);
        money = (TextView) findViewById(R.id.repay_ment_money);
        profit = (TextView) findViewById(R.id.repay_ment_profit);
        state = (TextView) findViewById(R.id.repay_ment_state);
        time = (TextView) findViewById(R.id.repay_ment_time);
        time1 = (TextView) findViewById(R.id.repay_ment_time1);
        limit = (TextView) findViewById(R.id.repay_ment_time_limit);
        ment_year = (TextView) findViewById(R.id.repay_ment_year);
        listView = (MyListView) findViewById(R.id.repay_ment_list);
        detail = (LinearLayout) findViewById(R.id.repay_ment_detail);
        negotiate = (LinearLayout) findViewById(R.id.repay_ment_negotiate);
        initToolBar();
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
        repaymentToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

    }


    private void initNetWork() {
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/userCenterBorrowRepList";
        RequestForListData(basicUrl);
    }

    public void RequestForListData(String basicUrl) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("id", id + "").
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Request request, final IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                repaBeen = gson.fromJson(s, RepaBeen.class);
                if (repaBeen.getRcd().equals("R0001")) {
                    hander.sendEmptyMessage(0);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RepayMentActivity.this, repaBeen.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    class RepayMentAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return repaBeen.getUserRepDetailList().size();
        }

        @Override
        public Object getItem(int position) {
            return repaBeen.getUserRepDetailList().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RepaBeen.UserRepDetailListBean userRepDetailListBean = repaBeen.getUserRepDetailList().get(position);
            convertView=View.inflate(RepayMentActivity.this,R.layout.repay_ment_item,null);
            TextView time= (TextView) convertView.findViewById(R.id.repay_ment_item_time);
            TextView principal= (TextView) convertView.findViewById(R.id.repay_ment_item_principal);
            TextView interest= (TextView) convertView.findViewById(R.id.repay_ment_item_interest);
            time.setText(userRepDetailListBean.getRepaymentDate());
            interest.setText(userRepDetailListBean.getWaitInterest());
            principal.setText(userRepDetailListBean.getWaitAccount());
            return convertView;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
