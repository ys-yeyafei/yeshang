package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.ysgroup.ysdai.Beans.user.UserHongbaoViews;
import cn.ysgroup.ysdai.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/11.
 */
public class ProjectTenderHongBaoAdapter extends BaseAdapter
{


    private List<UserHongbaoViews> hongBaoList;
    private LayoutInflater mInflater;
    private Context context;
    private BigDecimal totalSelectedHongBao;//选中的红包金额
    private BigDecimal limitHongBao;//选中的红包金额的上限
    private List<Integer> selectedhongBaoIdList;//选中的红包的id;
    private SelectedHbChangedListener selectedHbChangedListener;


    public ProjectTenderHongBaoAdapter(Context context, List<UserHongbaoViews> hongBaoList, BigDecimal limitHongBao, SelectedHbChangedListener selectedHbChangedListener)
    {
        this.context = context;
        this.hongBaoList = hongBaoList;
        this.mInflater = LayoutInflater.from(context);
        this.limitHongBao = limitHongBao;
        this.selectedHbChangedListener =selectedHbChangedListener;
        totalSelectedHongBao = new BigDecimal(0);
        selectedhongBaoIdList =new ArrayList<Integer>();
    }

    public BigDecimal getTotalSelectedHongBao()
    {
        return totalSelectedHongBao;
    }

    public void setTotalSelectedHongBao(BigDecimal totalSelectedHongBao)
    {
        this.totalSelectedHongBao = totalSelectedHongBao;
    }

    public BigDecimal getLimitHongBao()
    {
        return limitHongBao;
    }

    public void setLimitHongBao(BigDecimal limitHongBao)
    {
        this.limitHongBao = limitHongBao;
    }


    public List<Integer> getSelectedhongBaoIdList()
    {
        return selectedhongBaoIdList;
    }

    public void setSelectedhongBaoIdList(List<Integer> selectedhongBaoIdList)
    {
        this.selectedhongBaoIdList = selectedhongBaoIdList;
    }

    @Override
    public int getCount()
    {
        return hongBaoList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return hongBaoList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        final UserHongbaoViews entity = hongBaoList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.tender_hong_bao_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tenderHbItemMoney.setText(entity.getMoney() + "元");
        viewHolder.tenderHbItemDate.setText(entity.getOverDays() + "天后过期");
        viewHolder.tenderHbItemCheckBox.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if (isChecked)
                        {

                            if (totalSelectedHongBao.add(entity.getMoney()).compareTo(limitHongBao) > 0)
                            {
                                Toast.makeText(context, "对不起，红包超出上限", Toast.LENGTH_SHORT).show();
                                buttonView.setChecked(false);
                            } else
                            {
                                totalSelectedHongBao = totalSelectedHongBao.add(entity.getMoney());
                                selectedhongBaoIdList.add(entity.getId());
                                selectedHbChangedListener.setSelectedHb(totalSelectedHongBao);
                                selectedHbChangedListener.setYueToPay(totalSelectedHongBao);
                            }


                        } else
                        {
                            if (totalSelectedHongBao.compareTo(new BigDecimal(0)) > 0)
                            {
                                totalSelectedHongBao =totalSelectedHongBao.subtract(entity.getMoney());
                                selectedhongBaoIdList.remove(entity.getId());
                                selectedHbChangedListener.setSelectedHb(totalSelectedHongBao);
                                selectedHbChangedListener.setYueToPay(totalSelectedHongBao);
                            }
                        }

                    }
                }
        );


        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'tender_hong_bao_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        @Bind(R.id.tender_hb_item_check_box)
        CheckBox tenderHbItemCheckBox;
        @Bind(R.id.tender_hb_item_money)
        TextView tenderHbItemMoney;
        @Bind(R.id.tender_hb_item_date)
        TextView tenderHbItemDate;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

    public  interface SelectedHbChangedListener
    {
           void setSelectedHb(BigDecimal selectedHb);
           void setYueToPay(BigDecimal selectedHb);

    }


}
