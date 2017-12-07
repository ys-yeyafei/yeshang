package cn.ysgroup.ysdai.Beans.Article;

import cn.ysgroup.ysdai.Beans.BaseBean;

/**
 * Created by Administrator on 2015/11/3.
 */
public class ArticleBean  extends BaseBean {

    public ArticleBean() {
        setRcd("R0001");
    }

    private Long createDate;// 创建日期
    private String title;// 标题
    private String content;// 内容

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
