package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.List;

import cn.ysgroup.ysdai.Beans.bank.BankCard;
import cn.ysgroup.ysdai.Beans.bank.BankList;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;

public class SelectBankActivity extends MyBaseActivity implements View.OnClickListener {

    private IconFontTextView back;
    private ListView listView;
    private Button button;
    private SelectBankAdapter adapter;
    private List<BankCard> bankCardList;
    private int currentPosition=100;
    private BankCard currentBankCard;
    private int selectPosition=200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bank);
        initView();
        initListener();
    }

    private void initListener() {

    }

    private void initView() {
        Intent intent = getIntent();
        selectPosition=intent.getIntExtra("selectPosition",200);
        BankList bankList= (BankList) intent.getSerializableExtra("bean");
        bankCardList = bankList.getBankCardList();

        if(null != bankCardList){
            Iterator<BankCard> iterator = bankCardList.iterator();

            while(iterator.hasNext()){
                BankCard item = iterator.next();

                if("CMB".equals(item.getBankId())){
                    iterator.remove();
                }
            }
        }

        back = (IconFontTextView) findViewById(R.id.select_bank_back);
        listView = (ListView) findViewById(R.id.select_bank_list);
        button = (Button) findViewById(R.id.select_bank_button);
        button.setOnClickListener(this);
        back.setOnClickListener(this);
        adapter = new SelectBankAdapter();
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //返回按钮
            case R.id.select_bank_back:
                finish();
                break;
            case R.id.select_bank_button:
                if(currentPosition!=100 && currentBankCard!=null){
                    Intent intent=new Intent("RealNameAuthActivityBroadCast");
                    intent.putExtra("mark","selectBank");
                    intent.putExtra("position",currentPosition);
                    intent.putExtra("BankCard",currentBankCard);
                    sendBroadcast(intent);
                }
                finish();
                break;
        }
    }

    public class SelectBankAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bankCardList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //银行较少不需要做缓存
            final BankCard bankCard = bankCardList.get(position);
            convertView=View.inflate(SelectBankActivity.this,R.layout.select_bank_list_item,null);
            TextView name= (TextView) convertView.findViewById(R.id.select_bank_list_item_bankname);
            TextView limit= (TextView) convertView.findViewById(R.id.select_bank_list_item_limit);
            ImageView iv= (ImageView) convertView.findViewById(R.id.select_bank_list_item_iv);
            final CheckBox checkBox= (CheckBox) convertView.findViewById(R.id.select_bank_list_item_checkbox);
            name.setText(bankCard.getBankName());
            iv.setImageResource(SelectBankActivity.this.getResources().
                    getIdentifier("bank_"+bankCard.getBankId().toLowerCase(),
                            "mipmap" , SelectBankActivity.this.getPackageName()));
            limit.setText(bankCard.getBankLimit());
            if(position==selectPosition){
                bankCard.isCheck=true;
                selectPosition=200;
            }
            if(bankCard.isCheck ){
                //以选择状态
                checkBox.setBackgroundResource(R.mipmap.checked_box_);
            }else{
                checkBox.setBackgroundResource(R.mipmap.box_normal);
            }
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bankCard.isCheck){
                        checkBox.setBackgroundResource(R.mipmap.box_normal);
                        bankCard.isCheck=false;
                    }else {
                        for(int x=0;x<bankCardList.size();x++){
                            if(x!=position){
                                bankCardList.get(x).isCheck=false;
                                notifyDataSetChanged();
                                currentBankCard=null;
                                currentPosition=100;
                            }
                        }
                        currentBankCard=bankCard;
                        currentPosition=position;
                        checkBox.setBackgroundResource(R.mipmap.checked_box_);
                        bankCard.isCheck=true;
                    }
                }
            });
            return convertView;
        }
    }



    public interface BankButtonListener{
        public void ButtonListener();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
