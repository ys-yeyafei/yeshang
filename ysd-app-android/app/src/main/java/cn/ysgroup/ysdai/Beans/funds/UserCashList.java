package cn.ysgroup.ysdai.Beans.funds;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.PageBean;

import java.util.List;



public class UserCashList extends BaseBean
{

	private static final long serialVersionUID = 4246068695231595655L;

	public UserCashList() {
		setRcd("R0001");
	}
	
	private PageBean pageBean;
	private List<UserCash> userCashList;
	
	private String cashMoneySum;//累计提现金额

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public String getCashMoneySum() {
		return cashMoneySum;
	}

	public void setCashMoneySum(String cashMoneySum) {
		this.cashMoneySum = cashMoneySum;
	}

	public List<UserCash> getUserCashList() {
		return userCashList;
	}

	public void setUserCashList(List<UserCash> userCashList) {
		this.userCashList = userCashList;
	}
}
