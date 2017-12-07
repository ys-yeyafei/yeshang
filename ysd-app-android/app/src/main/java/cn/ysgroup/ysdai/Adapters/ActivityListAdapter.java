package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.Beans.Article.ActivityBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.BitmapCache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class ActivityListAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private Context context;
    private ImageLoader imageLoader;
    private List<ActivityBean> articleItemList;

    static class ListItemViewHolder {
        public TextView title;
        public NetworkImageView img;
        public TextView date;
        public ImageView iv;
    }

    public ActivityListAdapter(Context context, List<ActivityBean> articleItemList){
        this.context = context;
        this.articleItemList = articleItemList;
        this.mInflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(CustomApplication.newInstance().getRequestQueue(), new BitmapCache());
    }

    public List<ActivityBean> getArticleItemList() {
        return articleItemList;
    }

    public void setArticleItemList(List<ActivityBean> articleItemList) {
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
        ActivityBean item = articleItemList.get(position);
        ListItemViewHolder viewHolder;
        viewHolder = new ListItemViewHolder();

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.activity_list_item,null);
            viewHolder.title = (TextView)convertView.findViewById(R.id.activity_item_title);
            viewHolder.img = (NetworkImageView) convertView.findViewById(R.id.activity_item_img);
            viewHolder.date = (TextView)convertView.findViewById(R.id.activity_item_date);
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.activity_item_iv);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ListItemViewHolder)convertView.getTag();
        }

        if(item.getStatus().equals("1")){
            viewHolder.iv.setBackgroundResource(R.mipmap.activity_unuser);
        }else{
            viewHolder.iv.setBackgroundResource(R.mipmap.activity_user);
        }
        viewHolder.title.setText(item.getTitle());
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.date.setText(sdf.format(new Date(item.getCreateDate())));
        String imgUrl =item.getImgApp();
        if (imgUrl != null && !imgUrl.equals(""))
        {
            viewHolder.img.setDefaultImageResId(R.mipmap.default_image);
            viewHolder.img.setErrorImageResId(R.mipmap.default_image);
            viewHolder.img.setImageUrl(AppConstants.IMG_URL_SUFFIX+imgUrl, imageLoader);
        }

        return convertView;
    }


}
