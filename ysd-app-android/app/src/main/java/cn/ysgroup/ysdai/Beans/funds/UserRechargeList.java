package cn.ysgroup.ysdai.Beans.funds;

import cn.ysgroup.ysdai.Beans.PageBean;
import cn.ysgroup.ysdai.Beans.BaseBean;

import java.util.List;



public class UserRechargeList extends BaseBean
{

	private static final long serialVersionUID = -217229670170300033L;


	public UserRechargeList() {
		setRcd("R0001");
	}
	
	private PageBean pageBean;
	private List<UserRecharge> userRechargesList;
	
	private String rechargeMoneySum;//累计充值金额


	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}


	public List<UserRecharge> getUserRechargesList() {
		return userRechargesList;
	}

	public void setUserRechargesList(List<UserRecharge> userRechargesList) {
		this.userRechargesList = userRechargesList;
	}

	public String getRechargeMoneySum() {
		return rechargeMoneySum;
	}

	public void setRechargeMoneySum(String rechargeMoneySum) {
		this.rechargeMoneySum = rechargeMoneySum;
	}
	
	
	
	
}
