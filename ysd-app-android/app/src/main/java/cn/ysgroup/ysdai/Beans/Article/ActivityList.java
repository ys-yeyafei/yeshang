package cn.ysgroup.ysdai.Beans.Article;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.PageBean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class ActivityList extends BaseBean{

    public ActivityList() {
        setRcd("R0001");
    }

    private List<ActivityBean> activityList;

    private PageBean pageBean;

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<ActivityBean> getActivityList()
    {
        return activityList;
    }

    public void setActivityList(List<ActivityBean> activityList)
    {
        this.activityList = activityList;
    }
}
