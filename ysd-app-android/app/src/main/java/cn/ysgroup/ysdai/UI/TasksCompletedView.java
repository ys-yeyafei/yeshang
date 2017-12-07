package cn.ysgroup.ysdai.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.ysgroup.ysdai.R;


/**
 * Created by Administrator on 2015/10/15.
 */
public class TasksCompletedView extends View
{
    // 画实心圆的画笔
    private Paint mCirclePaint;
    // 画圆环的画笔
    private Paint mRingPaint;
    // 画字体的画笔
    private Paint mTextPaint;
    // 圆形颜色
    private int mCircleColor;
    // 圆环颜色
    private int mRingColor;
    //圆环满标的时候的颜色
    private int mFullColor;
    // 半径
    private float mRadius;
    // 圆环半径
    private float mRingRadius;
    // 圆环宽度
    private float mStrokeWidth;
    //实心圆边框宽度
    private float mCircleStrokeWidth;
    // 圆心x坐标
    private int mXCenter;
    // 圆心y坐标
    private int mYCenter;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;
    // 总进度
    private int mTotalProgress = 100;
    // 当前进度
    private int mProgress;

    //自定义字符串
    private String textTV;



    public TasksCompletedView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        // 获取自定义的属性
        initAttrs(context, attrs);
    }

    public void setTextTV(String textTV)
    {
        this.textTV = textTV;
    }

    private void initAttrs(Context context, AttributeSet attrs)
    {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TasksCompletedView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
        mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
        mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);
        mFullColor = typeArray.getColor(R.styleable.TasksCompletedView_fullColor, 0xFFFFFFFF);
        mCircleStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_circleStrokeWidth, 10);
        // mRingRadius = mRadius + mStrokeWidth / 2;
        mRingRadius = mRadius;
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setARGB(255, 231, 121, 24);
        mTextPaint.setTextSize(mRadius / 2);

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);

        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;

        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

        if(mProgress >=100){
            mRingPaint.setColor(mFullColor);
            mTextPaint.setColor(mFullColor);
        }
        if (mProgress > 0)
        {
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius);
            oval.top = (mYCenter - mRingRadius);
            oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
            oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
            canvas.drawArc(oval, -90, ((float) mProgress / mTotalProgress) * 360, false, mRingPaint);
//                        canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);

        }
        String txt = mProgress + "%";

        if (textTV != null && !textTV.trim().equals(""))
        {
            txt = textTV + "\n" + mProgress + "%";
        }
        mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
        canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
    }

    public void setProgress(int progress)
    {

        mProgress = progress;
        postInvalidate();
    }

}
