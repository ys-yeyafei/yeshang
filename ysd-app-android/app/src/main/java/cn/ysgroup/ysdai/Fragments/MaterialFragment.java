package cn.ysgroup.ysdai.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.toolbox.ImageLoader;
import cn.ysgroup.ysdai.Activities.ImagePagerActivity;
import cn.ysgroup.ysdai.Adapters.DocumentAdapter;
import cn.ysgroup.ysdai.Application.CustomApplication;
import cn.ysgroup.ysdai.Beans.Borrow.BorrowDocuImage;
import cn.ysgroup.ysdai.R;
import cn.ysgroup.ysdai.UI.SmoothImageView;
import cn.ysgroup.ysdai.Util.AppConstants;
import cn.ysgroup.ysdai.Util.BitmapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linyujie on 16/10/14.
 */

public class MaterialFragment extends Fragment {

    private View view;
    private GridView gridView;
    private String borrowImg;

    public MaterialFragment() {

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public MaterialFragment(String borrowImg) {
        this.borrowImg = borrowImg;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.activity_project_documents, null);
        initView();
        initDate();
        return view;
    }

    private void initView() {
        gridView = (GridView) view.findViewById(R.id.doucument_girdview);
    }

    private void initDate() {
        if (borrowImg != null) {
            List<BorrowDocuImage> imglist = JSON.parseArray(borrowImg, BorrowDocuImage.class);

            ArrayList<View> viewList = new ArrayList<View>();
            final ArrayList<String> imageUrlList = new ArrayList<String>();
            final ArrayList<String> nameList = new ArrayList<String>();
            BitmapCache myBitmapCache = new BitmapCache();
            ImageLoader imageLoader = new ImageLoader(CustomApplication.newInstance().getRequestQueue(), myBitmapCache);
            for (int i = 0; i < imglist.size(); i++) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.document_view_pager_item_layout, null, false);
                SmoothImageView img = (SmoothImageView) view.findViewById(R.id.docu_item_img);
                TextView name = (TextView) view.findViewById(R.id.docu_item_name);
                name.setText(imglist.get(i).getName());
                String imgUrl = AppConstants.IMG_URL_SUFFIX + imglist.get(i).getUrl();
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(img, R.mipmap.default_image, R.mipmap.default_image);
                imageLoader.get(imgUrl, listener);
                viewList.add(view);
                imageUrlList.add(imgUrl);
                nameList.add(imglist.get(i).getName());
            }
            DocumentAdapter documentAdapter = new DocumentAdapter(viewList, getActivity(), imageUrlList);
            gridView.setAdapter(documentAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    imageBrower(position, imageUrlList,nameList);
                }
            });
        }
    }


    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2,ArrayList<String> urls3) {
        Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        intent.putExtra(ImagePagerActivity.EXTRA_NAME_URLS, urls3);
        getActivity().startActivity(intent);
    }
}

