package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.bank.QuickPlayBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.ToastUtil;
import cn.ysgroup.ysdai.Util.Utils;

/**
 * 步骤：
 * 1、进入页面查询订单信息。/rest/payOrder
 * 2、请求发送验证码  /rest/ajaxGetPhoneCodeRongbao
 * 3、提交支付请求(rest/toPayRongbao)，支付成功R0001后查询支付结果(/rest/payResult)，其他支付情况未失败时提示支付处理中同时跳转playsuccess;
 * 4、第三步查询支付结果  /rest/payResult，如果是充值，提示充值成功同时跳转playsuccess;如果有自动投资标志，则提交自动投资请求 /rest/investDoH/，如果自动投资请求成功，跳转投资成功页面
 */
public class QuicklyPlayActivity extends MyBaseActivity implements View.OnClickListener {

    private String orderString;
    private TextView id_card;
    private TextView bank_card;
    private TextView bank_name;
    private TextView phone;
    private Button sure;
    private IconFontTextView black;
    private EditText code;
    private Button code_button;
    private TextView name;
    private TextView money;
    private TextView order;
    private TextView time;
    private String TAG = "快速支付";
    private String TAG1 = "查询支付";
    private boolean isFirst = true;
    private CountDownTimer countDownTimer;
    private LoadingDialog loadingDialog;
    private int count = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    id_card.setText(quickPlayBean.getCard_id());
                    bank_card.setText(quickPlayBean.getBank_no());
                    bank_name.setText(quickPlayBean.getBank_name());
                    phone.setText(quickPlayBean.getPhone());
                    name.setText(quickPlayBean.getName());
                    money.setText(quickPlayBean.getMoney() + "");
                    order.setText(orderString);
                    time.setText(quickPlayBean.getCreate_time());
                    sure.setEnabled(true);
                    break;
                case 1:
                    String url = AppConstants.URL_SUFFIX + "/rest/payResult";
                    //查询支付结果
                    RequestForRechargeToResult(url);
                    break;
            }

        }
    };
    private QuickPlayBean quickPlayBean;
    private String inverstMark;
    private String selectedIdArrayString;
    private String safePass;
    private String tenderMoney;
    private String project;
    private int projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        count = 0;
        setContentView(R.layout.activity_quickly_play);
        Intent intent = getIntent();
        inverstMark = intent.getStringExtra("inverstMark");

        orderString = intent.getStringExtra("order");
        selectedIdArrayString = intent.getStringExtra("selectedIdArrayString");
        safePass = intent.getStringExtra("safePass");
        tenderMoney = intent.getStringExtra("tenderMoney");
        projectId = intent.getIntExtra("projectId", 0);
        project = intent.getStringExtra("project");

        initView();
        initListener();
        initDate();
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {

            @Override
            public void onTick(long millisUntilFinished) {
                code_button.setEnabled(false);
                code_button.setText((millisUntilFinished / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                code_button.setEnabled(true);
                code_button.setText("重新发送");
            }
        };
        code_button.setEnabled(false);
        countDownTimer.start();
    }

    private void initListener() {
        sure.setOnClickListener(this);
        black.setOnClickListener(this);
        code_button.setOnClickListener(this);
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() < 6) {
                    sure.setBackgroundResource(R.drawable.button_background_normal1);
                    sure.setEnabled(false);
                } else {
                    sure.setBackgroundResource(R.drawable.button_background_normal);
                    sure.setEnabled(true);
                }
            }
        });
    }

    private void initView() {
        id_card = (TextView) findViewById(R.id.quickly_play_id_card);
        bank_card = (TextView) findViewById(R.id.quickly_play_bank_card);
        bank_name = (TextView) findViewById(R.id.quickly_play_bank_name);
        phone = (TextView) findViewById(R.id.quickly_play_bank_phone);
        sure = (Button) findViewById(R.id.quickly_play_bank_sure);
        black = (IconFontTextView) findViewById(R.id.quickly_play_black);
        code = (EditText) findViewById(R.id.quickly_play_code);
        code_button = (Button) findViewById(R.id.quickly_play_code_button);
        name = (TextView) findViewById(R.id.quickly_play_name);
        money = (TextView) findViewById(R.id.quickly_play_money);
        order = (TextView) findViewById(R.id.quickly_play_order);
        time = (TextView) findViewById(R.id.quickly_play_time);
        if(inverstMark!=null )
        sure.setEnabled(false);
    }

    private void initDate() {
        String url = AppConstants.URL_SUFFIX + "/rest/payOrder";
        RequestForRechargeTo(url);
    }

    //访问表单
    public void RequestForRechargeTo(String url) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("orderNo", orderString).build();
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
                String s = response.body().string();
                Log.i(TAG, s);
                quickPlayBean = new Gson().fromJson(s, QuickPlayBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (quickPlayBean.getRcd().equals("R0001")) {
                            //请求成功
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
            }
        });
    }


    //重发验证码
    public void RequestForRechargeToCode(String url) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("orderNo", orderString).build();
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
                String s = response.body().string();
                Log.i(TAG, s);
                final BaseBean codeBean = new Gson().fromJson(s, BaseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (codeBean.getRcd().equals("R0001")) {
                            code_button.setEnabled(false);
                            countDownTimer.start();
                            //跳转到成功页面
                        } else {
                            Toast.makeText(QuicklyPlayActivity.this, codeBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    //确认支付
    public void RequestForRechargeToPlay(String url) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        //超时时间加长点
        client.setConnectTimeout(15, TimeUnit.SECONDS);
        client.setWriteTimeout(30, TimeUnit.SECONDS);
        client.setReadTimeout(60, TimeUnit.SECONDS);
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("orderNo", orderString).
                add("checkCode", code.getText().toString()).build();
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
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }

                        if(e instanceof SocketTimeoutException){
                            ToastUtil.showToast(QuicklyPlayActivity.this, "正在请求支付,请稍候确认结果");
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);
                final BaseBean codeBean = new Gson().fromJson(s, BaseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (codeBean.getRcd().equals("R0001")) {
                            //查询支付结果 支付成功
                            String url = AppConstants.URL_SUFFIX + "/rest/payResult";
                            RequestForRechargeToResult(url);
                        } else if(codeBean.getRcd().equals("S00010")){
                            Toast.makeText(QuicklyPlayActivity.this,codeBean.getRmg(),Toast.LENGTH_SHORT).show();
                        }else {
                            //支付处理中
                            Intent intent = new Intent(QuicklyPlayActivity.this, PlaySuccessActivity.class);
                            intent.putExtra("money", quickPlayBean.getMoney() + "");
                            intent.putExtra("order", orderString);
                            intent.putExtra("message", codeBean.getRmg());
                            startActivity(intent);
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            finish();
                        }
                    }
                });
            }
        });
    }



    //查询支付结果
    public void RequestForRechargeToResult(String url) {
        if (isFirst) {

            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(this);
                loadingDialog.show();
                loadingDialog.setMessage("交易处理中....");
            }
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("orderNo", orderString).build();
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
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG1, s);
                final BaseBean codeBean = new Gson().fromJson(s, BaseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (codeBean.getRcd().equals("R0002") && count < 5) {
                            count = count + 1;
                            handler.sendEmptyMessageDelayed(1, 2000);
                        } else {
                            //通知帐号刷新
                            Intent bIntent = new Intent("LoginContentBroadCast");
                            bIntent.putExtra("Quickly", "Quickly");
                            sendBroadcast(bIntent);
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }

                            if (inverstMark != null && inverstMark.equals("inverstMark")) {
                                //自动投资
                                String url = AppConstants.URL_SUFFIX + "/rest/investDoH/" + projectId;
                                RequestToInvestDo(url);

                            } else {
                                //充值成功
                                Intent intent = new Intent(QuicklyPlayActivity.this, PlaySuccessActivity.class);
                                intent.putExtra("money", quickPlayBean.getMoney() + "");
                                intent.putExtra("order", orderString);
                                intent.putExtra("message", codeBean.getRmg());
                                startActivity(intent);
                            }
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            finish();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        int ID = v.getId();
        switch (ID) {
            case R.id.quickly_play_bank_sure:
                String urlplay = AppConstants.URL_SUFFIX + "/rest/toPayRongbao";
                RequestForRechargeToPlay(urlplay);
                break;
            case R.id.quickly_play_black:
                finish();
                break;
            case R.id.quickly_play_code_button:
                //发送验证码
                String url = AppConstants.URL_SUFFIX + "/rest/ajaxGetPhoneCodeRongbao";
                RequestForRechargeToCode(url);
                break;
        }
    }


    /**
     * 请求提交投资该项目
     *
     * @param basicUrl
     */

    public void RequestToInvestDo(String basicUrl) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(QuicklyPlayActivity.this);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("tenderMoney", tenderMoney).
                add("safepwd", safePass).
                add("clientType", 1 + "").
                add("hongbaoArray", selectedIdArrayString).
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).build();
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
                        Toast.makeText(QuicklyPlayActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
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
                final BaseBean resultBean = JSON.parseObject(s, BaseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            //转入投资成功页面
                            Intent successIntent = new Intent(QuicklyPlayActivity.this, InvestResultActivity.class);
                            successIntent.putExtra("project", project);
                            //充值金额
                            successIntent.putExtra("money", (int) Utils.str2Float(tenderMoney));


                            startActivityForResult(successIntent, InvestResultActivity.CONTEXT_INCLUDE_CODE);
                            //关闭投资页面
                            Intent intent = new Intent("ProjectInvestActivityBroadCast");
                            intent.putExtra("quickPlay", "finish");
                            sendBroadcast(intent);
                            //setResult(AppConstants.projectInvestSuccess);
                        } else if (resultBean.getRcd().equals("M0007_211")) {
                            //被抢投 刷新项目
                            Toast.makeText(QuicklyPlayActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                            //充值成功 投资不成功  刷新页面  关闭页面刷新
                            Intent intent = new Intent("ProjectInvestActivityBroadCast");
                            intent.putExtra("quickPlay", "refresh");
                            sendBroadcast(intent);
                        } else {
                            //充值成功 投资不成功  刷新页面关闭 页面刷新
                            Intent intent = new Intent("ProjectInvestActivityBroadCast");
                            intent.putExtra("quickPlay", "refresh");
                            sendBroadcast(intent);
                            Toast.makeText(QuicklyPlayActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(inverstMark!=null){
            Intent intent = new Intent("ProjectInvestActivityBroadCast");
            intent.putExtra("quickPlay", "refresh");
            sendBroadcast(intent);
        }
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
