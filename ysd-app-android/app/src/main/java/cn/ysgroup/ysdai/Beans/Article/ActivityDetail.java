package cn.ysgroup.ysdai.Beans.Article;

/**
 * Created by Administrator on 2016/5/20.
 */
public class ActivityDetail
{

    private Integer id;
    private Long createDate;
    private Long modifyDate;
    private String title;
    private Long startTime;
    private Long endTime;
    private String imgWeb;
    private String imgApp;
    private String content;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Long getCreateDate()
    {
        return createDate;
    }

    public void setCreateDate(Long createDate)
    {
        this.createDate = createDate;
    }

    public Long getModifyDate()
    {
        return modifyDate;
    }

    public void setModifyDate(Long modifyDate)
    {
        this.modifyDate = modifyDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Long getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Long startTime)
    {
        this.startTime = startTime;
    }

    public Long getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Long endTime)
    {
        this.endTime = endTime;
    }

    public String getImgWeb()
    {
        return imgWeb;
    }

    public void setImgWeb(String imgWeb)
    {
        this.imgWeb = imgWeb;
    }

    public String getImgApp()
    {
        return imgApp;
    }

    public void setImgApp(String imgApp)
    {
        this.imgApp = imgApp;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
