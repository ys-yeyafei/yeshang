package cn.ysgroup.ysdai.Activities;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.meiqia.core.MQManager;
import com.meiqia.core.callback.OnEndConversationCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import cn.ysgroup.ysdai.Beans.bank.BankList;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.CustomerDialog;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.AppVersionUtils;
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
import java.util.HashMap;

public class CustomerActivity extends MyBaseActivity implements View.OnClickListener {

    private LinearLayout customr_phone;
    private LinearLayout customer;
    private IconFontTextView back;
    private String TAG = "客户服务";
    private LoadingDialog loadingDialog;
    private BankList resultBean;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //配置样式
            MQConfig.ui.titleTextColorResId=R.color.mq_white ;
            MQConfig.ui.titleBackgroundResId=R.color.mq_chat_robot_evaluate_textColor;
            HashMap<String, String> clientInfo = new HashMap<>();
            MQIntentBuilder mqIntentBuilder = new MQIntentBuilder(CustomerActivity.this);
            String currentPhone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
            switch (msg.what){
                case 0:
                    if (resultBean.getRealName() != null && resultBean.getPhone() != null) {
                        clientInfo.put("name", resultBean.getRealName());
                        clientInfo.put("tel", resultBean.getPhone());
                        mqIntentBuilder.setCustomizedId(resultBean.getPhone());
                    } else if (currentPhone != null) {
                        clientInfo.put("name", " ");
                        clientInfo.put("tel", currentPhone);
                        mqIntentBuilder.setCustomizedId(currentPhone);
                    } else {
                        clientInfo.put("name", "游客");
                        clientInfo.put("tel", " ");
                        mqIntentBuilder.setCustomizedId(AppVersionUtils.getPhoneIMEI(CustomerActivity.this));
                    }
                    break;
                case 1:
                    clientInfo.put("name", "游客");
                    clientInfo.put("tel", " ");
                    String IMEA=AppVersionUtils.getPhoneIMEI(CustomerActivity.this);
                    mqIntentBuilder.setCustomizedId(IMEA);
                    break;
            }
            clientInfo.put("avatar", "https://s3.cn-north-1.amazonaws.com.cn/pics.meiqia.bucket/1dee88eabfbd7bd4");
            mqIntentBuilder.setClientInfo(clientInfo);
            mqIntentBuilder.setScheduledGroup("5d6a0429e7f5ebc687e2e894514a4d62");
            startActivity(mqIntentBuilder.build());

        }
    };
    private MQManager mqManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        initView();
    }

    private void initView() {
        mqManager = MQManager.getInstance(this);
        back = (IconFontTextView) findViewById(R.id.customer_back);
        customer = (LinearLayout) findViewById(R.id.customer);
        customr_phone = (LinearLayout) findViewById(R.id.customer_phone);
        back.setOnClickListener(this);
        customer.setOnClickListener(this);
        customr_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_back:
                finish();
                break;
            case R.id.customer_phone:
                CustomerDialog dialog = new CustomerDialog(this, R.style.ActionSheetDialogStyle);
                dialog.show();
                break;
            case R.id.customer:
                //读取银行卡信息
                String url = AppConstants.URL_SUFFIX + "/rest/bankTo";
                RequestForBankData(url);
                break;
        }
    }


    /**
     * 获取银行卡数据
     *
     * @param basicUrl
     */
    public void RequestForBankData(String basicUrl) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
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
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (e != null && e.getMessage() != null) {
                            Log.i(TAG, e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);

                resultBean = JSON.parseObject(s, BankList.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            handler.sendEmptyMessage(0);
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                    }
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqManager.endCurrentConversation(new OnEndConversationCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(CustomerActivity.this,"退出客服服务",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {

            }
        });
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
