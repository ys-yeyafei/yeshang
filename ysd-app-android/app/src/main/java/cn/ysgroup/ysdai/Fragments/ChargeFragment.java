package cn.ysgroup.ysdai.Fragments;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ysgroup.ysdai.Activities.LoginActivity;
import cn.ysgroup.ysdai.Activities.QuicklyPlayActivity;
import cn.ysgroup.ysdai.Activities.RealNameAuthActivity;
import cn.ysgroup.ysdai.Activities.RechargeRecordActivity;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.ActivityToFragmentMessage;
import cn.ysgroup.ysdai.Beans.bank.RbOrderBean;
import cn.ysgroup.ysdai.Beans.user.UserRechargeBean;
import cn.ysgroup.ysdai.EventBus.EventBus;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChargeFragment extends Fragment {

    @Bind(R.id.charge_able_money)
    TextView chargeAbleMoney;
    @Bind(R.id.charge_money_number_yuan)
    TextView chargeMoneyNumberYuan;
    @Bind(R.id.charge_money_number)
    EditText chargeMoneyNumber;
//    @Bind(R.id.charge_safe_pass_forget)
//    TextView chargeSafePassForget;
    @Bind(R.id.charge_safe_pass)
    EditText chargeSafePass;
    @Bind(R.id.charge_bank)
    TextView chargeBank;
    @Bind(R.id.charge_action_submit)
    Button chargeActionSubmit;
    @Bind(R.id.charge_bank_icon)
    ImageView chargeBankIcon;

    private String TAG = "充值";
    private MessageDialog messageDialog;
    private final static int REQUEST_CODE_BAOFOO_SDK = 100;
    private int flag = 0;

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
                    safr_rl.setVisibility(View.VISIBLE);
                    bund.setVisibility(View.VISIBLE);
                    if(bankName.equals("")){
                        flag=4;
                    }else{
                        flag = 2;
                    }
                    chargeBank.setText(bankName + "（尾号" + cardNo.substring(cardNo.length() - 4, cardNo.length()) + "）");
                    chargeAbleMoney.setText(ableMoney);
                    int resId = getResources().getIdentifier("bank_" + bankId.toLowerCase(), "mipmap", getActivity().getPackageName());
                    chargeBankIcon.setImageResource(resId);
//                    chargeSafePassForget.setOnClickListener(
//                            new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent();
//                                    intent.setClass(getActivity(), ForgetSafePassActivity.class);
//                                    intent.putExtra("currentPhoneNo", phone);
//                                    startActivityForResult(intent, 8078);
//                                }
//                            }
//                    );

                    break;
                case 0x222:
                    Intent payintent = new Intent(getActivity(), BaofooPayActivity.class);
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
    private ChargeFragmentBroadCase broadCase;
    private String chargeUrl;
    private String totalIncome;
    private static final String ARG_PARAM = "totalIncome";
    private RelativeLayout safr_rl;
    private LinearLayout bund;
    private LoadingDialog loadingDialog;
    private String money;

    public static ChargeFragment newInstance(String param) {
        ChargeFragment fragment = new ChargeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    public ChargeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (getArguments() != null) {
            totalIncome = getArguments().getString(ARG_PARAM);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        flag = 0;
        broadCase = new ChargeFragmentBroadCase();
        IntentFilter intentFilter = new IntentFilter("ChargeFragmentBroadCase");
        getActivity().registerReceiver(broadCase, intentFilter);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_charge, container, false);
        ButterKnife.bind(this, view);

        chargeAbleMoney.setText(totalIncome);
//        unbund = (RelativeLayout) view.findViewById(R.id.charge_and_cash_unbund);
        bund = (LinearLayout) view.findViewById(R.id.charge_and_cash_bund);
        charge = (TextView) view.findViewById(R.id.charge_and_cash_toolbar_title);
        safr_rl = (RelativeLayout) view.findViewById(R.id.charge_safe_rl);
        chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
        RequestForRechargeTo(chargeUrl);
        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargeRecordActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @OnClick(R.id.charge_action_submit)
    public void onSubmitClick() {
        //如果快速点击了两次
        if (ClickEvent.isFastDoubleClick(R.id.charge_action_submit)) {
            return;
        }

        money = chargeMoneyNumber.getText().toString();
        String pwd = chargeSafePass.getText().toString();
        if (TextUtils.isEmpty(money.toString())) {
            Toast.makeText(getActivity(), "请输入充值金额", Toast.LENGTH_LONG).show();
            return;
        }
        if (Utils.str2Int(money) < 3) {
            Toast.makeText(getActivity(), "充值金额最少为3元", Toast.LENGTH_LONG).show();
            return;
        }

        if (flag == 0) {
            return;
        } else if (flag == 2) {
            if (TextUtils.isEmpty(pwd.toString())) {
                Toast.makeText(getActivity(), "请输入交易密码", Toast.LENGTH_LONG).show();
                return;
            }
            //调用接口，返回订单信息
            String url = AppConstants.URL_SUFFIX + "/rest/rechargeSaveHnew";
            RequestForRechargeData(url, money, pwd);
            chargeActionSubmit.setEnabled(false);
        } else if (flag == 1) {
            //进入绑卡页面
            Intent intent = new Intent();
            intent.setClass(getActivity(), RealNameAuthActivity.class);
            intent.putExtra("userId", userId);
            intent.putExtra("money", money + "");
            getActivity().startActivity(intent);
        }else if (flag == 4) {
            //进入绑卡页面
            Toast.makeText(getActivity(),"招商银行暂不支持快捷支付，请您联系客服",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取银行卡信息
     *
     * @param url
     */
    public void RequestForRechargeTo(String url) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                try{
                    getActivity().runOnUiThread(new Runnable() {
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
                }catch (Exception e1){
                    Log.e("ChargeFragment",e1.toString());
                }
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);
                final UserRechargeBean recharge = new Gson().fromJson(s, UserRechargeBean.class);
                getActivity().runOnUiThread(new Runnable() {
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

                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);

                        } else if (recharge.getRcd().equals("E0001")) {
                            getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1400);
                            getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(getActivity(), "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else if (recharge.getRcd().equals("M00010")) {
                            //没有绑定银行卡 点击确定进入绑卡页面
                            flag = 1;
                            safr_rl.setVisibility(View.GONE);
                        } else {
                            if (recharge != null)
                                //请求失败
                                Toast.makeText(getActivity(), recharge.getRmg(), Toast.LENGTH_SHORT).show();
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
            loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chargeActionSubmit.setEnabled(true);
                        if (e != null || e.getMessage() != null) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        chargeActionSubmit.setEnabled(true);
                        if (resultBean.getRcd().equals("R0001")) {
                            Intent intent = new Intent(getActivity(), QuicklyPlayActivity.class);
                            intent.putExtra("order", resultBean.getOrder_no());
                            getActivity().startActivity(intent);
                        } else if (resultBean.getRcd().equals("R0003")) {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setCancelable(false);
                            dialog.setTitle(resultBean.getRmg())
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent();
                                            intent.setClass(getActivity(), RealNameAuthActivity.class);
                                            intent.putExtra("userId", userId);
                                            intent.putExtra("money", money + "");
                                            getActivity().startActivity(intent);

                                        }
                                    }).show();

                        } else {
                            Toast.makeText(getActivity(), "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
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
//            CashFragment.isAgain = true;

            //通知用户中心刷新
            ActivityToFragmentMessage message = new ActivityToFragmentMessage();
            message.setTag(2004);
            EventBus.getDefault().post(message);

            messageDialog = new MessageDialog(getActivity());
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
    public void onDestroyView() {

        super.onDestroyView();
        ButterKnife.unbind(this);
        getActivity().unregisterReceiver(broadCase);
    }

    class ChargeFragmentBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RequestForRechargeTo(chargeUrl);
        }
    }
}
