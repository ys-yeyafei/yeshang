package cn.ysgroup.ysdai.Beans.payment;

/**
 * Created by Administrator on 2016/4/19.
 */
public class BaoFuRequestBean
{
    private String back_url;
    private String data_content;
    private String data_type;
    private String input_charset;
    private String language;
    private String member_id;
    private String terminal_id;
    private String txn_sub_type;
    private String txn_type;
    private String version;

    public String getBack_url()
    {
        return back_url;
    }

    public void setBack_url(String back_url)
    {
        this.back_url = back_url;
    }

    public String getData_content()
    {
        return data_content;
    }

    public void setData_content(String data_content)
    {
        this.data_content = data_content;
    }

    public String getData_type()
    {
        return data_type;
    }

    public void setData_type(String data_type)
    {
        this.data_type = data_type;
    }

    public String getInput_charset()
    {
        return input_charset;
    }

    public void setInput_charset(String input_charset)
    {
        this.input_charset = input_charset;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public String getMember_id()
    {
        return member_id;
    }

    public void setMember_id(String member_id)
    {
        this.member_id = member_id;
    }

    public String getTerminal_id()
    {
        return terminal_id;
    }

    public void setTerminal_id(String terminal_id)
    {
        this.terminal_id = terminal_id;
    }

    public String getTxn_sub_type()
    {
        return txn_sub_type;
    }

    public void setTxn_sub_type(String txn_sub_type)
    {
        this.txn_sub_type = txn_sub_type;
    }

    public String getTxn_type()
    {
        return txn_type;
    }

    public void setTxn_type(String txn_type)
    {
        this.txn_type = txn_type;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }
}
