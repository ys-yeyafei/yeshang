package cn.ysgroup.ysdai.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;


/**
 * Created by Administrator on 2015/11/19.
 */
public class LoadingDialog extends Dialog
{

private TextView message;

    public LoadingDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCustomDialog();
    }

    private void setCustomDialog() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.loading_layout, null);
        message =(TextView)mView.findViewById(R.id.loading_message);
        this.setCanceledOnTouchOutside(false);
        super.setContentView(mView);
    }


    public void setMessage(String msg){
        message.setText(msg);
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

}
