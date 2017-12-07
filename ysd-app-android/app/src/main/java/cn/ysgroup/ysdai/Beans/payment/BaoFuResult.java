package cn.ysgroup.ysdai.Beans.payment;

/**
 * Created by Administrator on 2016/4/20.
 */
public class BaoFuResult
{
    private String  retCode;
    private String  retMsg;
    private String  tradeNo;


    public String getRetCode()
    {
        return retCode;
    }

    public void setRetCode(String retCode)
    {
        this.retCode = retCode;
    }

    public String getRetMsg()
    {
        return retMsg;
    }

    public void setRetMsg(String retMsg)
    {
        this.retMsg = retMsg;
    }


    public String getTradeNo()
    {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo)
    {
        this.tradeNo = tradeNo;
    }
}
