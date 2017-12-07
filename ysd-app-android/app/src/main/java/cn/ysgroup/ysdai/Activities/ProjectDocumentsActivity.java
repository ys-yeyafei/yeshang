package cn.ysgroup.ysdai.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.ImageLoader;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowDocuImage;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.BitmapCache;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ProjectDocumentsActivity extends MyBaseActivity
{
//    @Bind(R.id.document_view_pager)
//    ViewPager documentViewPager;
//    @Bind(R.id.project_documents_toolbar_back)
//    IconFontTextView projectDocumentsToolbarBack;
//    @Bind(R.id.project_documents_toolbar_title)
//    TextView projectDocumentsToolbarTitle;
//    @Bind(R.id.project_documents_toolbar)
    Toolbar projectDocumentsToolbar;
    private String TAG = "项目材料公示";
    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_documents);
        ButterKnife.bind(this);
//        initToolBar();


        String borrowImg = getIntent().getStringExtra("borrowImg");
        if (borrowImg != null)
        {
            List<BorrowDocuImage> imglist = JSON.parseArray(borrowImg, BorrowDocuImage.class);

            //填充每一个页面
//            documentViewPager.removeAllViews();
            viewList = new ArrayList<View>();
            BitmapCache myBitmapCache = new BitmapCache();
            ImageLoader imageLoader = new ImageLoader(CustomApplication.newInstance().getRequestQueue(), myBitmapCache);
            for (int i = 0; i < imglist.size(); i++)
            {

                View view = LayoutInflater.from(this).inflate(R.layout.document_view_pager_item_layout, null, false);
                ImageView img = (ImageView) view.findViewById(R.id.docu_item_img);
                TextView name = (TextView) view.findViewById(R.id.docu_item_name);
                name.setText(imglist.get(i).getName());
                String imgUrl = AppConstants.IMG_URL_SUFFIX + imglist.get(i).getUrl();

                ImageLoader.ImageListener listener = ImageLoader.getImageListener(img, R.mipmap.default_image, R.mipmap.default_image);
                imageLoader.get(imgUrl, listener);
                viewList.add(view);
//                documentViewPager.addView(view);

            }
//            documentViewPager.setAdapter(new DocumentViewPagerAdapter(viewList));
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

//    public void initToolBar()
//    {
//        projectDocumentsToolbarBack.setOnClickListener(
//                new View.OnClickListener()
//                {
//                    @Override
//                    public void onClick(View v)
//                    {
//                        finish();
//                    }
//                }
//        );
//        setSupportActionBar(projectDocumentsToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//    }



}
