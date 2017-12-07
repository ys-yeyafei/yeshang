package cn.ysgroup.ysdai.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linyujie on 17/3/3.
 */

public class MarqueeTextView extends ViewFlipper {

    private Context mContext;
    private List<String> notices;
    private boolean isSetAnimDuration = false;

    private int interval = 2000;
    private int animDuration = 500;
    private int textSize = 13;
    private int textColor = 0x000000;
    private List<String> notices1;

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        if (notices == null) {
            notices = new ArrayList<>();
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);
        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);
        if (typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)) {
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = Utils.px2dip(mContext, textSize);
        }
        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        typedArray.recycle();

        setFlipInterval(6000);

        Animation animIn = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_in);
        if (isSetAnimDuration) animIn.setDuration(animDuration);
        setInAnimation(animIn);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.anim_marquee_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

    // 根据公告字符串启动轮播
    public void startWithText(final String notice) {
        if (TextUtils.isEmpty(notice)) return;
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startWithFixedWidth(notice, getWidth());
            }
        });
    }

    // 根据公告字符串列表启动轮播
    public void startWithList(List<String> notices,List<String> notices1) {
        setNotices(notices,notices1);
        start();
    }

    // 根据宽度和公告字符串启动轮播
    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();
        int dpW = Utils.px2dip(mContext, width);
        int limit = dpW/textSize;
        if (dpW == 0) {
            throw new RuntimeException("Please set MarqueeView width !");
        }

        if (noticeLength <= limit) {
            notices.add(notice);
        } else {
            int size = noticeLength/limit + (noticeLength%limit != 0? 1:0);
            for (int i=0; i<size; i++) {
                int startIndex = i*limit;
                int endIndex = ((i+1)*limit >= noticeLength? noticeLength:(i+1)*limit);
                notices.add(notice.substring(startIndex, endIndex));
            }
        }
        start();
    }

    // 启动轮播
    public boolean start() {
        if (notices == null || notices.size() == 0) return false;
        removeAllViews();
//        for (String notice:notices) {
//            addView(createTextView(notice));
//        }
        for(int x=0;x<notices.size();x++){
            addView(createTextView(notices.get(x),notices1.get(x)));
        }
        if (notices.size() > 1) {
            startFlipping();
        }
        return true;
    }

    // 创建ViewFlipper下的TextView
    private TextView createTextView(String text,String text1) {
        TextView tv = new TextView(mContext);
        tv.setTag(text1);
        tv.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
        tv.setText(text);
        tv.setTextColor(Color.parseColor("#848484"));
        tv.setTextSize(11);
        return tv;
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setNotices(List<String> notices,List<String> notices1) {
        this.notices = notices;
        this.notices1 = notices1;
    }

}
