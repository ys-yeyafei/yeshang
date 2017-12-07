package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ysgroup.ysdai.Beans.user.UserHongbaoViews;
import cn.ysgroup.ysdai.R;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/5.
 */
public class HongBaoListAdapter extends BaseAdapter
{

    private List<UserHongbaoViews> hongbaoList;
    private LayoutInflater mInflater;
    private Context context;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd");

    public HongBaoListAdapter(Context context, List<UserHongbaoViews> hongbaoList, LayoutInflater mInflater)
    {
        this.context = context;
        this.hongbaoList = hongbaoList;
        this.mInflater = mInflater;
    }

    public List<UserHongbaoViews> getHongbaoList()
    {
        return hongbaoList;
    }

    public void setHongbaoList(List<UserHongbaoViews> hongbaoList)
    {
        this.hongbaoList = hongbaoList;
    }

    @Override
    public int getCount()
    {
        return hongbaoList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return hongbaoList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        UserHongbaoViews entity = hongbaoList.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.hong_bao_list_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            viewHolder.money= (TextView) convertView.findViewById(R.id.hongbao_list_item_money_line);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        int isApp =entity.getIsApp()==null?0:entity.getIsApp().intValue();
        int isPc =entity.getIsPc()==null?0:entity.getIsPc().intValue();
//        String type ="";
//        if(isApp==1&&isPc==1){
//            type ="—通用";
//        }else if(isApp==1&& isPc!=1){
//            type ="—移动端";
//        }else if(isApp!=1&& isPc==1){
//            type ="—电脑端";
//        }

        viewHolder.money.setText("投资金额：满"+entity.getInvestFullMomey()+" 元可用");
        viewHolder.hongbaoListItemType.setText(entity.getName());
        viewHolder.hongbaoListItemDeadLine.setText( "("+sdf.format(entity.getEndTime())+" 日到期)");
        viewHolder.hongbaoListItemTimeLine.setText( "项目期限：满"+entity.getLimitStart()+"天可以使用");
        SpannableString moneyText =new SpannableString(entity.getMoney().intValue()+"元");
        moneyText.setSpan(new AbsoluteSizeSpan(22, true), 0, moneyText.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        moneyText.setSpan(new AbsoluteSizeSpan(14, true), moneyText.length() - 1, moneyText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.hongbaoListItemNumber.setText(moneyText);
        if(entity.getStatus().intValue()==0){
            viewHolder.hongbaoImg.setImageResource(R.mipmap.hb_red);
            viewHolder.hongbaoListItemStatusImg.setBackgroundResource(R.mipmap.hb_unused);
            viewHolder.hongbaoListItemNumber.setTextColor(context.getResources().getColor(R.color.colorHbYellow));

        }else if (entity.getStatus().intValue()==1){
            viewHolder.hongbaoImg.setImageResource(R.mipmap.hb_grey);
            viewHolder.hongbaoListItemStatusImg.setBackgroundResource(R.mipmap.hb_used);
            viewHolder.hongbaoListItemNumber.setTextColor(context.getResources().getColor(R.color.colorBackgroundWhite));

        }else if(entity.getStatus().intValue()==2){
            viewHolder.hongbaoImg.setImageResource(R.mipmap.hb_grey);
            viewHolder.hongbaoListItemStatusImg.setBackgroundResource(R.mipmap.hb_fail);
            viewHolder.hongbaoListItemNumber.setTextColor(context.getResources().getColor(R.color.colorBackgroundWhite));

        }
        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'hong_bao_list_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        @Bind(R.id.hongbao_img)
        ImageView hongbaoImg;
        @Bind(R.id.hongbao_list_item_number)
        TextView hongbaoListItemNumber;
        @Bind(R.id.hongbao_list_item_type)
        TextView hongbaoListItemType;
        @Bind(R.id.hongbao_list_item_time_line)
        TextView hongbaoListItemTimeLine;
        @Bind(R.id.hongbao_list_item_dead_line)
        TextView hongbaoListItemDeadLine;
        @Bind(R.id.hongbao_list_item_status_img)
        ImageView hongbaoListItemStatusImg;
        public TextView money;
        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
