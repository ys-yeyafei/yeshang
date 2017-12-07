package cn.ysgroup.ysdai.Gesture.gestures.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;

/**
 * Created by linyujie on 16/7/18.
 */
public class CustomerDialog extends Dialog {
    private Context context;
    private TextView call;
    private TextView cancel;

    public CustomerDialog(Context context) {
        super(context, 0);
    }

    public CustomerDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initListenet();
    }

    private void initListenet() {
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000577820"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void initView() {
        View view = View.inflate(context, R.layout.customer_dialog, null);
        call = (TextView) view.findViewById(R.id.customer_call);
        cancel = (TextView) view.findViewById(R.id.customer_cancel);
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        setContentView(view);

    }


}
