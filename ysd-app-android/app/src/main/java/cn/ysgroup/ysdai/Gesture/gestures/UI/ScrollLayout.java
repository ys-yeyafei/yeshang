package cn.ysgroup.ysdai.Gesture.gestures.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import cn.ysgroup.ysdai.Activities.PreviewOneActivity;
import cn.ysgroup.ysdai.Activities.ProjectDetailActivity;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowBean;
import cn.ysgroup.ysdai.R;

/**
 * Created by linyujie on 16/10/14.
 */

public class ScrollLayout extends LinearLayout {
    private Context context;
    private WindowManager wm;
    private int screenHeight;
    private int startY;
    //处理滑动事件的最小距离
    private float FLING_MIN_DISTANCE;
    private GestureDetector gestureDetector;
    private PreviewOneActivity activity;
    private String Title;
    private int id;
    private String image;
    private BorrowBean projectItem;

    public ScrollLayout(Context context) {
        super(context);
        initDate(context);
    }


    public ScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initDate(context);
    }

    public ScrollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDate(context);
    }


    private void initDate(Context context) {
        this.context=context;
        screenHeight = getScreenHeight();
        FLING_MIN_DISTANCE=screenHeight/15;
        gestureDetector = new GestureDetector(context, new MyGestureListener());

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        gestureDetector.onTouchEvent(event);
        return true;
    }


    //获取屏幕高度的方法
    public int getScreenHeight(){
        WindowManager wm =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    public void setActivity(PreviewOneActivity activity) {
        this.activity = activity;
    }

    public void setDate(int id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setProjectItem(BorrowBean projectItem) {
        this.projectItem = projectItem;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            float x1 = e1.getX();
            float y1 = e1.getY();
            float x2 = e2.getX();
            float y2 = e2.getY();
            Log.d("TAG", "x1 = " + x1 + ", x2 = " + x2 + ", y1 = " + y1 + ", y2 = " + y2);

            float flingX = Math.abs(x2 - x1);
            float flingY = Math.abs(y2 - y1);
            Log.d("TAG", "flingX = " + flingX + ", flingY = " + flingY);

            if (flingY < FLING_MIN_DISTANCE) {
                Log.d("TAG", "滑动距离太小，不处理！");
                return false;
            }

            if (flingX > flingY) {
                Log.d("TAG", "滑动倾斜度太大，不处理！");
                return false;
            }

            //判断滑动方向
            if (y2 <y1) {
                Log.d("TAG", "滑出界面");
                Intent intent=new Intent(context, ProjectDetailActivity.class);
                System.out.println(Title);
                intent.putExtra("id",id);
                intent.putExtra("title",Title);
                intent.putExtra("borrowImg",image);
                Bundle bundle=new Bundle();
                bundle.putSerializable("item",projectItem);
                intent.putExtras(bundle);
                if(projectItem==null || activity==null){
                    Toast.makeText(context,"请检查您的网络",Toast.LENGTH_SHORT).show();

                }else{
                    activity.startActivityForResult(intent,10000);
                    activity.overridePendingTransition(R.anim.activity_up,R.anim.activity_down);
                }
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }
    public void setTitle(String title){
        Title=title;
    }
}
