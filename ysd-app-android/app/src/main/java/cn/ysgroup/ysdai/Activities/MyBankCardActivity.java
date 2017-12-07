package cn.ysgroup.ysdai.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Beans.bank.BankCard;
import cn.ysgroup.ysdai.Beans.bank.BankList;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyBankCardActivity extends MyBaseActivity {


    @Bind(R.id.my_bank_card_toolbar_back)
    IconFontTextView myBankCardToolbarBack;
    @Bind(R.id.my_bank_card_toolbar_title)
    TextView myBankCardToolbarTitle;
    @Bind(R.id.my_bank_card_toolbar)
    Toolbar myBankCardToolbar;
    @Bind(R.id.my_bank_card_bank_img)
    ImageView myBankCardBankImg;
    @Bind(R.id.my_bank_card_bank_name)
    TextView myBankCardBankName;
    @Bind(R.id.my_bank_card_card_id)
    TextView myBankCardCardId;
    @Bind(R.id.my_bank_card_user_name)
    TextView myBankCardUserName;

    private String TAG = "银行卡信息";
    private BankList banklist;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            UpdateViews(banklist);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_card);
        ButterKnife.bind(this);

        initToolBar();

        String url = AppConstants.URL_SUFFIX + "/rest/bankTo";
        RequestForBankData(url);

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    /**
     * 请求拉取银行信息
     *
     * @param basicUrl
     */
    public void RequestForBankData(String basicUrl) {
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
                Log.i(TAG, s);

                final BankList resultBean = JSON.parseObject(s, BankList.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            banklist = resultBean;
                            if (banklist != null) {
                                Message message = new Message();
                                mHandler.sendMessage(message);
                            }
                        } else {
                            Toast.makeText(MyBankCardActivity.this, "获取银行数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void UpdateViews(BankList banklist) {
        if (banklist.getCardNo() == null)
            return;
        myBankCardUserName.setText(banklist.getRealName());
        int length = banklist.getCardNo().length();
        myBankCardCardId.setText("**** **** **** " + banklist.getCardNo().substring(length - 4, length));

        for (BankCard itemn : banklist.getBankCardList()) {
            if (itemn.getBankId().equals(banklist.getBankId())) {
                myBankCardBankName.setText(itemn.getBankName());
                int resId = getResources().getIdentifier("bank_" + itemn.getBankId().toLowerCase(), "mipmap", getPackageName());
                myBankCardBankImg.setImageResource(resId);

            }
        }
    }

    public void initToolBar() {
        myBankCardToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(myBankCardToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


}
