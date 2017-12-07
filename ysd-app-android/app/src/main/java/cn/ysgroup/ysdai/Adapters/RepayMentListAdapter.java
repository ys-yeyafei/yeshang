package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.ysgroup.ysdai.Beans.center.UserRepayment;
import cn.ysgroup.ysdai.R;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/2.
 */
public class RepayMentListAdapter extends BaseAdapter
{
    private List<UserRepayment> repaymentsList;
    private LayoutInflater mInflater;
    private Context context;

    public RepayMentListAdapter(Context context, List<UserRepayment> repaymentsList)
    {
        this.context = context;
        this.repaymentsList = repaymentsList;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<UserRepayment> getRepaymentsList()
    {
        return repaymentsList;
    }

    public void setRepaymentsList(List<UserRepayment> repaymentsList)
    {
        this.repaymentsList = repaymentsList;
    }

    @Override
    public int getCount()
    {
        return repaymentsList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return repaymentsList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        UserRepayment entity = repaymentsList.get(position);

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.repay_ment_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.repayMentListItemName.setText(entity.getBorrowName());

        BigDecimal waitAccount =new BigDecimal(entity.getWaitAccount());
        BigDecimal waitInterest =new BigDecimal(entity.getWaitInterest());

        viewHolder.repayMentListItemBorrowCount.setText(waitAccount.toString());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.tenderRecordItemTime.setText(sdf.format(new Date(entity.getRepaymentDate())));
        viewHolder.repayMentListItemProfit.setText("+"+entity.getWaitInterest());

        if(entity.getRepaymentStatus()!=null&&entity.getRepaymentStatus().intValue()==1){
            viewHolder.repayMentListItemBorrowCount.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            viewHolder.repayMentListItemBorrowCount.setTextColor(context.getResources().getColor(R.color.colorGreen));
        }


        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'repay_ment_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        @Bind(R.id.repay_ment_list_item_name)
        TextView repayMentListItemName;
        @Bind(R.id.repay_ment_list_item_borrow_count)
        TextView repayMentListItemBorrowCount;
        @Bind(R.id.tender_record_item_time)
        TextView tenderRecordItemTime;
        @Bind(R.id.repay_ment_list_item_profit)
        TextView repayMentListItemProfit;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
