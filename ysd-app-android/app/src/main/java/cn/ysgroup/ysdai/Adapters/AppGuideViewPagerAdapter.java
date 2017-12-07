package cn.ysgroup.ysdai.Adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2015/11/12.
 */
public class AppGuideViewPagerAdapter extends PagerAdapter
{
    //界面列表
    private List<View> views;

    public AppGuideViewPagerAdapter (List<View> views){
        this.views = views;
    }

    @Override
    public int getCount()
    {
        if (views != null)
    {
        return views.size();
    }

        return 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object)
    {
         return (view == object);
    }

    //初始化arg1位置的界面
    @Override
    public Object instantiateItem(View arg0, int arg1) {

        ((ViewPager) arg0).addView(views.get(arg1), 0);

        return views.get(arg1);
    }
    //销毁arg1位置的界面
    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView(views.get(arg1));
    }
}
