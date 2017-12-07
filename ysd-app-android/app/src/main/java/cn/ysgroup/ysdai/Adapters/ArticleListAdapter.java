package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ysgroup.ysdai.Activities.ArticleListActivity;
import cn.ysgroup.ysdai.Beans.Article.ArticleItem;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class ArticleListAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private ArticleListActivity activity;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private List<ArticleItem> articleItemList;
    private final boolean isTime;
    private LinearLayout.LayoutParams params;
    private String type;

    static class ListItemViewHolder {
        public  TextView time;
        public TextView title;
        public TextView help;
        public ImageView iv;
    }

    public ArticleListAdapter(Context context,List<ArticleItem> articleItemList){
        activity = (ArticleListActivity) context;
        this.articleItemList = articleItemList;
        this.mInflater = LayoutInflater.from(context);
        isTime = activity.isTime();
        if(!isTime && params==null){
            params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.topMargin= Utils.dip2px(activity,8);
            params.bottomMargin= Utils.dip2px(activity,8);
        }
    }

    public List<ArticleItem> getArticleItemList() {
        return articleItemList;
    }

    public void setArticleItemList(List<ArticleItem> articleItemList) {
        this.articleItemList = articleItemList;
    }


    @Override
    public int getCount() {
        return articleItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return articleItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArticleItem item = articleItemList.get(position);
        ListItemViewHolder viewHolder;
        viewHolder = new ListItemViewHolder();

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.article_list,null);
            viewHolder.title = (TextView)convertView.findViewById(R.id.article_list_title);
            viewHolder.time= (TextView) convertView.findViewById(R.id.article_list_time);
            viewHolder.help= (TextView) convertView.findViewById(R.id.article_list_help_tx);
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.article_list_iv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ListItemViewHolder)convertView.getTag();
        }

        if (item.isOpen){
            viewHolder.help.setVisibility(View.VISIBLE);
        }else{
            viewHolder.help.setVisibility(View.GONE);
        }

        viewHolder.title.setText(item.getTitle());
        viewHolder.help.setText(item.getContent());
        if(isTime){
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.time.setText(sdf.format(new Date(Long.parseLong(item.getCreateDate()))));

        }else{
            viewHolder.time.setVisibility(View.GONE);
            if(params!=null){
                viewHolder.title.setLayoutParams(params);
            }
        }

        if(type!=null && type.equals("app_help_center") && !item.isOpen){
            RelativeLayout.LayoutParams params=
                    new RelativeLayout.LayoutParams(Utils.dip2px(activity,15),
                            Utils.dip2px(activity,8));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.rightMargin=Utils.dip2px(activity,15);
            viewHolder.iv.setLayoutParams(params);
            viewHolder.iv.setBackgroundResource(R.mipmap.drow_icon);
        }

        if(type!=null && type.equals("app_help_center") && item.isOpen){
            RelativeLayout.LayoutParams params=
                    new RelativeLayout.LayoutParams(Utils.dip2px(activity,15),
                            Utils.dip2px(activity,8));
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.rightMargin=Utils.dip2px(activity,15);
            viewHolder.iv.setLayoutParams(params);
            viewHolder.iv.setBackgroundResource(R.mipmap.up_icon);
        }

        return convertView;
    }


    public void setType(String type){
        this.type=type;
    }

}
