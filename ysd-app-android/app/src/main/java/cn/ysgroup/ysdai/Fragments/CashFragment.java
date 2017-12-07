package cn.ysgroup.ysdai.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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

import com.alibaba.fastjson.JSON;
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
import cn.ysgroup.ysdai.Activities.ForgetSafePassActivity;
import cn.ysgroup.ysdai.Activities.LoginActivity;
import cn.ysgroup.ysdai.Activities.TxRecordActivity;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.funds.UserCashBean;
import cn.ysgroup.ysdai.Beans.user.UserRechargeBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;

public class CashFragment extends Fragment {


    @Bind(R.id.cash_able_money)
    TextView cashAbleMoney;
    @Bind(R.id.cash_money_number_yuan)
    TextView cashMoneyNumberYuan;
    @Bind(R.id.cash_money_number)
    EditText cashMoneyNumber;
    @Bind(R.id.cash_safe_pass_forget)
    TextView cashSafePassForget;
    @Bind(R.id.cash_safe_pass)
    EditText cashSafePass;
    @Bind(R.id.cash_bank)
    TextView cashBank;
    @Bind(R.id.cash_action_submit)
    Button cashActionSubmit;
    @Bind(R.id.cash_charged_times)
    TextView cashChargedTimes;
    @Bind(R.id.cash_bank_icon)
    ImageView cashBankIcon;

