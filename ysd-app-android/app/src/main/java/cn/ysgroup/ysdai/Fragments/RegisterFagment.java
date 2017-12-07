package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Activities.RegisterNegotiateActivity;
import cn.ysgroup.ysdai.Beans.login.LoginBean;
import cn.ysgroup.ysdai.Beans.reguser.RegUserBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.AppVersionUtils;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by linyujie on 16/10/21.
 */

public class RegisterFagment extends BaseFragment {
    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    private CountDownTimer countDownTimer;
    private MessageDialog myMessagedialog;
    private Button registerButton;//注册按钮
    private Button requestForVerifycode;//获取验证码
    private EditText phoneNumber;//手机号码
    private EditText verifyCode;//验证码
    private EditText pass;//密码
    private EditText passTwice;//再次输入的密码
    private View view;
    private Activity activity;
    private String TAG = "RegisterFagment";
    private LoadingDialog loaginDialog;
    private String phoneNumberString;
    private String passWordString;
    private SharedPreferences preferences;
    private CheckBox checkBox;
    private TextView negotiate;
    private String sessionid;

    public RegisterFagment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public RegisterFagment(Activity activity) {
        this.activity = activity;
        view = View.inflate(activity, R.layout.register_fragment, null);
        initViews();
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {

            @Override
            public void onTick(long millisUntilFinished) {
                requestForVerifycode.setEnabled(false);
                requestForVerifycode.setText((millisUntilFinished / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                requestForVerifycode.setEnabled(true);
                requestForVerifycode.setText("重新发送");
            }
        };
    }


    @Override
    public View getmView() {
        return view;
    }

    public void initViews() {
        registerButton = (Button) view.findViewById(R.id.register_button);
        requestForVerifycode = (Button) view.findViewById(R.id.register_request_verify_button);
//        loginAction =(TextView)findViewById(R.id.register_to_login);
        phoneNumber = (EditText) view.findViewById(R.id.register_phone_number);
        verifyCode = (EditText) view.findViewById(R.id.register_verify_number);
        checkBox = (CheckBox) view.findViewById(R.id.regiest_check);
        negotiate = (TextView) view.findViewById(R.id.regiest_negotiate);
        pass = (EditText) view.findViewById(R.id.register_password);
        passTwice = (EditText) view.findViewById(R.id.register_password_twice);

        negotiate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, RegisterNegotiateActivity.class));
            }
        });

        registerButton.setOnClickListener(
                new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        if (checkBox.isChecked()) {
                            String basicUrl = AppConstants.URL_SUFFIX + "/rest/reg";
                            phoneNumberString = phoneNumber.getText().toString();
                            passWordString = pass.getText().toString();
                            String verifyCodeString = verifyCode.getText().toString();
                            if (checkForm()) {
                                RequestForRegister(basicUrl, phoneNumberString, passWordString, verifyCodeString);
                            }
                        } else {
                            Toast.makeText(activity, "请阅读服务协议", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        requestForVerifycode.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!ClickEvent.isFastDoubleClick()) {
                            String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCode";
                            String phoneNumberString = phoneNumber.getText().toString();
                            if (!isMobile(phoneNumber.getText().toString())) {
                                Toast.makeText(activity, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                            } else if (isMobileNO(phoneNumberString)) {
                                RequestForVerifyCode(basicUrl, phoneNumberString);
                            } else {
                                Toast.makeText(activity, "手机号码格式不正确", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }


    public boolean checkForm() {
        String phoneNumberString = phoneNumber.getText().toString();
        String passWordString = pass.getText().toString();
        String twicePassWordString = passTwice.getText().toString();
        String verifyCodeString = verifyCode.getText().toString();
        if (phoneNumberString == null || phoneNumberString.trim().equals("")) {
            Toast.makeText(activity, "请输入电话号码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isMobile(phoneNumber.getText().toString())) {
            Toast.makeText(activity, "手机号码格式不正确", Toast.LENGTH_LONG).show();
            return false;
        }
        if (verifyCodeString == null || verifyCodeString.trim().equals("")) {
            Toast.makeText(activity, "请输入验证码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (passWordString == null || passWordString.trim().equals("") || twicePassWordString == null || twicePassWordString.trim().equals("")) {
            Toast.makeText(activity, "请输入登录密码", Toast.LENGTH_LONG).show();
            return false;
        } else if (passWordString.contains(" ")) {
            Toast.makeText(activity, "登录密码包含了空格", Toast.LENGTH_LONG).show();
            return false;
        } else if (isNumeric(passWordString)) {
            Toast.makeText(activity, "登录密码必须包含字母", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isStringLengthIngnel(passWordString)) {
            Toast.makeText(activity, "登录密码长度至少8个字符", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!passWordString.equals(twicePassWordString)) {
            Toast.makeText(activity, "密码输入不一致！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

    /**
     * 请求注册
     *
     * @param basicUrl
     */
    public void RequestForRegister(String basicUrl, String phoneNoString, String passWord, String verifyCode) {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormEncodingBuilder().
                add("appType", "1").
                add("placeName", AppVersionUtils.getChannelName(activity, "UMENG_CHANNEL") + "").
                add("im", AppVersionUtils.getPhoneIMEI(activity)).
                add("deviceToken", PreferenceUtil.getPrefString(activity, "deviceToken", "")).
                add("user.password", passWord).
                add("user.phone", phoneNoString).
                add("codeReg", verifyCode).build();
        try {
            Request request = new Request.Builder().url(basicUrl).header("cookie", sessionid).post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(activity, "连接网络失败", Toast.LENGTH_LONG).show();
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
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultBack.getRcd().equals("R0001")) {
                                SharedPreferences preferences = activity.getSharedPreferences("AppToken", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                //存入数据
                                editor.putString("loginToken", resultBack.getToken());
                                //提交修改
                                editor.commit();

                                myMessagedialog = new MessageDialog(activity);
                                myMessagedialog.setMessage("注册成功！");
                                myMessagedialog.setCanceledOnTouchOutside(false);
                                myMessagedialog.setOnPositiveListener(
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                myMessagedialog.dismiss();
                                                //自动登入
                                                VoluntaryLogin();
                                            }
                                        }
                                );

                                myMessagedialog.show();

                            } else {
                                Toast.makeText(activity, "" + resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e("RegisterFagment",e.toString());
        }

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


    /**
     * 请求获取验证码
     *
     * @param basicUrl
     */
    public void RequestForVerifyCode(String basicUrl, String phoneNoString) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("phoneReg", phoneNoString).build();
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
                String cookieval = response.header("Set-Cookie");
                if (cookieval != null) {
                    sessionid = cookieval.substring(0, cookieval.indexOf(";"));
                    String s = response.body().string();
                    Log.i(TAG, s);
                    final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultBack.getRcd().equals("R0001")) {
                                //成功
                                countDownTimer.start();//开始倒计时

                            } else {
                                Toast.makeText(activity, "" + resultBack.getRmg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });


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
     * 判断字符串是否是6位之间
     *
     * @return
     */
    public static boolean isStringLengthIngnel(String str) {
        if (str.length() >= 8) {
            return true;
        }
        return false;
    }

    //自动登入
    private void VoluntaryLogin() {
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/login";


        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(pass.getWindowToken(), 0);


        if (loaginDialog == null) {

            loaginDialog = new LoadingDialog(activity);
            loaginDialog.setMessage("正在登录");
            loaginDialog.show();
        }
        RequestForLogin(basicUrl);
    }

    public void RequestForLogin(String basicUrl) {

        preferences = activity.getSharedPreferences("AppToken", activity.MODE_PRIVATE);
        String deviceToken = preferences.getString("deviceToken", "");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("username", phoneNumberString).
                add("password", passWordString).
                add("deviceToken", deviceToken).build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "获取数据失败", Toast.LENGTH_SHORT).show();
                        if (loaginDialog != null && loaginDialog.isShowing()) {
                            loaginDialog.dismiss();
                            loaginDialog = null;
                        }
                        if (e == null || e.getMessage() == null) {
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
                String s = response.body().string();
                Log.i(TAG, s);
                final LoginBean resultBack = new Gson().fromJson(s, LoginBean.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (loaginDialog != null && loaginDialog.isShowing()) {
                            loaginDialog.dismiss();
                            loaginDialog = null;
                        }
                        if (resultBack.getRcd().equals("R0001")) {

                            SharedPreferences.Editor editor = preferences.edit();
                            //存入数据
                            editor.putString("loginToken", resultBack.getToken());
                            //提交修改
                            editor.commit();

                            //登录成功
//                    FragmenToActivityMessage msg = new FragmenToActivityMessage();
//                    msg.setTag(1001);
//                    EventBus.getDefault().post(msg);
                            activity.sendBroadcast(new Intent("MoreBroadCast"));
                            Intent intent = new Intent("HomeFragmentBroadCast");
                            intent.putExtra("LoginSuccess", 102);
                            intent.putExtra("id", resultBack.getUsername());
                            activity.sendBroadcast(intent);
                            activity.setResult(10086);
                            activity.finish();

                        } else if (resultBack.getRcd().equals("M0001")) {
                            //用户名不存在或密码错误
                            Toast.makeText(activity, "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();

                        } else if (resultBack.getRcd().equals("M0002")) {
                            //您的账号已被禁用,无法登录，如有疑问请联系客服人员
                            Toast.makeText(activity, "您的账号已被禁用,无法登录", Toast.LENGTH_SHORT).show();
                        } else if (resultBack.getRcd().equals("M0003")) {
                            //您的账号已被锁定，如有疑问请联系客服人员

                            Toast.makeText(activity, "您的账号将被锁定!", Toast.LENGTH_SHORT).show();


                        } else if (resultBack.getRcd().equals("M0004")) {
                            //若连续N次密码输入错误,您的账号将被锁定!

                            Toast.makeText(activity, "连续N次密码输入错误,您的账号将被锁定!", Toast.LENGTH_SHORT).show();


                        }
                    }
                });
            }
        });

    }

    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }
}
