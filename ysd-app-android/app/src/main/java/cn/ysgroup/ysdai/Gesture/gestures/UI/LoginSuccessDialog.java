package cn.ysgroup.ysdai.Gesture.gestures.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ysgroup.ysdai.Activities.HongBaoActivity;
import cn.ysgroup.ysdai.R;

/**
 * Created by linyujie on 16/7/18.
 */
public class LoginSuccessDialog extends Dialog {
    private Context context;
    private ImageView iv;
    private Button button;
    private TextView tv;
    private String userId;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    tv.setText("1秒");
                    handler.sendEmptyMessageDelayed(1,2000);
                    break;
                case 1:
                    handler.removeCallbacks(null);
                    dismiss();
                    SMessageDialog dialog=new SMessageDialog(context,R.style.ActionSheetDialogStyle);
                    dialog.setCancelable(false);
                    dialog.setMtitle("下次再说");
                    dialog.setMtitle1("立即开启");
                    dialog.show();

                    break;
            }

        }
    };

    public LoginSuccessDialog(Context context) {
        super(context, 0);
    }

    public LoginSuccessDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initData();
        initListenet();
    }

    private void initListenet() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, HongBaoActivity.class);
                intent.putExtra("userId", userId);
                context.startActivity(intent);
                handler.removeCallbacks(null);
                dismiss();
            }
        });
    }


    private void initView() {
        View view = View.inflate(context, R.layout.login_success_dialog, null);
        button = (Button) view.findViewById(R.id.login_success_dialog_button);
        iv = (ImageView) view.findViewById(R.id.login_success_dialog_iv);
        tv = (TextView) view.findViewById(R.id.login_success_dialog_tv);
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        int with = defaultDisplay.getWidth();
        int height = defaultDisplay.getHeight();


        setContentView(view);

    }

    private void initData() {

    }


    public void start(String id) {
        userId=id;
        Intent intent=new Intent("HomeFragmentBroadCast");
        intent.putExtra("Login",101);
        context.sendBroadcast(intent);
        handler.sendEmptyMessageDelayed(0,2000);
        show();
    }
}
