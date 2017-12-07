package cn.ysgroup.ysdai.Gesture.gestures.Service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.support.v7.app.NotificationCompat;

import cn.ysgroup.ysdai.Activities.MainActivity;
import cn.ysgroup.ysdai.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateService extends IntentService {

    //下载apk存放的文件夹
    private String SAVE_APK_PATH =Environment.getExternalStorageDirectory().getPath() + "/YueShangDai/upgrade/";
    //下载apk的地址
    private String DOWNLOAD_APK_URL=null;
    private String DOWNLOAD_APK_NAME=null;


    private int fileSize = 0;
    private int downloadSize = 0;
    public static final int DOWNLOAD_PREPARE = 0;
    public static final int DOWNLOAD_WORK = 1;
    public static final int DOWNLOAD_OK = 2;
    public static final int DOWNLOAD_ERROR = 3;

    //通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    //通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;

    /*private Handler handler = new Handler() {

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_PREPARE:
                    NotificationCompat.Builder mPreBuilder = new NotificationCompat.Builder(UpdateService.this);
                    mPreBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                            .setContentText("准备下载新版本") //设置通知栏显示内容
                            .setTicker("下载新版本") //通知首次出现在通知栏，带上升动画效果的
                            .setContentIntent(updatePendingIntent) //设置通知栏点击意图
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                            //.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setSmallIcon(R.drawable.appicon_small);//设置通知小ICON

                    updateNotification =mPreBuilder.build();
                    updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                    //发出通知
                    updateNotificationManager.notify(0, updateNotification);

                    break;
                case DOWNLOAD_WORK:
                    NotificationCompat.Builder mWorkBuilder = new NotificationCompat.Builder(UpdateService.this);
                    mWorkBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                            .setContentText("已经下载" + (int) downloadSize * 100 / fileSize + "%") //设置通知栏显示内容
                            .setContentIntent(updatePendingIntent) //设置通知栏点击意图
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                                    // .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setSmallIcon(R.drawable.appicon_small);//设置通知小ICON
                    updateNotification =mWorkBuilder.build();
                    updateNotificationManager.notify(0, updateNotification);
                    break;
                case DOWNLOAD_OK:
                    downloadSize = 0;
                    fileSize = 0;
                    NotificationCompat.Builder mOkBuilder = new NotificationCompat.Builder(UpdateService.this);
                    mOkBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                            .setContentText("下载成功") //设置通知栏显示内容
                            .setTicker("下载成功") //通知首次出现在通知栏，带上升动画效果的
                            .setContentIntent(updatePendingIntent) //设置通知栏点击意图
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                           // .setDefaults(Notification.)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setSmallIcon(R.drawable.appicon_small);//设置通知小ICON

                    updateNotification =mOkBuilder.build();
                    updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                    //发出通知
                    updateNotificationManager.notify(0, updateNotification);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(new File(SAVE_APK_PATH + DOWNLOAD_APK_NAME)), "application/vnd.android.package-archive");;
                    startActivity(intent);
                    break;
                case DOWNLOAD_ERROR:

                    NotificationCompat.Builder mErrorBuilder = new NotificationCompat.Builder(UpdateService.this);
                    mErrorBuilder.setContentTitle(getResources().getString(R.string.app_name))//设置通知栏标题
                            .setContentText("下载失败") //设置通知栏显示内容
                            .setContentIntent(updatePendingIntent) //设置通知栏点击意图
                            .setTicker("下载失败") //通知首次出现在通知栏，带上升动画效果的
                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                            .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                            .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                            //.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                            .setSmallIcon(R.drawable.exx);//设置通知小ICON

                    updateNotification =mErrorBuilder.build();
                    updateNotification.flags |= Notification.FLAG_AUTO_CANCEL; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
                    //发出通知
                    updateNotificationManager.notify(0, updateNotification);
                    Toast.makeText(UpdateService.this, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
            super.handleMessage(msg);
        }

    };*/
    public UpdateService() {
        super("UpdateService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        //获取传值
            DOWNLOAD_APK_URL = intent.getStringExtra("apkUrl");
            DOWNLOAD_APK_NAME = intent.getStringExtra("apkName");

        final ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("receiver");

        updateNotificationManager = (NotificationManager)
                this.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        updateIntent = new Intent(this, MainActivity.class);
        updatePendingIntent = PendingIntent.getActivity(this, 0, updateIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("版本更新")//设置通知栏标题
                .setContentText("正在准备下载") //设置通知栏显示内容
                .setContentIntent(updatePendingIntent) //设置通知栏点击意图
                .setTicker("正在下载新版本") //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
               // .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON

        updateNotification =mBuilder.build();
        updateNotification.flags |= Notification.FLAG_ONGOING_EVENT; // 将此通知放到通知栏的"Ongoing"即"正在运行"组中
        updateNotification.flags |= Notification.FLAG_NO_CLEAR; // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        //发出通知
        updateNotificationManager.notify(0, updateNotification);
        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        Thread thread = new Thread(new Runnable() {
            public void run() {
                downloadFile(receiver);
            }
        });
        thread.start();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

  /*  private void sendMessage(int what) {
        Message m = new Message();
        m.what = what;
        handler.sendMessage(m);
    }*/
    private void downloadFile( ResultReceiver receiver ) {
        FileOutputStream output =null;
        int times = 0;
        try {
            URL u = new URL(DOWNLOAD_APK_URL);
            URLConnection conn = u.openConnection();
            InputStream is = conn.getInputStream();
            fileSize = conn.getContentLength();
            if (fileSize < 1 || is == null) {
                Bundle errorData = new Bundle();
                receiver.send(DOWNLOAD_ERROR, errorData);
            } else {
                Bundle preData = new Bundle();
                receiver.send(DOWNLOAD_PREPARE, preData);

                File dir = new File(SAVE_APK_PATH);
                if (! dir.exists() || !dir.isDirectory()) {
                    dir.mkdirs();
                }

                File apkfile = new File(SAVE_APK_PATH + DOWNLOAD_APK_NAME+".apk");
                if (apkfile.exists()) {
                    apkfile.delete();
                }
                apkfile.createNewFile();
                output = new FileOutputStream(apkfile);
                byte[] buffer = new byte[1024];
                int inputSize  = -1;
                while ((inputSize  = is.read(buffer)) != -1) {
                    output.write(buffer, 0, inputSize);
                    downloadSize += inputSize;
                    if(times>=512)
                    {
                       /*这是防止频繁地更新通知，而导致系统变慢甚至崩溃。*/
                        Bundle resultData = new Bundle();
                        resultData.putInt("progress", (int)(downloadSize * 100 / fileSize ));
                        receiver.send(DOWNLOAD_WORK,resultData);
                        times =0;
                    }
                    times++;

                }
                output.flush();
                is.close();
                output.close();
                Bundle resultData = new Bundle();
                resultData.putInt("progress", 100);
                receiver.send(DOWNLOAD_OK, resultData);
            }
        } catch (Exception e) {
            System.out.println("下载服务中===>下载失败：" + e.getLocalizedMessage());
            Bundle resultData = new Bundle();
            if(receiver!=null){
                receiver.send(DOWNLOAD_ERROR, resultData);
            }
            e.printStackTrace();
        }
    }
}
