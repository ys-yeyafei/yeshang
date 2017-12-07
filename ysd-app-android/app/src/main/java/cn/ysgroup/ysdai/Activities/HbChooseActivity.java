package cn.ysgroup.ysdai.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cn.ysgroup.ysdai.Adapters.HongBaoChooseListAdapter;
import cn.ysgroup.ysdai.Beans.user.HbPriorityBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.Util.AppConstants;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HbChooseActivity extends MyBaseActivity implements HongBaoChooseListAdapter.OneItemClickListener {

    @Bind(R.id.hb_choose_toolbar_back)
    IconFontTextView hbChooseToolbarBack;
    @Bind(R.id.hb_choose_toolbar_title)
    TextView hbChooseToolbarTitle;
    @Bind(R.id.hb_choose_toolbar_ok)
    TextView hbChooseToolbarOk;
    @Bind(R.id.hb_choose_toolbar)
    Toolbar hbChooseToolbar;
    @Bind(R.id.hb_choose_list_view)
    ListView hbChooseListView;

    private String TAG = "项目投资";
    private int hongBaoCount = 0;//被选中的红包总数值
    private String hongBaoArray = "";//被选中的红包ID数组
    private List<String> choosedHbIdList;//被选中的红包ID列表
    private List<HbPriorityBean.UserHongbaoListBean> hongbaoList = new ArrayList<>();
    private HongBaoChooseListAdapter myAdapter;

    private String hongBaoArrayString = "";
    StringBuilder hongbaoArray = new StringBuilder();
    int selectedHbSum = 0;
    private TextView choose_money;
    private TextView choose_hb;
    private HbBroadCast broadCast;
    int HBmoney1 = 0;
    private String certainMoney;
    private TextView explain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hb_choose);
        ButterKnife.bind(this);
        choose_money = (TextView) findViewById(R.id.choose_money);
        choose_hb = (TextView) findViewById(R.id.choose_hb);
        explain = (TextView) findViewById(R.id.hb_choose_toolbar_explain);
        broadCast = new HbBroadCast();
        IntentFilter intentFilter = new IntentFilter("HbBroadCast");
        registerReceiver(broadCast, intentFilter);

        initToolBar();

        String hbListString = getIntent().getStringExtra("hbListString");
        String choosenIdString = getIntent().getStringExtra("choosenIdString");

        hongbaoList = new Gson().fromJson(hbListString, new TypeToken<ArrayList<HbPriorityBean.UserHongbaoListBean>>() {
        }.getType());
        choosedHbIdList = new Gson().fromJson(choosenIdString, new TypeToken<ArrayList<String>>() {
        }.getType());
        for (HbPriorityBean.UserHongbaoListBean item : hongbaoList) {
            if (item.isOn())
                HBmoney1 = (int) (HBmoney1 + item.getMoney());
        }
        choose_money.setText(getIntent().getStringExtra("tenderMoney"));
        choose_hb.setText(HBmoney1 + "");

        if (hongbaoList != null && hongbaoList.size() > 0) {
            myAdapter = new HongBaoChooseListAdapter(this, hongbaoList, getLayoutInflater(),
                    choosedHbIdList, getIntent().getIntExtra("onSize", 0),
                    getIntent().getStringExtra("tenderMoney"));
            myAdapter.setOneItemClickListener(this);
            hbChooseListView.setAdapter(myAdapter);
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


    public void initToolBar() {
        hbChooseToolbarBack.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );


        explain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HbChooseActivity.this, ArticleActivity.class);
                intent.putExtra("id","655");
                intent.putExtra("header","红包说明");
                startActivity(intent);
            }
        });


        hbChooseToolbarOk.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, choosedHbIdList.toString() + "   ...........");
                        if (choosedHbIdList != null && choosedHbIdList.size() > 0) {
                            for (HbPriorityBean.UserHongbaoListBean item : hongbaoList) {
                                if (choosedHbIdList.contains(item.getId() + "") || choosedHbIdList.contains(item.getId())) {
                                    selectedHbSum += item.getMoney();
                                    hongbaoArray.append(item.getId() + ",");
                                }
                            }
                            Log.i(TAG, hongbaoArray.toString() + "   ...........");
                            hongBaoArrayString = hongbaoArray.substring(0, hongbaoArray.length() - 1);
                        }


                        System.out.println("--------------------" + hongBaoArrayString);
                        Intent intent = new Intent();
                        intent.putExtra("hongBaoList", new Gson().toJson(choosedHbIdList));
                        intent.putExtra("hongBaoCount", selectedHbSum);
                        intent.putExtra("hongBaoArray", hongBaoArrayString);
                        if (certainMoney == null) {
                            intent.putExtra("certainMoney", getIntent().getStringExtra("tenderMoney"));
                        } else {
                            intent.putExtra("certainMoney", certainMoney);
                        }
                        setResult(AppConstants.ChooseHongBaoOkCode, intent);
                        finish();
                    }
                }
        );

        setSupportActionBar(hbChooseToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public void OnCheckBoxClick(int position, boolean isChecked) {
        if (isChecked) {
            //取消选择
            if (choosedHbIdList.contains(hongbaoList.get(position).getId()) || choosedHbIdList.contains(hongbaoList.get(position).getId() + "")) {
                choosedHbIdList.remove(hongbaoList.get(position).getId() + "");
                Log.i(TAG, choosedHbIdList.toString() + "++++++++");
            }
        } else {
            //选择
            if (!choosedHbIdList.contains(hongbaoList.get(position).getId() + "") || !choosedHbIdList.contains(hongbaoList.get(position).getId())) {
                choosedHbIdList.add(hongbaoList.get(position).getId() + "");

            }

        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadCast);
        super.onDestroy();
    }

    class HbBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            certainMoney = intent.getStringExtra("money");
            HBmoney1 = (int) intent.getIntExtra("HBmoney", 0);
            choose_hb.setText(HBmoney1 + "");

            if (certainMoney != null) {
                choose_money.setText(certainMoney);
            }

        }
    }
}
