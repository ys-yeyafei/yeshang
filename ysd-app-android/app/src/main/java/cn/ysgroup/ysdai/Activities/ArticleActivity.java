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

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Beans.Article.ArticleBean;
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

public class ArticleActivity extends MyBaseActivity {

    @Bind(R.id.article_toolbar_back)
    IconFontTextView articleToolbarBack;
    @Bind(R.id.article_toolbar_title)
    TextView articleToolbarTitle;
    @Bind(R.id.article_toolbar)
    Toolbar articleToolbar;
    @Bind(R.id.article_web_view)
    WebView articleWebView;
    private String TAG = "文章详情";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        String id = getIntent().getStringExtra("id");
        initToolBar();
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/article/";
        WebSettings settings = articleWebView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
        articleToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(articleToolbar);
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
                String s=response.body().string();
                Log.i(TAG, "response json:" + s);
                final ArticleBean article = JSON.parseObject(s, ArticleBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "rcd===" + article.getRcd());
                        if (article.getRcd().equals("R0001")) {
                            articleToolbarTitle.setText(article.getTitle());
//                            articleWebView.loadData(article.getContent(), "text/html; charset=UTF-8", null);
                            String sss=article.getContent();
                            articleWebView.loadDataWithBaseURL(null,getHtmlData(article.getContent()), "text/html", "utf-8", null);
                        } else {
                            Toast.makeText(ArticleActivity.this, "请求数据失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }


    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }


}
