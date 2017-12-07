package cn.ysgroup.ysdai.Gesture.gestures.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;


/**
 * Created by Administrator on 2015/10/28.
 */
public class DownDialog extends Dialog {
    private TextView cancle;
    private ListView listView;
    private TextView now;
    private Context context;
    private TextView version;
    private String versionT;
    private String msg;

    public DownDialog(Context context, String versionT,String msg) {
        super(context, R.style.CustomDialog);
        this.context = context;
        this.versionT = versionT;
        this.msg=msg;
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.down_dialog_layout, null);
        cancle = (TextView) mView.findViewById(R.id.down_cancle);
        listView = (ListView) mView.findViewById(R.id.down_list);
        now = (TextView) mView.findViewById(R.id.down_now);
        listView.setAdapter(new DownAdapter());
        version = (TextView) mView.findViewById(R.id.down_version);
        version.setText("V"+versionT);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        super.setContentView(mView);
    }


    @Override
    public void setContentView(int layoutResID) {
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
    }

    @Override
    public void setContentView(View view) {


    }

    public TextView getNow(){
        return now;
    }




    class DownAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 1;
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(context, R.layout.down_dialog_tiem, null);
            TextView msgT = (TextView) convertView.findViewById(R.id.down_item_msg);
            msgT.setText(msg);
            return convertView;
        }
    }

}
