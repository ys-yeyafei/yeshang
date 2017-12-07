package cn.ysgroup.ysdai.Adapters;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.ysgroup.ysdai.Activities.HbChooseActivity;
import cn.ysgroup.ysdai.Beans.user.HbPriorityBean;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.Utils;

/**
 * Created by Administrator on 2015/11/5.
 */
public class HongBaoChooseListAdapter extends BaseAdapter {

    private List<HbPriorityBean.UserHongbaoListBean> hongbaoList;
    private List<String> choosedHbIdList;
    private LayoutInflater mInflater;
    private HbChooseActivity context;
    private String TAG = "项目投资列表";
    private OneItemClickListener oneItemClickListener;
    private int onSize;
    private String money;
    private int MaxInvestFullMomey;
    private int lastInvestFullMomey;
    int HBmoney;


    public HongBaoChooseListAdapter(HbChooseActivity context, List<HbPriorityBean.UserHongbaoListBean> hongbaoList,
                                    LayoutInflater mInflater,
                                    List<String> choosedHbIdList, int onSize, String money) {
        this.context = context;
        this.hongbaoList = hongbaoList;
        this.mInflater = mInflater;
        this.choosedHbIdList = choosedHbIdList;
        this.onSize = onSize;
        this.money = money;
        hongbaoList.add(onSize, new HbPriorityBean.UserHongbaoListBean());

        for (HbPriorityBean.UserHongbaoListBean item : hongbaoList) {
            if (item.isOn()) {
                item.isright = true;
                HBmoney = (int) (HBmoney + item.getMoney());
                MaxInvestFullMomey = MaxInvestFullMomey + item.getInvestFullMomey();
            }
        }
        lastInvestFullMomey = MaxInvestFullMomey;

    }

    public List<HbPriorityBean.UserHongbaoListBean> getHongbaoList() {
        return hongbaoList;
    }

    public void setHongbaoList(List<HbPriorityBean.UserHongbaoListBean> hongbaoList) {
        this.hongbaoList = hongbaoList;
    }

    public OneItemClickListener getOneItemClickListener() {
        return oneItemClickListener;
    }

    public void setOneItemClickListener(OneItemClickListener oneItemClickListener) {
        this.oneItemClickListener = oneItemClickListener;
    }

    @Override
    public int getCount() {
        return hongbaoList.size();
    }

