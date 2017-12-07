package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import cn.ysgroup.ysdai.Beans.Borrow.BorrowBean;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowPoputMessage;
import cn.ysgroup.ysdai.Fragments.InvestFragment;
import cn.ysgroup.ysdai.Fragments.MaterialFragment;
import cn.ysgroup.ysdai.Fragments.ProjectDescribeFragment;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.ConfirmDialog;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import cn.ysgroup.ysdai.Util.Utils;

public class ProjectDetailActivity extends MyBaseActivity {

    private Toolbar previewOneToolBar;
    private IconFontTextView toolBarBack;
    private TextView toolBarTitleText;
    private Intent intent;
    private TabLayout tab;
    private ViewPager pager;
    private ArrayList<Fragment> al;
    private String[] titleS;
    private Button button;
    private String TAG = "ProjectDetailActivity:";
    private BigDecimal mData;
    private String timeLimit;
    private String tokenString;
    private BorrowBean projectItem;
    private int PROJECTDETAILACTIVITYCODE = 10030;
    private int id;
    private Integer realStatus;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);
        intent = getIntent();
        initToolBar();
        initView();
        initDate();
    }


    private void initToolBar() {
        toolBarTitleText = (TextView) findViewById(R.id.project_detail_toolbar_title);
        toolBarBack = (IconFontTextView) findViewById(R.id.project_detail_toolbar_back);
        previewOneToolBar = (Toolbar) findViewById(R.id.project_detail_toolbar);
        button = (Button) findViewById(R.id.project_detail_button);
        toolBarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(previewOneToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (intent.getStringExtra("title") != null) {
            toolBarTitleText.setText(intent.getStringExtra("title"));
        }
    }

    private void initView() {
        titleS = new String[]{"项目描述", "材料公示", "投资记录"};
        al = new ArrayList<Fragment>();
        id = intent.getIntExtra("id", 10000);
        ProjectDescribeFragment projectDescribeFragment = new ProjectDescribeFragment(id);
        String borrowImg = intent.getStringExtra("borrowImg");
        MaterialFragment materialFragment = new MaterialFragment(borrowImg);
        InvestFragment investFragment = new InvestFragment(id);
        al.add(projectDescribeFragment);
        al.add(materialFragment);
        al.add(investFragment);

        tab = (TabLayout) findViewById(R.id.project_detail_tab);
        pager = (ViewPager) findViewById(R.id.project_detail_pager);
    }

    private void initDate() {
        projectItem = (BorrowBean) intent.getSerializableExtra("item");
        mData = projectItem.getBaseApr().setScale(2, BigDecimal.ROUND_HALF_UP);
        timeLimit = projectItem.getTimeLimit();
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return al.get(position);
            }

            @Override
            public int getCount() {
                return al.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titleS[position];
            }
        });
        //绑定 vp
        tab.setupWithViewPager(pager);
        buttonState();

    }

    private void buttonState() {
        tokenString = PreferenceUtil.getPrefString(this, "loginToken", null);
        if (tokenString == null) {
            button.setText("立即登录");
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(ProjectDetailActivity.this, LoginActivity.class), 1500);
                            overridePendingTransition(R.anim.activity_up, R.anim.activity_down);

                        }
                    }
            );

        } else {
            if(Utils.str2Double(projectItem.getBalance()) == 0){
                button.setBackgroundResource(R.drawable.button_grey_selector);
                button.setText("投资其他项目");
            }else{
                final BigDecimal lowestAcc = new BigDecimal(projectItem.getLowestAccount());
                final BigDecimal ableMoney = projectItem.getUserAbleMoney();
                realStatus = projectItem.getRealStatus();
                final int projectId = projectItem.getId();
                button.setText("立即投资");
                button.setOnClickListener(
                        new View.OnClickListener() {

                            private ConfirmDialog confirmDialog;

                            @Override
                            public void onClick(View v) {
                                String url = AppConstants.URL_SUFFIX + "/rest/poputInvest/" + projectId;
                                RequestSaveFormData(url, projectId, projectItem.getLowestAccount(), projectItem.getBalance());

                            }
                        }
                );
            }

        }

    }
//    }

    /**
     * 请求获取该项目的数据
     *
     * @param basicUrl
     */
    public void RequestSaveFormData(String basicUrl, final int projectId, final String lowestTender, final String availableTender) {
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
                        Toast.makeText(ProjectDetailActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
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
                final BorrowPoputMessage resultBean = JSON.parseObject(s, BorrowPoputMessage.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {

                            Intent intent = new Intent(ProjectDetailActivity.this, ProjectInvestActivity.class);
                            intent.putExtra("projectId", projectId);
                            intent.putExtra("mData", mData.toString());
                            intent.putExtra("timeLimit", timeLimit);
                            intent.putExtra("lowestTender", lowestTender);
                            intent.putExtra("availableTender", availableTender);

                            if (realStatus !=null&& realStatus.intValue() != 1) {
                                //没绑卡
                                intent.putExtra("isBund", false);
                            } else {
                                //邦卡了
                                intent.putExtra("isBund", true);
                            }
                            ProjectDetailActivity.this.startActivityForResult(intent, 2020);
                            setResult(AppConstants.projectInvestSuccess);
                        } else {
                            Toast.makeText(ProjectDetailActivity.this, resultBean.getRmg() + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    //请求网络数据
    public void RequestForListData(String basicUrl, int projectId) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
            loadingDialog.show();
        }
        String url = basicUrl + "/" + projectId;
        Log.i(TAG, url);
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        Log.i(TAG, "网络连接失败");
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                final BorrowBean resultBean = JSON.parseObject(s, BorrowBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                            loadingDialog = null;
                        }
                        if (resultBean.getRcd().equals("R0001")) {
                            projectItem = resultBean;
                            mData = projectItem.getBaseApr().setScale(2, BigDecimal.ROUND_HALF_UP);
                            timeLimit = projectItem.getTimeLimit();
                            buttonState();
                        } else {
                            Toast.makeText(ProjectDetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1500 && resultCode == 10086) {
            Intent intent = new Intent("PreviewOneActivityBroadCast");
            intent.putExtra("register", 100);
            sendBroadcast(intent);
            finish();
        } else if (requestCode == 1500) {
            Intent intent = new Intent("PreviewOneActivityBroadCast");
            intent.putExtra("register", 200);
            sendBroadcast(intent);
            buttonState();
        } else if (requestCode == PROJECTDETAILACTIVITYCODE) {
            RequestForListData(AppConstants.URL_SUFFIX + "/rest/borrow", id);
            Intent intent = new Intent("PreviewOneActivityBroadCast");
            intent.putExtra("register", 300);
            sendBroadcast(intent);
            return;
        }

        if (realStatus!=null && realStatus.intValue() != 1) {
            //没绑卡
            RequestForListData(AppConstants.URL_SUFFIX + "/rest/borrow", id);
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
