package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ysgroup.ysdai.Activities.LoginActivity;
import cn.ysgroup.ysdai.Activities.MainActivity;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LazyViewPager;
import cn.ysgroup.ysdai.UI.LoginSuccessDialog;
import cn.ysgroup.ysdai.UI.OneViewPage;
import cn.ysgroup.ysdai.Util.AppConstants;

import java.util.ArrayList;

/**
 * Created by linyujie on 16/10/27.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private OneViewPage pager;
    private Activity activity;
    private ArrayList<BaseFragment> al;
    private LinearLayout shouye_ll;
    private ImageView shouye_iv;
    private TextView shouye_tv;
    private LinearLayout list_ll;
    private ImageView list_iv;
    private TextView list_tv;
    private LinearLayout zhanghu_ll;
    private ImageView zhanghu_iv;
    private TextView zhanghu_tv;
    private LinearLayout home_more_ll;
    private ImageView home_more_iv;
    private TextView home_more_tv;
    private HomeFragmentBroadCast broadCast;
    private SharedPreferences preferences;
    private ListFragment listFragment;
    private LoginContentFragment loginContentFragment;
    private MoreFragment moreFragment;
    private int currentPosition;

    public HomeFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public HomeFragment(Activity activity) {
        this.activity = activity;
        view = View.inflate(activity, R.layout.main_fragment, null);
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return view;
    }

    public View getmView() {
        return view;
    }

    private void initView() {
        preferences = activity.getSharedPreferences("AppToken", activity.MODE_PRIVATE);
        broadCast = new HomeFragmentBroadCast();
        IntentFilter intentFilter = new IntentFilter("HomeFragmentBroadCast");
        activity.registerReceiver(broadCast, intentFilter);

        pager = (OneViewPage) view.findViewById(R.id.main_view_pager);
        shouye_ll = (LinearLayout) view.findViewById(R.id.home_shouye_ll);
        shouye_iv = (ImageView) view.findViewById(R.id.home_shouye_iv);
        shouye_tv = (TextView) view.findViewById(R.id.home_shouye_tv);
        LinearLayout tab= (LinearLayout) view.findViewById(R.id.home_tab);
        list_ll = (LinearLayout) view.findViewById(R.id.home_list_ll);
        list_iv = (ImageView) view.findViewById(R.id.home_list_iv);
        list_tv = (TextView) view.findViewById(R.id.home_list_tv);
        zhanghu_ll = (LinearLayout) view.findViewById(R.id.home_zhanghu_ll);
        zhanghu_iv = (ImageView) view.findViewById(R.id.home_zhanghu_iv);
        zhanghu_tv = (TextView) view.findViewById(R.id.home_zhanghu_tv);
        home_more_ll = (LinearLayout) view.findViewById(R.id.home_more_ll);
        home_more_iv = (ImageView) view.findViewById(R.id.home_more_iv);
        home_more_tv = (TextView) view.findViewById(R.id.home_more_tv);
        tab.measure(0,0);

        shouye_ll.setOnClickListener(this);
        list_ll.setOnClickListener(this);
        zhanghu_ll.setOnClickListener(this);
        home_more_ll.setOnClickListener(this);

        al = new ArrayList<BaseFragment>();
        al.add(new HomePageFragment(activity));
        listFragment = new ListFragment(activity);
        loginContentFragment = new LoginContentFragment(activity);
        moreFragment = new MoreFragment((MainActivity) activity);
        al.add(listFragment);
        al.add(loginContentFragment);
        al.add(moreFragment);
        pager.setAdapter(new HomePagerAdapter());
        pager.setOnPageChangeListener(new LazyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        pager.setCurrentItem(0, false);
                        break;
                    case 1:
                        pager.setCurrentItem(1, false);
                        break;
                    case 2:
                        pager.setCurrentItem(2, false);
                        break;
                    case 3:
                        pager.setCurrentItem(3, false);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_shouye_ll:
                shouye_iv.setBackgroundResource(R.mipmap.shouye_selected);
                shouye_tv.setTextColor(Color.parseColor("#e12d2d"));
                list_iv.setBackgroundResource(R.mipmap.list);
                list_tv.setTextColor(Color.parseColor("#848484"));
                zhanghu_iv.setBackgroundResource(R.mipmap.zhanghu);
                zhanghu_tv.setTextColor(Color.parseColor("#848484"));
                home_more_iv.setBackgroundResource(R.mipmap.more);
                home_more_tv.setTextColor(Color.parseColor("#848484"));
                pager.setCurrentItem(0, false);
                break;
            case R.id.home_list_ll:
                if (listFragment.isFirst) {
                    String basicUrl = AppConstants.URL_SUFFIX + "/rest/borrow";
                    listFragment.RequestForListData(basicUrl, listFragment.currentBottomPageIndex, 10, true);
                }
                shouye_iv.setBackgroundResource(R.mipmap.shouye);
                shouye_tv.setTextColor(Color.parseColor("#848484"));
                list_iv.setBackgroundResource(R.mipmap.list_selected);
                list_tv.setTextColor(Color.parseColor("#e12d2d"));
                zhanghu_iv.setBackgroundResource(R.mipmap.zhanghu);
                zhanghu_tv.setTextColor(Color.parseColor("#848484"));
                home_more_iv.setBackgroundResource(R.mipmap.more);
                home_more_tv.setTextColor(Color.parseColor("#848484"));
                pager.setCurrentItem(1, false);
                break;
            case R.id.home_zhanghu_ll:
                //根据存在sp中站好的tocken去判断是否进入注册页面
                if (preferences.getString("loginToken", null) == null) {
                    //登入注册页面
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                    activity.overridePendingTransition(R.anim.activity_up, R.anim.activity_down);
                } else {
                    //进入我的页面
                    if (loginContentFragment.isFirst) {
                        String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
                        loginContentFragment.RequestForUserCenter(url);
                    } else {
                        loginContentFragment.RequestForHbPoint();
                        loginContentFragment.RequestForMoney();

                    }

                    shouye_iv.setBackgroundResource(R.mipmap.shouye);
                    shouye_tv.setTextColor(Color.parseColor("#848484"));
                    list_iv.setBackgroundResource(R.mipmap.list);
                    list_tv.setTextColor(Color.parseColor("#848484"));
                    zhanghu_iv.setBackgroundResource(R.mipmap.zhanghu_selected);
                    zhanghu_tv.setTextColor(Color.parseColor("#e12d2d"));
                    home_more_iv.setBackgroundResource(R.mipmap.more);
                    home_more_tv.setTextColor(Color.parseColor("#848484"));
                    pager.setCurrentItem(2, false);
                    //请求网络数据
//                    loginContentFragment.RequestForProfit();
                }
                break;
            case R.id.home_more_ll:
                shouye_iv.setBackgroundResource(R.mipmap.shouye);
                shouye_tv.setTextColor(Color.parseColor("#848484"));
                list_iv.setBackgroundResource(R.mipmap.list);
                list_tv.setTextColor(Color.parseColor("#848484"));
                zhanghu_iv.setBackgroundResource(R.mipmap.zhanghu);
                zhanghu_tv.setTextColor(Color.parseColor("#848484"));
                home_more_iv.setBackgroundResource(R.mipmap.more_selected);
                home_more_tv.setTextColor(Color.parseColor("#e12d2d"));
                pager.setCurrentItem(3, false);
                break;
        }
    }


    class HomePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            System.out.println(al.get(position) + "....................");
            container.addView(al.get(position).getmView());
            return al.get(position).getmView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadCast!=null)
        activity.unregisterReceiver(broadCast);
    }


    //各种通知
    class HomeFragmentBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra("current", 100) == 1) {
                shouye_iv.setBackgroundResource(R.mipmap.shouye);
                shouye_tv.setTextColor(Color.parseColor("#848484"));
                list_iv.setBackgroundResource(R.mipmap.list_selected);
                list_tv.setTextColor(Color.parseColor("#e12d2d"));
                zhanghu_iv.setBackgroundResource(R.mipmap.zhanghu);
                zhanghu_tv.setTextColor(Color.parseColor("#848484"));
                home_more_iv.setBackgroundResource(R.mipmap.more);
                home_more_tv.setTextColor(Color.parseColor("#848484"));
                pager.setCurrentItem(1, false);
            } else if (intent.getIntExtra("Login", 0) == 101) {
                String url = AppConstants.URL_SUFFIX + "/rest/userCenter";
                loginContentFragment.RequestForUserCenter(url);
            } else if (intent.getIntExtra("LoginSuccess", 0) == 102) {
                shouye_iv.setBackgroundResource(R.mipmap.shouye_selected);
                shouye_tv.setTextColor(Color.parseColor("#e12d2d"));
                list_iv.setBackgroundResource(R.mipmap.list);
                list_tv.setTextColor(Color.parseColor("#848484"));
                zhanghu_iv.setBackgroundResource(R.mipmap.zhanghu);
                zhanghu_tv.setTextColor(Color.parseColor("#848484"));
                home_more_iv.setBackgroundResource(R.mipmap.more);
                home_more_tv.setTextColor(Color.parseColor("#848484"));
                pager.setCurrentItem(0, false);
                LoginSuccessDialog homeDialog = new LoginSuccessDialog(context, R.style.ActionSheetDialogStyle);
                homeDialog.start(intent.getStringExtra("id"));

            }else if(intent.getIntExtra("gesture", 100) == 200){
                shouye_iv.setBackgroundResource(R.mipmap.shouye_selected);
                shouye_tv.setTextColor(Color.parseColor("#e12d2d"));
                list_iv.setBackgroundResource(R.mipmap.list);
                list_tv.setTextColor(Color.parseColor("#848484"));
                zhanghu_iv.setBackgroundResource(R.mipmap.zhanghu);
                zhanghu_tv.setTextColor(Color.parseColor("#848484"));
                home_more_iv.setBackgroundResource(R.mipmap.more);
                home_more_tv.setTextColor(Color.parseColor("#848484"));
                pager.setCurrentItem(0, false);
            }
        }
    }
}
