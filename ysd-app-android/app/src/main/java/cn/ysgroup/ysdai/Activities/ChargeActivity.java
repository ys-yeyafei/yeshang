package cn.ysgroup.ysdai.Activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baofoo.sdk.vip.BaofooPayActivity;
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

import butterknife.ButterKnife;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.ActivityToFragmentMessage;
import cn.ysgroup.ysdai.Beans.bank.RbOrderBean;
import cn.ysgroup.ysdai.Beans.user.UserRechargeBean;
import cn.ysgroup.ysdai.EventBus.EventBus;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;


/**
 * 充值页面流程：
 * 1、调用接口：/rest/rechargeTo查询已绑定银行卡信息。如果已绑定，则返回绑定的银行卡信息，招行卡（不能进行快捷支付）设置属性flag = 4，其他行设置属性flag =2 ;
 * 如果未绑定银行卡，则设置flag=1；
 * 2、点击充值按钮，如果已绑定且不是招行卡，调用/rest/rechargeSaveHnew请求产生订单信息，如果错误码是R0001，则从订单信息中拿到订单号，同时跳转到quickplayactivity界面，如果错误码是r0003，则跳转绑卡界面；
 * 如果未绑定银行卡，则跳转绑卡界面 RealNameAuthActivity
 */
public class ChargeActivity extends MyBaseActivity {
    TextView chargeAbleMoney;
    TextView chargeMoneyNumberYuan;
    EditText chargeMoneyNumber;
//    TextView chargeSafePassForget;
    TextView chargeBank;
    Button chargeActionSubmit;
    ImageView chargeBankIcon;

    private String TAG = "充值";
    private MessageDialog messageDialog;
    private final static int REQUEST_CODE_BAOFOO_SDK = 100;
    private int flag = 0;
    private int bind_verify_flag = 0;

    private String userId = "";
    private String realName = "";
    private String cardNo = "";
    private String cardId = "";
    private String registerTime = "";
    private String ableMoney = "";
    private String bankId = "";
    private String bankName = "";
    private String phone = "";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111://认证提交
//                    safr_rl.setVisibility(View.VISIBLE);
                    bund.setVisibility(View.VISIBLE);
                    charge_limit_rl.setVisibility(View.VISIBLE);
                    if(bankName.equals("")){
                        flag=4;
                    }else{
                        flag = 2;
                    }
                    chargeBank.setText(bankName + "（尾号" + cardNo.substring(cardNo.length() - 4, cardNo.length()) + "）");
                    chargeAbleMoney.setText(ableMoney);
                    int resId = getResources().getIdentifier("bank_" + bankId.toLowerCase(), "mipmap", ChargeActivity.this.getPackageName());
                    chargeBankIcon.setImageResource(resId);
                    upper_limit.setText(upper_limitS);
//                    chargeSafePassForget.setOnClickListener(
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent();
//                                    intent.setClass(ChargeActivity.this, ForgetSafePassActivity.class);
//                                    intent.putExtra("currentPhoneNo", phone);
//                                    startActivityForResult(intent, 8078);
//                                }
//                            }
//                    );

                    charge_limit_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(ChargeActivity.this,SafeControlActivity.class));
                        }
                    });

                    break;
                case 0x222:

                    //跳到第三方支付（宝付）的付款确认页面
                    Intent payintent = new Intent(ChargeActivity.this, BaofooPayActivity.class);
                    // 通过业务流水请求报文获得的交易号
                    payintent.putExtra(BaofooPayActivity.PAY_TOKEN, msg.obj.toString());
                    // 标记是否为测试，传True为正式环境，不传或者传False则为测试调用
                    payintent.putExtra(BaofooPayActivity.PAY_BUSINESS, false);
                    startActivityForResult(payintent, REQUEST_CODE_BAOFOO_SDK);

                    break;
                default:
                    break;

            }
        }
    };

    private MessageDialog payResultDialog;//支付结果
    private TextView charge;
    //    private RelativeLayout unbund;
    private ChargeActivity.ChargeFragmentBroadCase broadCase;
    private String chargeUrl;
