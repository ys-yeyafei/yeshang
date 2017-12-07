package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowTenderItem;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.Utils;

/**
 * Created by Administrator on 2015/11/17.
 */
public class ProjectTenderRecordListAdapter extends BaseAdapter
{
    private String apr;
    private List<BorrowTenderItem> tenderList;
    private LayoutInflater mInflater;
    private Context context;
    private DecimalFormat decimalFormat = new DecimalFormat(".00");


    public ProjectTenderRecordListAdapter(Context context, List<BorrowTenderItem> tenderList, LayoutInflater mInflater, String apr)
    {
        this.apr = apr;
        this.context = context;
        this.tenderList = tenderList;
        this.mInflater = mInflater;
        decimalFormat = new DecimalFormat(".00");
    }

    public List<BorrowTenderItem> getTenderList()
    {
        return tenderList;
    }

    public void setTenderList(List<BorrowTenderItem> tenderList)
    {
        this.tenderList = tenderList;
    }


    public String getApr()
    {
        return apr;
    }

    public void setApr(String apr)
    {
        this.apr = apr;
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

        BorrowTenderItem entity = tenderList.get(position);
        ViewHolder viewHolder = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.tender_record_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String name =entity.getUsername().substring(0,3)+"****"+entity.getUsername().substring(7,entity.getUsername().length());
        viewHolder.tenderJlUsername.setText(name);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        viewHolder.tenderJlDate.setText(sdf.format(entity.getCreateDate()));
        float moneyFloat = Utils.str2Float(entity.getAccount());
        viewHolder.tenderMoney.setText("￥" + decimalFormat.format(moneyFloat));

        return convertView;
    }

    static class ViewHolder
    {
        @Bind(R.id.tender_jl_username)
        TextView tenderJlUsername;
        @Bind(R.id.tender_jl_date)
        TextView tenderJlDate;
        @Bind(R.id.tender_money)
        TextView tenderMoney;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
