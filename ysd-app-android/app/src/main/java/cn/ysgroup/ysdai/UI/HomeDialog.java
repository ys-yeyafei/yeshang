package cn.ysgroup.ysdai.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import cn.ysgroup.ysdai.Activities.ArticleActivity;
import cn.ysgroup.ysdai.Activities.HuoDongDetailActivity;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by linyujie on 16/7/18.
 */
public class HomeDialog extends Dialog {
    private Context context;
    private ImageView x;
    private ImageView iv;
    private int with;
    private int height;

    private  Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            show();
        }
    };
    public HomeDialog(Context context) {
        super(context, 0);
    }

    public HomeDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        initView();
        initData();
        initListenet();
    }

    private void initListenet() {
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(
                        "Setting", Context.MODE_PRIVATE);
                String currentUrl = sharedPreferences.getString("typeTarget", "");
                sharedPreferences.edit().putBoolean("handled", true).commit();
                //进入活动或者文章页面
                Intent intent = new Intent();

                if (currentUrl.contains("article")) {
                    intent.setClass(context, ArticleActivity.class);

                    int startIndex = currentUrl.lastIndexOf("/");
                    String lastHalf = currentUrl.substring(startIndex + 1, currentUrl.length());
                    System.out.println("滚公试图--" + lastHalf);
                    String articleId = lastHalf.substring(0, lastHalf.length() - 4);
                    System.out.println("滚公试图Id--" + lastHalf);
                    intent.putExtra("header", "文章详情");
                    intent.putExtra("id", articleId);

                } else if (currentUrl.contains("activity")) {
                    intent.setClass(context, HuoDongDetailActivity.class);

                    int startIndex = currentUrl.lastIndexOf("=");
                    String articleId = currentUrl.substring(startIndex + 1, currentUrl.length());
                    System.out.println("活动Id--" + articleId);

                    intent.putExtra("header", "活动详情");
                    intent.putExtra("id", articleId);
                } else {
                    return;
                }
                context.startActivity(intent);
                dismiss();
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private void initView() {
        setCancelable(false);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = windowManager.getDefaultDisplay();
        with = defaultDisplay.getWidth();
        height = defaultDisplay.getHeight();
        View view = View.inflate(context, R.layout.home_dialog, null);

        x = (ImageView) view.findViewById(R.id.home_dialog_x);
        iv = (ImageView) view.findViewById(R.id.home_dialog_iv);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(with, with );
        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(Utils.dip2px(context, 25), Utils.dip2px(context, 25));
        //params1.topMargin = height / 2;
        //params1.leftMargin = (int) (with /2.5);
        params1.leftMargin = (int) (with / 1.3);
        x.setLayoutParams(params1);
//        view.setMinimumWidth(with - with / 3);
//        view.setMinimumHeight(height);
        setContentView(view);

    }

    private void initData() {

    }


    public void setImage(String imageUrl) {
        String imageUrl1 = AppConstants.IMG_URL_SUFFIX + imageUrl;

//        Picasso.with(context).load(imageUrl1).into(iv);
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                if (source.getHeight() > source.getWidth()) {
                    int p = source.getHeight() / source.getWidth();
                    RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(with ,
                                    with * p);
                    params.leftMargin = with / 16;
                    params.rightMargin = with / 16;
                    params.topMargin = height / 20;
                    try{
                        iv.setLayoutParams(params);
                        handler.sendEmptyMessage(0);
                    }catch (Exception e){
                        Log.e("主页弹窗",e.toString());
                    }
                } else {
                    RelativeLayout.LayoutParams params =
                            new RelativeLayout.LayoutParams(with ,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                    int a = (with - with / 4);
                    params.leftMargin = with / 16;
                    params.rightMargin = with / 16;
                    params.topMargin = height / 20;
                    try {
                        iv.setLayoutParams(params);
                        handler.sendEmptyMessage(0);
                    } catch (Exception e) {
                        Log.e("主页弹窗",e.toString());
                    }
                }
                return source;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
        Picasso.with(context)
                .load(imageUrl1)
                .transform(transformation)
                .into(iv);

//        iv.setBackgroundResource(R.mipmap.login_success);
    }
}
