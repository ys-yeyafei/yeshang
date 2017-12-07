package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;
import com.umeng.analytics.MobclickAgent;

public class PlaySuccessActivity extends MyBaseActivity {

    private TextView money;
    private TextView order;
    private Button sure;
    private TextView text;
    private Intent intent;
//    private IconFontTextView black;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_success);
        intent = getIntent();
        initView();
        initDate();
    }

    private void initView() {
        money = (TextView) findViewById(R.id.play_success_money);
        order = (TextView) findViewById(R.id.play_success_order);
        sure = (Button) findViewById(R.id.play_success_sure);
        text = (TextView) findViewById(R.id.play_success_text);
//        black = (IconFontTextView) findViewById(R.id.play_success_black);
    }

    private void initDate() {
        String moneyString = intent.getStringExtra("money");
        String orderString = intent.getStringExtra("order");
        String messageString = intent.getStringExtra("message");
        money.setText(moneyString);
        order.setText(orderString);
        text.setText(messageString);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        black.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//
//            }
//        });

        Intent againIntent = new Intent("ChargeFragmentBroadCase");
        sendBroadcast(againIntent);
        Intent cash = new Intent("CashFragmentBroadCase");
        sendBroadcast(cash);
        Intent invest = new Intent("ProjectInvestActivityBroadCast");
        sendBroadcast(invest);

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
