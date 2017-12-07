package cn.ysgroup.ysdai.Beans.Borrow;

import java.io.Serializable;

public class BorrowRepaymentDetailList implements Serializable {

	private static final long serialVersionUID = -576907487816869960L;

	
	private int orderNum;// 借款标分期顺序
	private String interest;// 本期所还利息
	private String capital;// 本期所还本金
	private String account;
	private Integer repaymentDateInt;// 还款时间
	
	
	public int getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getRepaymentDateInt() {
		return repaymentDateInt;
	}
	public void setRepaymentDateInt(Integer repaymentDateInt) {
		this.repaymentDateInt = repaymentDateInt;
	}
}
