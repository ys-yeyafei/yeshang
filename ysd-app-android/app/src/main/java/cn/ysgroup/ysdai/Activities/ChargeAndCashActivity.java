package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.ysgroup.ysdai.Fragments.CashFragment;
import cn.ysgroup.ysdai.Fragments.ChargeFragment;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;


public class ChargeAndCashActivity extends MyBaseActivity {


    private Fragment chargeFragment;
    private Fragment cashFragment;
    private String currentPhoneNo;
    private TabLayout tab;
    private ViewPager pager;
    private String[] titles;
    private ArrayList<Fragment> al;
    private IconFontTextView chargeAndCashToolbarBack;
    private TextView chargeAndCashToolbarTitle;
    private Toolbar chargeAndCashToolbar;
    private String totalIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_and_cash);
        currentPhoneNo = getIntent().getStringExtra("currentPhoneNo");
        totalIncome = getIntent().getStringExtra("totalIncome");
        chargeAndCashToolbarBack = (IconFontTextView) findViewById(R.id.charge_and_cash_toolbar_back);
        chargeAndCashToolbarTitle = (TextView) findViewById(R.id.charge_and_cash_toolbar_title);
        chargeAndCashToolbar = (Toolbar) findViewById(R.id.charge_and_cash_toolbar);
        tab = (TabLayout) findViewById(R.id.content_charge_and_cash_tab);
        pager = (ViewPager) findViewById(R.id.content_charge_and_cash_pager);
        chargeFragment = ChargeFragment.newInstance(totalIncome);
        cashFragment = CashFragment.newInstance(currentPhoneNo,totalIncome);
        titles = new String[]{"充值", "提现"};
        al = new ArrayList<Fragment>();
        al.add(chargeFragment);
        al.add(cashFragment);
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
                return titles[position];
            }
        });
        //绑定 vp
        tab.setupWithViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                CashFragment cf= (CashFragment) cashFragment;
//                if(CashFragment.isAgain){
//                    CashFragment.isAgain=false;
//                    String chargeUrl = AppConstants.URL_SUFFIX + "/rest/rechargeTo";
//                    cf.RequestForRechargeTo(chargeUrl);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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


    public void initToolBar() {

        chargeAndCashToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
        setSupportActionBar(chargeAndCashToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("LoginContentBroadCast"));
        super.onDestroy();
    }


}
