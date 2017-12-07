package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.ysgroup.ysdai.Activities.ArticleActivity;
import cn.ysgroup.ysdai.Activities.CustomerActivity;
import cn.ysgroup.ysdai.Activities.HuoDongActivity;
import cn.ysgroup.ysdai.Activities.HuoDongDetailActivity;
import cn.ysgroup.ysdai.Activities.PreviewOneActivity;
import cn.ysgroup.ysdai.Activities.ShareToFriendsActivity;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.Beans.Article.ArticleItem;
import cn.ysgroup.ysdai.Beans.Article.ArticleList;
import cn.ysgroup.ysdai.Beans.index.IndexBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.MarqueeTextView;
import cn.ysgroup.ysdai.UI.MyRefresh;
import cn.ysgroup.ysdai.UI.SlideShowView;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.Utils;


public class HomePageFragment extends BaseFragment implements SlideShowView.OnImageItemClickListener {
    private final String TAG = "首页";

    private IndexBean indexBean;
    private LinearLayout linearLayout;
    private Button share_row;
    private TextView more;
    private TextView repay;
    private TextView repay_introduce;
    private RelativeLayout repay_more;
    private RelativeLayout rl_more;
    private Activity activity;
    private View rootView;
    private TextView people_count;
    private TextView project_count;
    private TextView introduce;
    private TextView date;
    private MyRefresh homerefresh;
    private String url;
    private TextView remaining;
    private TextView pname;
    private TextView pBaseApr;
    private TextView pAwardApr;
    private LinearLayout awardlayout;
    private TextView pAccount;//总额
    private TextView peroid;//产品期限
    private SlideShowView slideShowView;

