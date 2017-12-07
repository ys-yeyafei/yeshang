package cn.ysgroup.ysdai.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2016/5/9.
 */
public class PreferenceUtil
{
    public static String getPrefString(Context context, String key, final String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences("AppToken", context.MODE_PRIVATE);
        return  preferences.getString(key, defaultValue);
    }

    public static int getPrefInt(Context context, String key, final int defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences("AppToken", context.MODE_PRIVATE);
        return  preferences.getInt(key, defaultValue);
    }

    public static void setPrefInt(Context context, final String key, final int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        settings.edit().putInt(key, value).commit();
    }
}
