package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;

public class MoneyNegotiateActivity extends MyBaseActivity {

    private String TAG = "借款协议";
    private int projectId;
    private int itemId;
    private WebView web;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_negotiate);
        initView();
        String url = AppConstants.URL_SUFFIX + "/rest/borrowAgreement?borrow.id=" + projectId + "&id=" + itemId + "&token=" + PreferenceUtil.getPrefString(this, "loginToken", "");
        web.loadUrl(url);
    }

    private void initView() {
        Intent intent = getIntent();
        itemId = intent.getIntExtra("itemId", 0);
        projectId = intent.getIntExtra("projectId", 0);
        web = (WebView) findViewById(R.id.money_negotiate_web);
        IconFontTextView back= (IconFontTextView) findViewById(R.id.money_negotiate_back);
        WebSettings settings = web.getSettings();
        web.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放
        web.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        web.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        web.getSettings().setUseWideViewPort(true);
//自适应屏幕
        web.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web.getSettings().setLoadWithOverviewMode(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
