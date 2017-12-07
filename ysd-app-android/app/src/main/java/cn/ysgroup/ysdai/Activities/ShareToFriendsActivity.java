package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import cn.ysgroup.ysdai.Adapters.HongBaoFriendsAdapter;
import cn.ysgroup.ysdai.Beans.user.MyFriendBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LoadMoreListView;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShareToFriendsActivity extends MyBaseActivity
{

    @Bind(R.id.share_to_friends_toolbar_back)
    IconFontTextView shareToFriendsToolbarBack;
    @Bind(R.id.share_to_friends_toolbar_title)
    TextView shareToFriendsToolbarTitle;
    @Bind(R.id.share_to_friends_toolbar)
    Toolbar shareToFriendsToolbar;
    @Bind(R.id.hongbao_friends_list_view)
    LoadMoreListView hongbaoFriendsListView;
    @Bind(R.id.share_to_friends_share_button)
    Button shareToFriendsShareButton;
    @Bind(R.id.share_to_friends_extra)
    TextView shareToFriendsExtra;
    private Gson gson=new Gson();
    private String TAG = "我的好友";
    private String BASIC_URL = AppConstants.URL_SUFFIX + "/rest/friendsList";

    private List<MyFriendBean.FriendListBean> friendList = new ArrayList<MyFriendBean.FriendListBean>();

    private final int PAGESIZE = 10;//一页的条目数
    private int totalPageCount;//总页数
    private int currentBottomPageIndex = 1;//已经加载的页数

    private HongBaoFriendsAdapter myAdapter;
    private LoadingDialog loadingDialog;

    private UMSocialService mController;
    private boolean isShow;
    private String stagNo;//分享的链接
    private String spreadText;//邀请好友文字
    private String spreadTextarea;//邀请好友奖励说明

    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0x111:
                    if(friendList.size()>0){
                        no.setVisibility(View.GONE);
                    }
                    //刷新数据
                    myAdapter = new HongBaoFriendsAdapter(ShareToFriendsActivity.this, friendList, getLayoutInflater());
                    hongbaoFriendsListView.setAdapter(myAdapter);
                    break;
                case 0x222:
                    if(friendList.size()>0){
                        no.setVisibility(View.GONE);
                    }
                    //直接添加数据
                    shareToFriendsExtra.setText(spreadTextarea);
                    myAdapter.setFriendsList(friendList);
                    myAdapter.notifyDataSetChanged();
                    if (stagNo != null)
                    {
                        initData(stagNo, spreadText, spreadTextarea);
                        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN, SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
                        final SocializeListeners.SnsPostListener mSnsPostListener = new SocializeListeners.SnsPostListener()
                        {

                            @Override
                            public void onStart()
                            {

                            }

                            @Override
                            public void onComplete(SHARE_MEDIA platform, int stCode,
                                                   SocializeEntity entity)
                            {
                                if (stCode == 200)
                                {
                                    Toast.makeText(ShareToFriendsActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                                } else
                                {
                                    Toast.makeText(ShareToFriendsActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        mController.registerListener(mSnsPostListener);
                    }
                    break;
            }
        }
    };
    private TextView no;
    private UMSsoHandler ssoHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_to_friends);
        ButterKnife.bind(this);
        no = (TextView) findViewById(R.id.hongbao_friends_no);
        initToolBar();

        myAdapter = new HongBaoFriendsAdapter(this, friendList, getLayoutInflater());
        hongbaoFriendsListView.setAdapter(myAdapter);
        RequestForListData(BASIC_URL, 1, PAGESIZE, false);

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
        shareToFriendsToolbarBack.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        finish();
                    }
                }
        );
        setSupportActionBar(shareToFriendsToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @OnClick(R.id.share_to_friends_share_button)
    public void OnShareClick()
    {

        if (mController != null)
        {
            mController.openShare(ShareToFriendsActivity.this, false);
        } else
        {
            Toast.makeText(ShareToFriendsActivity.this, "请登录", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 请求好友列表
     *
     * @param basicUrl
     * @param pageNumber
     * @param pageSize
     * @param refreshing
     */
    public void RequestForListData(String basicUrl, int pageNumber, int pageSize, final boolean refreshing)
    {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("token", PreferenceUtil.getPrefString(this, "loginToken", "")).
                add("pager.pageNumber", pageNumber + "").
                add("pager.pageSize", pageSize + "").build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (e != null && e.getMessage() != null) {
                            if (refreshing)
                            {
                            } else
                            {
                                hongbaoFriendsListView.onLoadComplete();
                            }
                            Toast.makeText(ShareToFriendsActivity.this, "连接网络失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, "" + s);

                final MyFriendBean resultBean =  gson.fromJson(s, MyFriendBean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001"))
                        {
                            stagNo = resultBean.getTgNo();
                            spreadText = resultBean.getSpreadText();
                            spreadTextarea = resultBean.getSpreadTextarea();
                            totalPageCount = resultBean.getPageBean().getPageCount();
                            friendList.addAll(resultBean.getFriendList());
                            Message msg = new Message();
                            msg.what = 0x222;
                            mHandler.sendMessage(msg);
                        } else
                        {
                            Toast.makeText(ShareToFriendsActivity.this, "" + resultBean.getRmg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //分享初始化
    private void initData(String shareUrl, String spreadText, String spreadTextarea)
    {

        // 首先在您的Activity中添加如下成员变量
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        // 设置分享内容
        mController.setShareContent(spreadText);
        // 设置分享图片, 参数2为图片的url地址
        mController.setShareMedia(new UMImage(ShareToFriendsActivity.this, R.mipmap.ic_launcher));

        mController.setShareBoardListener(new SocializeListeners.UMShareBoardListener()
        {

            @Override
            public void onShow()
            {
                isShow = true;
            }

            @Override
            public void onDismiss()
            {
                isShow = false;
            }
        });


        initWeiXin(spreadText, shareUrl);
        initWeiXinPY(spreadText, shareUrl);
        initQQ(spreadText, shareUrl);
        initQQZone(spreadText, shareUrl);

    }

    private void initWeiXin(String shareContent, String shareUrl)
    {
        String appID = "wx021f5a9de35cd071";
        String appSecret = "a5c02074924673cb9f9cd271d6d33096";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(ShareToFriendsActivity.this, appID, appSecret);
        // 设置Title
        wxHandler.setTitle("乐商贷,灵活投资,轻松理财。");

        wxHandler.showCompressToast(false);

        // 设置分享内容
        mController.setShareContent(shareContent);
        // 设置URL
        wxHandler.setTargetUrl(shareUrl);
        wxHandler.addToSocialSDK();
    }

    /**
     * 朋友圈支持纯文字，纯图片（点击查看大图），图文，音乐，视频分享。SSO设置
     */
    private void initWeiXinPY(String shareContent, String shareUrl)
    {
        String appID = "wx021f5a9de35cd071";
        String appSecret = "a5c02074924673cb9f9cd271d6d33096";
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(ShareToFriendsActivity.this, appID, appSecret);
        // 设置Title
        wxCircleHandler.setTitle("乐商贷,灵活投资,轻松理财。");
        wxCircleHandler.showCompressToast(false);
        // 设置分享内容
        mController.setShareContent(shareContent);
        // 设置URL
        wxCircleHandler.setTargetUrl(shareUrl);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }

    /**
     * QQ SSO设置
     */
    private void initQQ(String shareContent, String shareUrl)
    {

        String appID = "1106230643";
        String appKEY = "K5SN62TY23VkA5RM";
        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ShareToFriendsActivity.this, appID, appKEY);
        qqSsoHandler.addToSocialSDK();

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(shareContent);
        qqShareContent.setTitle("乐商贷,灵活投资,轻松理财。");
        qqShareContent.setShareImage(new UMImage(ShareToFriendsActivity.this,R.mipmap.ic_launcher));
        qqShareContent.setTargetUrl(shareUrl);
        mController.setShareMedia(qqShareContent);

    }

    /**
     * QQ空间SSO设置
     */
    private void initQQZone(String shareContent, String shareUrl)
    {

        String appID = "1106230643";
        String appKEY = "K5SN62TY23VkA5RM";
        // 参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(ShareToFriendsActivity.this, appID, appKEY);
        qZoneSsoHandler.setTargetUrl(shareUrl);
        qZoneSsoHandler.addToSocialSDK();

        QZoneShareContent QZoneShareContent = new QZoneShareContent();
        QZoneShareContent.setTitle("乐商贷,灵活投资,轻松理财。");
        QZoneShareContent.setShareContent(shareContent);
        QZoneShareContent.setShareImage(new UMImage(ShareToFriendsActivity.this, R.mipmap.ic_launcher));
        QZoneShareContent.setTargetUrl(shareUrl);
        mController.setShareMedia(QZoneShareContent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */
        try {
            ssoHandler = mController.getConfig().getSsoHandler(requestCode);
        }catch (Exception e){
            Log.e("ShareToFriendsActivity",e.toString());
        }
        if (ssoHandler != null)
        {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        sendBroadcast(new Intent("LoginContentBroadCast"));
        super.onDestroy();
    }


}
