package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.ysgroup.ysdai.Beans.user.AwawdCashBean;
import cn.ysgroup.ysdai.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/5.
 */
public class CashRewardListAdapter extends BaseAdapter
{

    private List<AwawdCashBean.CashListBean> awardList;
    private LayoutInflater mInflater;
    private Context context;


    public CashRewardListAdapter(Context context, List<AwawdCashBean.CashListBean> awardList, LayoutInflater mInflater)
    {
        this.context = context;
        this.awardList = awardList;
        this.mInflater = mInflater;
    }

    public List<AwawdCashBean.CashListBean> getAwardList()
    {
        return awardList;
    }

    public void setAwardList(List<AwawdCashBean.CashListBean> awardList)
    {
        this.awardList = awardList;
    }

    @Override
    public int getCount()
    {
        return awardList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return awardList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        AwawdCashBean.CashListBean entity = awardList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.cash_award_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (entity.getCreateDate() != 0)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            viewHolder.cashAwardListItemDate.setText(sdf.format(new Date(entity.getCreateDate())));
        }
        if(entity.getType().equals("tui_detail_award_ht")){
            viewHolder.cashAwardListItemType.setText("普通奖励");

        }else{
            viewHolder.cashAwardListItemType.setText("好友邀请奖励");
        }
        viewHolder.cashAwardListItemMoney.setText("+" + entity.getMoney());
        viewHolder.cashAwardListItemRemark.setText(Html.fromHtml(entity.getRemark()));


        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'cash_award_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        @Bind(R.id.cash_award_list_item_remark)
        TextView cashAwardListItemRemark;
        @Bind(R.id.cash_award_list_item_money)
        TextView cashAwardListItemMoney;
        @Bind(R.id.cash_award_list_item_date)
        TextView cashAwardListItemDate;
        @Bind(R.id.cash_award_list_item_type)
        TextView cashAwardListItemType;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
