package cn.ysgroup.ysdai.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;


/**
 * Created by Administrator on 2015/10/28.
 */
public class MessageDialog extends Dialog {
        private TextView confirm;
        private TextView message;

        public MessageDialog(Context context) {
            super(context, R.style.CustomDialog);
            setCustomDialog();
        }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.message_dialog_layout, null);
        message = (TextView) mView.findViewById(R.id.message_dialog_msg);
        confirm = (TextView) mView.findViewById(R.id.message_dialog_confirm);
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
    /**
     * 设置信息
     * @param msg
     */
    public void setMessage(String msg){
        message.setText(msg);
    }
    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        confirm.setOnClickListener(listener);
    }

}
