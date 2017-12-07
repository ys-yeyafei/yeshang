package cn.ysgroup.ysdai.Beans.bank;

import java.io.Serializable;

/**
 * Created by linyujie on 16/12/27.
 */

public class ZSBeen implements Serializable{


    /**
     * rcd : R0002
     * rmg : 12560035
     * order_no : 2016122909430079
     */

    private String rcd;
    private String rmg;
    private String order_no;

    public String getRcd() {
        return rcd;
    }

    public void setRcd(String rcd) {
        this.rcd = rcd;
    }

    public String getRmg() {
        return rmg;
    }

    public void setRmg(String rmg) {
        this.rmg = rmg;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }
}
