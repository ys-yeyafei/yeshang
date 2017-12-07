package cn.ysgroup.ysdai.UI;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;

/**
 * Created by linyujie on 16/7/18.
 */
public class LMessageDialog extends Dialog {
    private Context context;
    private TextView message;
    private TextView title;

    public LMessageDialog(Context context) {
        super(context, 0);
    }

    public LMessageDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initData();
    }



    private void initView() {
        View view = View.inflate(context, R.layout.lmessage_dialog, null);
        title = (TextView) view.findViewById(R.id.imessage_title);
        message = (TextView) view.findViewById(R.id.imessage_message);
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        setContentView(view);

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
}
