package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Adapters.ArticleListAdapter;
import cn.ysgroup.ysdai.Beans.Article.ArticleItem;
import cn.ysgroup.ysdai.Beans.Article.ArticleList;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadMoreListView;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.Utils;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ArticleListActivity extends MyBaseActivity implements LoadMoreListView.OnLoadMore, SwipeRefreshLayout.OnRefreshListener, ListView.OnItemClickListener{

    String BASIC_URL = AppConstants.URL_SUFFIX+"/rest/articleList/";
    private final String TAG = "文章列表";
    private  String title;
    private String type;
    private LoadMoreListView atricleListView;
    private ImageView nothingImg;
    private Toolbar atricleListToolBar;
    private IconFontTextView atriclelistToolBarBack;
    private TextView atriclelistToolTitle;
    private SwipeRefreshLayout articleSwipeLayout;

    private final int PAGESIZE = 20;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private ArticleListAdapter myAdapter;
    private List<ArticleItem> articleItemList =new ArrayList<>();

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x111:
                    //刷新数据
                    nothingImg.setVisibility(View.GONE);
                    myAdapter = new ArticleListAdapter(ArticleListActivity.this, articleItemList);
                    myAdapter.setType(type);
                    atricleListView.setAdapter(myAdapter);
                    if (articleSwipeLayout.isShown())
                    {
                        articleSwipeLayout.setRefreshing(false);
                    }
                    break;
                case 0x222:
                    //直接添加数据
                    nothingImg.setVisibility(View.GONE);
                    atricleListView.onLoadComplete();
                    myAdapter.setArticleItemList(articleItemList);
                    myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    private boolean isTime;


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArticleItem item = articleItemList.get(position);
        if(type.equals("app_help_center")){
            TextView help = (TextView) view.findViewById(R.id.article_list_help_tx);
            ImageView iv = (ImageView) view.findViewById(R.id.article_list_iv);
            RelativeLayout.LayoutParams params=
                    new RelativeLayout.LayoutParams(Utils.dip2px(ArticleListActivity.this,15),
                            Utils.dip2px(ArticleListActivity.this,8));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.rightMargin=Utils.dip2px(ArticleListActivity.this,15);
            iv.setLayoutParams(params);
            if(item.isOpen){
                //关闭
                item.isOpen=false;
                help.setVisibility(View.GONE);
                iv.setBackgroundResource(R.mipmap.drow_icon);
                help=null;
                iv=null;
            }else {
                //打开
                item.isOpen=true;
                help.setVisibility(View.VISIBLE);
                iv.setBackgroundResource(R.mipmap.up_icon);
                help=null;
                iv=null;
            }
        }else{
            Intent intent = new Intent(ArticleListActivity.this, ArticleActivity.class);
            intent.putExtra("id",item.getId()+"");
            intent.putExtra("header",title);
            startActivity(intent);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_list);
        title=getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        isTime = getIntent().getBooleanExtra("isTime", false);
        initToolBar(title);

        atricleListView = (LoadMoreListView) findViewById(R.id.article_list_list_view);
        nothingImg =(ImageView)findViewById(R.id.article_list_nothing_img);
        articleSwipeLayout =(SwipeRefreshLayout) findViewById(R.id.article_list_swipe_layout);

        myAdapter = new ArticleListAdapter(ArticleListActivity.this,articleItemList);
        atricleListView.setAdapter(myAdapter);
        atricleListView.setLoadMoreListen(this);
        articleSwipeLayout.setOnRefreshListener(this);
        articleSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);

        atricleListView.setOnItemClickListener(this);

        RequestForListData(BASIC_URL, type,1,PAGESIZE,true);

    }

    public void initToolBar(String title) {

        atricleListToolBar =(Toolbar)findViewById(R.id.article_list_toolbar);
        atriclelistToolTitle =(TextView)findViewById(R.id.article_list_toolbar_title);
        atriclelistToolBarBack =(IconFontTextView)findViewById(R.id.article_list_toolbar_back);
        atriclelistToolBarBack.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finish();
                    }
                }
        );
        atriclelistToolTitle.setText(title);
        setSupportActionBar(atricleListToolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


    }

    //请求网络数据
    public void RequestForListData(String basicUrl, String type, int pageNumber, int pageSize, final boolean refreshing) {

        String url = basicUrl + type;
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("pager.pageNumber",pageNumber+"").
                add("pager.pageSize", pageSize+"").build();
        Request request = new Request.Builder().url(url).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
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
                String s=response.body().string();
                Log.i(TAG, "response json:" + s);
                final ArticleList articleList = JSON.parseObject(s, ArticleList.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (articleList.getRcd().equals("R0001")) {

                            totalPageCount = articleList.getPageBean().getPageCount();
                            articleItemList.addAll(articleList.getArticleItemList());
                            if (refreshing)
                            {
                                if(articleList.getArticleItemList()!=null&&articleList.getArticleItemList().size()>0)
                                {
                                    Message msg = new Message();
                                    msg.what = 0x111;
                                    mHandler.sendMessage(msg);
                                }else{
                                    nothingImg.setVisibility(View.VISIBLE);
                                    if (articleSwipeLayout.isShown())
                                    {
                                        articleSwipeLayout.setRefreshing(false);
                                    }
                                }
                            } else
                            {
                                Message msg = new Message();
                                msg.what = 0x222;
                                mHandler.sendMessage(msg);
                            }
                        } else {
                            if (refreshing)
                            {
                                if (articleSwipeLayout.isShown())
                                {
                                    articleSwipeLayout.setRefreshing(false);
                                }
                            } else
                            {
                                atricleListView.onLoadComplete();
                            }
                            Toast.makeText(ArticleListActivity.this, "请求数据失败，"+articleList.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    @Override
    public void loadMore()
    {
        if (currentBottomPageIndex < totalPageCount)
        {
            currentBottomPageIndex++;
            new Handler().postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    RequestForListData(BASIC_URL,type, currentBottomPageIndex, PAGESIZE, false);
                }
            }, 500);
        } else
        {
            atricleListView.onLoadComplete();
            return;
        }
    }

    @Override
    public void onRefresh()
    {
        currentBottomPageIndex = 1;
        articleItemList.clear();
        myAdapter.notifyDataSetInvalidated();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                RequestForListData(BASIC_URL,type, 1, PAGESIZE, true);
            }

        }, 500);
    }

    public boolean isTime(){
        return isTime;
    }


}
