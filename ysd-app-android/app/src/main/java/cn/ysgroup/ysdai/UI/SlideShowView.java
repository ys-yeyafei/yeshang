package cn.ysgroup.ysdai.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/11/12.
 */
public class SlideShowView extends FrameLayout {

    public boolean isPlay = false;
    //轮播图图片数量
    private final static int IMAGE_COUNT = 5;
    //自动轮播的时间间隔
    private final static int TIME_INTERVAL = 5;
    //自动轮播启用开关
    private final static boolean isAutoPlay = true;

    //自定义轮播图的资源
    private List<String> imageUrls;
    //放轮播图片的ImageView 的list
    private List<ImageView> imageViewsList;
    //放圆点的View的list
    private List<View> dotViewsList;

    private ViewPager viewPager;
    //当前轮播页
    private int currentItem = 0;
    //定时任务
    private ScheduledExecutorService scheduledExecutorService;

    private Context context;
    private ImageLoader mImageLoader;
    private OnImageItemClickListener onImageItemClickListener;

    //Handler
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        }

    };
    private int donwnTime;

    public SlideShowView(Context context) {
        this(context, null);
    }

    public SlideShowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public OnImageItemClickListener getOnImageItemClickListener() {
        return onImageItemClickListener;
    }

    public void setOnImageItemClickListener(OnImageItemClickListener onImageItemClickListener) {
        this.onImageItemClickListener = onImageItemClickListener;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public void START(List<String> imageUrls) {

        setImageUrls(imageUrls);
        mImageLoader = new ImageLoader(CustomApplication.newInstance().getRequestQueue(), new BitmapCache());

        initData();
        if (isAutoPlay) {
            startPlay();
        }

    }

    /**
     * 开始轮播图切换
     */
    public void startPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 2, 5, TimeUnit.SECONDS);
    }

    /**
     * 停止轮播图切换
     */
    public void stopPlay() {
        if(scheduledExecutorService!=null){
            scheduledExecutorService.shutdown();
        }
    }

    /**
     * 初始化相关Data
     */
    private void initData() {
        imageViewsList = new ArrayList<ImageView>();
        dotViewsList = new ArrayList<View>();

        // 一步任务获取图片
        new GetListTask().execute("");
    }

    /**
     * 初始化Views等UI
     */
    private void initUI(Context context) {
        if (imageUrls == null || imageUrls.size() == 0)
            return;

        LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);
        View.inflate(context, R.layout.layout_slideshow, this);

        LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
        dotLayout.removeAllViews();
        int iSize=0;
        if(imageUrls.size()<=3){
            iSize=imageUrls.size()*2;
        }else{
            iSize=imageUrls.size();
        }
        // 热点个数与图片特殊相等
        for (int i = 0; i < iSize; i++) {
            final int position = i;
            ImageView view = (ImageView) View.inflate(context,R.layout.sild_item_view,null);
            if(iSize>3){
                view.setTag(imageUrls.get(i%imageUrls.size()));
            }else{
                view.setTag(imageUrls.get(i));
            }
            if (i == 0)//给一个默认图
                view.setBackgroundResource(R.drawable.u49);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            imageViewsList.add(view);

//            ImageView dotView = new ImageView(context);
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
//            params.leftMargin = 10;
//            params.rightMargin = 10;
//            dotLayout.addView(dotView, params);
//            dotViewsList.add(dotView);
        }
        for(int i = 0; i < imageUrls.size(); i++){
            ImageView dotView = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
            params.leftMargin = 10;
            params.rightMargin = 10;
            dotLayout.addView(dotView, params);
            dotViewsList.add(dotView);
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setFocusable(true);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.setCurrentItem(imageViewsList.size() * 10000);
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
        viewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 移动的时候停止轮播图
                        stopPlay();
                        break;
                    case MotionEvent.ACTION_UP:

                        startPlay();
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 填充ViewPager的页面适配器
     */
    private class MyPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View container, int position, Object object) {
            int a=position % imageViewsList.size();
            System.out.println("回收机制：回收了："+a);
            ((ViewPager) container).removeView(imageViewsList.get(position % imageViewsList.size()));
        }

        @Override
        public Object instantiateItem(View container, final int position) {
            final int count = position % imageViewsList.size();
            System.out.println("回收机制：加载了："+count);
            ImageView imageView = imageViewsList.get(count);
            System.out.println(imageView.getTag().toString() + "........");
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.drawable.u49, R.drawable.u49);
            mImageLoader.get(imageView.getTag() + "", listener);
            imageView.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onImageItemClickListener != null) {
                                onImageItemClickListener.onItemClick(count);
                            }
                        }
                    }

            );
            if(imageViewsList.size()<=3 && imageViewsList.get(count).getParent()!=null){
                destroyItem(container,position,null);
            }
            try {
                ((ViewPager) container).addView(imageViewsList.get(count));
            }catch (Exception e){
                Log.e("轮播图",e.toString());
            }
            return imageViewsList.get(count);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {

        }

        @Override
        public void finishUpdate(View arg0) {

        }

    }

    /**
     * ViewPager的监听器
     * 当ViewPager中页面的状态发生改变时调用
     */
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int pos) {

            currentItem = pos % dotViewsList.size();
            for (int i = 0; i < dotViewsList.size(); i++) {
                if (i == currentItem) {
                    ((View) dotViewsList.get(currentItem)).setBackgroundResource(R.drawable.point_hb);
                } else {
                    ((View) dotViewsList.get(i)).setBackgroundResource(R.drawable.point);
                }
            }
        }

    }

    /**
     * 执行轮播图切换任务
     */
    private class SlideShowTask implements Runnable {

        @Override
        public void run() {
            synchronized (viewPager) {
                handler.obtainMessage().sendToTarget();
            }
        }

    }

    /**
     * 销毁ImageView资源，回收内存
     */
    private void destoryBitmaps() {

        for (int i = 0; i < IMAGE_COUNT; i++) {
            ImageView imageView = imageViewsList.get(i);
            Drawable drawable = imageView.getDrawable();
            if (drawable != null) {
                //解除drawable对view的引用
                drawable.setCallback(null);
            }
        }
    }


    /**
     * 异步任务,获取数据
     */
    class GetListTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                initUI(context);
            }
        }
    }

    public class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> mCache;

        public BitmapCache() {
            int maxSize = 10 * 1024 * 1024;
            mCache = new LruCache<String, Bitmap>(maxSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getRowBytes() * value.getHeight();
                }

            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return mCache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            mCache.put(url, bitmap);
        }

    }

    //图片点击事件
    public static interface OnImageItemClickListener {
        public void onItemClick(int position);
    }
}

