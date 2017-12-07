package cn.ysgroup.ysdai.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.ysgroup.ysdai.R;

import java.util.List;


public class LoadMoreListView extends ListView implements OnScrollListener {
    private View footer;

    private int totalItem;
    private int lastItem;

    private boolean isLoading;

    private OnLoadMore onLoadMore;

    private LayoutInflater inflater;

    public LoadMoreListView(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    @SuppressLint("InflateParams")
    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        footer = inflater.inflate(R.layout.load_more_footer, null, false);
        footer.setVisibility(View.GONE);
        this.addFooterView(footer);
        this.setOnScrollListener(this);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//		System.out.println("firstVisibleItem:"+firstVisibleItem+"visibleItemCount:"+visibleItemCount+
//		"totalItemCount:"+totalItemCount);
        this.lastItem = firstVisibleItem + visibleItemCount;
        this.totalItem = totalItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (this.totalItem == lastItem && scrollState == SCROLL_STATE_IDLE) {
            Log.v("isLoading", "yes");
            //// TODO: 2016/10/11 null异常
            if (!isLoading && footer != null && onLoadMore != null) {
                isLoading = true;
                footer.setVisibility(View.VISIBLE);
                onLoadMore.loadMore();
            }
        }
    }

    public void setLoadMoreListen(OnLoadMore onLoadMore) {
        this.onLoadMore = onLoadMore;
    }

    /**
     * 加载完成调用此方法
     */
    public void onLoadComplete(List list) {
        if (list!=null && list.size() == 0 ) {
            TextView view = (TextView) footer.findViewById(R.id.no_more_textView);
            ProgressBar bar = (ProgressBar) footer.findViewById(R.id.load_more_progressBar);
            bar.setVisibility(GONE);
            view.setText("已经到底啦");

        } else {
            footer.setVisibility(View.GONE);
        }
        isLoading = false;

    }

    public void onLoadComplete() {
        footer.setVisibility(View.GONE);
        isLoading = false;
    }

    public interface OnLoadMore {
        public void loadMore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.onTouchEvent(ev);
    }
}