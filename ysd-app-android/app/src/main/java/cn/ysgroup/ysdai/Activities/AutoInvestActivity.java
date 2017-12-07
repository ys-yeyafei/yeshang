package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.center.UserAuto;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
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

public class AutoInvestActivity extends MyBaseActivity {


    @Bind(R.id.auto_invest_toolbar_back)
    IconFontTextView autoInvestToolbarBack;
    @Bind(R.id.auto_invest_toolbar_title)
    TextView autoInvestToolbarTitle;
    @Bind(R.id.auto_invest_toolbar)
    Toolbar autoInvestToolbar;
    @Bind(R.id.auto_tender_status)
    SwitchButton autoTenderStatus;
    @Bind(R.id.auto_invest_top)
    EditText autoInvestTop;
    @Bind(R.id.auto_tender_safe_pass)
    EditText autoTenderSafePass;
    @Bind(R.id.auto_tender_save_button)
    Button autoTenderSaveButton;
    @Bind(R.id.auto_invest_setting_detail_lauout)
    LinearLayout autoInvestSettingDetailLauout;
    private String TAG = "自动投资设置";
    private UserAuto userAutoSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_invest);
        ButterKnife.bind(this);
        initToolBar();
        String url = AppConstants.URL_SUFFIX + "/rest/zdtzTo";
        RequestForFormData(url);


    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.auto_tender_save_button)
    public void SaveSetting() {
        String url = AppConstants.URL_SUFFIX + "/rest/zdtzSave";

        if (autoTenderSafePass.getText().toString() == null || autoTenderSafePass.getText().toString().trim().equals("")) {
            Toast.makeText(AutoInvestActivity.this, "请输入交易密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (autoTenderStatus.isChecked()) {
            if (autoInvestTop.getText().toString() == null || autoInvestTop.getText().toString().trim().equals("")) {
                Toast.makeText(AutoInvestActivity.this, "请输入最高投资金额", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        RequestSaveFormData(url);

    }

    public void initToolBar() {
        autoInvestToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(autoInvestToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void RequestForFormData(String basicUrl) {
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

                final UserAuto resultBean = JSON.parseObject(s, UserAuto.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {
                            userAutoSetting = resultBean;
                            new Handler().post(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            UpdateViews(resultBean);
                                        }
                                    }
                            );
                        } else {
                            Toast.makeText(AutoInvestActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void RequestSaveFormData(String basicUrl) {

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder()
                .add("user.payPassword",autoTenderSafePass.getText().toString() + "")
                .add("userAuto.autoTenderBorrowType","1,2")
                .add("userAuto.autoTenderMoneyTop",autoInvestTop.getText().toString())
                .add("userAuto.autoTenderStatus",autoTenderStatus.isChecked() ? "1" : "0")
                .add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                build();
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
                String s=response.body().string();
                final BaseBean resultBean = new Gson().fromJson(s, BaseBean.class);
                Log.i(TAG, s);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        autoTenderSafePass.setText("");
                        try {

                            if (resultBean.getRcd().equals("R0001")) {
                                Toast.makeText(AutoInvestActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            } else if (resultBean.getRcd().equals("S0001")) {
                                Toast.makeText(AutoInvestActivity.this, "保存失败，请稍后重试", Toast.LENGTH_SHORT).show();
                            } else if (resultBean.getRcd().equals("E0001")) {
                                startActivityForResult(new Intent(AutoInvestActivity.this, LoginActivity.class), 1400);
                                overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                                Toast.makeText(AutoInvestActivity.this, "登录已过期，请重新登录!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AutoInvestActivity.this, resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(AutoInvestActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    public void UpdateViews(UserAuto resultBean) {
        if (resultBean.getAutoTenderStatus() != null && resultBean.getAutoTenderStatus().intValue() == 1) {
            autoInvestTop.setEnabled(true);
            autoTenderStatus.setChecked(true);
        } else {
            autoInvestTop.setEnabled(false);
            autoTenderStatus.setChecked(false);
        }
        autoTenderStatus.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            autoInvestTop.setEnabled(true);
                        } else {
                            autoInvestTop.setEnabled(false);
                        }
                    }
                }
        );
        String type = resultBean.getAutoTenderBorrowType();

        autoInvestTop.setText(resultBean.getAutoTenderMoneyTop().toString());
    }


}
