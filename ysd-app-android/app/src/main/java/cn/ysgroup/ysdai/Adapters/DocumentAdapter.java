package cn.ysgroup.ysdai.Adapters;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.ysgroup.ysdai.Activities.EnlargeActivity;
import cn.ysgroup.ysdai.Activities.ProjectDetailActivity;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.SmoothImageView;

import java.util.ArrayList;

/**
 * Created by linyujie on 16/10/14.
 */

public class DocumentAdapter extends BaseAdapter {
    private ArrayList viewList;
    private ProjectDetailActivity activity;
    private final Intent intent;
    private ArrayList<String> imageUrlList;

    public DocumentAdapter(ArrayList<View> viewList, FragmentActivity activity, ArrayList<String> imageUrlList) {
        this.viewList = viewList;
        intent = new Intent(activity, EnlargeActivity.class);
        this.activity = (ProjectDetailActivity) activity;
        this.imageUrlList=imageUrlList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = (View) viewList.get(position);
        final SmoothImageView iv = (SmoothImageView) view.findViewById(R.id.docu_item_img);
//
        return view;
    }
}
