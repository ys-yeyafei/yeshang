package cn.ysgroup.ysdai.Activities;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import cn.ysgroup.ysdai.Lock.GestureLockViewGroup;
import cn.ysgroup.ysdai.Lock.GestureLockViewGroupLittle;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.IconFontTextView;
import cn.ysgroup.ysdai.UI.LMessageDialog;
import cn.ysgroup.ysdai.UI.LoadingDialog;
import com.umeng.analytics.MobclickAgent;


public class GestureCodeActivity extends AppCompatActivity {

    private GestureLockViewGroup gesture_view;
    private GesturebroadCast broadCast;
    private TextView text;
    private String code;
    private Toolbar toolbar;
    public static String from;
    private GestureLockViewGroupLittle littleView;
    public static String cancle;
    private TextView gesture_forget;
    private LoadingDialog loadingDialog;
    private String phone;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_code);
        initView();
    }

    private void initView() {
        phone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        if (phone == null)
            phone = "0";
        Intent mIntent = getIntent();
        from = mIntent.getStringExtra("from");
        cancle = mIntent.getStringExtra("cancle");

        code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                getString(phone, null);
        broadCast = new GesturebroadCast();
        IntentFilter intentFilter = new IntentFilter("GesturebroadCast");
        registerReceiver(broadCast, intentFilter);
        gesture_view = (GestureLockViewGroup) findViewById(R.id.gesture_view);
        littleView = (GestureLockViewGroupLittle) findViewById(R.id.gesture_little_view);
        gesture_forget = (TextView) findViewById(R.id.gesture_forget);
        IconFontTextView back = (IconFontTextView) findViewById(R.id.gesture_back);
        gesture_view.setDate(this);
        toolbar = (Toolbar) findViewById(R.id.gesture_toolbar);
        text = (TextView) findViewById(R.id.gesture_text);

        if (from != null && from.equals("welcome")) {
            toolbar.setVisibility(View.GONE);
            littleView.setVisibility(View.GONE);
            gesture_forget.setVisibility(View.VISIBLE);
        } else if (from != null && from.equals("launch")) {
            toolbar.setVisibility(View.GONE);
            littleView.setVisibility(View.GONE);
            gesture_forget.setVisibility(View.VISIBLE);
        } else {
            gesture_forget.setVisibility(View.GONE);
        }
        if (cancle != null) {
            littleView.setVisibility(View.GONE);
        }
        if (mIntent.getStringExtra("cancle") != null && mIntent.getStringExtra("cancle").equals("sure")
                || mIntent.getStringExtra("cancle") != null && mIntent.getStringExtra("cancle").equals("modify")) {
            text.setText("请输入原手势密码");
        }
        if (code != null) {
            gesture_view.setAnswer(code);
            gesture_view.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {
                @Override
                public void onBlockSelected(int cId) {

                }

                @Override
                public void onGestureEvent(boolean matched) {

                }

                @Override
                public void onUnmatchedExceedBoundary() {

                }
            });
        }


        gesture_forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LMessageDialog dialog = new LMessageDialog(GestureCodeActivity.this, R.style.ActionSheetDialogStyle);
                dialog.setMessage("登录成功后，原手势密码将失效。您可到账户设置中心重新设置手势密码");
                dialog.setMtitle("确定");
                dialog.show();
                dialog.setCancelable(true);
                dialog.getMessage().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent lIntent = new Intent(new Intent(GestureCodeActivity.this, LoginActivity.class));
                        lIntent.putExtra("from", "gesture");
                        GestureCodeActivity.this.
                                startActivity(lIntent);
                    }
                });
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    class GesturebroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("value").equals("four")) {
                text.setText("请至少连接4个点以上");
                text.setTextColor(Color.parseColor("#ff0000"));
                //动画

                Animation loadAnimation = AnimationUtils.loadAnimation(GestureCodeActivity.this, R.anim.gesture_translate);
                text.startAnimation(loadAnimation);
            } else if (intent.getStringExtra("value").equals("again")) {
                //此时拿到密码
                littleView.setAnswer(getSharedPreferences(phone, Context.MODE_PRIVATE).
                        getString(phone, null));
                text.setText("确认解锁图案");
                text.setTextColor(Color.parseColor("#6c6c6c"));
            } else if (intent.getStringExtra("value").equals("finish")) {
                finish();
            } else if (intent.getStringExtra("value").equals("difference")) {
                text.setText("与上一次绘制不一致，请重新绘制");
                text.setTextColor(Color.parseColor("#ff0000"));
                Animation loadAnimation = AnimationUtils.loadAnimation(GestureCodeActivity.this, R.anim.gesture_translate);
                text.startAnimation(loadAnimation);
            } else if (intent.getStringExtra("value").equals("mTryTimes")) {
                String mTryTimes = context.getSharedPreferences(phone, Context.MODE_PRIVATE).getString("mTryTimes", null);
                text.setTextColor(Color.parseColor("#ff0000"));
                text.setText("您还有" + mTryTimes + "次输入机会");
                Animation loadAnimation = AnimationUtils.loadAnimation(GestureCodeActivity.this, R.anim.gesture_translate);
                text.startAnimation(loadAnimation);
            } else if (intent.getStringExtra("value").equals("close")) {
                GestureCodeActivity.this.setResult(100, GestureCodeActivity.this.getIntent());
                finish();
            } else if (intent.getStringExtra("value").equals("modify")) {
                GestureCodeActivity.this.setResult(200, GestureCodeActivity.this.getIntent());
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadCast);
        if (gesture_view.isAgain) {
            getSharedPreferences(phone, Context.MODE_PRIVATE).edit().clear().commit();
            gesture_view.isAgain = false;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (from != null && from.equals("launch")){
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 2000) {
                    Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    exitTime = System.currentTimeMillis();
                } else {
                    int currentVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentVersion > android.os.Build.VERSION_CODES.ECLAIR_MR1) {
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);
                        System.exit(0);
                    } else {// android2.1
                        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                        am.restartPackage(getPackageName());
                    }
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
