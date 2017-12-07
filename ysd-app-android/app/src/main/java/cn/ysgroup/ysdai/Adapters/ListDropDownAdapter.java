package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.SpinnerData;

import java.util.List;


public class ListDropDownAdapter extends BaseAdapter
{

    private Context context;
    private List<SpinnerData> list;
    private int checkItemPosition = 0;

    public void setCheckItem(int position)
    {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public ListDropDownAdapter(Context context, List<SpinnerData> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView != null)
        {
            viewHolder = (ViewHolder) convertView.getTag();
        } else
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_default_drop_down, null);
            viewHolder = new ViewHolder();
            viewHolder.iv= (ImageView) convertView.findViewById(R.id.drop_down_iv);
            viewHolder.v=convertView.findViewById(R.id.drop_down_v);
            viewHolder.mText= (TextView) convertView.findViewById(R.id.drop_down_text);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder)
    {
        viewHolder.mText.setText(list.get(position).getText());
        if (checkItemPosition != -1)
        {
            if (checkItemPosition == position)
            {
                viewHolder.iv.setVisibility(View.VISIBLE);
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.red));
                viewHolder.mText.setBackgroundResource(R.color.colorWhite);
            } else
            {
                viewHolder.iv.setVisibility(View.GONE);
                viewHolder.mText.setTextColor(context.getResources().getColor(R.color.colorDarkGrey));
                viewHolder.mText.setBackgroundResource(R.color.colorWhite);
            }
            if(position==0){
                viewHolder.v.setVisibility(View.VISIBLE);
            }else{
                viewHolder.v.setVisibility(View.GONE);
            }
        }
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_default_drop_down.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder
    {
        public View v;
       public TextView mText;
       public ImageView iv;
    }
}
