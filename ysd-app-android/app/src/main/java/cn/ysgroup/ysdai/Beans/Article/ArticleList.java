package cn.ysgroup.ysdai.Beans.Article;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.PageBean;

import java.util.List;

/**
 * Created by Administrator on 2015/11/2.
 */
public class ArticleList extends BaseBean{

    public ArticleList() {
        setRcd("R0001");
    }

    private List<ArticleItem> articleItemList;

    private PageBean pageBean;

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public List<ArticleItem> getArticleItemList() {
        return articleItemList;
    }

    public void setArticleItemList(List<ArticleItem> articleItemList) {
        this.articleItemList = articleItemList;
    }

}
