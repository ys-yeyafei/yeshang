package cn.ysgroup.ysdai.Gesture.gestures.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.ysgroup.ysdai.Activities.GestureCodeActivity;
import cn.ysgroup.ysdai.R;

/**
 * Created by linyujie on 16/7/18.
 */
public class SMessageDialog extends Dialog {
    private Context context;
    private TextView message;
    private TextView title;
    private TextView message1;

    public SMessageDialog(Context context) {
        super(context, 0);
    }

    public SMessageDialog(Context context, int actionSheetDialogStyle) {
        super(context, actionSheetDialogStyle);
        this.context = context;
        initView();
        initData();
    }


    private void initView() {
        View view = View.inflate(context, R.layout.smessage_dialog, null);
        title = (TextView) view.findViewById(R.id.smessage_title);
        message = (TextView) view.findViewById(R.id.smessage_message);
        message1 = (TextView) view.findViewById(R.id.smessage_message1);
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        setContentView(view);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        message1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //立即开启
                //进入密码设置页面
                Intent intent=new Intent(context,GestureCodeActivity.class);
                context.startActivity(intent);
                dismiss();
            }
        });
    }

    private void initData() {

    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        title.setText(msg);
    }

    public void setMtitle(String mtitle) {
        message.setText(mtitle);
    }
    public void setMtitle1(String mtitle1) {
        message1.setText(mtitle1);
    }
}
