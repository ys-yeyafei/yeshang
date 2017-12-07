package cn.ysgroup.ysdai.Beans.bank;

import java.io.Serializable;


public class CashBank implements Serializable  {

	private static final long serialVersionUID = -7683603982464447706L;

	private Integer id;
	private String cardNo;
	private String bankId;
	private String bankName;
	private String branch;
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	
}
