package cn.ysgroup.ysdai.Util;

import android.content.Context;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.ysgroup.ysdai.Application.CustomApplication;

/**
 * Created by linyujie on 16/10/18.
 */

public class Utils {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 设置极光推送
     * @param alias 别名
     * @param tags tags
     */
    public static void setJPushInterface(String alias, String tags){
        JPushInterface.resumePush(CustomApplication.newInstance());//恢复推送

        //设置极光推送的别名
        //设置极光推送的tag--store_id
        Set<String> tagSet = new LinkedHashSet<>();
        tagSet.add(tags);
        JPushInterface.setAliasAndTags(CustomApplication.newInstance(), alias, tagSet, null);
    }

    /**
     * 将整形字符串转化为整数
     * @param str
     * @return
     */
    public static int str2Int(String str){
        if(null == str) return 0;
        try {
            return Integer.parseInt(str);
        }catch (NumberFormatException e){
            return 0;
        }
    }

    /**
     * 将双精度字符串转化为双精度
     * @param str
     * @return
     */
    public static double str2Double(String str){
        if(null == str) return 0d;
        try {
            return Double.parseDouble(str);
        }catch (NumberFormatException e){
            return 0d;
        }
    }

    /**
     * 将单精度字符串转化为单精度
     * @param str
     * @return
     */
    public static float str2Float(String str){
        if(null == str) return 0f;
        try {
            return Float.parseFloat(str);
        }catch (NumberFormatException e){
            return 0f;
        }
    }
}