    @Override
    public Object getItem(int position) {
        return hongbaoList.get(position);
    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == onSize) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == 1) {
            View view = View.inflate(context, R.layout.choose_item, null);
            if (position == hongbaoList.size() - 1)
                view.setVisibility(View.GONE);
            return view;
        } else {
            final HbPriorityBean.UserHongbaoListBean entity = hongbaoList.get(position);

            ViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.hong_bao_list_choose_item_layout, null);
                viewHolder = new ViewHolder(convertView);
                viewHolder.money_line = (TextView) convertView.findViewById(R.id.hongbao_list_item_money_line);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd");

            int isApp = entity.getIsApp();
            int isPc = entity.getIsPc();
            String type = "";
            if (isApp == 1 && isPc == 1) {
                type = "—通用";
            } else if (isApp == 1 && isPc != 1) {
                type = "—移动端";
            } else if (isApp != 1 && isPc == 1) {
                type = "—电脑端";
            }

            viewHolder.hongbaoListItemType.setText(entity.getName());
            viewHolder.hongbaoListItemDeadLine.setText("(" + sdf.format(entity.getEndTime()) + " 日到期)");
            viewHolder.hongbaoListItemTimeLine.setText("项目期限：满" + entity.getLimitStart() + " 天可用");
            viewHolder.money_line.setText("投资金额：满" + entity.getInvestFullMomey() + " 元可用");
            SpannableString moneyText = new SpannableString(entity.getMoney() + "元");
            moneyText.setSpan(new AbsoluteSizeSpan(20, true), 0, moneyText.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            moneyText.setSpan(new AbsoluteSizeSpan(12, true), moneyText.length() - 1, moneyText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.hongbaoListItemNumber.setText(moneyText);
            if (entity.getStatus() == 0) {
                viewHolder.hongbaoImg.setImageResource(R.mipmap.hb_red);
                viewHolder.hongbaoListItemNumber.setTextColor(context.getResources().getColor(R.color.colorHbYellow));

            } else if (entity.getStatus() == 1) {
                viewHolder.hongbaoImg.setImageResource(R.mipmap.hb_grey);
                viewHolder.hongbaoListItemNumber.setTextColor(context.getResources().getColor(R.color.colorBackgroundWhite));

            } else if (entity.getStatus() == 2) {
                viewHolder.hongbaoImg.setImageResource(R.mipmap.hb_grey);
                viewHolder.hongbaoListItemNumber.setTextColor(context.getResources().getColor(R.color.colorBackgroundWhite));
            }

            System.out.println(entity.isright + "+++++++++++");
            if (entity.isright) {
                viewHolder.hongbaoChooseBox.setChecked(true);
            } else {
                viewHolder.hongbaoChooseBox.setChecked(false);
            }

            final ViewHolder finalViewHolder = viewHolder;
            viewHolder.hongbaoChooseBox.setOnClickListener(new View.OnClickListener() {

                Intent intent = new Intent("HbBroadCast");
                int InvestFullMomey = 0;

                @Override
                public void onClick(View v) {
                    if (!entity.isright) {
                        InvestFullMomey = 0;
                        finalViewHolder.hongbaoChooseBox.setChecked(true);
                        entity.isright = true;
                        if (oneItemClickListener != null) {
                            if (position > onSize) {

                                for (int x = 0; x < hongbaoList.size(); x++) {
                                    //循环添加所有使用条件money的合
                                    if (hongbaoList.get(x).isright)
                                        InvestFullMomey = InvestFullMomey + hongbaoList.get(x).getInvestFullMomey();
                                }
                                // MaxInvestFullMomey 勾选过的最大值
                                if (InvestFullMomey > MaxInvestFullMomey) {
                                    if (InvestFullMomey < Utils.str2Int(money)){
                                        InvestFullMomey = Utils.str2Int(money);
                                        lastInvestFullMomey=Utils.str2Int(money);
                                    }
                                    MaxInvestFullMomey = (int) InvestFullMomey;
                                    Toast.makeText(context, "需增加投资额￥" + (InvestFullMomey - lastInvestFullMomey), Toast.LENGTH_SHORT).show();
                                    intent.putExtra("money", InvestFullMomey + "");
                                }
                                lastInvestFullMomey = InvestFullMomey;
                            } else {
                                MaxInvestFullMomey = 0;
                                for (int x = 0; x < hongbaoList.size(); x++) {
                                    //循环添加所有使用条件money的合
                                    if (hongbaoList.get(x).isright) {
                                        int a = hongbaoList.get(x).getInvestFullMomey();
                                        MaxInvestFullMomey = MaxInvestFullMomey + hongbaoList.get(x).getInvestFullMomey();
                                    }
                                }
                                if (MaxInvestFullMomey < Utils.str2Int(money))
                                    MaxInvestFullMomey = Utils.str2Int(money);
                                intent.putExtra("money", MaxInvestFullMomey + "");
                                if (lastInvestFullMomey < MaxInvestFullMomey) {
                                    Toast.makeText(context, "需增加投资额￥" + (MaxInvestFullMomey - lastInvestFullMomey), Toast.LENGTH_SHORT).show();
                                }
                                lastInvestFullMomey = MaxInvestFullMomey;
                            }
                            System.out.println(HBmoney + "......" + entity.getMoney() + "");
                            HBmoney = (int) (HBmoney + entity.getMoney());
                            intent.putExtra("HBmoney", HBmoney);
                            oneItemClickListener.OnCheckBoxClick(position, false);
                        }
                    } else {
                        entity.isright = false;
                        finalViewHolder.hongbaoChooseBox.setChecked(false);
                        if (oneItemClickListener != null) {
                            int a = (int) hongbaoList.get(position).getMoney();
                            HBmoney = (int) (HBmoney - entity.getMoney());
                            intent.putExtra("HBmoney", HBmoney);

                            int investFullMomey = entity.getInvestFullMomey();
                            if (hongbaoList.size() - 1 == onSize || position > onSize) {
                                MaxInvestFullMomey = MaxInvestFullMomey - investFullMomey;
                                if (MaxInvestFullMomey < Utils.str2Int(money)) {
                                    MaxInvestFullMomey = Utils.str2Int(money);
                                }
                            } else {
                                MaxInvestFullMomey = MaxInvestFullMomey - entity.getInvestFullMomey();
                                if (MaxInvestFullMomey < Utils.str2Int(money)) {
                                    MaxInvestFullMomey = Utils.str2Int(money);
                                }
                            }
                            intent.putExtra("money", MaxInvestFullMomey + "");
                            lastInvestFullMomey = MaxInvestFullMomey;
                        }
                        oneItemClickListener.OnCheckBoxClick(position, true);
                    }
                    context.sendBroadcast(intent);
                }
            });

            return convertView;
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'hong_bao_list_choose_item_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.hongbao_img)
        ImageView hongbaoImg;
        @Bind(R.id.hongbao_list_item_number)
        TextView hongbaoListItemNumber;
        @Bind(R.id.hongbao_img_layout)
        RelativeLayout hongbaoImgLayout;
        @Bind(R.id.hongbao_choose_box)
        CheckBox hongbaoChooseBox;
        @Bind(R.id.hongbao_list_item_type)
        TextView hongbaoListItemType;
        @Bind(R.id.hongbao_list_item_dead_line)
        TextView hongbaoListItemDeadLine;
        @Bind(R.id.hongbao_list_item_time_line)
        TextView hongbaoListItemTimeLine;
        public TextView money_line;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }

    public static interface OneItemClickListener {
        void OnCheckBoxClick(int position, boolean isChecked);
    }

}
