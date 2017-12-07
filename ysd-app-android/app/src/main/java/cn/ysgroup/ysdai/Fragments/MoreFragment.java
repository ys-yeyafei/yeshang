package cn.ysgroup.ysdai.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import cn.ysgroup.ysdai.Activities.ArticleListActivity;
import cn.ysgroup.ysdai.Activities.CustomerActivity;
import cn.ysgroup.ysdai.Activities.FeedBackActivity;
import cn.ysgroup.ysdai.Activities.MainActivity;
import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.VersionBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Service.UpdateService;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.AppVersionUtils;
import cn.ysgroup.ysdai.Util.PreferenceUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;


public class MoreFragment extends BaseFragment {


    PopupWindow menuWindow;
//    @Bind(R.id.fragement_more_activity_center_row)
//    RelativeLayout fragementMoreActivityCenterRow;


    private final String TAG = "更多";

    String basicUrl = AppConstants.URL_SUFFIX + "/rest/logout";
    private int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 10;
    //下载apk的地址h
    private String DOWNLOAD_APK_URL;
    //下载apk存放的文件夹
    private String SAVE_APK_PATH = Environment.getExternalStorageDirectory().getPath() + "/YueShangDai/upgrade/";
    //下载的APK的文件名
    private String DOWNLOAD_APK_NAME = "YueShangDai";
    //通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    NotificationCompat.Builder mBuilder = null;
    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;
    private View rootView;
    private RelativeLayout fragementMoreActivityNotificationRow;
    private RelativeLayout fragementMoreActivityAboutUsRow;
    private RelativeLayout fragementMoreActivityHelpCenterRow;
    private RelativeLayout fragementMoreActivityKfRow;
    private RelativeLayout fragementMoreActivityUpdateRow;
    private TextView moreUpdateCurrentVersion;
    private RelativeLayout fragmentMoreFeedbackRow;
    private Button exit;
    private PopupWindow popupWindow;
    private MoreBroadCast mbc;
    private MainActivity activity;

    public MoreFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public MoreFragment(final MainActivity activity) {

        this.activity = activity;
        rootView = View.inflate(activity, R.layout.fragment_more, null);
        initView();
        String currentVersionName = AppVersionUtils.getVersionName(activity);
        moreUpdateCurrentVersion.setText("V" + currentVersionName);
        //判断是否是登入状态
        SharedPreferences preferences = activity.getSharedPreferences("AppToken", activity.MODE_PRIVATE);
        String faRongToken = preferences.getString("loginToken", null);
        if (faRongToken != null) {
            exit.setVisibility(View.VISIBLE);
        } else {
            exit.setVisibility(View.GONE);
        }
        //消息公告
        fragementMoreActivityNotificationRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ArticleListActivity.class);
                intent.putExtra("title", "消息公告");
                intent.putExtra("type", "app_site_notice");
                intent.putExtra("isTime", true);
                activity.startActivity(intent);
            }
        });

        //关于我们
        fragementMoreActivityAboutUsRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ArticleListActivity.class);
                intent.putExtra("title", "关于我们");
                intent.putExtra("type", "app_user_hand");
                activity.startActivity(intent);
            }
        });

        //帮助中心
        fragementMoreActivityHelpCenterRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ArticleListActivity.class);
                intent.putExtra("title", "帮助中心");
                intent.putExtra("type", "app_help_center");
                activity.startActivity(intent);
            }
        });


        //联系我们
        fragementMoreActivityKfRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, CustomerActivity.class);
                activity.startActivity(intent);
            }
        });
        //版本更新
        fragementMoreActivityUpdateRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String basicUrl = AppConstants.URL_SUFFIX + "/rest/version";
                RequestForSatusData(basicUrl);
            }
        });
        //意见反馈
        fragmentMoreFeedbackRow.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(activity, FeedBackActivity.class);
                        activity.startActivity(intent);
                    }
                }
        );

        exit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        initPopuptWindowExit();
                        popupWindow.showAtLocation(fragementMoreActivityHelpCenterRow, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        SetBackgroundAlpha(0.5f);
                    }
                }
        );
    }

    private void initView() {
        fragementMoreActivityNotificationRow = (RelativeLayout) rootView.findViewById(R.id.fragement_more_activity_notification_row);
//        RelativeLayout fragementMoreActivityShareRow= (RelativeLayout) rootView.findViewById(R.id.fragement_more_activity_share_row);
        fragementMoreActivityAboutUsRow = (RelativeLayout) rootView.findViewById(R.id.fragement_more_activity_about_us_row);
        fragementMoreActivityHelpCenterRow = (RelativeLayout) rootView.findViewById(R.id.fragement_more_activity_help_center_row);
        fragementMoreActivityKfRow = (RelativeLayout) rootView.findViewById(R.id.fragement_more_activity_kf_row);
        fragementMoreActivityUpdateRow = (RelativeLayout) rootView.findViewById(R.id.fragement_more_activity_update_row);
        moreUpdateCurrentVersion = (TextView) rootView.findViewById(R.id.more_update_current_version);
        fragmentMoreFeedbackRow = (RelativeLayout) rootView.findViewById(R.id.fragment_more_feedback_row);
        exit = (Button) rootView.findViewById(R.id.app_exit_button);
        mbc = new MoreBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("MoreBroadCast");
        activity.registerReceiver(mbc, filter);
    }


    //请求网络数据
    public void RequestForSatusData(String basicUrl) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(basicUrl)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "连接网络失败,请检查网络！", Toast.LENGTH_SHORT).show();
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
                final VersionBean userBean = JSON.parseObject(s, VersionBean.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String currentVersionName = AppVersionUtils.getVersionName(activity);
                        int currentVersionCode = AppVersionUtils.getVersionCode(activity);

                        if (userBean.getVersionName() != null && userBean.getUrl() != null) {
                            if (currentVersionCode < userBean.getVersionCode().intValue() && !currentVersionName.equals(userBean.getVersionName())) {
                                DOWNLOAD_APK_URL = userBean.getUrl();
                                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                builder.setTitle("检查新版本");
                                builder.setCancelable(false);
                                builder.setMessage("发现新版本" + userBean.getVersionName() + "，是否现在下载新版本？");
                                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @TargetApi(Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        //检查权限
                                        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                != PackageManager.PERMISSION_GRANTED) {
                                            //申请WRITE_EXTERNAL_STORAGE权限
                                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
                                        } else {
                                            //直接下载
                                            DownLoadAPK();
                                        }
                                    }
                                });
                                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                builder.show();

                            } else {
                                Toast.makeText(activity, "当前已是最新版本", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });
    }