    List<String> targetUrllist = new ArrayList<String>(); //轮播图Target
    DecimalFormat df = new DecimalFormat("#0");
    DecimalFormat df1 = new DecimalFormat("#0.0");
    private Gson gson = new Gson();
    private boolean isDown = true;
    private boolean ismove=false;//手指是否在滑动

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what){

                case 0x111:
                    remaining.setText(df.format(Utils.str2Double(indexBean.getIndexBorrow().getBalance())) + "元");
                    pname.setText(indexBean.getIndexBorrow().getName());
                    if (indexBean.getIndexBorrow() != null) {
                        pBaseApr.setText(indexBean.getIndexBorrow().getBaseApr() + "");
                    }

                    if (indexBean.getIndexBorrow() != null && indexBean.getIndexBorrow().getAwardApr() != 0) {
                        pAwardApr.setText(indexBean.getIndexBorrow().getAwardApr() + "%");
                        awardlayout.setVisibility(View.VISIBLE);
                    } else {
                        awardlayout.setVisibility(View.INVISIBLE);
                    }
                    String accountString = indexBean.getIndexBorrow().getAccount();
                    float accountFloat = Utils.str2Float(accountString);
                    int accountInt = (int) accountFloat;
                    pAccount.setText(accountInt + "元");

//          pLowest.setText(indexBean.getIndexBorrow().getLowestAccount());
                    peroid.setText(Utils.str2Int(indexBean.getIndexBorrow().getTimeLimit())+ "天");
                    List<String> imgUrlList = new ArrayList<String>();
                    for (IndexBean.IndexImageItemListBean item : indexBean.getIndexImageItemList()) {
                        //图片url
                        imgUrlList.add(AppConstants.IMG_URL_SUFFIX + item.getImageUrl());
                        targetUrllist.add(item.getTypeTarget());
                    }
                    //自定义轮询
                    if (!slideShowView.isPlay) {
                        slideShowView.setOnImageItemClickListener(HomePageFragment.this);
                        slideShowView.START(imgUrlList);
                        slideShowView.isPlay = true;
                    }



                    people_count.setText(indexBean.getTotalUserNum() + " ");
                    project_count.setText(indexBean.getTotalTenderMoney() + " ");

                    introduce.setText(indexBean.getActivity().getTitle() + " ");

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    date.setText("活动时间：" + sdf.format(new Date(indexBean.getActivity().getStartTime()))
                            + "—" + sdf.format(new Date(indexBean.getActivity().getEndTime())));
                    break;
                case 0x222:
                    if(!ismove){
                        marqueeView.start();
                    }
                    break;
            }
        }
    };
    private Button online;
    private int screenWidth;
    private int screenHeight;
    private long startTime;
    private MarqueeTextView marqueeView;

    public HomePageFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public HomePageFragment(final Activity activity) {
        this.activity = activity;
        rootView = View.inflate(activity, R.layout.fragment_home_page, null);
        LinearLayout layout5 = (LinearLayout) rootView.findViewById(R.id.home_page_project_layout5);
        homerefresh = (MyRefresh) rootView.findViewById(R.id.page_home_refresh);
        people_count = (TextView) rootView.findViewById(R.id.home_page_project_people_count);
        project_count = (TextView) rootView.findViewById(R.id.home_page_project_count);
        remaining = (TextView) rootView.findViewById(R.id.home_page_remaining);
        date = (TextView) rootView.findViewById(R.id.home_page_repay_date);
        introduce = (TextView) rootView.findViewById(R.id.home_page_repay_introduce);
        more = (TextView) rootView.findViewById(R.id.home_page_project_more);
        share_row = (Button) rootView.findViewById(R.id.fragement_more_activity_share_row);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.home_page_project_layout);
        pname = (TextView) rootView.findViewById(R.id.home_page_project_name);
        pBaseApr = (TextView) rootView.findViewById(R.id.home_page_base_apr);
        pAwardApr = (TextView) rootView.findViewById(R.id.home_page_award_apr);
        pAccount = (TextView) rootView.findViewById(R.id.home_page_project_account);
        //pLowest = (TextView) rootView.findViewById(R.id.home_page_project_lowest);
        awardlayout = (LinearLayout) rootView.findViewById(R.id.home_page_project_award_layout);
        //investRightNow = (Button) rootView.findViewById(R.id.home_page_invest_button);
        slideShowView = (SlideShowView) rootView.findViewById(R.id.home_page_slideshowView);
        peroid = (TextView) rootView.findViewById(R.id.home_page_project_peroid);
        //活动入口
        repay_introduce = (TextView) rootView.findViewById(R.id.home_page_repay_introduce);
        repay_more = (RelativeLayout) rootView.findViewById(R.id.home_page_repay_more);
        rl_more = (RelativeLayout) rootView.findViewById(R.id.home_page_project_rl_more);
        online = (Button) rootView.findViewById(R.id.home_online);
        marqueeView = (MarqueeTextView) rootView.findViewById(R.id.marqueeview);
        online.getParent().requestDisallowInterceptTouchEvent(true);
        //项目详情入口
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexBean != null && indexBean != null) {
                    Intent intent = new Intent();
                    intent.setClass(activity, PreviewOneActivity.class);
                    intent.putExtra("itemId", indexBean.getIndexBorrow().getId());
                    activity.startActivityForResult(intent, 8090);
                }
            }
        });
        //邀请好友
        share_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = activity.getSharedPreferences("AppToken", activity.MODE_PRIVATE);
                String faRongToken = preferences.getString("loginToken", null);
                if (faRongToken != null) {
                    Intent intent = new Intent(activity, ShareToFriendsActivity.class);
                    activity.startActivity(intent);
                } else {
                    Toast.makeText(activity, "请登录后再邀请好友！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //点击第二个more
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("HomeFragmentBroadCast");
                intent.putExtra("current", 1);
                activity.sendBroadcast(intent);
            }
        });

        //当个活动入口
        rl_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (indexBean != null && indexBean.getActivity() != null) {
                    Intent intent = new Intent(activity, HuoDongDetailActivity.class);
                    intent.putExtra("id", indexBean.getActivity().getId() + "");
                    intent.putExtra("header", indexBean.getActivity().getTitle());
                    activity.startActivity(intent);
                }
            }
        });

        //更多活动入口
        repay_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, HuoDongActivity.class);
                intent.putExtra("title", "活动中心");
                activity.startActivity(intent);
            }
        });

        //避免refresh冲突
        layout5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("23234324");
            }
        });

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDown) {
                    activity.startActivity(new Intent(activity, CustomerActivity.class));
                } else {
                    return;
                }

            }
        });

        online.setOnTouchListener(new View.OnTouchListener() {
            private int tabHeight;
            int lastX, lastY; // 记录移动的最后的位置

            public boolean onTouch(View v, MotionEvent event) {
                // 获取Action
                int ea = event.getAction();
                switch (ea) {
                    case MotionEvent.ACTION_DOWN: // 按下
                        ismove=true;
                        tabHeight = Utils.dip2px(activity, 60);
                        startTime = System.currentTimeMillis();
                        isDown = false;
                        slideShowView.stopPlay();
                        marqueeView.stopFlipping();
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        screenWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
                        screenHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
                        break;
                    case MotionEvent.ACTION_MOVE: // 移动

                        // 移动中动态设置位置
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int left = v.getLeft() + dx;
                        int top = v.getTop() + dy;
                        int right = v.getRight() + dx;
                        int bottom = v.getBottom() + dy;
                        if (left < 0) {
                            left = 0;
                            right = left + v.getWidth();
                        }
                        if (right > screenWidth) {
                            right = screenWidth;
                            left = right - v.getWidth();
                        }
                        if (top < 0) {
                            top = 0;
                            bottom = top + v.getHeight();
                        }
                        if (bottom > (screenHeight-tabHeight-v.getHeight()/2)) {
                            bottom = screenHeight-tabHeight-v.getHeight()/2;
                            top = screenHeight-tabHeight-v.getHeight()-v.getHeight()/2;
                        }
                        v.layout(left, top, right, bottom);
                        // Toast.makeText(getActivity(), "position：" + left + ", " +
                        // top + ", " + right + ", " + bottom, 0)
                        // .show();
                        // 将当前的位置再次设置
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP: // 抬起
                        ismove=false;
                        RelativeLayout.LayoutParams lpFeedback = new RelativeLayout.LayoutParams(
                                Utils.dip2px(activity, 39),
                                Utils.dip2px(activity, 40));
                        lpFeedback.leftMargin = v.getLeft();
                        lpFeedback.topMargin = v.getTop();
                        lpFeedback.setMargins(v.getLeft(), v.getTop(), 0, 0);
                        v.setLayoutParams(lpFeedback);
                        slideShowView.startPlay();

                        mHandler.sendEmptyMessageDelayed(0x222,1500);
                        if (System.currentTimeMillis() - startTime < 200) {
                            isDown = true;
                        } else {
                            isDown = false;
                        }
                        break;
                }
                return false;
            }
        });

        homerefresh.setColorSchemeColors(Color.BLACK, Color.RED);

        homerefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RequestForListData(url);
            }
        });
        url = AppConstants.URL_SUFFIX + "/rest/indexH?way=1";
        RequestForListData(url);
        RequestForListDataMsg(AppConstants.URL_SUFFIX + "/rest/articleList/");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //请求网络数据
    public void RequestForListData(String url) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                final String string = activity.getSharedPreferences("stockpile1", Context.MODE_PRIVATE).getString("stockpile1", null);
                try {
                    indexBean = gson.fromJson(string, IndexBean.class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homerefresh.setRefreshing(false);
                            Toast.makeText(activity, "连接网络失败", Toast.LENGTH_SHORT).show();
                            if (string != null) {
                                Message msg = new Message();
                                msg.what = 0x111;
                                mHandler.sendMessage(msg);
                            }
                        }
                    });

                } catch (Exception e1) {
                    Log.e("主页", e1.toString());
                }
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String s = response.body().string();
                Log.i(TAG, s);
                try{
                    indexBean = gson.fromJson(s, IndexBean.class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            homerefresh.setRefreshing(false);
                            if (indexBean.getRcd().equals("R0001")) {
                                activity.getSharedPreferences("stockpile1", Context.MODE_PRIVATE).
                                        edit().putString("stockpile1", s).commit();
                                Message msg = new Message();
                                msg.what = 0x111;
                                mHandler.sendMessage(msg);
                            }
                        }
                    });
                }catch (Exception e){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"服务器维护中.....",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    //消息接口
    public void RequestForListDataMsg(String basicUrl) {

        String url = basicUrl + "app_site_notice";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("pager.pageNumber", "1").
                add("pager.pageSize", "3").build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
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
                String s = response.body().string();
                Log.i(TAG, "response json:" + s);
                try {
                    final ArticleList articleList = JSON.parseObject(s, ArticleList.class);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final List<ArticleItem> articleItemList = articleList.getArticleItemList();
                            final List<String> info = new ArrayList<>();
                            final List<String> info1 = new ArrayList<>();
                            info.clear();
                            info1.clear();
                            for (ArticleItem item : articleItemList) {
                                if (item.getTitle().length() > 20) {
                                    info.add(item.getTitle().substring(0, 20) + "......");
                                } else {
                                    info.add(item.getTitle());
                                }
                                info1.add(item.getTitle());
                            }
                            marqueeView.startWithList(info, info1);
                            //每一项的点击事件监听

                            marqueeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int position = 0;
                                    TextView tv = (TextView) marqueeView.getCurrentView();
                                    String tag = (String) tv.getTag();
                                    if (tag.equals(articleItemList.get(0).getTitle())) {
                                        position = 0;
                                    } else if (tag.equals(articleItemList.get(1).getTitle())) {
                                        position = 1;
                                    } else if (tag.equals(articleItemList.get(2).getTitle())) {
                                        position = 2;
                                    }
                                    ArticleItem item = articleItemList.get(position);
                                    Intent intent = new Intent(activity, ArticleActivity.class);
                                    intent.putExtra("id", item.getId() + "");
                                    intent.putExtra("header", tag);
                                    activity.startActivity(intent);
                                }
                            });
                        }
                    });
                }catch (Exception e){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity,"服务器维护中.....",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent();
        String urlString = "";
        if (targetUrllist.size() > 3) {
            urlString = targetUrllist.get(position);
        } else {
            urlString = targetUrllist.get(position % targetUrllist.size());
        }

        if (urlString.contains("article")) {
            intent.setClass(activity, ArticleActivity.class);

            int startIndex = urlString.lastIndexOf("/");
            String lastHalf = urlString.substring(startIndex + 1, urlString.length());
            System.out.println("滚公试图--" + lastHalf);
            String articleId = lastHalf.substring(0, lastHalf.length() - 4);
            System.out.println("滚公试图Id--" + lastHalf);
            intent.putExtra("header", "文章详情");
            intent.putExtra("id", articleId);

        } else if (urlString.contains("activity")) {
            intent.setClass(activity, HuoDongDetailActivity.class);

            int startIndex = urlString.lastIndexOf("=");
            String articleId = urlString.substring(startIndex + 1, urlString.length());
            System.out.println("活动Id--" + articleId);


            intent.putExtra("header", "活动详情");
            intent.putExtra("id", articleId);
        } else {
            System.out.println("-------------------------no click--------------------------------");
            return;
        }
        activity.startActivity(intent);
    }


    @Override
    public void onStop() {
        Log.i(TAG, "home page fragment on stop");
        super.onStop();
        CustomApplication.newInstance().getRequestQueue().cancelAll(this);

    }

    public View getmView() {
        return rootView;
    }
}
