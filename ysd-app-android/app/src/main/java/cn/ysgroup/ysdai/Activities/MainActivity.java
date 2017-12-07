package cn.ysgroup.ysdai.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.ActivityToFragmentMessage;
import cn.ysgroup.ysdai.Beans.InnerTransBeans.FragmenToActivityMessage;
import cn.ysgroup.ysdai.Beans.VersionBean;
import cn.ysgroup.ysdai.Beans.index.IndexImageItem;
import cn.ysgroup.ysdai.Beans.reguser.RegUserBean;
import cn.ysgroup.ysdai.EventBus.EventBus;
import cn.ysgroup.ysdai.Fragments.HomeFragment;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Service.UpdateService;
import cn.ysgroup.ysdai.UI.DownDialog;
import cn.ysgroup.ysdai.UI.HomeDialog;
import cn.ysgroup.ysdai.UI.MessageDialog;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.AppVersionUtils;
import cn.ysgroup.ysdai.Util.BitmapCache;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends MyBaseActivity {

    private LayoutInflater mLayoutInflater;
    PopupWindow menuWindow;
    private Toolbar mainToolbar;
    private TextView toolbarTitle;
    private Button maskCancelButton;
    private  Gson gson=new Gson();

    private static int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 10;
    private static int READ_PHONE_STATE_REQUEST_CODE = 9;
    //下载apk的地址
    private String DOWNLOAD_APK_URL;
    //下载apk存放的文件夹
    private String SAVE_APK_PATH = Environment.getExternalStorageDirectory().getPath() + "/YueShangDai/upgrade/";
    //下载的APK的文件名
    private String DOWNLOAD_APK_NAME = "YueShangDai";
    //通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    NotificationCompat.Builder mBuilder = null;


    private long exitTime = 0;
    private ImageLoader mImageLoader;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        int a=getWindowManager().getDefaultDisplay().getHeight();
//        int b=getWindowManager().getDefaultDisplay().getWidth();

        //注册接收消息
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);
        initToolBar();
        initView();
        mImageLoader = new ImageLoader(CustomApplication.newInstance().getRequestQueue(), new BitmapCache());
        //检查更新
        String basicUrl = AppConstants.URL_SUFFIX + "/rest/version";
        RequestForSatusData(basicUrl);
        //请求弹出层图片
        String imgPicUrl = AppConstants.URL_SUFFIX + "/rest/scrollpic?way=4";
        RequestForPopupPic(imgPicUrl);

        //检查权限
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    READ_PHONE_STATE_REQUEST_CODE);
        } else {
            new Handler().postDelayed(
                    new Runnable() {
                        @Override
                        public void run() {
                            int appRunCount = getSharedPreferences("AppToken", MODE_PRIVATE).getInt("appRunCount", 0);
                            if (appRunCount == 0) {
                                String url = AppConstants.URL_SUFFIX + "/rest/firstStarting";
                                SubmitRunLabel(url);
                            }
                        }
                    }
                    , 2000);
        }
        //替换界面
        HomeFragment homeFragment = new HomeFragment(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, homeFragment).commit();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    private void initView() {
        preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
        maskCancelButton = (Button) findViewById(R.id.main_mask_button);

    }


    /**
     * 初始化actionbars
     */
    public void initToolBar() {
        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.main_toolbar_title_text);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public void onEventMainThread(FragmenToActivityMessage mfe) {
        switch (mfe.getTag()) {

            case 1003:
                //被强推
                //现在仅仅有一个自定义的通知（即登录被抢占）

                final MessageDialog messageDialog = new MessageDialog(this);
                messageDialog.setMessage("您的账号已经在其他设备登录!");
                messageDialog.setCanceledOnTouchOutside(false);
                messageDialog.setOnPositiveListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.clear().commit();
                                messageDialog.dismiss();
                                messageDialog.cancel();
                            }
                        }
                );
                messageDialog.show();

            case 9999:
                //token错误,要求重新登陆
                SharedPreferences preferences = getSharedPreferences("AppToken", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().commit();
                mainToolbar.setVisibility(View.GONE);
                toolbarTitle.setText("登录");
//                toolbarRightIcon.setVisibility(View.GONE);
//                mTabHost.ReplaceOneTabFragment(currentTabId, LoginFragment.class, null);


            default:

                break;
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void SetBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == AppConstants.AppLoginEXitCode) {
            toolbarTitle.setText("登录");
            mainToolbar.setVisibility(View.GONE);
        } else if (resultCode == AppConstants.PreviewToLoginCode) {

        } else if (resultCode == AppConstants.PreviewToInvestOthersCode) {

        } else if (resultCode == AppConstants.IntroToInvestCode) {
            int bessType = data.getIntExtra("bussinessType", 1);
            ActivityToFragmentMessage atfmessage = new ActivityToFragmentMessage();
            atfmessage.setTag(2001);
            atfmessage.setMessageInt(bessType);
            EventBus.getDefault().post(atfmessage);
        } else if (resultCode == AppConstants.ChargeMoneyResultBack) {
            ActivityToFragmentMessage atfmessage = new ActivityToFragmentMessage();
            atfmessage.setTag(2002);
            EventBus.getDefault().post(atfmessage);

        } else if (resultCode == AppConstants.registerSuccessCode) {
            toolbarTitle.setText("会员中心");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 提交第一次运行的信息
     */
    private void SubmitRunLabel(String basicUrl) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("appType","1").
                add("placeName",AppVersionUtils.getChannelName(this, "UMENG_CHANNEL") + "").
                add("im",AppVersionUtils.getPhoneIMEI(this)).
                add("deviceToken",PreferenceUtil.getPrefString(this, "deviceToken", "")).
                add("welcome", AppVersionUtils.getPhoneIMEI(this)).build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();

                Log.i("welcome", s);
                try{
                    final RegUserBean resultBack = new Gson().fromJson(s, RegUserBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (resultBack.getRcd().equals("R0001")) {
                                PreferenceUtil.setPrefInt(MainActivity.this, "appRunCount", 1);
                            }
                        }
                    });
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"服务器维护中.....",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    //请求网络数据
    public void RequestForSatusData(String basicUrl) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().
                add("appType","1").
                add("placeName",AppVersionUtils.getChannelName(this, "UMENG_CHANNEL") + "").
                add("im",AppVersionUtils.getPhoneIMEI(this)).
                add("deviceToken",PreferenceUtil.getPrefString(this, "deviceToken", "")).
                add("welcome", AppVersionUtils.getPhoneIMEI(this)).build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "连接网络失败,请检查网络！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                Log.v("版本",s);
                try {
                    final VersionBean userBean =gson.fromJson(s, VersionBean.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String currentVersionName = AppVersionUtils.getVersionName(MainActivity.this);
                            int currentVersionCode = AppVersionUtils.getVersionCode(MainActivity.this);
                            if (userBean.getVersionName() != null && userBean.getUrl() != null) {
                                if (currentVersionCode < userBean.getVersionCode().intValue() && !currentVersionName.equals(userBean.getVersionName())) {
                                    DOWNLOAD_APK_URL = userBean.getUrl();
                                    final DownDialog downDialog=new DownDialog(MainActivity.this,userBean.getVersionName()
                                            ,userBean.getRmg());
                                    downDialog.show();
                                    downDialog.getNow().setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            downDialog.dismiss();
                                            //检查权限
                                            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                    != PackageManager.PERMISSION_GRANTED) {
                                                //申请WRITE_EXTERNAL_STORAGE权限
                                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                                            } else {
                                                //直接下载
                                                DownLoadAPK();
                                            }
                                        }
                                    });
