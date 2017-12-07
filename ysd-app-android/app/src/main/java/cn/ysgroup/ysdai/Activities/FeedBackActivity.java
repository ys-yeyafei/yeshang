package cn.ysgroup.ysdai.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

public class FeedBackActivity extends MyBaseActivity
{

    @Bind(R.id.feedback_toolbar_back)
    IconFontTextView feedbackToolbarBack;
    @Bind(R.id.feedback_toolbar_title)
    TextView feedbackToolbarTitle;
    @Bind(R.id.feedback_toolbar)
    Toolbar feedbackToolbar;
    @Bind(R.id.feedback_edit_text)
    EditText feedbackEditText;
    @Bind(R.id.feedback_count_text)
    TextView feedbackCountText;
    @Bind(R.id.feedback_submit)
    Button feedbackSubmit;

    private final String TAG = "意见反馈";
    private EditText emal;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ButterKnife.bind(this);
        emal = (EditText) findViewById(R.id.feedback_edit_emal);
        initToolBar();

        feedbackEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().length() > 150)
                {
                    feedbackCountText.setText("0");
                }else{
                    int count =150-s.toString().length();
                    feedbackCountText.setText(count+"");
                }
            }
        });
    }

    public void initToolBar()
    {
        feedbackToolbarBack.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finish();
                    }
                }
        );
        setSupportActionBar(feedbackToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void feedBack(String content,String emailString)
    {
        final String basicUrl = AppConstants.URL_SUFFIX + "/rest/adFeedbackNew";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("content", content).
                add("contact",emailString).build();
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
                final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBack.getRcd().equals("R0001"))
                        {
                            Toast.makeText(FeedBackActivity.this, "提交成功，谢谢您的反馈", Toast.LENGTH_SHORT).show();
                            finish();

                        } else
                        {
                            Toast.makeText(FeedBackActivity.this, "" + resultBack.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @OnClick(R.id.feedback_submit)
    public void onClick()
    {
        String emailString=emal.getText().toString();
        String contentString = feedbackEditText.getText().toString();
        if (contentString.length() ==0)
        {
            Toast.makeText(this,"反馈内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }else if(emailString.length()==0){
            Toast.makeText(this,"请填写您的联系方式",Toast.LENGTH_SHORT).show();
            return;
        }else{
            feedBack(contentString,emailString);
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
