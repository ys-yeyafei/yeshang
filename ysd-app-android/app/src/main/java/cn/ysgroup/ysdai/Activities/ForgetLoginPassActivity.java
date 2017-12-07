package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
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
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetLoginPassActivity extends MyBaseActivity {

    @Bind(R.id.forget_login_pass_verify_phone)
    EditText forgetLoginPassVerifyPhone;
    @Bind(R.id.forget_login_pass_verify_code)
    EditText forgetLoginPassVerifyCode;
    @Bind(R.id.forget_login_pass_verify_request_button)
    Button forgetLoginPassVerifyRequestButton;
    @Bind(R.id.forget_login_pass_verify_action_button)
    Button forgetLoginPassVerifyActionButton;
    @Bind(R.id.request_verify_code_toolbar_back)
    IconFontTextView requestVerifyCodeToolbarBack;
    @Bind(R.id.request_verify_code_toolbar_title)
    TextView requestVerifyCodeToolbarTitle;
    @Bind(R.id.request_verify_code_toolbar)
    Toolbar requestVerifyCodeToolbar;
    private MessageDialog myMessagedialog;
    private CountDownTimer countDownTimer;
    private String TAG = "忘记登录密码";
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Toast.makeText(ForgetLoginPassActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
            //退出登入
            Intent intent = new Intent("MoreBroadCast");
            intent.putExtra("isVisible", false);
            sendBroadcast(intent);
            finish();
        }
    };
    private EditText password;
    private EditText password1;
    private String sessionid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_verify_code);
        ButterKnife.bind(this);
        password = (EditText) findViewById(R.id.forget_login_password);
        password1 = (EditText) findViewById(R.id.forget_login_password1);
        initToolBar();
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {

            @Override
            public void onTick(long millisUntilFinished) {
                forgetLoginPassVerifyRequestButton.setEnabled(false);
                forgetLoginPassVerifyRequestButton.setText((millisUntilFinished / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                forgetLoginPassVerifyRequestButton.setEnabled(true);
                forgetLoginPassVerifyRequestButton.setText("重新发送");
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

    @OnClick(R.id.forget_login_pass_verify_request_button)
    public void OnRequestCodeButton() {

        String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCodeb";
        String phoneNumberString = forgetLoginPassVerifyPhone.getText().toString();
        if (isMobileNO(phoneNumberString)) {
            RequestForVerifyCode(basicUrl, phoneNumberString);
        } else {
            Toast.makeText(ForgetLoginPassActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.forget_login_pass_verify_action_button)
    public void OnActionButton() {

        String basicUrl = AppConstants.URL_SUFFIX + "/rest/userLoginPasswordUpdate";
        String phoneNumberString = forgetLoginPassVerifyPhone.getText().toString();
        String verifyCode = forgetLoginPassVerifyCode.getText().toString();
        String passwordString = password.getText().toString();
        String passwordString1 = password1.getText().toString();

        if (phoneNumberString == null || phoneNumberString.trim().equals("")) {
            Toast.makeText(ForgetLoginPassActivity.this, "请填写手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isMobileNO(phoneNumberString)) {
            Toast.makeText(ForgetLoginPassActivity.this, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
            return;
        }

        if (verifyCode == null || verifyCode.equals("")) {
            Toast.makeText(ForgetLoginPassActivity.this, "请填写验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkFormData(passwordString, passwordString1)) {
            return;
        }

        try {
            RequestForNewPassWord(basicUrl, phoneNumberString, verifyCode, passwordString, passwordString1);
        } catch (Exception E) {
            Log.e("ForgetLoginPassActivity",E.toString());
        }
    }

    public void initToolBar() {
        requestVerifyCodeToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(requestVerifyCodeToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /**
     * 请求获取验证码
     *
     * @param basicUrl
     */
    public void RequestForVerifyCode(String basicUrl, String phoneNoString) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("phoneReg", phoneNoString).build();
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
                String cookieval = response.header("Set-Cookie");
                if (cookieval != null) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    String s = response.body().string();
                    Log.i(TAG, s);
                    final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultBack.getRcd().equals("R0001")) {
                                //成功
                                countDownTimer.start();//开始倒计时
                            } else {
                                Toast.makeText(ForgetLoginPassActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });
    }

    public void RequestForNewPassWord(String basicUrl, String phoneNoString, String verifyCode, String password, String password1) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("phone", phoneNoString).
                add("codeReg", verifyCode).
                add("newPassword", password).
                add("againPassword", password1).
                build();
        Request request = new Request.Builder().url(basicUrl).header("cookie", sessionid).post(body).build();
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
                final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (resultBack.getRcd().equals("R0001")) {
                            //成功
                            Message message = new Message();
                            message.what = 0x110;
                            mHandler.sendMessage(message);

                        } else {
                            Toast.makeText(ForgetLoginPassActivity.this, resultBack.getRmg(), Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });
    }

    /**
     * 判断是否是电话号码
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        return mobiles.length() > 0;
    }

    public boolean checkFormData(String newSafe, String onewSafetwice) {

        if (newSafe == null || newSafe.trim().equals("") || onewSafetwice == null || onewSafetwice.trim().equals("")) {
            Toast.makeText(ForgetLoginPassActivity.this, "请输入新密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (isNumeric(newSafe)) {
            Toast.makeText(ForgetLoginPassActivity.this, "密码必须包含字母", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isStringLengthIngnel(newSafe)) {
            Toast.makeText(ForgetLoginPassActivity.this, "密码长度至少为6个字符", Toast.LENGTH_LONG).show();
            return false;
        }
        if (onewSafetwice == null || onewSafetwice.trim().equals("")) {
            Toast.makeText(ForgetLoginPassActivity.this, "请重复新密码！", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!newSafe.equals(onewSafetwice)) {
            Toast.makeText(ForgetLoginPassActivity.this, "两次新密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /**
     * 判断是否为纯数字字符串
     *
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 判断字符串是否是8-16位之间
     *
     * @return
     */
    public static boolean isStringLengthIngnel(String str) {
        if (str.length() >= 6) {
            return true;
        }
        return false;
    }

}