//    private RelativeLayout safr_rl;
    private LinearLayout bund;
    private LoadingDialog loadingDialog;
    private String money;
    private IconFontTextView back;
    private TextView upper_limit;
    private String upper_limitS;
    private RelativeLayout charge_limit_rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        flag = 0;
        bind_verify_flag = 0;
        broadCase = new ChargeActivity.ChargeFragmentBroadCase();
        IntentFilter intentFilter = new IntentFilter("ChargeFragmentBroadCase");
        registerReceiver(broadCase, intentFilter);
        chargeAbleMoney = (TextView) findViewById(R.id.charge_able_money);
        upper_limit = (TextView) findViewById(R.id.upper_limit);
        chargeMoneyNumberYuan = (TextView) findViewById(R.id.charge_money_number_yuan);
        chargeMoneyNumber = (EditText) findViewById(R.id.charge_money_number);
//        chargeSafePassForget = (TextView) findViewById(R.id.charge_safe_pass_forget);
        chargeBank = (TextView) findViewById(R.id.charge_bank);
        chargeActionSubmit = (Button) findViewById(R.id.charge_action_submit);
        chargeBankIcon = (ImageView) findViewById(R.id.charge_bank_icon);
        back = (IconFontTextView) findViewById(R.id.charge_back);
        charge_limit_rl = (RelativeLayout) findViewById(R.id.charge_limit_rl);

        String totalIncome=getIntent().getStringExtra("totalIncome");
//        if(totalIncome!=null){
//            chargeAbleMoney.setText(totalIncome);
//        }
//        unbund = (RelativeLayout) view.findViewById(R.id.charge_and_cash_unbund);
        bund = (LinearLayout) findViewById(R.id.charge_and_cash_bund);
        charge = (TextView) findViewById(R.id.charge_and_cash_toolbar_title);
