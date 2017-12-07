package cn.ysgroup.ysdai.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ysgroup.ysdai.Beans.AwardTextBean;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowBean;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowPoputMessage;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.ActivityToFragmentMessage;
import cn.ysgroup.ysdai.Beans.user.UserRechargeBean;
import cn.ysgroup.ysdai.EventBus.EventBus;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.ConfirmDialog;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;

//主页进入的投资页面
public class PreviewOneActivity extends MyBaseActivity {
    private String TAG = "查看详细";

    private BorrowBean projectBean;//详情信息（全面，需要请求网络获取）
    private long recLength = 0;//截止时间的秒数
    private int mTotalProgress = 0;//总进度
    private int mCurrentProgress = 0;//当前进度
    private SeekBar progressBar;
    private TextView toolBarTitleText;
    private IconFontTextView toolBarBack;
    private Toolbar previewOneToolBar;
    private String Basic_Url = AppConstants.URL_SUFFIX + "/rest/borrow";
    private String Boss_Url = AppConstants.URL_SUFFIX + "/rest/reward";
    private int itemId;
    private int REALNAMEAUTHACTIVITYCODE = 10020;
    private ConfirmDialog confirmDialog;
    private boolean isBound = false;
    private boolean isBoundVerify = false;
    DecimalFormat df = new DecimalFormat("#0");

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0x111:

