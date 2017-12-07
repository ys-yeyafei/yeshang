package cn.ysgroup.ysdai.Beans;

/**
 * Created by Administrator on 2016/5/9.
 */
public class VersionBean
{
    private Integer versionCode;
    private String versionName;
    private String url;
    private String rmg;

    public String getRmg(){
        return rmg;
    }
    public Integer getVersionCode()
    {
        return versionCode;
    }

    public void setVersionCode(Integer versionCode)
    {
        this.versionCode = versionCode;
    }

    public String getVersionName()
    {
        return versionName;
    }

    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
