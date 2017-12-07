package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.ysgroup.ysdai.Beans.user.MyFriendBean;
import cn.ysgroup.ysdai.R;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/5.
 */
public class HongBaoFriendsAdapter extends BaseAdapter
{

    private List<MyFriendBean.FriendListBean> friendsList;
    private LayoutInflater mInflater;
    private Context context;


    public HongBaoFriendsAdapter(Context context, List<MyFriendBean.FriendListBean> friendsList, LayoutInflater mInflater)
    {
        this.context = context;
        this.friendsList = friendsList;
        this.mInflater = mInflater;
    }

    public List<MyFriendBean.FriendListBean> getFriendsList()
    {
        return friendsList;
    }

    public void setFriendsList(List<MyFriendBean.FriendListBean> friendsList)
    {
        this.friendsList = friendsList;
    }

    @Override
    public int getCount()
    {
        return friendsList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return friendsList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MyFriendBean.FriendListBean entity = friendsList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.hong_bao_friends_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        viewHolder.hongBaoFriendsItemId.setText(entity.getUsername());
        viewHolder.hongBaoFirendsItemAward.setText(entity.getUadSumMoney() == null ? "￥ 0.00" : "￥" + entity.getUadSumMoney().toString());
        viewHolder.hongBaoFirendsItemDate.setText(sdf.format(entity.getCreateDate()));
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'hong_bao_friends_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        @Bind(R.id.hong_bao_friends_item_id)
        TextView hongBaoFriendsItemId;
        @Bind(R.id.hong_bao_firends_item_date)
        TextView hongBaoFirendsItemDate;
        @Bind(R.id.hong_bao_firends_item_award)
        TextView hongBaoFirendsItemAward;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
