package cn.ysgroup.ysdai.Activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Beans.reguser.RegUserBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.Utils;
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

public class LoginActivity extends MyBaseActivity {

    private final String TAG = "用户登录";
    //    private LinearLayout layout;
//    private ImageView trigonometry1;
//    private ImageView trigonometry2;
//    private TextView login;
//    private TextView registerUserText;
    private static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
    private IconFontTextView back;
    private Intent intent;
    private String from;
    private EditText phone;
    private boolean isAnimator = true;
    private ObjectAnimator animator;
    private StringBuilder resultText;
    private ObjectAnimator animator1;
    private LinearLayout nextLL;
    private LoadingDialog loaginDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        intent = getIntent();
        from = intent.getStringExtra("from");
        back = (IconFontTextView) findViewById(R.id.login_regist_back);
        final Button next = (Button) findViewById(R.id.login_regist_next);
        phone = (EditText) findViewById(R.id.login_regist_phone);
        nextLL = (LinearLayout) findViewById(R.id.login_regist_ll);
        final TextView phone_display = (TextView) findViewById(R.id.login_regist_phone_display);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.finish();
            }
        });


        String UserId = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (animator == null)
                        animator = ObjectAnimator.ofFloat(nextLL, "translationY", 0, Utils.dip2px(LoginActivity.this, 50));


                    String orignText = s.toString();
                    resultText = new StringBuilder();
                    int count = 3;
                    while (orignText.length() > count) {
                        String fourCharText = orignText.substring(0, count);
                        resultText.append(fourCharText + "    ");
                        orignText = orignText.substring(count, orignText.length());
                        if (count < 4) {
                            count++;
                        }
                    }
                    resultText.append(orignText);
                    phone_display.setText(resultText.toString());
                    phone_display.setVisibility(View.VISIBLE);
                    next.setBackgroundResource(R.drawable.button_background_pink);
                    next.setEnabled(false);
                    //展开动画
                    if (isAnimator) {
                        animator.setDuration(200);
                        animator.setRepeatCount(0);
                        animator.start();
                        isAnimator = false;
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                if(animator1!=null)
                                    animator1.cancel();
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //phone_display.setVisibility(View.VISIBLE);
                                System.out.println("展开动画结束");
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                                System.out.println("展开动画取消");
                                isAnimator=true;
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                } else {
                    //执行收尾动画
                    phone_display.setText("");
                    if (animator1 == null)
                        animator1 = ObjectAnimator.ofFloat(nextLL, "translationY", Utils.dip2px(LoginActivity.this, 50), 0);
                    animator1.setDuration(200);
                    animator1.setRepeatCount(0);
                    animator1.start();
                    phone_display.setVisibility(View.GONE);
                    isAnimator=true;
                    animator1.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            System.out.println("收尾动画开始");
                            if(animator!=null)
                                animator.cancel();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            System.out.println("收尾动画结束");
                            //phone_display.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            System.out.println("收尾动画取消");
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                }
                if (s.toString().length() == 11) {
                    next.setBackgroundResource(R.drawable.button_background_normal);
                    next.setEnabled(true);
                }


            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMobile(phone.getText().toString())) {
                    String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCode";
                    String phoneNumberString = phone.getText().toString();
                    RequestForVerifyCode(basicUrl, phoneNumberString);
                } else {
                    Toast.makeText(LoginActivity.this, "手机格式错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void RequestForVerifyCode(String basicUrl, final String phoneNoString) {
        loaginDialog = new LoadingDialog(this);
        loaginDialog.setMessage("正在登录");
        loaginDialog.show();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("phoneReg", phoneNoString).
                build();
        final Request request = new Request.Builder().url(basicUrl).post(body).build();
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
                if (loaginDialog != null && loaginDialog.isShowing()) {
                    loaginDialog.dismiss();
                    loaginDialog = null;
                }
                final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String cookieval = response.header("Set-Cookie");
                        if (resultBack.getRcd().equals("M0008_7")) {
                            //进入登入密码页面
                            Intent intent = new Intent(LoginActivity.this, LoginPasswordActivity.class);
                            intent.putExtra("phone_number", phoneNoString);
                            intent.putExtra("from",from);
                            startActivityForResult(intent,200);
                        }else if (resultBack.getRcd().equals("R0001")) {
                            //进入注册页面
                            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                            intent.putExtra("phone_number", phoneNoString);
                            intent.putExtra("from",from);
                            intent.putExtra("sessionid",cookieval.substring(0, cookieval.indexOf(";")));
                            startActivityForResult(intent,200);
                        } else {
                            Toast.makeText(LoginActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==100){
            finish();
        }else  if(resultCode==10086){
            setResult(10086,data);
            finish();
        }
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
