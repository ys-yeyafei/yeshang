package cn.ysgroup.ysdai.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Beans.Article.ActivityDetailBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.Util.AppConstants;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HuoDongDetailActivity extends MyBaseActivity {


    @Bind(R.id.huo_dong_detail_toolbar_back)
    IconFontTextView huoDongDetailToolbarBack;
    @Bind(R.id.huo_dong_detail_toolbar_title)
    TextView huoDongDetailToolbarTitle;
    @Bind(R.id.huo_dong_detail_toolbar)
    Toolbar huoDongDetailToolbar;
    @Bind(R.id.huodong_detail_image_view)
    WebView huodongDetailImageView;

    private String TAG = "活动详情";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huo_dong_detail);
        ButterKnife.bind(this);
        String header = getIntent().getStringExtra("header");
        String id = getIntent().getStringExtra("id");
        WebSettings settings = huodongDetailImageView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);
        initToolBar();

        String basicUrl = AppConstants.URL_SUFFIX + "/rest/activity/";
        RequestForListData(basicUrl, id);
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
        huoDongDetailToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        huoDongDetailToolbarTitle.setText("活动详情");
        setSupportActionBar(huoDongDetailToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //请求网络数据
    public void RequestForListData(String basicUrl, String id) {

        String url = basicUrl + id;
        Log.i(TAG, url);
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
                final ActivityDetailBean article = new Gson().fromJson(s, ActivityDetailBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (article.getRcd().equals("R0001")) {
                            huodongDetailImageView.loadUrl(article.getActivity().getContent());
                            huoDongDetailToolbarTitle.setText(article.getActivity().getTitle());
                        } else {
                            Toast.makeText(HuoDongDetailActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }



}
