package cn.ysgroup.ysdai.Adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2015/11/16.
 */
public class DocumentViewPagerAdapter extends PagerAdapter {
    private List<View> vlist;

    public DocumentViewPagerAdapter(List<View> vlist) {

        this.vlist = vlist;

    }

    @Override
    public int getCount() {
        return vlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.removeView(vlist.get(position));
        container.addView(vlist.get(position));
        return vlist.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(vlist.get(position));
        //super.destroyItem(container, position, object);
        //object = null;
    }
}
