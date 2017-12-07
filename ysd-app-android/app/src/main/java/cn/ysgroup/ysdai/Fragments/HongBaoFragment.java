package cn.ysgroup.ysdai.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.ysgroup.ysdai.Activities.ArticleActivity;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HongBaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HongBaoFragment extends Fragment implements View.OnClickListener {
    private String basicUrl = AppConstants.URL_SUFFIX + "/rest/hbListLooked";
    private View rootView;
    private FrameLayout fl;
    private LinearLayout user;
    private TextView tunuser;
    private TextView tuser;
    private TextView tbefore;
    private PopupWindow pp;
    private List<HongBaoDetailFragment> fragmentList;
    private TextView text;
    private TextView explanation;


    public static HongBaoFragment newInstance() {
        HongBaoFragment fragment = new HongBaoFragment();
        return fragment;
    }

    public HongBaoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Activity activity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_hong_bao, container, false);
        ButterKnife.bind(this, rootView);
        initViews(activity);
        return rootView;
    }

    public void initViews(final Activity activity) {
        user = (LinearLayout) rootView.findViewById(R.id.hongbao_user);
        fl = (FrameLayout) rootView.findViewById(R.id.hongbao_fl);
        text = (TextView) rootView.findViewById(R.id.hongbao_text);
        explanation = (TextView) rootView.findViewById(R.id.hb_explanation);
        fragmentList = new ArrayList<>();

        fragmentList.add(new HongBaoDetailFragment(activity, 0));
        fragmentList.add(new HongBaoDetailFragment(activity, 1));
        fragmentList.add(new HongBaoDetailFragment(activity, 2));
        fl.addView(fragmentList.get(0).getmView());
        fragmentList.get(0).RequestForListData(basicUrl,fragmentList.get(0).lastPageNumber,10,0,false);

        final View view = View.inflate(activity, R.layout.hongbao_pupo, null);
        view.measure(0,0);
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.measure(0, 0);
                pp = new PopupWindow(view, view.getMeasuredWidth(), view.getMeasuredHeight());
                //设置 popu 的位置
                pp.setFocusable(true);//必须设置焦点 否则点不到
                pp.setOutsideTouchable(true);//点击除了pw以外的部分，让pw隐藏
                pp.setBackgroundDrawable(new BitmapDrawable());//为了让setOutsideTouchable有效必须设置背景
                pp.showAsDropDown(v, -Utils.dip2px(activity,30), 0);
                tunuser = (TextView) view.findViewById(R.id.hb_pupo_unuser);
                tuser = (TextView) view.findViewById(R.id.hb_pupo_user);
                tbefore = (TextView) view.findViewById(R.id.hb_pupo_before);
                tunuser.setOnClickListener(HongBaoFragment.this);
                tuser.setOnClickListener(HongBaoFragment.this);
                tbefore.setOnClickListener(HongBaoFragment.this);
            }
        });

        //红包说明
        explanation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ArticleActivity.class);
                intent.putExtra("id","655");
                intent.putExtra("header","红包说明");
                startActivity(intent);
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hb_pupo_unuser:
                tunuser.setTextColor(getResources().getColor(R.color.red_button));
                tuser.setTextColor(getResources().getColor(R.color.colorWhite));
                tbefore.setTextColor(getResources().getColor(R.color.colorWhite));
                fl.removeAllViews();
                HongBaoDetailFragment fragment=  fragmentList.get(0);
                fragment.isClick=true;
                fragment.setCurrentState(0);
                pp.dismiss();
                text.setText("未使用");
                fragment.lastPageNumber=1;
                fragment.RequestForListData(basicUrl,1,10,0,false);
                fl.addView(fragment.getmView());
                break;
            case R.id.hb_pupo_before:
                tunuser.setTextColor(getResources().getColor(R.color.colorWhite));
                tuser.setTextColor(getResources().getColor(R.color.colorWhite));
                tbefore.setTextColor(getResources().getColor(R.color.red_button));
                fl.removeAllViews();
                HongBaoDetailFragment fragment2= fragmentList.get(2);
                fragment2.isClick=true;
                fragment2.setCurrentState(2);
                pp.dismiss();
                text.setText("已过期");
                fragment2.lastPageNumber=1;
                fragment2.RequestForListData(basicUrl,1,10,2,false);
                fl.addView(fragment2.getmView());
                break;
            case R.id.hb_pupo_user:
                tunuser.setTextColor(getResources().getColor(R.color.colorWhite));
                tuser.setTextColor(getResources().getColor(R.color.red_button));
                tbefore.setTextColor(getResources().getColor(R.color.colorWhite));
                fl.removeAllViews() ;
                HongBaoDetailFragment fragment1= fragmentList.get(1);
                fragment1.isClick=true;
                fragment1.setCurrentState(1);
                pp.dismiss();
                text.setText("已使用");
                fragment1.RequestForListData(basicUrl,1,10,1,false);
                fragment1.lastPageNumber=1;
                fl.addView(fragment1.getmView());
                break;
        }
    }
}
