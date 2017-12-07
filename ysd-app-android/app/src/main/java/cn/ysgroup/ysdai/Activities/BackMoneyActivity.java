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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.ysgroup.ysdai.Fragments.RepayMentFragment;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BackMoneyActivity extends MyBaseActivity {
    @Bind(R.id.repayment_toolbar_back)
    IconFontTextView repaymentToolbarBack;
    //    @Bind(R.id.repayment_toolbar_radio_group)
    RadioGroup repaymentToolbarRadioGroup;
    @Bind(R.id.repayment_toolbar)
    Toolbar repaymentToolbar;
    //    @Bind(R.id.repayment_fragment_content_layout)
//    FrameLayout repaymentFragmentContentLayout;
//    @Bind(R.id.repayment_toolbar_radio_unpay)
//    RadioButton repaymentToolbarRadioUnpay;
//    @Bind(R.id.repayment_toolbar_radio_payed)
    RadioButton repaymentToolbarRadioPayed;

    private Fragment payedFragment;
    private Fragment unpayedFragment;
    private Fragment currentFragment;
    private TabLayout tab;
    private ViewPager pager;
    private ArrayList<Fragment> al;
    private String[] titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_money);
        ButterKnife.bind(this);
        tab = (TabLayout) findViewById(R.id.content_repay_ment_tab);
        pager = (ViewPager) findViewById(R.id.content_repay_ment_pager);
        initToolBar();
        unpayedFragment = RepayMentFragment.newInstance("0");
        payedFragment = RepayMentFragment.newInstance("1");
        titles = new String[]{"待回款", "已回款"};
        al = new ArrayList<Fragment>();
        al.add(unpayedFragment);
        al.add(payedFragment);
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
        repaymentToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
//        repaymentToolbarRadioGroup.setOnCheckedChangeListener(this);
        setSupportActionBar(repaymentToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("LoginContentBroadCast"));
        super.onDestroy();
    }


}