//    protected void initPopuptWindow() {
//        if (menuWindow == null) {
//            // 获取自定义布局文件activity_popupwindow_left.xml的视图
//            View popupWindow_view = activity.getLayoutInflater().inflate(R.layout.poput_kefu_phone, null,
//                    false);
//
//            Button cancel = (Button) popupWindow_view.findViewById(R.id.poput_cancel);
//            Button confirm = (Button) popupWindow_view.findViewById(R.id.poput_phone);
//
//            confirm.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (menuWindow != null && menuWindow.isShowing()) {
//                                menuWindow.dismiss();
//                                menuWindow = null;
//                            }
//                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000333113"));
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            activity.startActivity(intent);
//                        }
//                    }
//            );
//            cancel.setOnClickListener(
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (menuWindow != null && menuWindow.isShowing()) {
//                                menuWindow.dismiss();
//                                menuWindow = null;
//                            }
//                        }
//                    }
//
//            );
//
//            // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
//            menuWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//            // 设置动画效果
//            menuWindow.setAnimationStyle(R.style.AnimationFade);
//            menuWindow.setOnDismissListener(
//                    new PopupWindow.OnDismissListener() {
//                        @Override
//                        public void onDismiss() {
//                            SetBackgroundAlpha(1f);
//                        }
//                    }
//
//            );
//        }
//    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void SetBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getmView() {
        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                DownLoadAPK();
            } else {
                // Permission Denied
                Toast.makeText(activity, "无法下载新版本", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void DownLoadAPK() {
        //后台下载
        String suffix = new SimpleDateFormat("yyyyMMdd").format(new Date());
        DOWNLOAD_APK_NAME = DOWNLOAD_APK_NAME + suffix;
        Intent updateIntent = new Intent(activity, UpdateService.class);
        updateIntent.putExtra("apkUrl", DOWNLOAD_APK_URL);
        updateIntent.putExtra("apkName", DOWNLOAD_APK_NAME);
        updateIntent.putExtra("receiver", new DownloadReceiver(new Handler()));
        activity.startService(updateIntent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        activity.unregisterReceiver(mbc);
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
                        activity.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            if (resultCode == UpdateService.DOWNLOAD_PREPARE) {
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(activity);
                }
                mBuilder.setContentTitle(activity.getResources().getString(R.string.app_name));//设置通知栏标题
                mBuilder.setContentText("准备下载新版本"); //设置通知栏显示内容
                mBuilder.setTicker("下载新版本"); //通知首次出现在通知栏，带上升动画效果的
                        // .setContentIntent(updatePendingIntent) //设置通知栏点击意图
                mBuilder.setWhen(System.currentTimeMillis());//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                mBuilder.setPriority(Notification.PRIORITY_DEFAULT) ;//设置该通知优先级
                mBuilder.setOngoing(false);//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                mBuilder.setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON

                updateNotification = mBuilder.build();
                updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                //发出通知
                updateNotificationManager.notify(0, updateNotification);
            } else if (resultCode == UpdateService.DOWNLOAD_WORK) {
                int pregress = resultData.getInt("progress");
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(activity);
                }
                mBuilder.setContentTitle(activity.getResources().getString(R.string.app_name))//设置通知栏标题
                        .setContentText("已经下载" + pregress + "%"); //设置通知栏显示内容
                //.setContentIntent(updatePendingIntent) //设置通知栏点击意图
//                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                        .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
                updateNotification = mBuilder.build();
                updateNotificationManager.notify(0, updateNotification);
            } else if (resultCode == UpdateService.DOWNLOAD_OK) {
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(activity);
                }
                mBuilder.setContentTitle(activity.getResources().getString(R.string.app_name))//设置通知栏标题
                        .setContentText("下载成功") //设置通知栏显示内容
                        .setTicker("下载成功") //通知首次出现在通知栏，带上升动画效果的
                        //.setContentIntent(updatePendingIntent) //设置通知栏点击意图
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
                activity.startActivity(intent);
            } else if (resultCode == UpdateService.DOWNLOAD_ERROR) {
                if (mBuilder == null) {
                    mBuilder = new NotificationCompat.Builder(activity);
                }
                mBuilder.setContentTitle(activity.getResources().getString(R.string.app_name))//设置通知栏标题
                        .setContentText("下载失败") //设置通知栏显示内容
                        .setTicker("下载失败") //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        //.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON

                updateNotification = mBuilder.build();
                updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                //发出通知
                updateNotificationManager.notify(0, updateNotification);
                Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    protected void initPopuptWindowExit() {
        if (popupWindow == null) {
            // 获取自定义布局文件activity_popupwindow_left.xml的视图
            View popupWindow_view = activity.getLayoutInflater().inflate(R.layout.exit_popup_confirm_layout, null,
                    false);
            Button cancel = (Button) popupWindow_view.findViewById(R.id.exit_cancel);
            Button confirm = (Button) popupWindow_view.findViewById(R.id.exit_action);

            confirm.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                            RequestForLogout(basicUrl);
                        }
                    }
            );
            cancel.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (popupWindow != null && popupWindow.isShowing()) {
                                popupWindow.dismiss();
                                popupWindow = null;
                            }
                        }
                    }

            );

            // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
            popupWindow = new PopupWindow(popupWindow_view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            // 设置动画效果
            popupWindow.setAnimationStyle(R.style.AnimationFade);
            popupWindow.setOnDismissListener(
                    new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            SetBackgroundAlpha(1f);
                        }
                    }

            );
        }
    }


    //请求退出登录
    public void RequestForLogout(String basicUrl) {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormEncodingBuilder().add("token", PreferenceUtil.getPrefString(activity, "loginToken", "")).
               build();
        Request request = new Request.Builder().url(basicUrl).post(body).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "网络连接失败");
                    }
                });
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String s=response.body().string();
                Log.i(TAG, "response json:" + s);
                final BaseBean resultBean = JSON.parseObject(s, BaseBean.class);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resultBean.getRcd().equals("R0001")) {

                            SharedPreferences preferences = activity.getSharedPreferences("AppToken", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            //清除Token数据
                            editor.clear().commit();

                            activity.getSharedPreferences("UserId",Context.MODE_PRIVATE).edit().clear().commit();


                        } else {
                            Toast.makeText(activity, "网络异常，请重试！", Toast.LENGTH_SHORT).show();
                        }
                        //退出登录
//                activity.startActivityForResult(new Intent(activity,LoginActivity.class),1400);
//                activity.overridePendingTransition(R.anim.activity_up,R.anim.activity_down);
                        exit.setVisibility(View.GONE);
                    }
                });
            }
        });
    }

    class MoreBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getBooleanExtra("isVisible", true)) {
                exit.setVisibility(View.GONE);
                RequestForLogout(basicUrl);
                return;
            }else if(intent.getStringExtra("value")!=null && intent.getStringExtra("value").equals("gesture")){
                exit.setVisibility(View.GONE);
                return;
            }
            exit.setVisibility(View.VISIBLE);
        }
    }
}
