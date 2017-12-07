package cn.ysgroup.ysdai.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;


/**
 * Created by 2014 on 2015/8/13.
 */
public class ConfirmDialog extends Dialog {
    private Button positiveButton, negativeButton;
    private TextView message;

    public ConfirmDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.confirm_dialog_layout, null);
        message = (TextView) mView.findViewById(R.id.dia_message);
        positiveButton = (Button) mView.findViewById(R.id.dia_positiveButton);
        negativeButton = (Button) mView.findViewById(R.id.dia_negativeButton);
        this.setCanceledOnTouchOutside(false);
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

    public void setMessage(String messageString){
        message.setText(messageString);
    }

    /**
     * 确定键监听器
     * @param listener
     */
    public void setOnPositiveListener(View.OnClickListener listener){
        positiveButton.setOnClickListener(listener);
    }
    /**
     * 取消键监听器
     * @param listener
     */
    public void setOnNegativeListener(View.OnClickListener listener){
        negativeButton.setOnClickListener(listener);
    }

}
