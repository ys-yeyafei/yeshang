package cn.ysgroup.ysdai.Activities;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.ysgroup.ysdai.R;
import com.umeng.analytics.MobclickAgent;

public class SplashActivity extends MyBaseActivity {

    private Integer[] imageS;
    private ImageView button;
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        button = (ImageView) findViewById(R.id.splash_rl);
        vp = (ViewPager) findViewById(R.id.splash_vp);
        imageS = new Integer[]{R.mipmap.s1, R.mipmap.s2, R.mipmap.s3, R.mipmap.s4};
        vp.setAdapter(new SplashAdapter());
        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imageS.length - 1) {
                    button.setVisibility(View.VISIBLE);

                } else {
                    button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        });
    }

    class SplashAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageS.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = (ImageView) View.inflate(SplashActivity.this, R.layout.splash_item, null);
            iv.setBackgroundResource(imageS[position]);
            container.addView(iv);
            return iv;
        }
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
