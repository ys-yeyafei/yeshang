package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ysgroup.ysdai.Beans.center.UserTender;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.Util.Utils;

/**
 * Created by Administrator on 2015/10/30.
 */
public class InvestRecordListAdapter extends BaseAdapter
{

    private List<UserTender> tenderList;
    private LayoutInflater mInflater;
    private Context context;
    DecimalFormat df = new DecimalFormat("#0.00");

    public InvestRecordListAdapter(Context context, List<UserTender> tenderList)
    {
        this.context = context;
        this.tenderList = tenderList;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<UserTender> getTenderList()
    {
        return tenderList;
    }

    public void setTenderList(List<UserTender> tenderList)
    {
        this.tenderList = tenderList;
    }

    @Override
    public int getCount()
    {
        return tenderList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return tenderList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        UserTender entity = tenderList.get(position);

        ListItemViewHolder viewHolder = null;


        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.invest_record_list_view_item_layout, null);
            viewHolder = new ListItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ListItemViewHolder) convertView.getTag();
        }
        String format = df.format(Utils.str2Double(entity.getTenderAccount()));
        viewHolder.tenderRecordItemBorrowName.setText(entity.getBorrowName());
        viewHolder.tenderRecordItemBorrowCount.setText(format);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        viewHolder.tenderRecordItemTime.setText(sdf.format(new Date(entity.getCreateDate())));
        if(entity.getBorrowStatus()!=null&&entity.getBorrowStatus().intValue()==7){
            viewHolder.tenderRecordItemStatus.setText(context.getResources().getString(R.string.checkbox));
            viewHolder.tenderRecordItemStatus.setTextColor(context.getResources().getColor(R.color.colorDarkGrey));
        }else{
            viewHolder.tenderRecordItemStatus.setText(context.getResources().getString(R.string.clock));
            viewHolder.tenderRecordItemStatus.setTextColor(context.getResources().getColor(R.color.colorLightYellow));
        }
        viewHolder.tenderRecordItemProfit.setText(entity.getInterest());

        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'invest_record_list_view_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ListItemViewHolder
    {
        @Bind(R.id.tender_record_item_borrow_name)
        TextView tenderRecordItemBorrowName;
        @Bind(R.id.tender_record_item_status)
        IconFontTextView tenderRecordItemStatus;
        @Bind(R.id.tender_record_item_borrow_count)
        TextView tenderRecordItemBorrowCount;
        @Bind(R.id.tender_record_item_time)
        TextView tenderRecordItemTime;
        @Bind(R.id.tender_record_item_profit)
        TextView tenderRecordItemProfit;

        ListItemViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