//
                                }
                            }
                        }
                    });
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"服务器维护中.....",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @SuppressLint("ParcelCreator")
    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);

            if (updateNotificationManager == null) {
                updateNotificationManager = (NotificationManager)
                        MainActivity.this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
            }

            if (resultCode == UpdateService.DOWNLOAD_PREPARE) {
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this);
                }
                mBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                        .setContentText("准备下载新版本") //设置通知栏显示内容
                        .setTicker("下载新版本") //通知首次出现在通知栏，带上升动画效果的
                        // .setContentIntent(updatePendingIntent) //设置通知栏点击意图
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON

                updateNotification = mBuilder.build();
                updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                //发出通知
                updateNotificationManager.notify(0, updateNotification);
            } else if (resultCode == UpdateService.DOWNLOAD_WORK) {
                int pregress = resultData.getInt("progress");
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this);
                }
                mBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                        .setContentText("已经下载" + pregress + "%"); //设置通知栏显示内容
                updateNotification = mBuilder.build();
                updateNotificationManager.notify(0, updateNotification);
            } else if (resultCode == UpdateService.DOWNLOAD_OK) {
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this);
                }
                mBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                        .setContentText("下载成功") //设置通知栏显示内容
                        .setTicker("下载成功") //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON

                updateNotification = mBuilder.build();
                updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                //发出通知
                updateNotificationManager.notify(0, updateNotification);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(SAVE_APK_PATH + DOWNLOAD_APK_NAME + ".apk")), "application/vnd.android.package-archive");
                ;
                startActivity(intent);
            } else if (resultCode == UpdateService.DOWNLOAD_ERROR) {
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(MainActivity.this);
                }
                mBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                        .setContentText("下载失败") //设置通知栏显示内容
                        .setTicker("下载失败") //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON

                updateNotification = mBuilder.build();
                updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                //发出通知
                updateNotificationManager.notify(0, updateNotification);
                Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //请求弹出层图片
    private void RequestForPopupPic(String basicUrl) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(basicUrl)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                Log.i("请求弹出层图片", s);
                try{
                    final IndexImageItem userBean=gson.fromJson(s,IndexImageItem.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (userBean != null && userBean.getImageUrl() != null) {
                                SharedPreferences sharedPreferences = getSharedPreferences(
                                        "Setting", Context.MODE_PRIVATE);
                                String currentPicName = sharedPreferences.getString("picName", "");

                                if (!currentPicName.equals(userBean.getImageUrl())) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("handled", false);
                                    editor.putString("picName", userBean.getImageUrl());
                                    editor.putString("typeTarget", userBean.getTypeTarget());
                                    editor.commit();
                                    System.out.println(userBean.getImageUrl());
                                    setMask(userBean.getImageUrl());
                                } else {
                                    boolean handled = sharedPreferences.getBoolean("handled", false);
                                    if (!handled) {
                                        setMask(userBean.getImageUrl());
                                    }
                                }
                            }
                        }
                    });
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this,"服务器维护中.....",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    //设置蒙版
    public void setMask(String imgUrl) {
        HomeDialog homeDialog = new HomeDialog(this, R.style.ActionSheetDialogStyle);
        homeDialog.setImage(imgUrl);
        //homeDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try{
            if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    DownLoadAPK();
                } else {
                }
            } else if (requestCode == READ_PHONE_STATE_REQUEST_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Handler().postDelayed(
                            new Runnable() {
                                @Override
                                public void run() {
                                    int appRunCount = preferences.getInt("appRunCount", 0);
                                    if (appRunCount == 0) {
                                        String url = AppConstants.URL_SUFFIX + "/rest/firstStarting";
                                        SubmitRunLabel(url);
                                    }
                                }
                            }
                            , 2000);
                } else {
                }
            }
        }catch (Exception e){
            Log.e("MianActivity",e.toString());
        }
    }


    private void DownLoadAPK() {
        //后台下载
        String suffix = new SimpleDateFormat("yyyyMMdd").format(new Date());
        DOWNLOAD_APK_NAME = DOWNLOAD_APK_NAME + suffix;
        Intent updateIntent = new Intent(MainActivity.this, UpdateService.class);
        updateIntent.putExtra("apkUrl", DOWNLOAD_APK_URL);
        updateIntent.putExtra("apkName", DOWNLOAD_APK_NAME);
        updateIntent.putExtra("receiver", new DownloadReceiver(new Handler()));
        startService(updateIntent);

    }
}
