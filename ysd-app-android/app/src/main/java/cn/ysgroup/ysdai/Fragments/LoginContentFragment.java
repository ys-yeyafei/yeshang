package cn.ysgroup.ysdai.Fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.ysgroup.ysdai.Activities.AccountManageActivity;
import cn.ysgroup.ysdai.Activities.AllPropertyActivity;
import cn.ysgroup.ysdai.Activities.BackMoneyActivity;
import cn.ysgroup.ysdai.Activities.CashActivity;
import cn.ysgroup.ysdai.Activities.ChargeActivity;
import cn.ysgroup.ysdai.Activities.HongBaoActivity;
import cn.ysgroup.ysdai.Activities.InvestRecordActivity;
import cn.ysgroup.ysdai.Activities.LoginActivity;
import cn.ysgroup.ysdai.Activities.RewardActivity;
import cn.ysgroup.ysdai.Activities.SelectPicActivity;
import cn.ysgroup.ysdai.Activities.ShareToFriendsActivity;
import cn.ysgroup.ysdai.Activities.UserAccDetailActivity;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.File.FileBean;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.ActivityToFragmentMessage;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.FragmenToActivityMessage;
import cn.ysgroup.ysdai.Beans.userInfo.PrincipalBeen;
import cn.ysgroup.ysdai.Beans.userInfo.UserBean;
import cn.ysgroup.ysdai.Beans.userInfo.UserCenterBean;
import cn.ysgroup.ysdai.EventBus.EventBus;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.ConfirmDialog;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.BitmapCache;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.UploadUtil;
import cn.ysgroup.ysdai.Util.Utils;


/**
 * 用户中心首页
 * pingia
 */

public class LoginContentFragment extends BaseFragment implements UploadUtil.OnUploadProcessListener {
    private Gson gson = new Gson();
    private final String TAG = "我的账户";
    private TextView userName;//账号
    private TextView investMOney;//投资记录
    private TextView payBackMoney;//回款记录
    private TextView shouyiMoney;//累计收益
    private TextView chargeAndCash;
    private TextView chargeAndcharge;
//    private LinearLayout hongBaoReward;
    private LinearLayout moneyRecord;
    private LinearLayout investRecordLayout;
    private LinearLayout moneyBackRecordLayout;
    private UserCenterBean userCenterBean = null;
    private UserBean userStatusBean = null;
    private LoadingDialog loadingDialog;
    private ConfirmDialog realDialog;
    private double totalIncome = 0.00;

    //推按缓存
    private ImageLoader.ImageCache imageCache;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x100:
                    totalIncome = userCenterBean.getAbleMoney().doubleValue();
                    String url = AppConstants.URL_SUFFIX + "/rest/user";
                    RequestForSatusData(url, userCenterBean.getUsername());
                    RequestForMoney();
                    break;
                case 0x200:
                    updateViews(userCenterBean);
                    break;
                case 0x300:
                    updateViewClick(userStatusBean, userCenterBean.getUsername());
                    if (userStatusBean.getRealStatus() != null && userStatusBean.getRealStatus().intValue() == 1) {
                    }

                    break;
                case 0x400:
                    //设置头像


                    break;
                case TO_UPLOAD_FILE:
                    toUploadFile();
                    break;
                case UPLOAD_INIT_PROCESS:
                    break;

                case UPLOAD_IN_PROCESS:
                    break;

