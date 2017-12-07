package cn.ysgroup.ysdai.Gesture.gestures.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Administrator on 2015/10/21.
 *
 *
 *
 */
public class IconFontTextView extends TextView{


    public IconFontTextView(Context context) {
        super(context);
        init(context);

    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }


    /***
     * 设置字体
     *
     * @return
     */
    public void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fontawesome.ttf");
        setTypeface(tf);
    }



}
