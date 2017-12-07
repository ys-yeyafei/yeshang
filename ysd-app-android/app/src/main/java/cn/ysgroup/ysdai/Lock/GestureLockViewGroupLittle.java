package cn.ysgroup.ysdai.Lock;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import cn.ysgroup.ysdai.Activities.GestureCodeActivity;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.LoadingDialog;


/**
 * 整体包含n*n个GestureLockView,每个GestureLockView间间隔mMarginBetweenLockView，
 * 最外层的GestureLockView与容器存在mMarginBetweenLockView的外边距
 * <p/>
 * 关于GestureLockView的边长（n*n）： n * mGestureLockViewWidth + ( n + 1 ) *
 * mMarginBetweenLockView = mWidth ; 得：mGestureLockViewWidth = 4 * mWidth / ( 5
 * * mCount + 1 ) 注：mMarginBetweenLockView = mGestureLockViewWidth * 0.25 ;
 *
 * @author zhy
 */
public class GestureLockViewGroupLittle extends RelativeLayout {

    private static final String TAG = "GestureLockViewGroup";
    private Context context;
    /**
     * 保存所有的GestureLockView
     */
    private GestureLockLittleView[] mGestureLockViews;
    /**
     * 每个边上的GestureLockView的个数
     */
    private int mCount = 3;
    /**
     * 存储答案
     */
    private String mAnswer;
    /**
     * 保存用户选中的GestureLockView的id
     */
    private StringBuilder mChoose = new StringBuilder();

    private Paint mPaint;
    /**
     * 每个GestureLockView中间的间距 设置为：mGestureLockViewWidth * 25%
     */
    private int mMarginBetweenLockView = 40;
    /**
     * GestureLockView的边长 4 * mWidth / ( 5 * mCount + 1 )
     */
    private int mGestureLockViewWidth;

    /**
     * GestureLockView无手指触摸的状态下内圆的颜色
     */
    private int mNoFingerInnerCircleColor = 0x00000000;
    /**
     * GestureLockView无手指触摸的状态下外圆的颜色
     */
    private int mNoFingerOuterCircleColor = 0xFFFFFFFF;
    /**
     * GestureLockView手指触摸的状态下内圆和外圆的颜色
     */
    private int mFingerOnColor = 0xff3f9de3;
    /**
     * GestureLockView手指抬起的状态下内圆和外圆的颜色
     */
    private int mFingerUpColor = 0xFFFF0000;

    /**
     * 指引线的开始位置x
     */
    private int mLastPathX;
    /**
     * 指引线的开始位置y
     */
    private int mLastPathY;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;





    private Intent intent = new Intent("GesturebroadCast");
    public boolean isAgain = false;
    private LoadingDialog loadingDialog;
    private GestureCodeActivity date;
    private Path mPath;


    public GestureLockViewGroupLittle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public GestureLockViewGroupLittle(Context context, AttributeSet attrs,
                                      int defStyle) {
        super(context, attrs, defStyle);
        /**
         * 获得所有自定义的参数的值
         */
        this.context = context;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.GestureLockViewGroup, defStyle, 0);
        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle:
                    mNoFingerInnerCircleColor = a.getColor(attr,
                            mNoFingerInnerCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle:
                    mNoFingerOuterCircleColor = a.getColor(attr,
                            mNoFingerOuterCircleColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_on:
                    mFingerOnColor = a.getColor(attr, mFingerOnColor);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_up:
                    mFingerUpColor = a.getColor(attr, mFingerUpColor);
                    break;
                case R.styleable.GestureLockViewGroup_count:
                    mCount = a.getInt(attr, 3);
                    break;
                case R.styleable.GestureLockViewGroup_tryTimes:

                default:
                    break;
            }
        }

        a.recycle();

        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(20);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setColor(GestureLockLittleView.FILL_COLOR);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // Log.e(TAG, mWidth + "");
        // Log.e(TAG, mHeight + "");

        mHeight = mWidth = mWidth < mHeight ? mWidth : mHeight;

        // setMeasuredDimension(mWidth, mHeight);