                case UPLOAD_FILE_DONE:
                    String result = "响应码：" + msg.arg1 + "----响应信息：" + msg.obj + "----耗时：" + UploadUtil.getRequestTime() + "秒";
                    Log.i(TAG, "result=" + result);
                    if (msg.arg1 == 1) {
                        FileBean bean = new Gson().fromJson(msg.obj.toString(), FileBean.class);
                        if (bean.getRcd().equals("R0001")) {
                        } else {
                            Toast.makeText(activity, bean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (msg.arg1 == 4) {
                        Toast.makeText(activity, "上传超时", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private RelativeLayout friend;
    private Activity activity;
    private View rootView;
    private IconFontTextView icon;
    private SharedPreferences preferences;
    public boolean isFirst = true;
    private TextView today;
    private View point;
    private LoginContentBroadCast broadCast;
    private LinearLayout allProrert;
    private PrincipalBeen principalBeen;
    private LinearLayout hongbao;
    private LinearLayout reward;
    private TextView hongbao_count;
    private TextView award_count;
    private TextView invest_count;
    private TextView invest_money_count;
    private TextView back_money_count;

    public LoginContentFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public LoginContentFragment(Activity activity) {
        preferences = activity.getSharedPreferences("AppToken", activity.MODE_PRIVATE);
        this.activity = activity;
        rootView = View.inflate(activity, R.layout.fragment_login_content, null);
        broadCast = new LoginContentBroadCast();
        IntentFilter intentFilter = new IntentFilter("LoginContentBroadCast");
        activity.registerReceiver(broadCast, intentFilter);
        initViews(rootView);
        progressDialog = new ProgressDialog(activity);
        //初始化缓存设置
        imageCache = new BitmapCache();

        String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
//        RequestForUserCenter(url);


        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View getmView() {
        return rootView;
    }


    public void onEventMainThread(ActivityToFragmentMessage mfe) {
        if (mfe.getTag() == 2002) {
            if (loadingDialog == null) {

                loadingDialog = new LoadingDialog(activity);
                loadingDialog.show();
            }
            String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
            RequestForUserCenter(url);
        } else if (mfe.getTag() == 2003) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(activity);
                loadingDialog.show();
            }
            String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
            RequestForUserCenter(url);
        } else if (mfe.getTag() == 2004) {

            String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
            RequestForUserCenter(url);
        }
    }

    public void initViews(View rootView) {
        point = rootView.findViewById(R.id.login_content_table_row_point);
        today = (TextView) rootView.findViewById(R.id.login_content_invest_today);
        userName = (TextView) rootView.findViewById(R.id.login_content_account_name);
        investMOney = (TextView) rootView.findViewById(R.id.login_content_invest_money);
        payBackMoney = (TextView) rootView.findViewById(R.id.login_content_repay_money);
        shouyiMoney = (TextView) rootView.findViewById(R.id.login_content_all_profit);
        friend = (RelativeLayout) rootView.findViewById(R.id.login_my_friend);
        investRecordLayout = (LinearLayout) rootView.findViewById(R.id.login_content_invest_record);
        moneyBackRecordLayout = (LinearLayout) rootView.findViewById(R.id.login_content_money_back);
        icon = (IconFontTextView) rootView.findViewById(R.id.main_toolbar_right_icon);
        chargeAndcharge = (TextView) rootView.findViewById(R.id.login_content_table_row_charge);
        hongbao = (LinearLayout) rootView.findViewById(R.id.login_content_invest_hongbao);
        chargeAndCash = (TextView) rootView.findViewById(R.id.login_content_table_row_cash);
        hongbao_count = (TextView) rootView.findViewById(R.id.login_content_invest_hongbao_count);
        award_count = (TextView) rootView.findViewById(R.id.login_content_invest_award_count);
        invest_count = (TextView) rootView.findViewById(R.id.login_content_invest_invest_count);
        invest_money_count = (TextView) rootView.findViewById(R.id.login_content_invest_money_count);
        back_money_count = (TextView) rootView.findViewById(R.id.login_content_back_money_count);
//        hongBaoReward = (LinearLayout) rootView.findViewById(R.id.login_content_table_row_reward);
        moneyRecord = (LinearLayout) rootView.findViewById(R.id.login_content_table_row_money_record);
        allProrert = (LinearLayout) rootView.findViewById(R.id.login_content_today_layout);
        reward = (LinearLayout) rootView.findViewById(R.id.login_content_invest_reward);
    }

    public void updateViews(UserCenterBean resultBack) {

        BigDecimal bd1 = resultBack.getAbleMoney().setScale(2, BigDecimal.ROUND_HALF_UP);//保留两位小数，四舍五入
        BigDecimal bd3 = resultBack.getTotalIncome().setScale(2, BigDecimal.ROUND_HALF_UP);//保留两位小数，四舍五入
        shouyiMoney.setText(bd3.toString());
        userName.setText(resultBack.getUsername().substring(0, 3) + "****" + resultBack.getUsername().substring(7, resultBack.getUsername().length()));
        if (resultBack.getTasteMoney() == null) {
            resultBack.setTasteMoney(new BigDecimal(0));
        }


        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        if (resultBack.getTenderTime() != null) {
            String lastInvestTimeText = sdf.format(new Date(resultBack.getTenderTime()));
        }
        if (resultBack.getRepaymentTime() != null) {
            String lastRepayTimeText = sdf.format(new Date(resultBack.getRepaymentTime()));
        }
    }

    public void updateViewClick(UserBean userStatusBean, String userName) {

        RequestForHbPoint();
        final String userId = userName;
        final String currentPhoneNo = userStatusBean.getPhone();
        final int realStatus = userStatusBean.getRealStatus() == null ? 0 : userStatusBean.getRealStatus().intValue();
        final int safePwdStatus = userStatusBean.getSafePwdStatus() == null ? 0 : userStatusBean.getSafePwdStatus().intValue();

        //跳到取现首页 pingia
        chargeAndCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(activity, CashActivity.class);
                intent.putExtra("currentPhoneNo", currentPhoneNo);
                intent.putExtra("totalIncome", totalIncome + "");
                activity.startActivityForResult(intent, 2008);
            }
        });

        //跳到充值首页 pingia
        chargeAndcharge.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, ChargeActivity.class);
//                        intent.putExtra("currentPhoneNo", currentPhoneNo);
                        intent.putExtra("totalIncome", totalIncome + "");
                        activity.startActivityForResult(intent, 2008);
                    }
                }

        );
        moneyRecord.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(activity, UserAccDetailActivity.class);
                        intent.putExtra("userId", userId);
                        activity.startActivity(intent);

                    }
                }
        );

        investRecordLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(activity, InvestRecordActivity.class);
                        intent.putExtra("userId", userId);
                        activity.startActivity(intent);
                    }
                }
        );
        moneyBackRecordLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(activity, BackMoneyActivity.class);
                        intent.putExtra("userId", userId);
                        activity.startActivity(intent);
                    }
                }
        );

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = activity.getSharedPreferences("AppToken", activity.MODE_PRIVATE);
                String faRongToken = preferences.getString("loginToken", null);
                if (faRongToken != null) {
                    Intent intent = new Intent(activity, ShareToFriendsActivity.class);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "请登录！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        icon.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String username = preferences.getString("username", "");
                        Intent intent = new Intent();
                        intent.setClass(activity, AccountManageActivity.class);
                        intent.putExtra("userId", username);
                        activity.startActivityForResult(intent, 8080);
                    }
                }
        );

        allProrert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, AllPropertyActivity.class);
                intent.putExtra("principalBeen", principalBeen);
                activity.startActivity(intent);
            }
        });

        hongbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, HongBaoActivity.class));
            }
        });
        reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, RewardActivity.class));
            }
        });
    }

    public void RequestForUserCenter(String url) {
        //访问第二个

        if (loadingDialog == null) {

            loadingDialog = new LoadingDialog(activity);
            loadingDialog.show();
        }
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        Toast.makeText(activity, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
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
                try{
                    final UserCenterBean resultBack = new Gson().fromJson(s, UserCenterBean.class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                                loadingDialog = null;
                            }
                            if (resultBack.getRcd().equals("R0001")) {
                                isFirst = false;
                                //请求成功，通知activity
                                updateViews(resultBack);
                                userCenterBean = resultBack;
                                Message msg = new Message();
                                msg.what = 0x100;
                                mHandler.sendMessage(msg);

                            } else if (resultBack.getRcd().equals("E0001")) {
                                activity.startActivityForResult(new Intent(activity, LoginActivity.class), 1400);
                                activity.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            } else

                            {
                                //请求失败
                                Toast.makeText(activity, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
                catch (Exception e){
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    //请求网络数据
    public void RequestForSatusData(String basicUrl, String userId) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
                add("username", userId).build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
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
                final UserBean userBean = JSON.parseObject(s, UserBean.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (userBean.getRcd().equals("R0001")) {
                            userStatusBean = userBean;

                            Message msg = new Message();
                            msg.what = 0x300;
                            mHandler.sendMessage(msg);
                        } else if (userBean.getRcd().equals("E0001")) {
                            FragmenToActivityMessage exitMessage = new FragmenToActivityMessage();
                            exitMessage.setTag(9999);
                            EventBus.getDefault().post(exitMessage);
                        } else {
                            Toast.makeText(activity, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == TO_SELECT_PHOTO) {
            picPath = data.getStringExtra(SelectPicActivity.KEY_PHOTO_PATH);
            Log.i(TAG, "最终选择的图片=" + picPath);
            if (picPath != null) {
                mHandler.sendEmptyMessage(TO_UPLOAD_FILE);
            } else {
                Toast.makeText(activity, "上传的文件路径出错", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == AppConstants.AppLoginEXitCode) {

        } else if (resultCode == AppConstants.RealAuthSuccess) {
            String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
            RequestForUserCenter(url);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //上传图片
    /**
     * 去上传文件
     */
    protected static final int TO_UPLOAD_FILE = 1;
    /**
     * 上传文件响应
     */
    protected static final int UPLOAD_FILE_DONE = 2;  //
    /**
     * 选择文件
     */
    public static final int TO_SELECT_PHOTO = 3;
    /**
     * 上传初始化
     */
    private static final int UPLOAD_INIT_PROCESS = 4;
    /**
     * 上传中
     */
    private static final int UPLOAD_IN_PROCESS = 5;
    /***
     * 这里的这个URL是我服务器的javaEE环境URL
     */
    private static String requestURL = AppConstants.URL_SUFFIX + "/rest/upHeadImg";
    private String picPath = null;
    private ProgressDialog progressDialog;


    private void toUploadFile() {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在上传文件...");
        progressDialog.show();
        String fileKey = "imgFile";
        UploadUtil uploadUtil = UploadUtil.getInstance();
        uploadUtil.setOnUploadProcessListener(this);  //设置监听器监听上传状态

        Map<String, String> params = new HashMap<String, String>();
        params.put("fileFileName", "head");
        params.put("token", PreferenceUtil.getPrefString(activity, "loginToken", ""));
        uploadUtil.uploadFile(picPath, fileKey, requestURL, params, activity);
    }

    /**
     * 上传服务器响应回调
     */
    @Override
    public void onUploadDone(int responseCode, String message) {
        progressDialog.dismiss();
        Message msg = Message.obtain();
        msg.what = UPLOAD_FILE_DONE;
        msg.arg1 = responseCode;
        msg.obj = message;
        mHandler.sendMessageDelayed(msg, 1000);
    }


    @Override
    public void onUploadProcess(int uploadSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_IN_PROCESS;
        msg.arg1 = uploadSize;
        mHandler.sendMessage(msg);
    }

    @Override
    public void initUpload(int fileSize) {
        Message msg = Message.obtain();
        msg.what = UPLOAD_INIT_PROCESS;
        msg.arg1 = fileSize;
        mHandler.sendMessage(msg);
    }

    public void RequestForHbPoint() {
        String url = AppConstants.URL_SUFFIX + "/rest/hbNotLookCount";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null && e.getMessage() != null) {
                            Toast.makeText(activity, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                System.out.println(s);
                final BaseBean bean = gson.fromJson(s, BaseBean.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (bean.getRcd().equals("R0001")) {
                            if (Utils.str2Int(bean.getRmg()) > 0) {
                                point.setVisibility(View.VISIBLE);
                            } else {
                                point.setVisibility(View.GONE);
                            }
                        } else {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, bean.getRmg(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    //今日收益
//    public void RequestForProfit() {
//        String url = AppConstants.URL_SUFFIX + "/rest/userNowIncome";
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
//                build();
//        Request request = new Request.Builder().url(url).post(body).build();
//        Call call = client.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, final IOException e) {
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(activity, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(final Response response) throws IOException {
//                String s = response.body().string();
//                System.out.println(s);
//                final ProfitBean bean = gson.fromJson(s, ProfitBean.class);
//                activity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        payBackMoney.setText(bean.getUserNowIncome());
//                    }
//                });
//            }
//        });
//    }


    //本金利息
    public void RequestForMoney() {
        String url = AppConstants.URL_SUFFIX + "/rest/ajaxIndex";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null && e.getMessage() != null) {
                            Toast.makeText(activity, "请求数据失败，请检查网络！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s = response.body().string();
                System.out.println(s);
                principalBeen = gson.fromJson(s, PrincipalBeen.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (principalBeen.getRcd().equals("R0001")) {
                            today.setText(principalBeen.getTotal() + "");
                            investMOney.setText(principalBeen.getInvestorCollectionCapital() + Math.abs(principalBeen.getUnableMoneyTz())+"");
                            payBackMoney.setText(principalBeen.getAbleMoney() + "");

                            String hongbao_countS="<font color='#FF0000'>"+principalBeen.getHbNum()+"</font>"+"个可用红包";
                            String award_countS="<font color='#FF0000'>"+principalBeen.getJljlNum()+"</font>"+"条新记录";
                            String invest_countS="<font color='#FF0000'>"+principalBeen.getTzNum()+"</font>"+"条在投记录";
                            String invest_money_countS="<font color='#FF0000'>"+principalBeen.getZjjlNum()+"</font>"+"条新记录";


                            int dhkNum = Utils.str2Int(principalBeen.getDhkjlNum());
                            String back_money_countS="<font color='#FF0000'>"+dhkNum+"</font>"+"条待回款记录";


                            hongbao_count.setText(Html.fromHtml(hongbao_countS));
                            award_count.setText(Html.fromHtml(award_countS));
                            invest_count.setText(Html.fromHtml(invest_countS));
                            invest_money_count.setText(Html.fromHtml(invest_money_countS));
                            back_money_count.setText(Html.fromHtml(back_money_countS));
                        } else {
                            Toast.makeText(activity, principalBeen.getRmg(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    class LoginContentBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String quickly = intent.getStringExtra("Quickly");
            if (!TextUtils.isEmpty(quickly) && quickly.equals("Quickly")) {
                String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
                RequestForUserCenter(url);
                return;
            } else if (!TextUtils.isEmpty(quickly) && quickly.equals("cash")) {
                RequestForMoney();
            }
            RequestForHbPoint();

        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        activity.unregisterReceiver(broadCast);
    }
}
