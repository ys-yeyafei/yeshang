package cn.ysgroup.ysdai.Beans.Article;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/2.
 */
public class ArticleItem implements Serializable {

    private Integer id;// ID
    private String title;// 标题
    private String content;
    //是否是点击展开
    public boolean isOpen=false;

    public String getContent() {
        return content;
    }

    public void setContent(String content){
        this.content=content;
    }
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    private String createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
