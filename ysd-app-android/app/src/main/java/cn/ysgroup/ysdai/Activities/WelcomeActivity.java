package cn.ysgroup.ysdai.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;

import cn.ysgroup.ysdai.R;
import com.umeng.analytics.MobclickAgent;

import cn.jpush.android.api.JPushInterface;

public class WelcomeActivity extends Activity
{


    private String code;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (!isTaskRoot()) {
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        setContentView(R.layout.activity_welcome);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置为全屏幕显示
        phone = getSharedPreferences("UserId", Context.MODE_PRIVATE).getString("UserId", null);
        if(phone==null)
            phone="0";
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable()
        {
            public void run()
            {
                boolean isSplash=getSharedPreferences("splash", Context.MODE_PRIVATE).getBoolean("splash",true);
                if(isSplash){
                    getSharedPreferences("splash", Context.MODE_PRIVATE).edit().putBoolean("splash",false).commit();
                    //进入闪屏
                    startActivity(new Intent(WelcomeActivity.this,SplashActivity.class));
                    WelcomeActivity.this.finish();

                }else{
                    //判断是否有手势密码
                    code = getSharedPreferences(phone, Context.MODE_PRIVATE).
                            getString(phone, null);

                    if(code!=null && getSharedPreferences("UserId", Context.MODE_PRIVATE).
                            getString("UserId", null)!=null){
                        MyBaseActivity.isActive=true;
                        Intent intent = new Intent();
                        intent.putExtra("from","welcome");
                        intent.setClass(WelcomeActivity.this, GestureCodeActivity.class);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    }else{
                        Intent intent = new Intent();
                        intent.setClass(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        WelcomeActivity.this.finish();
                    }
                }
            }
        }, 2000);
    }

    public void onResume() {
        super.onResume();
        //友盟api
        MobclickAgent.onResume(this);
        JPushInterface.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        JPushInterface.onPause(this);
    }



}
