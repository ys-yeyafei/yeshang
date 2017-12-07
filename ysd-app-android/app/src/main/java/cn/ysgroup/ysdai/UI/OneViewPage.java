package cn.ysgroup.ysdai.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class OneViewPage extends LazyViewPager {

	public OneViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public OneViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		return false;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
