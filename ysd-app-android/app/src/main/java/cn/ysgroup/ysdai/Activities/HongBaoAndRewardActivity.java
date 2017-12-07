package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import cn.ysgroup.ysdai.Fragments.HongBaoFragment;
import cn.ysgroup.ysdai.Fragments.RewardFragment;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HongBaoAndRewardActivity extends MyBaseActivity
{

    @Bind(R.id.hb_and_reward_toolbar_back)
    IconFontTextView hbAndRewardToolbarBack;
    @Bind(R.id.hb_and_reward_toolbar)
    Toolbar hbAndRewardToolbar;


    private Fragment hbFragment;
    private Fragment rewardFragment;
    private Fragment currentFragment;
    private TabLayout tab;
    private ViewPager pager;
    private String[] titles;
    private ArrayList<Fragment> al;
    private TextView explanation;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hong_bao_and_reward);
        ButterKnife.bind(this);
        initToolBar();
        tab = (TabLayout) findViewById(R.id.hb_and_reward_content_tab);
        pager = (ViewPager) findViewById(R.id.hb_and_reward_content_pager);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hbFragment = HongBaoFragment.newInstance();
        rewardFragment = RewardFragment.newInstance();
//        currentFragment = hbFragment;
//        transaction.add(R.id.hb_and_reward_content, hbFragment).commit();
        titles = new String[]{"红包","奖励"};
        al = new ArrayList<Fragment>();
        al.add(hbFragment);
        al.add(rewardFragment);
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

    public void initToolBar()
    {

        hbAndRewardToolbarBack.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finish();
                    }
                }
        );
        setSupportActionBar(hbAndRewardToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }



    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("LoginContentBroadCast"));
        super.onDestroy();
    }


}
