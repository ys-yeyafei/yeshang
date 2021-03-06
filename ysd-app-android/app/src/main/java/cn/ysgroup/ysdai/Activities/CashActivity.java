package cn.ysgroup.ysdai.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.Freebean;
import cn.ysgroup.ysdai.Beans.funds.UserCashBean;
import cn.ysgroup.ysdai.Beans.user.UserRechargeBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;

/**
 * 提现流程：
 * 1、查询银行卡信息， 接口：/rest/rechargeTo  返回成功后，再查银行卡可提现信息：/rest/cashTo
 * 2、输入提现金额的时候，同时查询提现手续费/rest/cashChange
 * 3、提交提现请求，调用接口/rest/cashSave
 */

public class CashActivity extends MyBaseActivity {
    @Bind(R.id.cash_able_money)
    TextView cashAbleMoney;
    @Bind(R.id.cash_money_number)
    EditText cashMoneyNumber;
    @Bind(R.id.cash_safe_pass_forget)
    TextView cashSafePassForget;
    @Bind(R.id.cash_safe_pass)
    EditText cashSafePass;
    @Bind(R.id.cash_bank)
    TextView cashBank;
    @Bind(R.id.cash_count)
    TextView count;
    @Bind(R.id.cash_action_submit)
    Button cashActionSubmit;
    @Bind(R.id.cash_charged_times)
    TextView cashChargedTimes;
    @Bind(R.id.cash_bank_icon)
    ImageView cashBankIcon;
    @Bind(R.id.cash_back)
    IconFontTextView back;

    private int not_bindcard_flag = 0;
    private int not_bindcard_verify_flag = 0;

    //提现后是否重新访问
//    public static boolean isAgain = false;
    private final String TAG = "提现";
    private UserCashBean userCashBean;
    private MessageDialog messageDialog;
    private String currentPhoneNo;
    private String currentMoney;
    private int clickCount = 0;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    bund.setVisibility(View.VISIBLE);
                    unbund.setVisibility(View.GONE);
                    updateViews(userCashBean);
                    break;
                case 0x333:
                    messageDialog = new MessageDialog(CashActivity.this);
                    messageDialog.setMessage("提现成功,请注意查收");
                    messageDialog.setCancelable(false);
                    messageDialog.setOnPositiveListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    messageDialog.dismiss();
                                    messageDialog = null;
                                    RequestForRechargeTo(chargeUrl);
                                    CashActivity.this.sendBroadcast(new Intent("ChargeFragmentBroadCase"));
                                    cashMoneyNumber.setText("");
                                    cashSafePass.setText("");
                                }
                            }
                    );
                    messageDialog.show();
                    Intent intent = new Intent("LoginContentBroadCast");
                    intent.putExtra("Quickly", "cash");
                    CashActivity.this.sendBroadcast(intent);
                    break;

                case 0x444:
                    commissionNet();
                    break;
                default:
                    break;
            }


        }
    };
    private TextView charge;
    private LinearLayout bund;
    private RelativeLayout unbund;
    private String userId;
    //请求可体现数据
    String basicUrl = AppConstants.URL_SUFFIX + "/rest/cashTo";
    String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
    private CashActivity.CashFragmentBroadCase broadCase;
    private LoadingDialog loadingDialog;
    private TextView freeMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash);
        Intent intent = getIntent();
        currentPhoneNo = intent.getStringExtra("currentPhoneNo");
        currentMoney = intent.getStringExtra("totalIncome");
        IntentFilter intentFilter = new IntentFilter("CashFragmentBroadCase");
        broadCase = new CashActivity.CashFragmentBroadCase();
        registerReceiver(broadCase, intentFilter);
        ButterKnife.bind(this);
        bund = (LinearLayout) findViewById(R.id.cash_bund);
        unbund = (RelativeLayout) findViewById(R.id.cash_unbund);
        freeMoney = (TextView) findViewById(R.id.cash_free_money);
        charge = (TextView) findViewById(R.id.charge_and_cash_toolbar_title);

        not_bindcard_flag = 0;
        not_bindcard_verify_flag = 0;
//        cashAbleMoney.setText(currentMoney);
        RequestForRechargeTo(chargeUrl);

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CashActivity.this, TxRecordActivity.class);
                startActivity(intent);
            }
        });

        unbund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), RealNameAuthActivity.class);
