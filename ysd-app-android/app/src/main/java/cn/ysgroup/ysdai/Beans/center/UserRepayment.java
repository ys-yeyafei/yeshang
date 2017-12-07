package cn.ysgroup.ysdai.Beans.center;

import java.io.Serializable;

/**
 * 回款明细
 *
 */
public class UserRepayment implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 355482720231647119L;
	private Integer repaymentStatus;//待收状态
	private Integer repaymentStatusShow;//显示待收状态
	private Long repaymentDate;//待收时间
	
	private String waitTotal;//待收总额
	private String waitAccount;//待收本金
	private String waitInterest; // 待收利息
	private String serviceCharge;//利息管理费
	
	private String borrowName;//投资项目名称
	
	private Integer periods;//总期数
	private Integer repaymentPeriods;//当前期数
	
	 
	public String getRepaymentStatusShow() {
		String str="";
		if(repaymentStatus.compareTo(0)==0){
			str="待收";
		}else if(repaymentStatus.compareTo(1)==0){
			str="已收";
		}
		return str;
	}


	public Integer getRepaymentStatus() {
		return repaymentStatus;
	}


	public void setRepaymentStatus(Integer repaymentStatus) {
		this.repaymentStatus = repaymentStatus;
	}


	public Long getRepaymentDate() {
		return repaymentDate;
	}


	public void setRepaymentDate(Long repaymentDate) {
		this.repaymentDate = repaymentDate;
	}


	public String getWaitAccount() {
		return waitAccount;
	}


	public void setWaitAccount(String waitAccount) {
		this.waitAccount = waitAccount;
	}


	public String getWaitInterest() {
		return waitInterest;
	}


	public void setWaitInterest(String waitInterest) {
		this.waitInterest = waitInterest;
	}


	public String getServiceCharge() {
		return serviceCharge;
	}


	public void setServiceCharge(String serviceCharge) {
		this.serviceCharge = serviceCharge;
	}


	public String getBorrowName() {
		return borrowName;
	}


	public void setBorrowName(String borrowName) {
		this.borrowName = borrowName;
	}


	public Integer getPeriods() {
		return periods;
	}


	public void setPeriods(Integer periods) {
		this.periods = periods;
	}


	public Integer getRepaymentPeriods() {
		return repaymentPeriods+1;
	}


	public void setRepaymentPeriods(Integer repaymentPeriods) {
		this.repaymentPeriods = repaymentPeriods;
	}


	public String getWaitTotal() {
		return waitTotal;
	}


	public void setWaitTotal(String waitTotal) {
		this.waitTotal = waitTotal;
	}
	
	 
}
