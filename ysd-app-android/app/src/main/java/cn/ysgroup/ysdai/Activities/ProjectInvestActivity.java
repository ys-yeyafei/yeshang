package cn.ysgroup.ysdai.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowBean;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowPoputMessage;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.ActivityToFragmentMessage;
import cn.ysgroup.ysdai.Beans.bank.BankCard;
import cn.ysgroup.ysdai.Beans.bank.BankList;
import cn.ysgroup.ysdai.Beans.user.HbPriorityBean;
import cn.ysgroup.ysdai.Beans.userInfo.UserBean;
import cn.ysgroup.ysdai.EventBus.EventBus;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.UI.TopUpDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;

public class ProjectInvestActivity extends MyBaseActivity {

    @Bind(R.id.project_invest_toolbar_back)
    IconFontTextView projectInvestToolbarBack;
    @Bind(R.id.project_invest_toolbar_title)
    TextView projectInvestToolbarTitle;
    @Bind(R.id.project_invest_toolbar)
    Toolbar projectInvestToolbar;
    @Bind(R.id.project_invest_lowest_tender)
    TextView projectInvestLowestTender;
    @Bind(R.id.project_invest_able_tender)
    TextView projectInvestAbleTender;
    @Bind(R.id.project_invest_yuan)
    TextView projectInvestYuan;
    @Bind(R.id.project_invest_bank_name)
    TextView bank_name;
    @Bind(R.id.project_invest_bank_limit)
    TextView bank_limit;
    @Bind(R.id.project_invest_top_up)
    TextView top_up;
    @Bind(R.id.project_invest_tender_money)
    EditText projectInvestTenderMoney;
    @Bind(R.id.project_invest_hb_choose_action)
    IconFontTextView projectInvestHbChooseAction;
    @Bind(R.id.project_invest_hb_money)
    TextView projectInvestHbMoney;
    @Bind(R.id.project_invest_hb_choose_layout)
    RelativeLayout projectInvestHbChooseLayout;
    @Bind(R.id.project_invest_safe_pass)
    EditText projectInvestSafePass;
    @Bind(R.id.project_invest_confirm_button)
    Button projectInvestConfirmButton;
    @Bind(R.id.project_invest_able_money)
    TextView projectInvestAbleMoney;
    @Bind(R.id.add_bank_bank_layout)
    RelativeLayout bank_layout;
    private String TAG = "项目投资";


