package cn.ysgroup.ysdai.Adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import cn.ysgroup.ysdai.Fragments.BaseFragment;

import java.util.ArrayList;

/**
 * Created by linyujie on 16/10/21.
 */

public class LoginAdapter extends PagerAdapter {
    private ArrayList<BaseFragment> al;

    public LoginAdapter(ArrayList<BaseFragment> al) {
        this.al=al;
    }

    @Override
    public int getCount() {
        return al.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = al.get(position).getmView();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
