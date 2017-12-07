package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ysgroup.ysdai.Beans.bank.BankCard;
import cn.ysgroup.ysdai.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2015/11/9.
 */
public class BankListAdapter extends BaseAdapter
{
    private List<BankCard> banList;
    private LayoutInflater mInflater;
    private Context context;

    public BankListAdapter(Context context, List<BankCard> banList)
    {
        this.context = context;
        this.banList = banList;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return banList.size();
    }

    @Override
    public BankCard getItem(int position)
    {
        return banList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        BankCard entity =banList.get(position);
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.bank_list_view_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bankListViewText.setText(entity.getBankName());
        viewHolder.bankListViewImg.setImageResource(context.getResources().getIdentifier("bank_"+entity.getBankId().toLowerCase(), "mipmap" , context.getPackageName()));
        return convertView;
    }



    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'bank_list_view_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        @Bind(R.id.bank_list_view_text)
        TextView bankListViewText;
        @Bind(R.id.bank_list_view_img)
        ImageView bankListViewImg;

        ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
