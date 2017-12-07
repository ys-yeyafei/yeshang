package cn.ysgroup.ysdai.Gesture.gestures.Util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;


/**
 * Created by SilenceDut on 2015/12/11.
 */
public class AppVersionUtils {

    public static String getVersionName(Context context) {
        String versionName = null;
        try {
            versionName = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            versionCode = context.getApplicationContext().getPackageManager()
                    .getPackageInfo(context.getApplicationContext().getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    /**
     * 获取渠道名
     *
     * @param ctx
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context ctx, String keyString) {
        if (ctx == null) {
            return null;
        }
        String channelName = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelName = applicationInfo.metaData.getString(keyString);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelName;
    }

    /**
     * 获取IMEI
     *
     * @param ctx
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getPhoneIMEI(Context ctx) {
        if (ctx == null) {
            return null;
        }
        String imei = null;
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
        try {
            imei = tm.getDeviceId();

        } catch (Exception e) {

        }
        if (imei != null) {
            return imei;
        }else{
            return "000000";
        }
    }
}
