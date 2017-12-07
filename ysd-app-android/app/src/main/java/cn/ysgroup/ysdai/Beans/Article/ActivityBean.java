package cn.ysgroup.ysdai.Beans.Article;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ActivityBean
{
    private int id;
    private Long createDate;// 创建日期
    private Long modifyDate;// 创建日期
    private Long startTime;// 创建日期
    private Long endTime;// 创建日期
    private String title;// 标题
    private String content;// 内容
    private String imgWeb;
    private String imgApp;
    private String status;

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }
    public int getId()
    {
        return id;
    }

    public void setId(int id)
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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
}