    //是否让edit 通知激活改变监听
    private boolean isChange = true;
    private int projectId;
    private String lowestTender;
    private String availableTender;
    private BorrowPoputMessage borrowPoputMessage;
    private MessageDialog messageDialog;
    private Gson gson = new Gson();
    private int hongBaoCount = 0;//使用的红包数
    private List<String> selectedIdList = new ArrayList<>();//选择的红包ID列表
    private String selectedIdArrayString = "";//以逗号分隔的红包ID
    private boolean isEntry = false;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            RequestForListHB();

        }

    };
    private TextView peroid;
    private TextView date;
    private TextView forget;
    private String mostAccount;
    private boolean isMost = true;
    private int onSize;
    StringBuilder hongbaoArray;
    private List<HbPriorityBean.UserHongbaoListBean> userHongbaoList;
    DecimalFormat df1 = new DecimalFormat("#0.00");
    DecimalFormat df = new DecimalFormat("#0");
    private String url;
    private String project;
    private LoadingDialog loadingDialog;
    private LinearLayout ll_password;
    private boolean isBund;
    private boolean isBundVerify;
    private String hbMoney;
    private String tenderMoney;
    private String safePass;
    private ProjectInvestActivityBroadCast broadCast;
    private TextView expect;
    private BankList banklist;
    private String bankUrl;
    private float keyboardF;
    private float residueF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_invest);
        ButterKnife.bind(this);
        initToolBar();
        peroid = (TextView) findViewById(R.id.project_invest_able_peroid);
        expect = (TextView) findViewById(R.id.project_invest_expect_money);
        date = (TextView) findViewById(R.id.project_invest_able_date);
        forget = (TextView) findViewById(R.id.project_invest_forget);
        ll_password = (LinearLayout) findViewById(R.id.project_invest_password);


        projectId = getIntent().getIntExtra("projectId", 0);
        lowestTender = getIntent().getStringExtra("lowestTender");
        availableTender = getIntent().getStringExtra("availableTender");
        project = getIntent().getStringExtra("project");
        mostAccount = getIntent().getStringExtra("MostAccount");

        peroid.setText(getIntent().getStringExtra("mData") + "%");
        date.setText(getIntent().getStringExtra("timeLimit"));
        isBund = getIntent().getBooleanExtra("isBund", false);
        isBundVerify = getIntent().getBooleanExtra("isBundVerify", false);
        if (!(isBund && isBundVerify)) {
            ll_password.setVisibility(View.GONE);
            forget.setVisibility(View.GONE);
        }
        projectInvestLowestTender.setText(lowestTender);
        projectInvestAbleTender.setText(availableTender);

        broadCast = new ProjectInvestActivityBroadCast();
        IntentFilter intentFilter = new IntentFilter("ProjectInvestActivityBroadCast");
        registerReceiver(broadCast, intentFilter);

        url = AppConstants.URL_SUFFIX + "/rest/poputInvest/" + projectId;
        bankUrl = AppConstants.URL_SUFFIX + "/rest/bankTo";
        RequestSaveFormData(url);
        RequestForBankData(bankUrl);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getSharedPreferences("AppToken", MODE_PRIVATE).getString("username", "");
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
                RequestForListDataAccount(basicUrl, id);
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

    public void initToolBar() {
        projectInvestToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(projectInvestToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @OnClick(R.id.project_invest_confirm_button)
    public void SubmitButton() {
        tenderMoney = projectInvestTenderMoney.getText().toString();
        safePass = projectInvestSafePass.getText().toString();
        if (tenderMoney == null || tenderMoney.equals("")) {
            Toast.makeText(ProjectInvestActivity.this, "请填写投资金额", Toast.LENGTH_SHORT).show();
            return;
        } else if (Utils.str2Int(tenderMoney) < 100) {
            Toast.makeText(ProjectInvestActivity.this, "起投金额不得少于100", Toast.LENGTH_SHORT).show();
            return;
        } else if (Utils.str2Float(Utils.str2Int(tenderMoney) + "") / 100 != (int) Utils.str2Float(Utils.str2Int(tenderMoney) + "") / 100) {
            Toast.makeText(ProjectInvestActivity.this, "投资金额必须为100的整数倍", Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.isEmpty(mostAccount) && Utils.str2Int(mostAccount) != 0
                && !mostAccount.equals("null") && projectInvestTenderMoney.getText().toString().length() > 0
                && Utils.str2Int(projectInvestTenderMoney.getText().toString()) > Utils.str2Int(mostAccount)) {
            Toast.makeText(ProjectInvestActivity.this, "该项目标限购￥：" + mostAccount, Toast.LENGTH_SHORT).show();
            return;
        }
//        else {
//            testMap.put("tenderMoney", tenderMoney);
//
//        }
        hbMoney = projectInvestHbMoney.getText().toString();
        //判断是否绑卡过
        if (!isBund) {
            //没绑卡进入绑卡页面
            Toast.makeText(this, "您未绑卡，完成首次充值后，自动绑卡", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(this, RealNameAuthActivity.class);

            String chargeMoney;
            if(Utils.str2Int(tenderMoney) - Utils.str2Int(hbMoney.substring(0, hbMoney.length() - 1))- Utils.str2Float(borrowPoputMessage.getAbleMoney())>3){
                chargeMoney=(Utils.str2Int(tenderMoney) - Utils.str2Int(hbMoney.substring(0, hbMoney.length() - 1))- Utils.str2Float(borrowPoputMessage.getAbleMoney())+"");
            }else{
                chargeMoney="3";
            }
            intent.putExtra("money",chargeMoney );
            intent.putExtra("inverstMark","inverstMark");
            intent.putExtra("tenderMoney",tenderMoney);
            intent.putExtra("selectedIdArrayString",selectedIdArrayString);
            intent.putExtra("project",project);
            intent.putExtra("projectId",projectId);

//            startActivityForResult(intent, 10086);
            startActivity(intent);
        } else {
            if(!isBundVerify){
                Intent intent = new Intent();
                intent.setClass(this, BankCardBindVerifyActivity.class);

                startActivity(intent);
            }else {
                String url = AppConstants.URL_SUFFIX + "/rest/investDoH/" + projectId;
                RequestToInvestDo(url);
            }
        }
    }


    /**
     * 请求获取该项目的数据
     *
     * @param basicUrl
     */
    public void RequestSaveFormData(String basicUrl) {
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
                        Toast.makeText(ProjectInvestActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
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
                final BorrowPoputMessage resultBean = JSON.parseObject(s, BorrowPoputMessage.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        borrowPoputMessage = resultBean;
                        if (resultBean.getRcd().equals("R0001")) {
                            new Handler().post(
                                    new Runnable() {
                                        @Override
                                        public void run() {

                                            UpdateViews();
                                        }
                                    }
                            );
                        } else {
                            Toast.makeText(ProjectInvestActivity.this, resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    ;

    /**
     * 请求提交投资该项目
     *
     * @param basicUrl
     */

    public void RequestToInvestDo(String basicUrl) {
        if (safePass == null || safePass.equals("")) {
            Toast.makeText(ProjectInvestActivity.this, "请填写交易密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ProjectInvestActivity.this);
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
                        Toast.makeText(ProjectInvestActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
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
                            Intent successIntent = new Intent(ProjectInvestActivity.this, InvestResultActivity.class);
                            if (isInteger(hbMoney.substring(0, hbMoney.length() - 1))) {
                                successIntent.putExtra("money",
                                        Utils.str2Int(tenderMoney));
                            } else {
                                successIntent.putExtra("money", tenderMoney);
                            }
                            successIntent.putExtra("project", project);
                            startActivityForResult(successIntent, InvestResultActivity.CONTEXT_INCLUDE_CODE);
                            setResult(AppConstants.projectInvestSuccess);
                            finish();
                        } else if (resultBean.getRcd().equals("M0007_0")) {
                            //资金余额不足
                            TopUpDialog dialog = new TopUpDialog(ProjectInvestActivity.this, R.style.ActionSheetDialogStyle);
                            if (isInteger(hbMoney.substring(0, hbMoney.length() - 1))) {
                                dialog.setData(df1.format(Utils.str2Int(tenderMoney) - Utils.str2Int(hbMoney.substring(0, hbMoney.length() - 1)) - (Utils.str2Double(borrowPoputMessage.getAbleMoney())))
                                        , safePass, ProjectInvestActivity.this,tenderMoney,safePass,selectedIdArrayString,project,projectId);
                            } else {
                                dialog.setData(df1.format(Utils.str2Int(tenderMoney) - (Utils.str2Double(borrowPoputMessage.getAbleMoney())))
                                        , safePass, ProjectInvestActivity.this,tenderMoney,safePass,selectedIdArrayString,project,projectId);
                            }

                        } else if (resultBean.getRcd().equals("M0007_211")) {
                            //被抢投 刷新项目
                            Toast.makeText(ProjectInvestActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                            RequestForListData(AppConstants.URL_SUFFIX + "/rest/borrow");
                        } else {
                            Toast.makeText(ProjectInvestActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    public void UpdateViews() {
        projectInvestAbleMoney.setText(borrowPoputMessage.getAbleMoney() + "元");
        projectInvestTenderMoney.addTextChangedListener(

                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (isChange) {
                            if (projectInvestTenderMoney.getText().toString().length() > 0 && availableTender.length() > 0) {
                                //输入的钱大于可投金额
                                if (Utils.str2Float(projectInvestTenderMoney.getText().toString()) > Utils.str2Float(availableTender + "")) {
                                    projectInvestTenderMoney.setText(availableTender);
                                    projectInvestTenderMoney.setSelection(availableTender.length());

                                    if (isMost) {
                                        isMost = false;
                                        mHandler.removeCallbacksAndMessages(null);
                                        mHandler.sendEmptyMessageDelayed(0, 1000);
                                    }
                                    //输入的钱大于100金额
                                } else if (Utils.str2Float(projectInvestTenderMoney.getText().toString()) >= 100) {
                                    isMost = true;
                                    mHandler.removeCallbacksAndMessages(null);
                                    mHandler.sendEmptyMessageDelayed(0, 1000);
                                } else {
                                    top_up.setText("0元");
                                    projectInvestHbMoney.setText("0元");
                                }

                                //预期收益
                                String exceptS = df1.format(Utils.str2Float(projectInvestTenderMoney.getText().toString()) *
                                        (Utils.str2Float(getIntent().getStringExtra("mData")) / 100) *
                                        (Utils.str2Float(getIntent().getStringExtra("timeLimit")) / 360));
                                expect.setText(exceptS + "元");


                            }
                        } else {
                            isChange = true;
                        }
                        if (projectInvestTenderMoney.getText().toString().length() == 0) {
                            expect.setText("0元");
                            top_up.setText("0元");
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                }
        );


        //红包选择点击
        projectInvestHbChooseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestForListHB1();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == AppConstants.ChooseHongBaoOkCode) {
            hongBaoCount = data.getIntExtra("hongBaoCount", 0);
            String idListString = data.getStringExtra("hongBaoList");
            selectedIdArrayString = data.getStringExtra("hongBaoArray");
            String certainMoney = data.getStringExtra("certainMoney");
            selectedIdList = gson.fromJson(idListString, new TypeToken<ArrayList<String>>() {
            }.getType());
            Log.i(TAG, selectedIdList.size() + "   ...........");
            String tenderNumberString = projectInvestTenderMoney.getText().toString();
            if (tenderNumberString.length() > 0) {

                BigDecimal tenderNumber = new BigDecimal(tenderNumberString);
                projectInvestHbMoney.setText(hongBaoCount + "元");
                isChange = false;
                projectInvestTenderMoney.setText(certainMoney);
                projectInvestTenderMoney.setSelection(certainMoney.length());
            } else {
                hongBaoCount = 0;
                projectInvestHbMoney.setText(hongBaoCount + "元");
            }

            float hbF = Utils.str2Float(projectInvestHbMoney.getText().toString().
                    substring(0, projectInvestHbMoney.getText().toString().length() - 1));
            float keyboardF = Utils.str2Float(projectInvestTenderMoney.getText().toString());
            float residueF = Utils.str2Float(borrowPoputMessage.getAbleMoney());
            if (keyboardF - residueF - hbF < 0) {
                top_up.setText("0元");
            } else if (keyboardF - residueF - hbF > 0 && keyboardF - residueF - hbF < 3) {
                top_up.setText("最低充值额度3元");
            } else {
                top_up.setText(keyboardF - residueF - hbF + "元");
            }

        }
//        else if (requestCode == 10086) {
//            String bankUrl = AppConstants.URL_SUFFIX + "/rest/bankTo";
//            RequestForBankData(bankUrl);
//        }


        isEntry = true;
        super.onActivityResult(requestCode, resultCode, data);
    }

    //请求网络数据 查询该帐号号码
    public void RequestForListDataAccount(String basicUrl, String userId) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("username", userId).build();
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
                String s = response.body().string();
                Log.i(TAG, "response json:" + s);
                final UserBean resultBean = JSON.parseObject(s, UserBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            UpdateViewsAccount(resultBean);
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivity(new Intent(ProjectInvestActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Intent intent = new Intent("MoreBroadCast");
                            intent.putExtra("isVisible", false);
                            sendBroadcast(intent);
                            Toast.makeText(ProjectInvestActivity.this, "登录已过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProjectInvestActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void UpdateViewsAccount(UserBean userBean) {
        Intent intent = new Intent();
        intent.setClass(ProjectInvestActivity.this, ForgetSafePassActivity.class);
        intent.putExtra("currentPhoneNo", userBean.getPhone());
        startActivity(intent);
    }

    //查询最优红包
    public void RequestForListHB() {
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListBestH";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("investFullMomeyMax", projectInvestTenderMoney.getText().toString()).
                add("borrowId", projectId + "").build();
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
                String s = response.body().string();
                Log.i(TAG, "response json:" + s);
                final HbPriorityBean hbPriorityBean = JSON.parseObject(s, HbPriorityBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userHongbaoList = hbPriorityBean.getUserHongbaoList();
                        isEntry = true;
                        if (hbPriorityBean.getRcd().equals("R0001")) {
                            int hbcount = 0;
                            onSize = 0;
                            if (userHongbaoList != null && userHongbaoList.size() > 0) {
                                hongbaoArray = new StringBuilder();
                                for (HbPriorityBean.UserHongbaoListBean item : userHongbaoList) {
                                    if (item.isOn()) {
                                        hbcount = (int) (hbcount + item.getMoney());
                                        onSize = onSize + 1;
                                        hongbaoArray.append(item.getId() + ",");

                                    }
                                }
                                if (hongbaoArray.length() > 0)
                                    selectedIdArrayString = hongbaoArray.substring(0, hongbaoArray.length() - 1);
                            }
                            projectInvestHbMoney.setText(hbcount + "元");


                            //银行充值
                            float hbF = Utils.str2Float(projectInvestHbMoney.getText().toString().
                                    substring(0, projectInvestHbMoney.getText().toString().length() - 1));
                            if(TextUtils.isEmpty(projectInvestTenderMoney.getText().toString())){
                                keyboardF = 0;

                            }else{
                                keyboardF = Utils.str2Float(projectInvestTenderMoney.getText().toString());
                            }

                            if(TextUtils.isEmpty(borrowPoputMessage.getAbleMoney())){
                                residueF = 0;
                            }else{
                                residueF = Utils.str2Float(borrowPoputMessage.getAbleMoney());

                            }
                            if (keyboardF - residueF - hbF < 0) {
                                top_up.setText("0元");
                            } else if (keyboardF - residueF - hbF > 0 && keyboardF - residueF - hbF < 3) {
                                top_up.setText("最低充值额度3元");
                            } else {
                                top_up.setText(keyboardF - residueF - hbF + "元");
                            }
                        } else {

                        }
                    }
                });
            }
        });
    }

    //查询最优红包
    public void RequestForListHB1() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(ProjectInvestActivity.this);
            loadingDialog.show();
        }
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListBestH";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("investFullMomeyMax", projectInvestTenderMoney.getText().toString()).
                add("borrowId", projectId + "").build();
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
                Log.i(TAG, "response json:" + s);
                final HbPriorityBean hbPriorityBean = JSON.parseObject(s, HbPriorityBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        userHongbaoList = hbPriorityBean.getUserHongbaoList();
                        isEntry = true;
                        if (hbPriorityBean.getRcd().equals("R0001")) {
                            int hbcount = 0;
                            onSize = 0;
                            if (userHongbaoList != null && userHongbaoList.size() > 0) {
                                hongbaoArray = new StringBuilder();
                                for (HbPriorityBean.UserHongbaoListBean item : userHongbaoList) {
                                    if (item.isOn()) {
                                        hbcount = (int) (hbcount + item.getMoney());
                                        onSize = onSize + 1;
                                        hongbaoArray.append(item.getId() + ",");

                                    }
                                }
                                if (hongbaoArray.length() > 0) {
                                    selectedIdArrayString = hongbaoArray.substring(0, hongbaoArray.length() - 1);
                                }

                            } else {
                                Toast.makeText(ProjectInvestActivity.this, "当前标您没有可以使用的红包！", Toast.LENGTH_SHORT).show();
                            }
                            if (borrowPoputMessage != null) {
                                String tenderMoney = projectInvestTenderMoney.getText().toString();
                                Log.i(TAG, "投资金额" + tenderMoney);
                                if (!TextUtils.isEmpty(projectInvestTenderMoney.getText().toString()) && Utils.str2Float(projectInvestTenderMoney.getText().toString()) >= 100) {
                                    if (userHongbaoList != null && isEntry) {
                                        isEntry = false;
                                        Intent intent = new Intent();
                                        selectedIdList.clear();
                                        if (userHongbaoList != null && userHongbaoList.size() > 0) {
                                            for (HbPriorityBean.UserHongbaoListBean item : userHongbaoList) {
                                                if (item.isOn()) {
                                                    selectedIdList.add(item.getId() + "");
                                                }
                                            }
                                        }


                                        intent.setClass(ProjectInvestActivity.this, HbChooseActivity.class);
                                        intent.putExtra("hbListString", gson.toJson(userHongbaoList));
                                        Log.i(TAG, selectedIdList.size() + "   ...........");
                                        intent.putExtra("choosenIdString", gson.toJson(selectedIdList));
                                        intent.putExtra("tenderMoney", tenderMoney);
                                        intent.putExtra("onSize", onSize);
                                        startActivityForResult(intent, 7080);
                                    }
                                } else {
                                    Toast.makeText(ProjectInvestActivity.this, "起投金额不得少于100", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(ProjectInvestActivity.this, hbPriorityBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取银行卡数据
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
                String s = response.body().string();
                Log.i(TAG, s);

                final BankList resultBean = JSON.parseObject(s, BankList.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            banklist = resultBean;
                            for (BankCard item : banklist.getBankCardList()) {
                                if (item.getBankId().equals(banklist.getBankId())) {
                                    bank_limit.setText(item.getBankLimit());
                                    String bank_limitS = item.getBankName() + "（" + "尾号" + banklist.
                                            getCardNo().substring(banklist.getCardNo().length() -
                                            4, banklist.getCardNo().length()) + "）";
                                    bank_name.setText(bank_limitS);
                                    if(isBund && isBundVerify){
                                        bank_layout.setVisibility(View.VISIBLE);
                                        ll_password.setVisibility(View.VISIBLE);
                                    }else{
                                        bank_layout.setVisibility(View.GONE);
                                        ll_password.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } else {
                        }
                    }
                });
            }
        });
    }


    //获取项目剩余 可投
    public void RequestForListData(String basicUrl) {

        String url = basicUrl + "/" + projectId;
        Log.i(TAG, url);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                build();
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
                final BorrowBean resultBean = JSON.parseObject(s, BorrowBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            projectInvestAbleTender.setText(df.format(Utils.str2Double(resultBean.getBalance())));
                        } else {
                            Toast.makeText(ProjectInvestActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    //判断是否是int类型
    public boolean isInteger(String value) {
        try {
            Utils.str2Int(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCast);
    }

    class ProjectInvestActivityBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("quickPlay")!=null && intent.getStringExtra("quickPlay").equals("finish")){
                finish();
            }else if(intent.getStringExtra("quickPlay")!=null && intent.getStringExtra("quickPlay").equals("refresh")){
                //刷新页面
                RequestSaveFormData(url);
                RequestForListData(AppConstants.URL_SUFFIX + "/rest/borrow");
                RequestForBankData(bankUrl);
            }else{
                isBund = true;
                isBundVerify = true;
                ll_password.setVisibility(View.VISIBLE);
                forget.setVisibility(View.VISIBLE);
                //刷新账户信息
                String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
                RequestSaveFormData(url);

                //通知用户中心刷新
                ActivityToFragmentMessage message = new ActivityToFragmentMessage();
                message.setTag(2004);
                EventBus.getDefault().post(message);
            }
        }
    }


}
