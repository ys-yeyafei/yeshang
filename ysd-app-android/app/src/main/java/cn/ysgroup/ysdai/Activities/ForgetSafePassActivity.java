package cn.ysgroup.ysdai.Activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.reguser.RegUserBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetSafePassActivity extends MyBaseActivity {


    @Bind(R.id.forget_safe_pass_toolbar_back)
    IconFontTextView forgetSafePassToolbarBack;
    @Bind(R.id.forget_safe_pass_toolbar_title)
    TextView forgetSafePassToolbarTitle;
    @Bind(R.id.forget_safe_pass_toolbar)
    Toolbar forgetSafePassToolbar;
    @Bind(R.id.forget_safe_pass_id_number)
    EditText forgetSafePassIdNumber;
    //    @Bind(R.id.forget_safe_pass_current_phone)
//    TextView forgetSafePassCurrentPhone;
    @Bind(R.id.forget_safe_pass_verify_button)
    Button forgetSafePassVerifyButton;
    @Bind(R.id.forget_safe_pass_verify_code)
    EditText forgetSafePassVerifyCode;
    @Bind(R.id.forget_safe_pass_new_pass)
    EditText forgetSafePassNewPass;
    @Bind(R.id.forget_safe_pass_action_button)
    Button forgetSafePassActionButton;
    private String TAG = "忘记交易密码";
    private String currentPhoneNo;//当前手机号
    private CountDownTimer countDownTimer;
    private String sessionid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_safe_pass);
        currentPhoneNo = getIntent().getStringExtra("currentPhoneNo");

        ButterKnife.bind(this);
        initToolBar();

//        forgetSafePassCurrentPhone.setText(currentPhoneNo);
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {
            @Override
            public void onTick(long millisUntilFinished) {
                forgetSafePassVerifyButton.setEnabled(false);
                forgetSafePassVerifyButton.setText((millisUntilFinished / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                forgetSafePassVerifyButton.setEnabled(true);
                forgetSafePassVerifyButton.setText("重新发送");
            }
        };
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.forget_safe_pass_verify_button)
    public void OnRequestCodeButton() {

        String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCodeUser";
        RequestForVerifyCode(basicUrl);

    }

    @OnClick(R.id.forget_safe_pass_action_button)
    public void OnActionButton() {

        String basicUrl = AppConstants.URL_SUFFIX + "/rest/lostSafePwd";
        String idNumber = forgetSafePassIdNumber.getText().toString();
        String verifyCode = forgetSafePassVerifyCode.getText().toString();
        String newPass = forgetSafePassNewPass.getText().toString();

        if (idNumber == null || idNumber.trim().equals("")) {
            Toast.makeText(ForgetSafePassActivity.this, "请填写身份证后8位", Toast.LENGTH_SHORT).show();
            return;
        }

        if (verifyCode == null || verifyCode.trim().equals("")) {
            Toast.makeText(ForgetSafePassActivity.this, "请填写手机验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass == null || newPass.equals("")) {
            Toast.makeText(ForgetSafePassActivity.this, "请填写新的交易密码", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestToSetNewSafePass(basicUrl, idNumber, newPass, verifyCode);


    }

    public void initToolBar() {
        forgetSafePassToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(forgetSafePassToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /**
     * 请求获取验证码
     *
     * @param basicUrl
     */
    public void RequestForVerifyCode(String basicUrl) {
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
                        if (e == null || e.getMessage() == null) {
                            Toast.makeText(ForgetSafePassActivity.this, "访问服务器出错！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (e != null && e.getMessage() != null) {
                                Log.i(TAG, e.getMessage());
                            }
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String cookieval = response.header("Set-Cookie");
                sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                String s=response.body().string();
                Log.i(TAG, s);
                final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (resultBack.getRcd().equals("R0001")) {
                            //成功
                            countDownTimer.start();//开始倒计时
                        } else {
                            Toast.makeText(ForgetSafePassActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    public void RequestToSetNewSafePass(String basicUrl, String idNumber, String loginPass, String verifyCode) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("newPassword",loginPass).
                add("codeReq",verifyCode).
                add("cardId",idNumber).
                build();
        Request request = new Request.Builder().url(basicUrl).header("cookie",sessionid).post(body).build();
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
                String s=response.body().string();
                Log.i(TAG, s);
                final BaseBean resultBack = new Gson().fromJson(s, BaseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (resultBack.getRcd().equals("R0001")) {

                            final MessageDialog messageDialog = new MessageDialog(ForgetSafePassActivity.this);
                            messageDialog.setMessage("交易密码已经重置成功");
                            messageDialog.setCanceledOnTouchOutside(false);
                            messageDialog.setOnPositiveListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            messageDialog.dismiss();
                                            finish();
                                        }
                                    }
                            );
                            messageDialog.show();

                        } else {
                            Toast.makeText(ForgetSafePassActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }



}
