package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Activities.ForgetLoginPassActivity;
import cn.ysgroup.ysdai.Activities.GestureCodeActivity;
import cn.ysgroup.ysdai.Activities.MainActivity;
import cn.ysgroup.ysdai.Beans.login.LoginBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.URLEncoder;


/**
 * Created by linyujie on 16/10/21.
 */

public class LoginFragment1 extends BaseFragment implements View.OnClickListener {

    private View rootView;
    private Button loginActionButton;
    private EditText userNameText;
    private TextView registerUserText;
    private TextView forgetPassText;
    private LoadingDialog loaginDialog;
    private SharedPreferences preferences;
    private EditText userPassText;//用户密码
    private String TAG = "Fragment1:";
    private Activity activity;
    private String from;

    public LoginFragment1(){

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public LoginFragment1(Activity activity) {
        this.activity = activity;
        rootView = View.inflate(activity, R.layout.login_fragment, null);
        initView();
    }


    private void initView() {
        loginActionButton = (Button) rootView.findViewById(R.id.login_login_button);
        userNameText = (EditText) rootView.findViewById(R.id.login_username);
//        registerUserText = (TextView) rootView.findViewById(R.id.login_register);
        forgetPassText = (TextView) rootView.findViewById(R.id.login_forget_pass);
        userPassText = (EditText) rootView.findViewById(R.id.login_password);
        loginActionButton.setOnClickListener(this);
        forgetPassText.setOnClickListener(this);
    }

    @Override
    public View getmView() {
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_login_button:
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/login";

                String username = userNameText.getText().toString();
                String password = userPassText.getText().toString();

                ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(userPassText.getWindowToken(), 0);


                if (username != null && !username.equals("") && password != null && !password.equals("")) {
                    if (loaginDialog == null) {

                        loaginDialog = new LoadingDialog(activity);
                        loaginDialog.setMessage("正在登录");
                        loaginDialog.show();
                    }
                    RequestForLogin(basicUrl);
                } else {
                    Toast.makeText(activity, "请输入账号和密码", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.login_forget_pass:
                Intent intent = new Intent();
                intent.setClass(activity, ForgetLoginPassActivity.class);
                activity.startActivity(intent);
                break;
        }
    }


    public void RequestForLogin(String basicUrl) {
        from = getFrom();
        String username = userNameText.getText().toString();
        String password = userPassText.getText().toString();
        String sPassword=URLEncoder.encode(password);
        preferences = activity.getSharedPreferences("AppToken", activity.MODE_PRIVATE);
        String deviceToken = preferences.getString("deviceToken", "");
        String url = basicUrl + "?" + "username=" + username + "&password=" + sPassword + "&deviceToken=" + deviceToken;
        Log.i(TAG, "" + url);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
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
                String s=response.body().string();
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
                            activity.getSharedPreferences("UserId",Context.MODE_PRIVATE).
                                    edit().putString("UserId",userNameText.getText().toString())
                                    .commit();

                            if(from!=null && from.equals("gesture")){
                                //清除手势密码
                                activity.getSharedPreferences(userNameText.getText().toString(), Context.MODE_PRIVATE).edit()
                                        .clear().commit();
                                Intent intentMain = new Intent(activity, MainActivity.class);
                                Intent intent2 = new Intent("GesturebroadCast");
                                activity.startActivity(intentMain);
                                GestureCodeActivity.from=null;
                                intent2.putExtra("value", "finish");
                                activity.sendBroadcast(intent2);
                            }else{
                                activity.sendBroadcast(new Intent("MoreBroadCast"));
                                Intent intent1 = new Intent("HomeFragmentBroadCast");
                                intent1.putExtra("Login", 101);
                                activity.sendBroadcast(intent1);
                            }
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

}
