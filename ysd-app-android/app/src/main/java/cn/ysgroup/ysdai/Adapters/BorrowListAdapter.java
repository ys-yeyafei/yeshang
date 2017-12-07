package cn.ysgroup.ysdai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.ysgroup.ysdai.Beans.Borrow.BorrowItem;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.Utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/10/14.
 */
public class BorrowListAdapter extends BaseAdapter {

    private List<BorrowItem> borrowItemList;
    private LayoutInflater mInflater;
    private Context context;
    private int mTotalProgress = 0;//总进度
    DecimalFormat df = new DecimalFormat("#0");

    static class ListItemViewHolder {

        public TextView namee;//标题
        //public TasksCompletedView scheduleBar;//投标的百分比进度条
        public TextView baseApr;//年利率
        public TextView awardApr;//年利率
        public TextView peroid;//期限
        public TextView lowest;//最低起购
        public TextView payMMode;//付息方式
        public ImageView newerTag;//付息方式
        public ProgressBar progressBar;
        public TextView text;
    }

    public BorrowListAdapter(Context context, List<BorrowItem> borrowItemList) {
        this.context = context;
        this.borrowItemList = borrowItemList;
        this.mInflater = LayoutInflater.from(context);
    }

    public List<BorrowItem> getBorrowItemList() {
        return borrowItemList;
    }

    public void setBorrowItemList(List<BorrowItem> borrowItemList) {
        this.borrowItemList = borrowItemList;
    }

    @Override
    public int getCount() {
        return borrowItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return borrowItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        BorrowItem item = borrowItemList.get(position);


        ListItemViewHolder viewHolder = null;
        viewHolder = new ListItemViewHolder();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_layout, null);
            viewHolder.namee = (TextView) convertView.findViewById(R.id.list_item_name);
            viewHolder.baseApr = (TextView) convertView.findViewById(R.id.list_item_base_apr);
            viewHolder.awardApr = (TextView) convertView.findViewById(R.id.list_item_award_apr);
            viewHolder.peroid = (TextView) convertView.findViewById(R.id.list_item_peroid);
            //viewHolder.scheduleBar = (TasksCompletedView) convertView.findViewById(R.id.list_item_schedule);
            viewHolder.lowest = (TextView) convertView.findViewById(R.id.list_item_lowest);
            viewHolder.newerTag = (ImageView) convertView.findViewById(R.id.list_item_newer_tag);
            viewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.list_pregress);
            viewHolder.text= (TextView) convertView.findViewById(R.id.list_bar_text);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ListItemViewHolder) convertView.getTag();
        }


        float scheduleFloat = Utils.str2Float(item.getSchedule());
        int scheduleInt = (int) scheduleFloat;
//        if(scheduleInt>=100){
//            viewHolder.scheduleBar.setTextColor(context.getResources().getColor(R.color.colorTextHint));
//            viewHolder.scheduleBar.setRingColor(context.getResources().getColor(R.color.colorTextHint));
//            viewHolder.scheduleBar.setProgress(100);
//        }else{

        //viewHolder.scheduleBar.setProgress(scheduleInt);

//        }

        if (item.getBaseApr() != null) {
            BigDecimal mData = item.getBaseApr().setScale(2, BigDecimal.ROUND_HALF_UP);
            viewHolder.baseApr.setText(mData + "");
        }
        if (item.getAwardApr() != null && item.getAwardApr().compareTo(BigDecimal.ZERO) == 1) {
            BigDecimal mData = item.getAwardApr().setScale(2, BigDecimal.ROUND_HALF_UP);
            viewHolder.awardApr.setText("+" + mData + "%");
            viewHolder.awardApr.setVisibility(View.VISIBLE);
        } else {
            viewHolder.awardApr.setVisibility(View.INVISIBLE);
        }
        viewHolder.peroid.setText(item.getTimeLimit() + "天");
        viewHolder.namee.setText(item.getName());
        viewHolder.lowest.setText(df.format(Utils.str2Double(item.getBalance())) + "元");

        if (item.getType().equals("16") || item.getSchedule().equals("100")) {
            viewHolder.newerTag.setVisibility(View.VISIBLE);
            if(item.getType().equals("16")){
                viewHolder.newerTag.setBackgroundResource(R.mipmap.newer);
            }else{
                viewHolder.newerTag.setBackgroundResource(R.mipmap.shouzhi);
            }
        } else {
            viewHolder.newerTag.setVisibility(View.GONE);

        }


        viewHolder.progressBar.setProgress(scheduleInt);
        viewHolder.text.setText(scheduleInt+"%");
        WindowManager wm = (WindowManager) context .getSystemService(Context.WINDOW_SERVICE);
        float screenWidth = wm.getDefaultDisplay().getWidth();
        //bar的长度
        viewHolder.text.measure(0,0);
        float barWidth=screenWidth-Utils.dip2px(context,45);
        System.out.println("scheduleInt:"+scheduleInt+"barWidth:"+barWidth+"viewHolder.text.getWidth():"+viewHolder.text.getMeasuredWidth());
        if(scheduleInt<50){
            viewHolder.text.setX((scheduleInt*barWidth)/100);
        }else{
            viewHolder.text.setX((scheduleInt*barWidth)/100-10);
        }
        return convertView;
    }


}
