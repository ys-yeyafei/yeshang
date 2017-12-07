package cn.ysgroup.ysdai.Beans.funds;


import cn.ysgroup.ysdai.Beans.BaseBean;

public class UserCashReal extends BaseBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2551673715481931L;
	private String realMoney;//实际到账金额


	public UserCashReal() {
		setRcd("R0001");
	}


	public String getRealMoney() {
		return realMoney;
	}


	public void setRealMoney(String realMoney) {
		this.realMoney = realMoney;
	}
	
	
	
}
