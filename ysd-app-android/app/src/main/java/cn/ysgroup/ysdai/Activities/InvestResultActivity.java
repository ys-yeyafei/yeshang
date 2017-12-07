package cn.ysgroup.ysdai.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import com.umeng.analytics.MobclickAgent;

public class InvestResultActivity extends MyBaseActivity {

    public static  int INVESTRESULTCODE=10010;
    private Button again;
    private TextView Tmoney;
    private TextView Tproject;
    private Button watch;
    private Intent intent;
    private int money;
    private String project;
    private IconFontTextView toolBarBack;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invest_result);
        initView();
        initListener();
        initDate();
    }

    private void initView() {
        userId = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        intent = getIntent();
        money = intent.getIntExtra("money", 0);
        project = intent.getStringExtra("project");
        again = (Button) findViewById(R.id.invest_result_again);
        Tmoney = (TextView) findViewById(R.id.invest_result_money);
        Tproject = (TextView) findViewById(R.id.invest_result_project);
        toolBarBack = (IconFontTextView) findViewById(R.id.project_invest_toolbar_back);
        watch = (Button) findViewById(R.id.invest_result_watch);
    }

    private void initListener() {
        toolBarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent againIntent=new Intent("HomeFragmentBroadCast");
                againIntent.putExtra("current",1);
                sendBroadcast(againIntent);
                finish();
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(InvestResultActivity.this, InvestRecordActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            }
        });

    }

    private void initDate() {
        Tmoney.setText(money+"");
        Tproject.setText(project);
    }
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
