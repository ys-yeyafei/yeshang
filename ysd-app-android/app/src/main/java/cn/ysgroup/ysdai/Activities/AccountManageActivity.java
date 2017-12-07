package cn.ysgroup.ysdai.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.userInfo.UserBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LMessageDialog;
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

public class AccountManageActivity extends MyBaseActivity {

    private String userId;
    private final String TAG = "账户管理";
    private PopupWindow popupWindow;
    private TableRow realNameRow;
    private TableRow bankCardRow;
    private TableRow loginPassRow;
    private TableRow safePassRow;
    private TableRow gestureRow;

    private TextView realNameText;
    private ImageView realNameRag;

    //    private Button exitButton;
    private String gesture;
    private ImageView slide_button;
    private boolean isCheck = false;
    private String code;
    private int CLOSECODE=200;
    private RelativeLayout modify_code;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);
        userId = getIntent().getStringExtra("userId");
        initToolBar();
        initViews();
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";

        RequestForListData(basicUrl, userId);


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
        Toolbar toolbar = (Toolbar) findViewById(R.id.account_manage_toolbar);
        IconFontTextView toolBarBack = (IconFontTextView) findViewById(R.id.account_manage_toolbar_back);
        toolBarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    public void initViews() {
        phone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        if(phone==null)
            phone="0";
        code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                getString(phone, null);
        realNameRow = (TableRow) findViewById(R.id.account_manage_table_row_real_name);
        bankCardRow = (TableRow) findViewById(R.id.account_manage_table_row_bank_card);
        loginPassRow = (TableRow) findViewById(R.id.account_manage_table_row_set_login);
        safePassRow = (TableRow) findViewById(R.id.account_manage_table_row_set_safe);
        gestureRow = (TableRow) findViewById(R.id.account_manage_table_row_modify_gesture);
        realNameText = (TextView) findViewById(R.id.account_manage_real_name_text);
        realNameRag = (ImageView) findViewById(R.id.account_manage_real_name_tag);
        slide_button = (ImageView) findViewById(R.id.account_manage_slide_button);
        modify_code = (RelativeLayout) findViewById(R.id.account_manage_table_row_modify_code);
        if(code==null){
            isCheck=false;
            modify_code.setVisibility(View.GONE);
            slide_button.setBackgroundResource(R.mipmap.slide_button);
        }else{
            isCheck=true;
            modify_code.setVisibility(View.VISIBLE);
            slide_button.setBackgroundResource(R.mipmap.slide_button_check);
        }

    }

    public void UpdateViews(UserBean userBean) {
        final String phoneNo = userBean.getPhone();
        final int realStatus = userBean.getRealStatus() == null ? 0 : userBean.getRealStatus().intValue();

        if (realStatus == 1) {
            realNameText.setTextColor(getResources().getColor(R.color.colorYellow));
            realNameText.setText("已认证");
            realNameRag.setImageResource(R.mipmap.success);
        } else {
            realNameText.setTextColor(getResources().getColor(R.color.colorYellow));
            realNameText.setText("完成首次充值后,自动绑卡");
            realNameRag.setImageResource(R.mipmap.error);
        }

        //修改手势密码
        modify_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountManageActivity.this,GestureCodeActivity.class);
                intent.putExtra("cancle","modify");
                startActivityForResult(intent,CLOSECODE);
            }
        });

        //手势密码
        slide_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isCheck) {
                            code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                                    getString(phone, null);
                            //如果关闭手势密码
                            if(code!=null){
                                //进入手势密码页面 输一次手势密码
                                System.out.println("手势密码关闭");
                                slide_button.setBackgroundResource(R.mipmap.slide_button);
                                new Handler().postDelayed(
                                        new Runnable()
                                        {
                                            @Override
                                            public void run()
                                            {

                                                Intent intent=new Intent(AccountManageActivity.this,GestureCodeActivity.class);
                                                intent.putExtra("cancle","sure");
                                                startActivityForResult(intent,CLOSECODE);
                                            }
                                        }, 200);
                                isCheck = false;
                            }
                        } else {
                            slide_button.setBackgroundResource(R.mipmap.slide_button_check);
                            System.out.println("手势密码打开");
                            isCheck = true;
                            final LMessageDialog dialog = new LMessageDialog(AccountManageActivity.this, R.style.ActionSheetDialogStyle);
                            dialog.setMessage("进入app时，需要输入正确的手势密码");
                            dialog.setMtitle("我知道了");
                            dialog.show();
                            dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    //进入密码设置页面
                                    Intent intent=new Intent(AccountManageActivity.this,GestureCodeActivity.class);
                                    startActivityForResult(intent,200);
                                }
                            });
                        }
                    }
                });

        loginPassRow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(AccountManageActivity.this, ModifyLoginPassActivity.class);
                        startActivityForResult(intent, 1010);
                    }
                }
        );
        if (realStatus == 1) {
            realNameRow.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("userId", phoneNo);
                            intent.setClass(AccountManageActivity.this, MyBankCardActivity.class);
                            startActivity(intent);
                        }
                    }
            );
            safePassRow.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.putExtra("currentPhoneNo", phoneNo);
                            intent.setClass(AccountManageActivity.this, ModifySafePassActivity.class);
                            startActivity(intent);
                        }
                    }

            );
        } else {
            realNameRow.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent();
//                            intent.setClass(AccountManageActivity.this, RealNameAuthActivity.class);
//                            intent.putExtra("userId", userId);
//                            startActivityForResult(intent, 99);
                        }
                    }
            );
            safePassRow.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(AccountManageActivity.this, "您还未认证，无法设置交易密码", Toast.LENGTH_SHORT).show();
                        }
                    }

            );

        }
    }


    //请求网络数据
    public void RequestForListData(String basicUrl, String userId) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("username", userId).build();
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
                String s = response.body().string();
                Log.i(TAG, "response json:" + s);
                final UserBean resultBean = JSON.parseObject(s, UserBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            UpdateViews(resultBean);
                        } else if (resultBean.getRcd().equals("E0001")) {
                            startActivityForResult(new Intent(AccountManageActivity.this, LoginActivity.class), 1400);
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                            Toast.makeText(AccountManageActivity.this, "登录已过期，请重新登录!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountManageActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //请求退出登录
    public void RequestForLogout(String basicUrl) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).build();
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
                String s = response.body().string();
                Log.i(TAG, "response json:" + s);
                final BaseBean resultBean = JSON.parseObject(s, BaseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {

                            SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            //清除Token数据
                            editor.clear().commit();

                        } else {
                            Toast.makeText(AccountManageActivity.this, "网络异常，请重试！", Toast.LENGTH_SHORT).show();
                        }
                        //退出登录
                        AccountManageActivity.this.setResult(AppConstants.AppLoginEXitCode);
                        AccountManageActivity.this.finish();
                    }
                });
            }
        });
    }

    protected void initPopuptWindow() {
        if (popupWindow == null) {
            // 获取自定义布局文件activity_popupwindow_left.xml的视图
            View popupWindow_view = getLayoutInflater().inflate(R.layout.exit_popup_confirm_layout, null,
                    false);
            Button cancel = (Button) popupWindow_view.findViewById(R.id.exit_cancel);
            Button confirm = (Button) popupWindow_view.findViewById(R.id.exit_action);

            confirm.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                            String basicUrl = AppConstants.URL_SUFFIX + "/rest/logout";
                            RequestForLogout(basicUrl);
                        }
                    }
            );
            cancel.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                        }
                    }

            );

            // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
            popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // 设置动画效果
            popupWindow.setAnimationStyle(R.style.AnimationFade);
            popupWindow.setOnDismissListener(
                    new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            SetBackgroundAlpha(1f);
                        }
                    }

            );
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void SetBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == AppConstants.RealAuthIng) {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
            RequestForListData(basicUrl, userId);
        } else if (resultCode == AppConstants.RealAuthSuccess) {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
            RequestForListData(basicUrl, userId);
        } else if (resultCode == AppConstants.backToUserCenterResult) {
            String basicUrl = AppConstants.URL_SUFFIX + "/rest/user";
            RequestForListData(basicUrl, userId);
        }else if(resultCode ==100){
            Intent intent=new Intent("HomeFragmentBroadCast");
            intent.putExtra("gesture",200);
            sendBroadcast(intent);
            finish();
        }else if(resultCode ==200){
            Intent intent=new Intent(AccountManageActivity.this,GestureCodeActivity.class);
            startActivityForResult(intent,200);

        }

        if(getSharedPreferences(phone, Context.MODE_PRIVATE).
                getString(phone, null)!=null &&
                !getSharedPreferences("GestureAgain", Context.MODE_PRIVATE).getBoolean("isAgain",false)){
            slide_button.setBackgroundResource(R.mipmap.slide_button_check);
            System.out.println("手势密码回调打开");
            modify_code.setVisibility(View.VISIBLE);
            isCheck=true;
        }else{
            slide_button.setBackgroundResource(R.mipmap.slide_button);
            modify_code.setVisibility(View.GONE);
            System.out.println("手势密码回调关闭");
            isCheck=false;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}
