package cn.ysgroup.ysdai.Beans.payment;

import cn.ysgroup.ysdai.Beans.BaseBean;

/**
 * Created by Administrator on 2015/11/9.
 */
public class BaoFu  extends BaseBean
{
    public BaoFu() {
        setRcd("R0001");
    }

    private String postUrl;//提交地址
    private String reqData;//组合参数

    public String getPostUrl() {
        return postUrl;
    }
    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }
    public String getReqData() {
        return reqData;
    }
    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

}
