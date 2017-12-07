package cn.ysgroup.ysdai.Beans;

import java.io.Serializable;

public class BaseBean implements Serializable {

	private static final long serialVersionUID = -844693643385262727L;

	private String rcd;// 返回编码 return code
	private String rmg;// 返回信息 return message

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

	public boolean isOk(){
		return "R0001".equals(rcd);
	}

	public boolean isLoginExpired(){
        return "E0001".equals(rcd);
    }

}
