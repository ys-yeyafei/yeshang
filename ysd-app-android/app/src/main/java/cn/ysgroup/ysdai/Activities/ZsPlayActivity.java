package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import cn.ysgroup.ysdai.Beans.bank.ZSBeen;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.umeng.analytics.MobclickAgent;


public class ZsPlayActivity extends MyBaseActivity {

    private ZSBeen been;
    private WebView webView;
    private LoadingDialog loadingDialog;
    private String baseUrl;
    private String inverstMark;
    private String selectedIdArrayString;
    private String safePass;
    private String tenderMoney;
    private int projectId;
    private String project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zs_play);
        Intent intent = getIntent();
        inverstMark = intent.getStringExtra("inverstMark");
        been = (ZSBeen) intent.getSerializableExtra("zsBeen");
        webView = (WebView) findViewById(R.id.zs_web_view);
        WebSettings settings = webView.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setJavaScriptEnabled(true);

        selectedIdArrayString = intent.getStringExtra("selectedIdArrayString");
        safePass = intent.getStringExtra("safePass");
        tenderMoney = intent.getStringExtra("tenderMoney");
        projectId = intent.getIntExtra("projectId", 0);
        project = intent.getStringExtra("project");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
                if (url.contains("km_success")) {
                    //成功
                    String[] split = url.split("km_success=");
                    String order=split[1];
                    Intent intent = new Intent(ZsPlayActivity.this, QuicklyPlayActivity.class);
                    intent.putExtra("order", order);
                    if(inverstMark!=null){
                        intent.putExtra("inverstMark",inverstMark);
                        intent.putExtra("tenderMoney", tenderMoney);
                        intent.putExtra("selectedIdArrayString", selectedIdArrayString);
                        intent.putExtra("project", project);
                        intent.putExtra("projectId", projectId);
                        intent.putExtra("safePass", safePass);
                    }
                    ZsPlayActivity.this.startActivity(intent);
                    setResult(222,getIntent());
                    finish();
                } else if (url.contains("km_fail")) {
                    //失败
                    setResult(222,getIntent());
                    Toast.makeText(ZsPlayActivity.this,"支付失败,请重新支付",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (loadingDialog == null) {
                    loadingDialog = new LoadingDialog(ZsPlayActivity.this);
                    loadingDialog.show();
                }
            }


        });

        initDate();

    }

    private void initDate() {
        if (been != null) {
            String rmg = been.getRmg();
            String order_no = been.getOrder_no();
            String token = PreferenceUtil.getPrefString(this, "loginToken", "");
            baseUrl = AppConstants.URL_SUFFIX + "/rest/verifycmb" + "?rmg=" + rmg + "&order_no=" + order_no + "&token=" + token;
//            baseUrl = "http://160836c5u5.iok.la:9999/rest/rbkmreturn";

        }
        webView.loadUrl(baseUrl);
    }

    interface JsDefeat {
        @JavascriptInterface
        public void defeat();
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