        // 初始化mGestureLockViews
        if (mGestureLockViews == null) {
            mGestureLockViews = new GestureLockLittleView[mCount * mCount];
            // 计算每个GestureLockView的宽度
            mGestureLockViewWidth = (int) (2 * mWidth * 1.0f / (4 * mCount));
            //计算每个GestureLockView的间距
            mMarginBetweenLockView = (int) (mGestureLockViewWidth * 0.60);
            // 设置画笔的宽度为GestureLockView的内圆直径稍微小点（不喜欢的话，随便设）
            mPaint.setStrokeWidth(mGestureLockViewWidth * 0.29f);

            for (int i = 0; i < mGestureLockViews.length; i++) {
                //初始化每个GestureLockView
                mGestureLockViews[i] = new GestureLockLittleView(getContext(),
                        mNoFingerInnerCircleColor, mNoFingerOuterCircleColor,
                        mFingerOnColor, mFingerUpColor);
                mGestureLockViews[i].setId(i + 1);
                //设置参数，主要是定位GestureLockView间的位置
                LayoutParams lockerParams = new LayoutParams(
                        mGestureLockViewWidth, mGestureLockViewWidth);

                // 不是每行的第一个，则设置位置为前一个的右边
                if (i % mCount != 0) {
                    lockerParams.addRule(RelativeLayout.RIGHT_OF,
                            mGestureLockViews[i - 1].getId());
                }
                // 从第二行开始，设置为上一行同一位置View的下面
                if (i > mCount - 1) {
                    lockerParams.addRule(RelativeLayout.BELOW,
                            mGestureLockViews[i - mCount].getId());
                }
                //设置右下左上的边距
                int rightMargin = mMarginBetweenLockView;
                int bottomMargin = mMarginBetweenLockView;
                int leftMagin = 0;
                int topMargin = 0;
                /**
                 * 每个View都有右外边距和底外边距 第一行的有上外边距 第一列的有左外边距
                 */
                if (i >= 0 && i < mCount)// 第一行
                {
                    topMargin = mMarginBetweenLockView;
                }
                if (i % mCount == 0)// 第一列
                {
                    leftMagin = mMarginBetweenLockView;
                }

                lockerParams.setMargins(leftMagin, topMargin, rightMargin,
                        bottomMargin);
                mGestureLockViews[i].setMode(GestureLockLittleView.Mode.STATUS_NO_FINGER);
                addView(mGestureLockViews[i], lockerParams);
            }

            Log.e(TAG, "mWidth = " + mWidth + " ,  mGestureViewWidth = "
                    + mGestureLockViewWidth + " , mMarginBetweenLockView = "
                    + mMarginBetweenLockView);

        }
    }

    protected void dispatchDraw(Canvas canvas){
        super.dispatchDraw(canvas);

        //绘制GestureLockLittleView间的连线
        if (mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }
    }


//    private void changeItemMode(GestureLockView.Mode mode) {
//
//        for (GestureLockLittleView gestureLockView : mGestureLockViews) {
//            if (mChoose.indexOf(gestureLockView.getId() + "") != -1) {
//                gestureLockView.setMode();
//            }
//        }
//    }



    /**
     * 对外公布设置答案的方法
     *
     * @param answer
     */
    public void setAnswer(String answer) {
        this.mAnswer = answer;
        StringBuilder stringBuilder=new StringBuilder(answer);
        if(mGestureLockViews!=null){
            for (int x=0;x<stringBuilder.length();x++){
                String a=stringBuilder.substring(x,x+1);        //3241  index:2130
                for(int y=0;y<mGestureLockViews.length;y++){
                    if(a.equals(y+1+"")){
                        View child = mGestureLockViews[y];

                        mLastPathX = child.getLeft() / 2 + child.getRight() / 2;
                        mLastPathY = child.getTop() / 2 + child.getBottom() / 2;

                        if(x == 0 ){
                            mPath.moveTo(mLastPathX, mLastPathY);
                        }else {
                            mPath.lineTo(mLastPathX, mLastPathY);
                        }

                        mGestureLockViews[y].setMode(GestureLockLittleView.Mode.STATUS_FINGER_UP);


                    }
                }
            }

            postInvalidate();
        }
    }




    public void setDate(GestureCodeActivity date) {
        this.date = date;
    }




}