    //提现后是否重新访问
//    public static boolean isAgain = false;
    private static final String ARG_PARAM = "currentPhoneNo";
    private static final String ARG_PARAM1 = "currentMoney";
    private final String TAG = "提现";
    private UserCashBean userCashBean;
    private MessageDialog messageDialog;
    private String currentPhoneNo;
    private String currentMoney;
    private int clickCount=0;
    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111:
                    bund.setVisibility(View.VISIBLE);
                    unbund.setVisibility(View.GONE);
                    updateViews(userCashBean);
                    break;
                case 0x333:
                    messageDialog = new MessageDialog(getActivity());
                    messageDialog.setMessage("提现成功,请注意查收");
                    messageDialog.setCancelable(false);
                    messageDialog.setOnPositiveListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    messageDialog.dismiss();
                                    messageDialog = null;
                                    RequestForRechargeTo(chargeUrl);
                                    getActivity().sendBroadcast(new Intent("ChargeFragmentBroadCase"));
                                }
                            }
                    );
                    messageDialog.show();
                    Intent intent = new Intent("LoginContentBroadCast");
                    intent.putExtra("Quickly","cash");
                    getActivity().sendBroadcast(intent);
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
    private CashFragmentBroadCase broadCase;
    private LoadingDialog loadingDialog;

    public static CashFragment newInstance(String param,String param1) {
        CashFragment fragment = new CashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, param);
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public CashFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPhoneNo = getArguments().getString(ARG_PARAM);
            currentMoney = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter("CashFragmentBroadCase");
        broadCase = new CashFragmentBroadCase();
        getActivity().registerReceiver(broadCase, intentFilter);
        View view = inflater.inflate(R.layout.fragment_cash, container, false);
        ButterKnife.bind(this, view);
        bund = (LinearLayout) view.findViewById(R.id.cash_bund);
        unbund = (RelativeLayout) view.findViewById(R.id.cash_unbund);
        charge = (TextView) view.findViewById(R.id.charge_and_cash_toolbar_title);
        cashAbleMoney.setText(currentMoney);
        RequestForRechargeTo(chargeUrl);

        charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TxRecordActivity.class);
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
        return view;
    }

    @OnClick(R.id.cash_safe_pass_forget)
    public void forgetSafePassOnClick() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ForgetSafePassActivity.class);
        intent.putExtra("currentPhoneNo", currentPhoneNo);
        startActivityForResult(intent, 8078);

    }

    public void updateViews(UserCashBean entity) {
        int resId = getResources().getIdentifier("bank_" + entity.getBankId().toLowerCase(), "mipmap", getActivity().getPackageName());
        cashBankIcon.setImageResource(resId);
        cashBank.setText(entity.getBankName() + "（尾号" + entity.getCardNo().substring(entity.getCardNo().length() - 4, entity.getCardNo().length()) + "）");
        int wholeTimes = Utils.str2Int(entity.getCashChargeTimes());
        int userTimes = Utils.str2Int(entity.getUserCashChargeTimes());

        SpannableStringBuilder spannableString = new SpannableStringBuilder("每月前" + wholeTimes + "笔提现免手续费,超过每笔收取5元手续费，本月您已累计提现");
        SpannableStringBuilder usedTimeText = new SpannableStringBuilder(userTimes + "笔");
        usedTimeText.setSpan(new ForegroundColorSpan(Color.RED), 0, usedTimeText.length() - 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        spannableString.append(usedTimeText);
        cashAbleMoney.setText(entity.getAbleMoney());
        cashChargedTimes.setText(spannableString);
        cashActionSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(clickCount>0){
                            Toast.makeText(getActivity(),"数据处理中。。",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //如果快速点击了两次
                        if (ClickEvent.isFastDoubleClick(R.id.cash_action_submit)) {
                            Toast.makeText(getActivity(),"请勿重复点击",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        cashActionSubmit.setEnabled(false);
                        clickCount+=1;
                        String basicUrl = AppConstants.URL_SUFFIX + "/rest/cashSave";
                        String cashMoney = cashMoneyNumber.getText().toString();
                        String safePass = cashSafePass.getText().toString();
                        RequestForTX(basicUrl, cashMoney, safePass);
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
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
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
                String s=response.body().string();
                Log.i(TAG, "response json:" + s);
                final UserCashBean resultBean = JSON.parseObject(s, UserCashBean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            userCashBean = resultBean;
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1400);
                            getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(getActivity(), "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else if (resultBean.getRcd().equals("M00015_3")) {

                        } else {
                            Toast.makeText(getActivity(), resultBean.getRmg(), Toast.LENGTH_SHORT).show();

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
            if (loadingDialog == null)
            {

                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.show();
            }
            String url = basicUrl;
            OkHttpClient client = new OkHttpClient();
            RequestBody body = new FormEncodingBuilder().
                    add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
                    add("cashMoney", cashMoney).
                    add("safepwd", safePass).build();
            Request request = new Request.Builder().url(url).post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, final IOException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingDialog != null && loadingDialog.isShowing())
                            {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            cashActionSubmit.setEnabled(true);
                            clickCount=0;
                            Toast.makeText(getActivity(), "操作失败，请检查网络！", Toast.LENGTH_SHORT).show();
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
                    final BaseBean resultBean = JSON.parseObject(s, BaseBean.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingDialog != null && loadingDialog.isShowing())
                            {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            cashActionSubmit.setEnabled(true);
                            clickCount=0;
                            if (resultBean.getRcd().equals("R0001")) {
                                Message msg = new Message();
                                msg.what = 0x333;
                                mHandler.sendMessage(msg);
                            } else {
                                Toast.makeText(getActivity(), resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(getActivity(), "请输入提现金额", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (safePass == null || safePass.equals("")) {
            Toast.makeText(getActivity(), "请输入交易密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 获取银行卡信息
     *u
     * @param url
     */
    public void RequestForRechargeTo(String url) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(getActivity(), "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
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
                final UserRechargeBean recharge = new Gson().fromJson(s, UserRechargeBean.class);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (recharge.getRcd().equals("R0001")) {
                            //请求成功
                            userId = recharge.getUserId();
                            RequestForListData(basicUrl);

                        } else if (recharge.getRcd().equals("E0001")) {
                            getActivity().startActivityForResult(new Intent(getActivity(), LoginActivity.class), 1400);
                            getActivity().overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(getActivity(), "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        } else if (recharge.getRcd().equals("M00010")) {

                        } else {
                            //请求失败
                            Toast.makeText(getActivity(), recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadCase);
    }

    class CashFragmentBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            RequestForRechargeTo(chargeUrl);
        }
    }

}