                    if (projectBean != null) {
                        updateViews(projectBean);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    //    private ScrollLayout up;
    private int screenHeight;
    private BigDecimal mData;
    private String timeLimit;
    private String stringBalance;
    private PreviewOneActivityBroadCast broadCast;
    private IntentFilter intentFilter;
    private Integer realStatus;
    private LoadingDialog loadingDialog;
    private RelativeLayout detail;
    private ProgressBar bar;
    private TextView mark;
    private TextView over_award;
    private TextView boss_award;
    private RelativeLayout safe;
    private BigDecimal extraneousData;
    private String bankUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_one);
        itemId = getIntent().getIntExtra("itemId", 0);
        initToolBar();
        RequestForListData(Basic_Url, itemId);
        bankUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
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
        toolBarTitleText = (TextView) findViewById(R.id.preview_one_toolbar_title);
        previewOneToolBar = (Toolbar) findViewById(R.id.preview_one_toolbar);
        toolBarBack = (IconFontTextView) findViewById(R.id.preview_one_toolbar_back);
        toolBarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(previewOneToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    public void updateViews(final BorrowBean projectItem) {
        intentFilter = new IntentFilter("PreviewOneActivityBroadCast");
        broadCast = new PreviewOneActivityBroadCast();
        registerReceiver(broadCast, intentFilter);
//        up = (ScrollLayout) findViewById(R.id.preview_one_up);
        TextView balance = (TextView) findViewById(R.id.preview_balance);
        TextView baseApr = (TextView) findViewById(R.id.preview_base_apr);
        TextView awardApr = (TextView) findViewById(R.id.preview_award_apr);
        TextView account = (TextView) findViewById(R.id.preview_account);
        TextView investType = (TextView) findViewById(R.id.preview_intest_mode);
        TextView timwLimit = (TextView) findViewById(R.id.preview_timelimit);
        TextView investRegion = (TextView) findViewById(R.id.preview_region);
        TextView payMode = (TextView) findViewById(R.id.preview_pay_mode);
        TextView releaseDate = (TextView) findViewById(R.id.preview_release_time);
        over_award = (TextView) findViewById(R.id.preview_one_over_award);
        boss_award = (TextView) findViewById(R.id.preview_one_boss_award);
        Button actionButton = (Button) findViewById(R.id.preview_one_button);
        TextView impose_text = (TextView) findViewById(R.id.preview_base_impose_text);
        TextView impose = (TextView) findViewById(R.id.preview_base_impose);
        mark = (TextView) findViewById(R.id.preview_one_mark);
        detail = (RelativeLayout) findViewById(R.id.preview_one_detail);
        safe = (RelativeLayout) findViewById(R.id.ll4);
        bar = (ProgressBar) findViewById(R.id.preview_one_bar);
        impose.setText(projectItem.getMostAccount());
        if (TextUtils.isEmpty(projectItem.getMostAccount()) || Utils.str2Int(projectItem.getMostAccount()) == 0 ||
                projectItem.getMostAccount().equals("null")) {
            impose_text.setVisibility(View.GONE);
            impose.setVisibility(View.GONE);
        }
        stringBalance = df.format(Utils.str2Double(projectItem.getBalance()));
        mTotalProgress = projectItem.getShowSchedule();
        toolBarTitleText.setText(projectItem.getName());
        final String lowestTender = projectItem.getLowestAccount();
        timeLimit = projectItem.getExpireDate();
        final int id = projectItem.getId();
        final String borImg = projectItem.getBorImage();
//        up.setTitle(projectItem.getName());
//        up.setDate(id);
//        up.setImage(borImg);
//        up.setActivity(this);
//        up.setProjectItem(projectItem);
        final String tokenString = PreferenceUtil.getPrefString(this, "loginToken", null);
        if (projectItem.getVerifyTime() != null) {

            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm");
            releaseDate.setText(sdf.format(new Date(projectItem.getVerifyTime())));
        }
        if (projectItem.getBaseApr() != null) {
            mData = projectItem.getBaseApr().setScale(2, BigDecimal.ROUND_HALF_UP);
            baseApr.setText(mData + "");
        }
        if (projectItem.getAwardApr() != null && projectItem.getBaseApr().compareTo(BigDecimal.ZERO) == 1) {
            extraneousData = projectItem.getAwardApr().setScale(2, BigDecimal.ROUND_HALF_UP);
            if (Utils.str2Float(extraneousData.toString()) == 0) {
                awardApr.setVisibility(View.GONE);
            } else {
                awardApr.setVisibility(View.VISIBLE);
                awardApr.setText("+" + extraneousData + "%");
            }
        } else {
            awardApr.setVisibility(View.INVISIBLE);
        }

        //comment by zenglulin on 20170819
//        if(projectItem.getIsNewbor().equals("1")){
//            investType.setText("投资日起息");
//        }else{
//            investType.setText("募集完成日起息");
//        }


        balance.setText(stringBalance);
        account.setText(projectItem.getAccount());
        timwLimit.setText(projectItem.getTimeLimit() + "");
        String investRegionString = "";
        if (projectItem.getLowestAccount() != null && !projectItem.getLowestAccount().trim().equals("")) {
            investRegionString += projectItem.getLowestAccount() + "";
        } else {
            investRegionString += "50元";

        }
        investRegion.setText(investRegionString);
//        if ("1".equals(projectItem.getStyle())) {
//            payMode.setText("分期付息，到期本息");
//        } else if ("2".equals(projectItem.getStyle())) {
//            payMode.setText("到期付本息");
//        } else if ("3".equals(projectItem.getStyle())) {
//            payMode.setText("等额本金");
//        }
        if (tokenString == null) {
            actionButton.setText("立即登录");
            actionButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivityForResult(new Intent(PreviewOneActivity.this, LoginActivity.class), 11000);
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);

                        }
                    }
            );

        } else {
            if (Utils.str2Double(projectItem.getBalance()) == 0) {
                actionButton.setBackgroundResource(R.drawable.button_grey_selector);
                actionButton.setText("投资其他项目");
                actionButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PreviewOneActivity.this.setResult(AppConstants.PreviewToInvestOthersCode);
                                PreviewOneActivity.this.finish();
                            }
                        }
                );
            } else {

                final BigDecimal lowestAcc = new BigDecimal(projectItem.getLowestAccount());
                final BigDecimal ableMoney = projectItem.getUserAbleMoney();
                realStatus = projectItem.getRealStatus();

                final int projectId = projectItem.getId();
                actionButton.setText("立即投资");
                actionButton.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = AppConstants.URL_SUFFIX + "/rest/poputInvest/" + projectId;
                                RequestSaveFormData(url, projectId, lowestTender, stringBalance);
                            }
                        }
                );

            }
        }

        //设置bar
        bar.setProgress(projectItem.getShowSchedule());
        mark.setText(projectItem.getShowSchedule() + "%");
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        float screenWidth = wm.getDefaultDisplay().getWidth();
        //bar的长度
        mark.measure(0, 0);
        float barWidth = screenWidth - Utils.dip2px(this, 45);
        if (projectItem.getShowSchedule() <= 5) {
            mark.setX((projectItem.getShowSchedule() * barWidth) / 100 + Utils.dip2px(this, 15));
        } else if (projectItem.getShowSchedule() <= 20 && projectItem.getShowSchedule() > 5) {
            mark.setX((projectItem.getShowSchedule() * barWidth) / 100 + Utils.dip2px(this, 13));
        } else if (projectItem.getShowSchedule() <= 50 && projectItem.getShowSchedule() > 20) {
            mark.setX((projectItem.getShowSchedule() * barWidth) / 100 + Utils.dip2px(this, 10));
        } else if (projectItem.getShowSchedule() <= 75 && projectItem.getShowSchedule() > 50) {
            mark.setX((projectItem.getShowSchedule() * barWidth) / 100 + Utils.dip2px(this, 7));
        } else {
            mark.setX((projectItem.getShowSchedule() * barWidth) / 100 - 10 + Utils.dip2px(this, 5));
        }

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewOneActivity.this, ProjectDetailActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", projectItem.getName());
                intent.putExtra("borrowImg", borImg);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", projectItem);
                intent.putExtras(bundle);
                if (projectItem == null) {
                    Toast.makeText(PreviewOneActivity.this, "请检查您的网络", Toast.LENGTH_SHORT).show();

                } else {
                    startActivityForResult(intent, 10000);
//                    overridePendingTransition(R.anim.activity_up,R.anim.activity_down);
                }
            }
        });

        safe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PreviewOneActivity.this, SafeActivity.class));
            }
        });
        RequestForListDataBoss(Boss_Url, itemId);
    }


    //请求网络数据
    public void RequestForListData(String basicUrl, int projectId) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
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
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        Log.i(TAG, "网络连接失败");
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                try {
                    final BorrowBean resultBean = JSON.parseObject(s, BorrowBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            if (resultBean.getRcd().equals("R0001")) {
                                System.out.println(resultBean.toString());
                                projectBean = resultBean;
                                Message msg = new Message();
                                msg.what = 0x111;
                                mHandler.sendMessage(msg);
                            } else {
                                Toast.makeText(PreviewOneActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e) {
                    Log.v(TAG, e.toString());
                }
            }
        });
    }

    /**
     * 请求获取该项目的数据
     *
     * @param basicUrl
     */
    public void RequestSaveFormData(String basicUrl, final int projectId, final String lowestTender, final String availableTender) {
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
                        Toast.makeText(PreviewOneActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
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
                final BorrowPoputMessage resultBean = JSON.parseObject(s, BorrowPoputMessage.class);
                Log.i(TAG, s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            RequestForRechargeTo(bankUrl, projectId, lowestTender, availableTender);
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivityForResult(new Intent(PreviewOneActivity.this, LoginActivity.class), 11000);
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(PreviewOneActivity.this, resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("MoreBroadCast");
                            intent.putExtra("isVisible", false);
                            sendBroadcast(intent);
                        } else {
                            Toast.makeText(PreviewOneActivity.this, resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (broadCast != null) {
            unregisterReceiver(broadCast);
        }
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (AppConstants.projectInvestSuccess == resultCode) {
//            Log.i(TAG, "收到投资成功后的回调");
//            finish();
////            RequestForListData(Basic_Url, itemId);
//        } else
        if (resultCode == AppConstants.PreviewToLoginCode) {
            PreviewOneActivity.this.setResult(AppConstants.PreviewToLoginCode);
            PreviewOneActivity.this.finish();
        } else if (requestCode == 11000 && resultCode == 10086) {
            finish();
        } else if (requestCode == 11000) {
            updateViews(projectBean);
        }
        if (realStatus != null && realStatus.intValue() != 1) {
            RequestForListData(Basic_Url, itemId);
        }
        ActivityToFragmentMessage atfmessage = new ActivityToFragmentMessage();
        atfmessage.setTag(2003);
        EventBus.getDefault().post(atfmessage);

    }

    //土豪
    public void RequestForListDataBoss(String basicUrl, int projectId) {
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
                        Log.i(TAG, "网络连接失败");
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                final AwardTextBean awardBean = JSON.parseObject(s, AwardTextBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (awardBean.getRcd().equals("R0001")) {
                            String tuhaoStr = awardBean.getTuhaoStr();
                            String shouguanStr = awardBean.getShouguanStr();
                            String separator = awardBean.getSeparator();
                            if (awardBean.isHadInvested()) {
                                //有投资
                                String phone = awardBean.getTuhaoVals().get(0);
                                String quota = awardBean.getTuhaoVals().get(1);
                                String awardMoney = awardBean.getTuhaoVals().get(2);
                                String hbSG = awardBean.getShouguanVals().get(0);
                                String awardMoneySG = awardBean.getShouguanVals().get(1);

                                String s1 = tuhaoStr.replaceFirst(separator, phone);
                                String[] split = s1.split(separator);
                                String s = split[0] + "<font color='#FF0000'>" + quota + "</font>"
                                        + split[1] + "<font color='#FF0000'>" + awardMoney + "</font>"
                                        + split[2];
                                boss_award.setText(Html.fromHtml(s));

                                String[] split1 = shouguanStr.split(separator);
                                String s3 = split1[0] + "<font color='#FF0000'>" + hbSG + "</font>"
                                        + split1[1] + "<font color='#FF0000'>" + awardMoneySG + "</font>"
                                        + split1[2];
                                over_award.setText(Html.fromHtml(s3));
                            } else {
                                //无投资
                                boss_award.setText(tuhaoStr);
                                over_award.setText(shouguanStr);
                            }
                        }
                    }
                });
            }
        });
    }


    /**
     * 获取银行卡信息
     *
     * @param bankUrl
     * @param projectId
     * @param lowestTender
     */
    public void RequestForRechargeTo(String bankUrl, final int projectId, final String lowestTender, final String availableTender) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(PreviewOneActivity.this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(bankUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                try {
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
                } catch (Exception e1) {
                    Log.e("ChargeFragment", e1.toString());
                }
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                Log.i(TAG, s);
                final UserRechargeBean recharge = new Gson().fromJson(s, UserRechargeBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (recharge.getRcd().equals("R0001")) {
                            //请求成功
                            isBound = true;
                            isBoundVerify = true;
                        } else if (recharge.getRcd().equals("E0001")) {
                            Toast.makeText(PreviewOneActivity.this, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                        } else if (recharge.getRcd().equals("M00010")) {
                            //没有绑定银行卡 点击确定进入绑卡页面
                            isBound = false;
                        }
                        else if (recharge.getRcd().equals("M00020")) {
                            //没有绑定银行卡 点击确定进入绑卡页面
                            isBoundVerify = false;
                        }else {
                            if (recharge != null)
                                //请求失败
                                Toast.makeText(PreviewOneActivity.this, recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent(PreviewOneActivity.this, ProjectInvestActivity.class);
                        intent.putExtra("projectId", projectId);
                        intent.putExtra("mData", (Utils.str2Float(mData.toString()) + Utils.str2Float(extraneousData.toString())) + "");
                        intent.putExtra("timeLimit", timeLimit);
                        intent.putExtra("lowestTender", lowestTender);
                        intent.putExtra("availableTender", availableTender);
                        intent.putExtra("MostAccount", projectBean.getMostAccount());
                        intent.putExtra("project", projectBean.getName());
                        intent.putExtra("isBund", isBound);
                        intent.putExtra("isBundVerify", isBoundVerify);
                        PreviewOneActivity.this.startActivityForResult(intent, 2020);


                    }
                });
            }
        });
    }

    class PreviewOneActivityBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int registerCode = intent.getIntExtra("register", 1000);
            if (registerCode == 100) {
                finish();
            } else if (registerCode == 200) {
                updateViews(projectBean);
            } else if (registerCode == 300) {
                RequestForListData(Basic_Url, itemId);
            }
        }
    }

}