//        safr_rl = (RelativeLayout)findViewById(R.id.charge_safe_rl);
        chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
        RequestForRechargeTo(chargeUrl);
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChargeActivity.this, RechargeRecordActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        chargeActionSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitClick();
            }
        });
    }

    //点击充值按钮进行充值
    public void onSubmitClick() {
        //如果快速点击了两次
        if (ClickEvent.isFastDoubleClick(R.id.charge_action_submit)) {
            return;
        }

        money = chargeMoneyNumber.getText().toString();
//        String pwd = chargeSafePass.getText().toString();
        if (TextUtils.isEmpty(money.toString())) {
            Toast.makeText(ChargeActivity.this, "请输入充值金额", Toast.LENGTH_LONG).show();
            return;
        }
        if (Utils.str2Float(money) < 3) {
            Toast.makeText(ChargeActivity.this, "充值金额最少为3元", Toast.LENGTH_LONG).show();
            return;
        }

        if (flag == 0) {
            return;
        } else if (flag == 2) {
//            if (TextUtils.isEmpty(pwd.toString())) {
//                Toast.makeText(ChargeActivity.this, "请输入交易密码", Toast.LENGTH_LONG).show();
//                return;
//            }
            //调用接口，返回订单信息
            String url = AppConstants.URL_SUFFIX + "/rest/rechargeSaveHnew";
            RequestForRechargeData(url, money,"");
            chargeActionSubmit.setEnabled(false);
        } else if (flag == 1) {
            //进入绑卡页面
            Intent intent = new Intent();
            intent.setClass(ChargeActivity.this, RealNameAuthActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("money", money + "");
            ChargeActivity.this.startActivity(intent);
        }else if (bind_verify_flag == 1) {
            //进入绑卡页面
            Intent intent = new Intent();
            intent.setClass(ChargeActivity.this, BankCardBindVerifyActivity.class);
            ChargeActivity.this.startActivity(intent);
        }else if (flag == 4) {
            //进入绑卡页面
            Toast.makeText(ChargeActivity.this,"招商银行暂不支持快捷支付，请您联系客服",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取银行卡信息
     *
     * @param url
     */
    public void RequestForRechargeTo(String url) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ChargeActivity.this);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(ChargeActivity.this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                ChargeActivity.this.runOnUiThread(new Runnable() {
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
                Log.i(TAG, s);
                final UserRechargeBean recharge = new Gson().fromJson(s, UserRechargeBean.class);
                ChargeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (recharge.getRcd().equals("R0001")) {
                            //请求成功

                            userId = recharge.getUserId();
                            realName = recharge.getRealName();
                            cardNo = recharge.getCardNo();
                            cardId = recharge.getCardId();
                            registerTime = recharge.getRegisterTime();
                            ableMoney =recharge.getAbleMoney();
                            bankId = recharge.getBnakId();
                            bankName = recharge.getBankName();
                            phone = recharge.getPhone();
                            upper_limitS =recharge.getBankLimit();
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);

                        } else if (recharge.getRcd().equals("E0001")) {
                            ChargeActivity.this.startActivityForResult(new Intent(ChargeActivity.this, LoginActivity.class), 1400);
                            ChargeActivity.this.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(ChargeActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            ChargeActivity.this.finish();
                        } else if (recharge.getRcd().equals("M00010")) {
                            //没有绑定银行卡 点击确定进入绑卡页面
                            flag = 1;
//                            safr_rl.setVisibility(View.GONE);
                        }else if (recharge.getRcd().equals("M00020")) {
                            //没有绑定银行卡 点击确定进入绑卡页面
                            bind_verify_flag = 1;
//                            safr_rl.setVisibility(View.GONE);
                        } else {
                            if (recharge != null)
                                //请求失败
                                Toast.makeText(ChargeActivity.this, recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 请求充值（一）
     */

    public void RequestForRechargeData(String basicUrl, String chargeMoney, String safePwd) {

        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ChargeActivity.this);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(ChargeActivity.this, "loginToken", "")).
                add("userId", userId).
                add("idNo", cardId).
                add("realName", realName).
                add("cardNo", cardNo).
                add("safepwd", safePwd).
                add("userAccountRecharge.money", chargeMoney).build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                ChargeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chargeActionSubmit.setEnabled(true);
                        if (e != null || e.getMessage() != null) {
                            Toast.makeText(ChargeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                final RbOrderBean resultBean = new Gson().fromJson(s, RbOrderBean.class);
                Log.i(TAG, "（一）:" + s);
                ChargeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        chargeActionSubmit.setEnabled(true);
                        if (resultBean.getRcd().equals("R0001")) {

                            Intent intent = new Intent(ChargeActivity.this, QuicklyPlayActivity.class);
                            intent.putExtra("order", resultBean.getOrder_no());
                            ChargeActivity.this.startActivity(intent);
                        } else if (resultBean.getRcd().equals("R0003")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChargeActivity.this);
                            dialog.setCancelable(false);
                            dialog.setTitle(resultBean.getRmg())
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(ChargeActivity.this, RealNameAuthActivity.class);
                                            intent.putExtra("userId", userId);
                                            intent.putExtra("money", money + "");
                                            ChargeActivity.this.startActivity(intent);

                                        }
                                    }).show();

                        }else if ("M00010".equals(resultBean.getRcd())) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChargeActivity.this);
                            dialog.setCancelable(false);
                            dialog.setTitle(resultBean.getRmg())
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(ChargeActivity.this, RealNameAuthActivity.class);
                                            intent.putExtra("userId", userId);
                                            intent.putExtra("money", money + "");
                                            ChargeActivity.this.startActivity(intent);

                                        }
                                    }).show();

                        }else if ("M00020".equals(resultBean.getRcd())) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(ChargeActivity.this);
                            dialog.setCancelable(false);
                            dialog.setTitle(resultBean.getRmg())
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(ChargeActivity.this, BankCardBindVerifyActivity.class);
                                            ChargeActivity.this.startActivity(intent);

                                        }
                                    }).show();

                        } else {
                            Toast.makeText(ChargeActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_BAOFOO_SDK) {
            String result = "", msg = "";
            if (data == null || data.getExtras() == null) {
                msg = "支付已被取消";
            } else {
                //result返回值判断 -1:失败  0:取消  1:成功  10:处理中
                result = data.getExtras().getString(BaofooPayActivity.PAY_RESULT);
                msg = data.getExtras().getString(BaofooPayActivity.PAY_MESSAGE);
            }

            //刷新btype
            String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
            RequestForRechargeTo(chargeUrl);
            //CashFragment.isAgain = true;

            //通知用户中心刷新
            ActivityToFragmentMessage message = new ActivityToFragmentMessage();
            message.setTag(2004);
            EventBus.getDefault().post(message);

            messageDialog = new MessageDialog(this);
            messageDialog.setCanceledOnTouchOutside(false);
            messageDialog.setOnPositiveListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            messageDialog.dismiss();
                            messageDialog = null;

                        }
                    }
            );
            messageDialog.setMessage(msg);
            messageDialog.show();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        unregisterReceiver(broadCase);
        sendBroadcast(new Intent("LoginContentBroadCast"));
    }

    class ChargeFragmentBroadCase extends BroadcastReceiver {

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
