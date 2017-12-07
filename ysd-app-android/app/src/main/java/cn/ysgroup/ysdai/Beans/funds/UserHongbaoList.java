package cn.ysgroup.ysdai.Beans.funds;

import cn.ysgroup.ysdai.Beans.PageBean;
import cn.ysgroup.ysdai.Beans.BaseBean;

import java.util.List;



public class UserHongbaoList extends BaseBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1455797226513050955L;
	
	
	private String awardMoney;//红包余额
	private String awardMoneyTj;//推荐注册红包
	private String awardMoneyTz;//好友投资红包

	private PageBean pageBean;
	private List<UserHongbao> userHongbaoList;

	public UserHongbaoList() {
		setRcd("R0001");
	}
	
	

	public PageBean getPageBean() {
		return pageBean;
	}


	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}


	public List<UserHongbao> getUserHongbaoList() {
		return userHongbaoList;
	}


	public void setUserHongbaoList(List<UserHongbao> userHongbaoList) {
		this.userHongbaoList = userHongbaoList;
	}



	public String getAwardMoney() {
		return awardMoney;
	}



	public void setAwardMoney(String awardMoney) {
		this.awardMoney = awardMoney;
	}



	public String getAwardMoneyTj() {
		return awardMoneyTj;
	}



	public void setAwardMoneyTj(String awardMoneyTj) {
		this.awardMoneyTj = awardMoneyTj;
	}



	public String getAwardMoneyTz() {
		return awardMoneyTz;
	}



	public void setAwardMoneyTz(String awardMoneyTz) {
		this.awardMoneyTz = awardMoneyTz;
	}
}
