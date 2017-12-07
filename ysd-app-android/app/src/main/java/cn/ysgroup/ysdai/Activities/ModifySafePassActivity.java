package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
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

public class ModifySafePassActivity extends MyBaseActivity {
    @Bind(R.id.safe_pass_modify_old)
    EditText safePassModifyOld;
    @Bind(R.id.safe_pass_modify_new)
    EditText safePassModifyNew;
    @Bind(R.id.safe_pass_modify_new_twice)
    EditText safePassModifyNewTwice;
    @Bind(R.id.safe_pass_modify_submit)
    Button safePassModifySubmit;
    @Bind(R.id.safe_pass_modify_forget)
    TextView safePassModifyForget;
    @Bind(R.id.modify_safe_pass_toolbar_back)
    IconFontTextView modifySafePassToolbarBack;
    @Bind(R.id.modify_safe_pass_toolbar_title)
    TextView modifySafePassToolbarTitle;
    @Bind(R.id.modify_safe_pass_toolbar)
    Toolbar modifySafePassToolbar;

    private String TAG = "修改交易密码";
    private MessageDialog messageDialog;
    private String currentPhoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_safe_pass);
        currentPhoneNo = getIntent().getStringExtra("currentPhoneNo");

        ButterKnife.bind(this);
        initToolBar();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.safe_pass_modify_submit)
    public void modifySafePass() {

        String basicUrl = AppConstants.URL_SUFFIX + "/rest/userSafePwdUpdate";

        String oldSafe = safePassModifyOld.getText().toString();
        String newSafe = safePassModifyNew.getText().toString();
        String onewSafetwice = safePassModifyNewTwice.getText().toString();

        if (checkFormData(oldSafe, newSafe, onewSafetwice)) {
            RequestModifySafePass(basicUrl, oldSafe, newSafe);
        }

    }

    @OnClick(R.id.safe_pass_modify_forget)
    public void safePassForget() {

        Intent intent = new Intent();
        intent.setClass(this, ForgetSafePassActivity.class);
        intent.putExtra("currentPhoneNo", currentPhoneNo);
        startActivity(intent);
    }

    /**
     * 请求修改安全密码
     */
    public void RequestModifySafePass(String basicUrl, String oldPassword, String newPassword) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("oldPassword", oldPassword).
                add("newPassword", newPassword).build();
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
                final BaseBean resultBack = new Gson().fromJson(s, BaseBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (resultBack.getRcd().equals("R0001")) {
                            messageDialog = new MessageDialog(ModifySafePassActivity.this);
                            messageDialog.setMessage("交易密码修改成功！");
                            messageDialog.setOnPositiveListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                            messageDialog.dismiss();
                                            messageDialog = null;
                                        }
                                    }
                            );

                            messageDialog.show();
                        } else if (resultBack.getRcd().equals("S0002")) {
                            Toast.makeText(ModifySafePassActivity.this, "交易密码输入有误！", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    public boolean checkFormData(String oldSafe, String newSafe, String onewSafetwice) {


        if (oldSafe == null || oldSafe.trim().equals("")) {
            Toast.makeText(ModifySafePassActivity.this, "请输入原始密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (newSafe == null || newSafe.trim().equals("")) {
            Toast.makeText(ModifySafePassActivity.this, "请输入新密码!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (isNumeric(newSafe)) {
            Toast.makeText(ModifySafePassActivity.this, "密码必须包含字母", Toast.LENGTH_LONG).show();
            return false;
        } else if (!isStringLengthIngnel(newSafe)) {
            Toast.makeText(ModifySafePassActivity.this, "交易密码长度至少为6个字符", Toast.LENGTH_LONG).show();
            return false;
        }
        if (onewSafetwice == null || onewSafetwice.trim().equals("")) {
            Toast.makeText(ModifySafePassActivity.this, "新密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!newSafe.equals(onewSafetwice)) {
            Toast.makeText(ModifySafePassActivity.this, "两次新密码不一致！", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }


    public void initToolBar() {
        modifySafePassToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(modifySafePassToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
