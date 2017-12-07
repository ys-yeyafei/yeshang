package cn.ysgroup.ysdai.Activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.util.List;


/**
 * Created by linyujie on 17/4/24.
 */

public class MyBaseActivity extends AppCompatActivity {

    private String basePhone;
    private String code;
    public static boolean isActive; //全局变量

    @Override
    protected void onResume() {
        if (!isActive) {
            isActive = true;
            basePhone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
            //判断是否有手势密码
            code = getSharedPreferences(basePhone, Context.MODE_PRIVATE).
                    getString(basePhone, null);

            if (code != null && getSharedPreferences("UserId", Context.MODE_PRIVATE).
                    getString("UserId", null) != null) {
                Intent intent = new Intent();
                intent.putExtra("from", "launch");
                intent.setClass(MyBaseActivity.this, GestureCodeActivity.class);
                startActivity(intent);
            }
        }
        super.onResume();
    }


    @Override
    protected void onStop() {
        if (!isAppOnForeground()) {
            //app 进入后台
            isActive = false;//记录当前已经进入后台
        }
        super.onStop();
    }


    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

}
