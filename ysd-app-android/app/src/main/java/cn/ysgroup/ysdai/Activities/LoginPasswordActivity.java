package cn.ysgroup.ysdai.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Beans.login.LoginBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.Utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.net.URLEncoder;

public class LoginPasswordActivity extends MyBaseActivity implements View.OnClickListener {

    private EditText password;
    private IconFontTextView back;
    private Button next;
    private ImageView open;
    private String number;
    private boolean isClose = true;
    private TextView forget;
    private LoadingDialog loaginDialog;
    private String TAG="登录";
    private SharedPreferences preferences;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_password);
        initView();
        initListener();
    }

    private void initListener() {
        password.setOnClickListener(this);
        back.setOnClickListener(this);
//        close.setOnClickListener(this);
        next.setOnClickListener(this);
        open.setOnClickListener(this);
        forget.setOnClickListener(this);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 7) {
                    next.setEnabled(true);
                    next.setBackgroundResource(R.drawable.button_background_normal);
                } else {
                    next.setEnabled(false);
                    next.setBackgroundResource(R.drawable.button_background_pink);
                }
            }
        });
    }


    private void initView() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        number = intent.getStringExtra("phone_number");
        password = (EditText) findViewById(R.id.login_password);
        back = (IconFontTextView) findViewById(R.id.login_password_back);
//        close = (ImageView) findViewById(R.id.login_password_close);
        next = (Button) findViewById(R.id.login_password_next);
        open = (ImageView) findViewById(R.id.login_password_open);
        forget = (TextView) findViewById(R.id.login_password_forget);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_password_back:
                finish();
                break;
            case R.id.login_password_forget:
                Intent intent = new Intent();
                intent.setClass(this, ForgetLoginPassActivity.class);
                startActivity(intent);
                break;
            case R.id.login_password_next:
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/login";

                String passwordS = password.getText().toString();

                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(password.getWindowToken(), 0);


                if (loaginDialog == null) {

                    loaginDialog = new LoadingDialog(this);
                    loaginDialog.setMessage("正在登录");
                    loaginDialog.show();
                }
                RequestForLogin(basicUrl);
                break;
            case R.id.login_password_open:
                if (isClose) {
                    isClose = false;
                    open.setBackgroundResource(R.mipmap.login_open);
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                } else {
                    isClose = true;
                    open.setBackgroundResource(R.mipmap.login_close);
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    password.setSelection(password.getText().toString().length());
                }
                break;
        }
    }


    public void RequestForLogin(String basicUrl) {
        String passwordS = password.getText().toString();
        String sPassword= URLEncoder.encode(passwordS);
        preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String deviceToken = preferences.getString("deviceToken", "");
        String url = basicUrl + "?" + "username=" + number + "&password=" + sPassword + "&deviceToken=" + deviceToken;
        Log.i(TAG, "" + url);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginPasswordActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
                String s=response.body().string();
                Log.i(TAG, s);
                final LoginBean resultBack = new Gson().fromJson(s, LoginBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (loaginDialog != null && loaginDialog.isShowing()) {
                            loaginDialog.dismiss();
                            loaginDialog = null;
                        }
                        if (resultBack.getRcd().equals("R0001")) {  //登录成功
                            //登录成功,设置极光推送需要用到的别名和tag,别名用于推送单用户信息，tag用于推送全平台信息
                            Utils.setJPushInterface(String.valueOf(resultBack.getUserId()), AppConstants.JPUSH_TAG_ALL );
                            SharedPreferences.Editor editor = preferences.edit();
                            //存入数据
                            editor.putString("loginToken", resultBack.getToken());
                            //提交修改
                            editor.commit();
                            LoginPasswordActivity.this.getSharedPreferences("UserId",Context.MODE_PRIVATE).
                                    edit().putString("UserId",number)
                                    .commit();

                            if(from!=null && from.equals("gesture")){
                                //清除手势密码
                                LoginPasswordActivity.this.getSharedPreferences(number, Context.MODE_PRIVATE).edit()
                                        .clear().commit();
                                Intent intentMain = new Intent(LoginPasswordActivity.this, MainActivity.class);
                                Intent intent2 = new Intent("GesturebroadCast");
                                LoginPasswordActivity.this.startActivity(intentMain);
                                GestureCodeActivity.from=null;
                                intent2.putExtra("value", "finish");
                                LoginPasswordActivity.this.sendBroadcast(intent2);
                            }else{
                                LoginPasswordActivity.this.sendBroadcast(new Intent("MoreBroadCast"));
                                Intent intent1 = new Intent("HomeFragmentBroadCast");
                                intent1.putExtra("Login", 101);
                                LoginPasswordActivity.this.sendBroadcast(intent1);
                                setResult(100,getIntent());
                            }
                            LoginPasswordActivity.this.finish();


                        } else if (resultBack.getRcd().equals("M0001")) {
                            //用户名不存在或密码错误
                            Toast.makeText(LoginPasswordActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();

                        } else if (resultBack.getRcd().equals("M0002")) {
                            //您的账号已被禁用,无法登录，如有疑问请联系客服人员
                            Toast.makeText(LoginPasswordActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                        } else if (resultBack.getRcd().equals("M0003")) {
                            //您的账号已被锁定，如有疑问请联系客服人员

                            Toast.makeText(LoginPasswordActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();


                        } else if (resultBack.getRcd().equals("M0004")) {
                            //若连续N次密码输入错误,您的账号将被锁定!

                            Toast.makeText(LoginPasswordActivity.this,resultBack.getRmg(), Toast.LENGTH_SHORT).show();


                        }
                    }
                });
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

}