//                intent.putExtra("userId", userId);
//                getActivity().startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

         cashMoneyNumber.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

             }

             @Override
             public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!TextUtils.isEmpty(cashMoneyNumber.getText().toString()) &&cashMoneyNumber.getText().toString().length()>2){
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendEmptyMessageDelayed(0x444,1000);

                }else{
                    String countS = "提现手续费：" + "<font color='#FF0000'>" + 0 + "</font>" + " 元";
                    count.setText(Html.fromHtml(countS));
                }
             }

             @Override
             public void afterTextChanged(Editable editable) {

             }
         });
    }

    @OnClick(R.id.cash_safe_pass_forget)
    public void forgetSafePassOnClick() {
        Intent intent = new Intent();
        intent.setClass(this, ForgetSafePassActivity.class);
        intent.putExtra("currentPhoneNo", currentPhoneNo);
        startActivityForResult(intent, 8078);

    }

    public void updateViews(UserCashBean entity) {
        int resId;
        try {
            resId = getResources().getIdentifier("bank_" + entity.getBankId().toLowerCase(), "mipmap", this.getPackageName());
            cashBankIcon.setImageResource(resId);
        } catch (Exception e) {
            Log.v(TAG, "银行图片加载不出");
        }
        cashBank.setText(entity.getBankName() + "（尾号" + entity.getCardNo().substring(entity.getCardNo().length() - 4, entity.getCardNo().length()) + "）");
        int wholeTimes = Utils.str2Int(entity.getCashChargeTimes());
        int userTimes = Utils.str2Int(entity.getUserCashChargeTimes());

        SpannableStringBuilder usedTimeText = new SpannableStringBuilder(userTimes + "笔");
        usedTimeText.setSpan(new ForegroundColorSpan(Color.RED), 0, usedTimeText.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        cashAbleMoney.setText(entity.getAbleMoney());
        freeMoney.setText(Utils.str2Int(entity.getCashFeeMoney())+"");
        cashActionSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(not_bindcard_flag == 1){
                            //进入绑卡页面
                            Intent intent = new Intent();
                            intent.setClass(CashActivity.this, RealNameAuthActivity.class);
                            intent.putExtra("userId", userId);
                            CashActivity.this.startActivity(intent);
                        }else{
                            if(not_bindcard_verify_flag == 1){
                                //进入绑卡验证 ，输入验证码页面
                                Intent intent = new Intent();
                                intent.setClass(CashActivity.this, BankCardBindVerifyActivity.class);
                                intent.putExtra("userId", userId);
                                CashActivity.this.startActivity(intent);
                            }else{
                                if (clickCount > 0) {
                                    Toast.makeText(CashActivity.this, "数据处理中。。", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                //如果快速点击了两次
                                if (ClickEvent.isFastDoubleClick(R.id.cash_action_submit)) {
                                    Toast.makeText(CashActivity.this, "请勿重复点击", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                cashActionSubmit.setEnabled(false);
                                clickCount += 1;
                                String basicUrl = AppConstants.URL_SUFFIX + "/rest/cashSave";
                                String cashMoney = cashMoneyNumber.getText().toString();
                                String safePass = cashSafePass.getText().toString();
                                RequestForTX(basicUrl, cashMoney, safePass);
                            }
                        }

                    }
                }
        );


    }

    /**
     * 请求可提现数据
     *
     * @param basicUrl
     */
    public void RequestForListData(String basicUrl) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(CashActivity.this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                CashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CashActivity.this, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
                        if (e != null && e.getMessage() != null) {
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, "response json:" + s);
                final UserCashBean resultBean = JSON.parseObject(s, UserCashBean.class);
                CashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            userCashBean = resultBean;
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivityForResult(new Intent(CashActivity.this, LoginActivity.class), 1400);
                            CashActivity.this.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(CashActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            CashActivity.this.finish();
                        } else if (resultBean.getRcd().equals("M00015_3")) {

                        } else {
                            Toast.makeText(CashActivity.this, resultBean.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    /**
     * 请求提现
     *
     * @param basicUrl
     */
    public void RequestForTX(String basicUrl, String cashMoney, String safePass) {

        if (CheckFormData(cashMoney, safePass)) {
            if (loadingDialog == null) {

                loadingDialog = new LoadingDialog(CashActivity.this);
                loadingDialog.show();
            }
            String url = basicUrl;
            OkHttpClient client = new OkHttpClient();
            //超时时间加长点
            client.setConnectTimeout(15, TimeUnit.SECONDS);
            client.setWriteTimeout(30, TimeUnit.SECONDS);
            client.setReadTimeout(60, TimeUnit.SECONDS);
            RequestBody body = new FormEncodingBuilder().
                    add("token", PreferenceUtil.getPrefString(CashActivity.this, "loginToken", "")).
                    add("cashMoney", cashMoney).
                    add("safepwd", safePass).build();
            Request request = new Request.Builder().url(url).post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, final IOException e) {
                    CashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            cashActionSubmit.setEnabled(true);
                            clickCount = 0;
                            Toast.makeText(CashActivity.this, "操作失败，请检查网络！", Toast.LENGTH_SHORT).show();
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    });
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String s = response.body().string();
                    Log.i(TAG, "response json:" + s);
                    final BaseBean resultBean = JSON.parseObject(s, BaseBean.class);
                    CashActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            cashActionSubmit.setEnabled(true);
                            clickCount = 0;
                            if (resultBean.getRcd().equals("R0001")) {
                                Message msg = new Message();
                                msg.what = 0x333;
                                mHandler.sendMessage(msg);
                            }else if("M00010".equals(resultBean.getRcd())){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(CashActivity.this);
                                dialog.setCancelable(false);
                                dialog.setTitle(resultBean.getRmg())
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setClass(CashActivity.this, RealNameAuthActivity.class);
                                                intent.putExtra("userId", userId);
                                                CashActivity.this.startActivity(intent);

                                            }
                                        }).show();
                            }else if("M00020".equals(resultBean.getRcd())){
                                AlertDialog.Builder dialog = new AlertDialog.Builder(CashActivity.this);
                                dialog.setCancelable(false);
                                dialog.setTitle(resultBean.getRmg())
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent();
                                                intent.setClass(CashActivity.this, BankCardBindVerifyActivity.class);
                                                CashActivity.this.startActivity(intent);

                                            }
                                        }).show();
                            }
                            else {
                                Toast.makeText(CashActivity.this, resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * 检验数据完整
     *
     * @param cashMoney
     * @param safePass
     * @return
     */
    public boolean CheckFormData(String cashMoney, String safePass) {
        if (cashMoney == null || cashMoney.equals("")) {
            Toast.makeText(this, "请输入提现金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (safePass == null || safePass.equals("")) {
            Toast.makeText(this, "请输入交易密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    /**
     * 获取银行卡信息
     * u
     *
     * @param url
     */
    public void RequestForRechargeTo(String url) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(CashActivity.this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                CashActivity.this.runOnUiThread(new Runnable() {
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
                final UserRechargeBean recharge = new Gson().fromJson(s, UserRechargeBean.class);
                CashActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (recharge.getRcd().equals("R0001")) {
                            //请求成功
                            userId = recharge.getUserId();
                            RequestForListData(basicUrl);

                        } else if (recharge.getRcd().equals("E0001")) {
                            CashActivity.this.startActivityForResult(new Intent(CashActivity.this, LoginActivity.class), 1400);
                            CashActivity.this.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(CashActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            CashActivity.this.finish();
                        } else if (recharge.getRcd().equals("M00010")) {
                            not_bindcard_flag = 1;
                        }
                        else if (recharge.getRcd().equals("M00020")) {
                            not_bindcard_verify_flag = 1;
                        } else {
                            //请求失败
                            Toast.makeText(CashActivity.this, recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void commissionNet(){
        String  commissionUrl=AppConstants.URL_SUFFIX+ "/rest/cashChange";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(CashActivity.this, "loginToken", "")).
                add("cashMoney",cashMoneyNumber.getText().toString()).
                build();
        Request request = new Request.Builder().url(commissionUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CashActivity.this,"服务器连接失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                String s=response.body().string();
                final Freebean freebean = new Gson().fromJson(s, Freebean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(freebean.getRcd().equals("R0001")){
                            String countS = "提现手续费：" + "<font color='#FF0000'>" + freebean.getFee() + "</font>" + " 元";
                            count.setText(Html.fromHtml(countS));
                        }else{
                            Toast.makeText(CashActivity.this,freebean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCase);
        ButterKnife.unbind(this);
        Intent intent = new Intent("LoginContentBroadCast");
        intent.putExtra("Quickly", "cash");
        sendBroadcast(intent);
    }

    class CashFragmentBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RequestForRechargeTo(chargeUrl);
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
