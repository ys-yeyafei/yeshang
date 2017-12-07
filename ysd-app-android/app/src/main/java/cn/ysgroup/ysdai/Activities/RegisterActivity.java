package cn.ysgroup.ysdai.Activities;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.regex.Pattern;

import cn.ysgroup.ysdai.Beans.login.LoginBean;
import cn.ysgroup.ysdai.Beans.reguser.RegUserBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.AppVersionUtils;
import cn.ysgroup.ysdai.Util.ClickEvent;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;


public class RegisterActivity extends MyBaseActivity implements View.OnClickListener {


    private TextView idT;
    private Button register;
    private Button code_button;
    private EditText codeE;
    private LinearLayout imagineL;
    private ImageView imagine_iv;
    private EditText imagine_number;
    private EditText password;
    private ImageView open;
    private CheckBox check;
    private String id;
    private String sessionid;
    private String TAG = "注册";
    private CountDownTimer countDownTimer;
    private boolean isClose = true;
    private RelativeLayout register_rl;
    private boolean isImagineClose = true;
    private ImageView arrowhead;
    private TextView negotiate;
    private MessageDialog myMessagedialog;
    private LoadingDialog loaginDialog;
    private SharedPreferences preferences;
    private IconFontTextView back;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            register.setEnabled(true);
        }
    };
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListener();
    }

    private void initViews() {
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        sessionid = intent.getStringExtra("sessionid");
        id = intent.getStringExtra("phone_number");
        idT = (TextView) findViewById(R.id.register_id);
        negotiate = (TextView) findViewById(R.id.regiest_negotiate);
        register = (Button) findViewById(R.id.register);
        code_button = (Button) findViewById(R.id.register_code_button);
        codeE = (EditText) findViewById(R.id.register_code);
        imagineL = (LinearLayout) findViewById(R.id.register_imagine);
        imagine_iv = (ImageView) findViewById(R.id.register_imagine_iv);
        imagine_number = (EditText) findViewById(R.id.register_imagine_number);
        password = (EditText) findViewById(R.id.register_password);
        open = (ImageView) findViewById(R.id.register_open);
        check = (CheckBox) findViewById(R.id.regiester_check);
        arrowhead = (ImageView) findViewById(R.id.register_arrowhead);
        register_rl = (RelativeLayout) findViewById(R.id.register_rl);
        back = (IconFontTextView) findViewById(R.id.register_back);

        idT.setText(Html.fromHtml("以向手机" + "<font color='#ef3e44'>" + id + "</font>" +
                "发送短信"));

        //倒计时
        countDownTimer = new CountDownTimer(1000 * AppConstants.VerifyCodeTimeFuture, 1000 * AppConstants.VerifyCodeTimeInteral) {

            @Override
            public void onTick(long millisUntilFinished) {
                code_button.setEnabled(false);
                code_button.setText((millisUntilFinished / 1000) + "秒");
            }

            @Override
            public void onFinish() {
                code_button.setEnabled(true);
                code_button.setText("重新发送");
            }
        };
        countDownTimer.start();
    }

    private void initListener() {
        code_button.setOnClickListener(this);
        open.setOnClickListener(this);
        imagineL.setOnClickListener(this);
        negotiate.setOnClickListener(this);
        imagine_iv.setOnClickListener(this);
        register.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_code_button:
                if (!ClickEvent.isFastDoubleClick()) {
                    String basicUrl = AppConstants.URL_SUFFIX + "/rest/sendPCode";
                    RequestForVerifyCode(basicUrl, id);
                }
                break;
            case R.id.register_open:
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

            case R.id.register_imagine:
                final Float Y = register_rl.getY();
                arrowhead.setBackgroundResource(R.mipmap.up_icon);
                if (isImagineClose) {
                    final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    ValueAnimator va = ValueAnimator.ofFloat(Y, Y + Utils.dip2px(this, 50));
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float height = (Float) animation.getAnimatedValue() - Y;
                            params.height = (int) height;
                            register_rl.setLayoutParams(params);
                        }
                    });
                    va.setDuration(300);
                    va.start();
                    isImagineClose = false;
                } else {
                    imagine_number.setText("");
                    arrowhead.setBackgroundResource(R.mipmap.drow_icon);
                    final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    ValueAnimator va = ValueAnimator.ofFloat(Y + Utils.dip2px(this, 50), Y);
                    va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float height = (Float) animation.getAnimatedValue() - Y;
                            params.height = (int) height;
                            register_rl.setLayoutParams(params);
                        }
                    });
                    va.setDuration(300);
                    va.start();
                    isImagineClose = true;
                }
                break;
            case R.id.regiest_negotiate:
                startActivity(new Intent(this, RegisterNegotiateActivity.class));
                break;

            case R.id.register_imagine_iv:
                if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                            Manifest.permission.READ_CONTACTS)) {
                        Toast.makeText(RegisterActivity.this, "再次获取该权限需去设置打开权限许可", Toast.LENGTH_SHORT).show();

                    } else {

                        ActivityCompat.requestPermissions(RegisterActivity.this,
                                new String[]{Manifest.permission.READ_CONTACTS},
                                1);
                    }
                } else {
                    Uri uri = ContactsContract.Contacts.CONTENT_URI;
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            uri);

                    startActivityForResult(intent, 0);
                }
                break;

            case R.id.register:
                if (check.isChecked()) {
                    String basicUrl = AppConstants.URL_SUFFIX + "/rest/reg";
                    String passWordString = password.getText().toString();
                    String verifyCodeString = codeE.getText().toString();
                    if (checkForm() && register.isEnabled() ) {
                        //防重复
                        register.setEnabled(false);
                        handler.sendEmptyMessageDelayed(0,3000);
                        RequestForRegister(basicUrl, id, passWordString, verifyCodeString);
                    }
                } else {
                    Toast.makeText(this, "请阅读服务协议", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.register_back:
                finish();
                break;
        }
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
                                Toast.makeText(RegisterActivity.this, "" + resultBack.getRmg(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                StringBuilder stringBuilder = new StringBuilder();
                for (int x = 0; x < contacts[1].length(); x++) {
                    if (isInteger(contacts[1].substring(x, x + 1))) {
                        stringBuilder.append(contacts[1].substring(x, x + 1));
                    }
                }
                imagine_number.setText(stringBuilder.toString());
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
//            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
//            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    public static boolean isInteger(String str) {

        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }


    public boolean checkForm() {
        String passWordString = password.getText().toString();
        String verifyCodeString = codeE.getText().toString();
        if (verifyCodeString == null || verifyCodeString.trim().equals("")) {
            Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
            return false;
        }
        if (passWordString == null || passWordString.trim().equals("")) {
            Toast.makeText(this, "请输入登录密码", Toast.LENGTH_LONG).show();
            return false;
        } else if (passWordString.contains(" ")) {
            Toast.makeText(this, "登录密码包含了空格", Toast.LENGTH_LONG).show();
            return false;
        } else if (isInteger(passWordString)) {
            Toast.makeText(this, "登录密码必须包含字母", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isStringLengthIngnel(passWordString)) {
            Toast.makeText(this, "登录密码长度至少8个字符", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

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


    /**
     * 请求注册
     *
     * @param basicUrl
     */
    public void RequestForRegister(String basicUrl, String phoneNoString, String passWord, String verifyCode) {
        OkHttpClient client = new OkHttpClient();
//        RequestBody body =
        FormEncodingBuilder formEncodingBuilder = new FormEncodingBuilder().
                add("appType", "1").
                add("placeName", AppVersionUtils.getChannelName(RegisterActivity.this, "UMENG_CHANNEL") + "").
                add("im", AppVersionUtils.getPhoneIMEI(RegisterActivity.this)).
                add("deviceToken", PreferenceUtil.getPrefString(RegisterActivity.this, "deviceToken", "")).
                add("user.password", passWord).
                add("user.phone", phoneNoString).
                add("codeReg", verifyCode);
        if (!TextUtils.isEmpty(imagine_number.getText().toString())) {
            if (imagine_number.getText().toString().length() < 11) {
                Toast.makeText(this, "请输入正确的推荐人号码", Toast.LENGTH_SHORT).show();
                return;
            } else {
                formEncodingBuilder.add("referrer", imagine_number.getText().toString());
            }
        }
        RequestBody body = formEncodingBuilder.build();


        try {
            Request request = new Request.Builder().url(basicUrl).header("cookie", sessionid).post(body).build();
            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(final Request request, final IOException e) {
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(RegisterActivity.this, "连接网络失败", Toast.LENGTH_LONG).show();
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
                    RegisterActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultBack.getRcd().equals("R0001")) {
                                SharedPreferences preferences = RegisterActivity.this.getSharedPreferences("AppToken", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                //存入数据
                                editor.putString("loginToken", resultBack.getToken());
                                //提交修改
                                editor.commit();

//                                myMessagedialog = new MessageDialog(RegisterActivity.this);
//                                myMessagedialog.setMessage("注册成功！");
//                                myMessagedialog.setCanceledOnTouchOutside(false);
//                                myMessagedialog.setCancelable(false);
//                                myMessagedialog.setOnPositiveListener(
//                                        new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                myMessagedialog.dismiss();
                                //自动登入
                                VoluntaryLogin();
//                                            }
//                                        }
//                                );

//                                myMessagedialog.show();

                            } else {
                                Toast.makeText(RegisterActivity.this, "" + resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e("RegisterFagment", e.toString());
        }

    }


    //自动登入
    private void VoluntaryLogin() {
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/login";


        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(password.getWindowToken(), 0);


        if (loaginDialog == null) {

            loaginDialog = new LoadingDialog(this);
            loaginDialog.setMessage("正在登录");
            loaginDialog.show();
        }
        RequestForLogin(basicUrl);
    }


    public void RequestForLogin(String basicUrl) {

        preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        String deviceToken = preferences.getString("deviceToken", "");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("username", id).
                add("password", password.getText().toString()).
                add("deviceToken", deviceToken).build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RegisterActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (loaginDialog != null && loaginDialog.isShowing()) {
                            loaginDialog.dismiss();
                            loaginDialog = null;
                        }
                        if (resultBack.getRcd().equals("R0001")) {      //登录成功

                            //登录成功,设置极光推送需要用到的别名和tag,别名用于推送单用户信息，tag用于推送全平台信息
                            Utils.setJPushInterface(String.valueOf(resultBack.getUserId()), AppConstants.JPUSH_TAG_ALL );
//                    FragmenToActivityMessage msg = new FragmenToActivityMessage();
//                    msg.setTag(1001);
//                    EventBus.getDefault().post(msg);
                            if(from!=null && from.equals("gesture")){
                                //清除手势密码
                                RegisterActivity.
                                        this.getSharedPreferences(RegisterActivity.this.getSharedPreferences("UserId",
                                        Context.MODE_PRIVATE).getString("UserId",null),
                                        Context.MODE_PRIVATE).edit()
                                        .clear().commit();
                                Intent intentMain = new Intent(RegisterActivity.this, MainActivity.class);
                                Intent intent2 = new Intent("GesturebroadCast");
                                RegisterActivity.this.startActivity(intentMain);
                                GestureCodeActivity.from=null;
                                intent2.putExtra("value", "finish");
                                RegisterActivity.this.sendBroadcast(intent2);
                            }else{
                                RegisterActivity.this.sendBroadcast(new Intent("MoreBroadCast"));
                                Intent intent = new Intent("HomeFragmentBroadCast");
                                intent.putExtra("LoginSuccess", 102);
                                intent.putExtra("id", resultBack.getUsername());
                                RegisterActivity.this.sendBroadcast(intent);
                                RegisterActivity.this.setResult(10086);
                                RegisterActivity.this.finish();
                            }

                            SharedPreferences.Editor editor = preferences.edit();
                            //存入数据
                            editor.putString("loginToken", resultBack.getToken());
                            //提交修改
                            editor.commit();
                            RegisterActivity.this.getSharedPreferences("UserId", Context.MODE_PRIVATE).
                                    edit().putString("UserId", id)
                                    .commit();

                        } else if (resultBack.getRcd().equals("M0001")) {
                            //用户名不存在或密码错误
                            Toast.makeText(RegisterActivity.this, "用户名不存在或密码错误", Toast.LENGTH_SHORT).show();

                        } else if (resultBack.getRcd().equals("M0002")) {
                            //您的账号已被禁用,无法登录，如有疑问请联系客服人员
                            Toast.makeText(RegisterActivity.this, "您的账号已被禁用,无法登录", Toast.LENGTH_SHORT).show();
                        } else if (resultBack.getRcd().equals("M0003")) {
                            //您的账号已被锁定，如有疑问请联系客服人员

                            Toast.makeText(RegisterActivity.this, "您的账号将被锁定!", Toast.LENGTH_SHORT).show();


                        } else if (resultBack.getRcd().equals("M0004")) {
                            //若连续N次密码输入错误,您的账号将被锁定!

                            Toast.makeText(RegisterActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();


                        }else{
                            Toast.makeText(RegisterActivity.this, resultBack.getRmg(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions[0].equals(Manifest.permission.READ_CONTACTS)
                &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Uri uri = ContactsContract.Contacts.CONTENT_URI;
            Intent intent = new Intent(Intent.ACTION_PICK,
                    uri);

            startActivityForResult(intent, 0);
        }else{

        }
    }

}
