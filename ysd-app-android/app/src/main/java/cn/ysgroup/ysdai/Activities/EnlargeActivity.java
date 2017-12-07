package cn.ysgroup.ysdai.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

import com.android.volley.toolbox.ImageLoader;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.SmoothImageView;
import cn.ysgroup.ysdai.Util.BitmapCache;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

public class EnlargeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
        int mPosition = getIntent().getIntExtra("position", 0);
        int mLocationX = getIntent().getIntExtra("locationX", 0);
        int mLocationY = getIntent().getIntExtra("locationY", 0);
        int mWidth = getIntent().getIntExtra("width", 0);
        int mHeight = getIntent().getIntExtra("height", 0);

        SmoothImageView imageView = new SmoothImageView(this);
//        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
//        imageView.transformIn();
//        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
//        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        BitmapCache myBitmapCache = new BitmapCache();
        ImageLoader imageLoader = new ImageLoader(CustomApplication.newInstance().getRequestQueue(), myBitmapCache);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.mipmap.default_image, R.mipmap.default_image);
        imageLoader.get(mDatas.get(mPosition), listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                finish();
                break;
        }
        return super.onTouchEvent(event);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
