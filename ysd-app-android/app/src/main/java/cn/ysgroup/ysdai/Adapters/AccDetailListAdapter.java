package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.ysgroup.ysdai.Beans.funds.UserAccDetail;
import cn.ysgroup.ysdai.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2015/11/4.
 */
public class AccDetailListAdapter extends BaseAdapter
{
    private List<UserAccDetail> accDetailsList;
    private LayoutInflater mInflater;
    private Context context;

    public AccDetailListAdapter(List<UserAccDetail> accDetailsList, LayoutInflater mInflater, Context context)
    {
        this.accDetailsList = accDetailsList;
        this.mInflater = mInflater;
        this.context = context;
    }

    public List<UserAccDetail> getAccDetailsList()
    {
        return accDetailsList;
    }

    public void setAccDetailsList(List<UserAccDetail> accDetailsList)
    {
        this.accDetailsList = accDetailsList;
    }

    @Override
    public int getCount()
    {
        return accDetailsList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return accDetailsList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        UserAccDetail entity = accDetailsList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.acc_detail_list_view_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.accDetailItemType.setText(entity.getTypeShow());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(entity.getAbleMoney()!=null)
        {
            viewHolder.accDetailItemMoney.setText(entity.getAbleMoney());

        }
        if(entity.getCreateDate()!=null)
        {
            viewHolder.accDetailItemTime.setText(sdf.format(new Date(entity.getCreateDate())));
        }
        if(entity.getSign().equals("1"))
        {
            viewHolder.accDetailItemAbleMoney.setText("+"+entity.getMoney());
            viewHolder.accDetailItemAbleMoney.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else if(entity.getSign().equals("-1")){
            viewHolder.accDetailItemAbleMoney.setTextColor(context.getResources().getColor(R.color.colorBlue));
            viewHolder.accDetailItemAbleMoney.setText("-"+entity.getMoney());
        }else{
            viewHolder.accDetailItemAbleMoney.setText(entity.getMoney());
        }

        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'acc_detail_list_view_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        @Bind(R.id.acc_detail_item_type)
        TextView accDetailItemType;
        @Bind(R.id.acc_detail_item_time)
        TextView accDetailItemTime;
        @Bind(R.id.acc_detail_item_money)
        TextView accDetailItemMoney;
        @Bind(R.id.acc_detail_item_able_money)
        TextView accDetailItemAbleMoney;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
