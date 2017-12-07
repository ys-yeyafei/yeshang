package cn.ysgroup.ysdai.Lock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;

public class GestureLockLittleView extends View {
    private static final String TAG = "GestureLockView";


    public static final int FILL_COLOR = Color.parseColor("#1552ff");
    private int answerLength;

    public void setAnswerLength(int answerLength) {
        this.answerLength = answerLength;
    }

    /**
     * GestureLockView的三种状态
     */
    enum Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP;
    }

    /**
     * GestureLockView的当前状态
     */
    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;

    /**
     * 宽度
     */
    private int mWidth;
    /**
     * 高度
     */
    private int mHeight;
    /**
     * 外圆半径
     */
    private int mRadius;
    /**
     * 画笔的宽度
     */
    private int mStrokeWidth = 2;

    /**
     * 圆心坐标
     */
    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;

    /**
     * 箭头（小三角最长边的一半长度 = mArrawRate * mWidth / 2 ）
     */
    private float mArrowRate = 0.25f;
    private int mArrowDegree = -1;
    private Path mArrowPath;
    /**
     * 内圆的半径 = mInnerCircleRadiusRate * mRadus
     */
    private float mInnerCircleRadiusRate = 0.25F;

    /**
     * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
     */
    private int mColorNoFingerInner;
    private int mColorNoFingerOutter;
    private int mColorFingerOn;
    private int mColorFingerUp;

    public GestureLockLittleView(Context context, int colorNoFingerInner, int colorNoFingerOutter, int colorFingerOn, int colorFingerUp) {
        super(context);
        mCurrentStatus=Mode.STATUS_NO_FINGER;
        this.mColorNoFingerInner = colorNoFingerInner;
        this.mColorNoFingerOutter = colorNoFingerOutter;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUp = colorFingerUp;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPath = new Path();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 取长和宽中的小值
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        mRadius = mCenterX = mCenterY = mWidth / 2;
        mRadius -= mStrokeWidth / 2;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mCurrentStatus) {
            case STATUS_NO_FINGER:
                mPaint.setStyle(Style.STROKE);
                mPaint.setStrokeWidth(3);
                mPaint.setColor(mColorNoFingerInner);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                break;
            case STATUS_FINGER_UP:
                mPaint.setStyle(Style.FILL);
                mPaint.setColor(FILL_COLOR);
                canvas.drawCircle(mCenterX, mCenterY, mRadius
                        , mPaint);
//                mPaint.setColor(mColorNoFingerInner);
//                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                break;
        }

    }


    /**
     * 设置当前模式并重绘界面
     *
     * @param mode
     */
    public void setMode(GestureLockLittleView.Mode mode) {
        this.mCurrentStatus = mode;
        invalidate();
    }

    public void setArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }

    public int getArrowDegree() {
        return this.mArrowDegree;
    }
}
