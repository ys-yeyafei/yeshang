package cn.ysgroup.ysdai.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import cn.ysgroup.ysdai.Activities.QuicklyPlayActivity;
import cn.ysgroup.ysdai.Beans.bank.RbOrderBean;
import cn.ysgroup.ysdai.Beans.user.UserRechargeBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;

/**
 * Created by linyujie on 16/7/18.
 */
public class TopUpDialog extends Dialog {
    private Context context;
    private ImageView iv;
    private Button button;
    private TextView tv;
    private String userId;
    private TextView top_up;
    private TextView cancel;
    private TextView money;
    private String TopUpMoney;
    private String savePass;
    private String TAG = "自动充值";
    private String realName;
    private String cardNo;
    private String cardId;
    private Activity activity;
    private String tenderMoney;
    private String safePass;
    private String project;
    private String selectedIdArrayString;
    private int projectId;
    String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
    String url = AppConstants.URL_SUFFIX + "/rest/rechargeSave";
    public final static int REQUEST_CODE_BAOFOO_SDK = 100;
    private MessageDialog messageDialog;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x111://认证提交
                    String url = AppConstants.URL_SUFFIX + "/rest/rechargeSaveHnew";
                    RequestForRechargeData(url, TopUpMoney, savePass);
                    break;
                case 0x222:


                    break;
                default:
                    break;

            }
        }
    };
    private LoadingDialog loadingDialog;
    private TextView msg;

    public TopUpDialog(Context context) {
        super(context, 0);
    }

    public TopUpDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initData();
        initListenet();
    }

    private void initListenet() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //点击充值
        top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果快速点击了两次
                if (ClickEvent.isFastDoubleClick(R.id.charge_action_submit)) {
                    return;
                }
                dismiss();
                //调用接口，返回订单信息
                RequestForRechargeTo(chargeUrl);
            }
        });

    }


    private void initView() {
        View view = View.inflate(context, R.layout.top_up_dialog, null);
        top_up = (TextView) view.findViewById(R.id.top_up);
        cancel = (TextView) view.findViewById(R.id.top_up_cancel);
        money = (TextView) view.findViewById(R.id.top_up_money);
        msg = (TextView) view.findViewById(R.id.top_up_msg);
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();


        setContentView(view);

    }

    private void initData() {

    }

    public void RequestForRechargeData(String basicUrl, String chargeMoney, String safePwd) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(activity);
            loadingDialog.show();
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
                add("userId", userId).
                add("idNo", cardId).
                add("realName", realName).
                add("cardNo", cardNo).
                add("safepwd", safePwd).
                add("userAccountRecharge.money", chargeMoney).
                build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null || e.getMessage() != null) {
                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                String s=response.body().string();
                Log.i(TAG, "（一）:" + s);
                final RbOrderBean resultBean = new Gson().fromJson(s, RbOrderBean.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            Intent intent = new Intent(activity, QuicklyPlayActivity.class);
                            intent.putExtra("order", resultBean.getOrder_no());
                            intent.putExtra("inverstMark","inverstMark");
                            intent.putExtra("tenderMoney",tenderMoney);
                            intent.putExtra("safePass",safePass);
                            intent.putExtra("selectedIdArrayString",selectedIdArrayString);
                            intent.putExtra("project",project);
                            intent.putExtra("projectId",projectId);
                            activity.startActivity(intent);
                        } else {
                            Toast.makeText(activity, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 获取银行卡信息
     *
     * @param url
     */
    public void RequestForRechargeTo(String url) {
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
                        Toast.makeText(context, "银行卡信息查询失败", Toast.LENGTH_SHORT).show();
                        if (e != null &&e.getMessage() != null) {
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
                activity.runOnUiThread(new Runnable() {
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
                            Message msg = new Message();
                            msg.what = 0x111;
                            mHandler.sendMessage(msg);

                        } else if (recharge.getRcd().equals("E0001")) {
                            Toast.makeText(context, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                        }
//                else if (recharge.getRcd().equals("M00010")) {
//                    //没有绑定银行卡
//
//                }
                        else {
                            //请求失败
                            Toast.makeText(context, recharge.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void setData(String TopUpMoney, String savePass, Activity activity, String tenderMoney,
                        String safePass, String selectedIdArrayString,String project,int projectId) {
        this.activity = activity;
        this.project=project;
        this.projectId=projectId;
        this.savePass = savePass;
        this.tenderMoney=tenderMoney;
        this.safePass=safePass;
        this.selectedIdArrayString=selectedIdArrayString;
        if(Utils.str2Double(TopUpMoney)<3){
            this.TopUpMoney = 3+"";
            money.setVisibility(View.GONE);
            msg.setText("您的账户余额不足，还需充值3元（平台最小充值额为3元）");
        }else{
            money.setVisibility(View.VISIBLE);
            this.TopUpMoney = TopUpMoney;
            money.setText(TopUpMoney + "元");
        }
//        show();
        //直接默认点击充值
        RequestForRechargeTo(chargeUrl);
    }


}
