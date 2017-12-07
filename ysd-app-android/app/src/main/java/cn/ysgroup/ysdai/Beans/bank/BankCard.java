package cn.ysgroup.ysdai.Beans.bank;

import java.io.Serializable;


public class BankCard implements Serializable  {

	private static final long serialVersionUID = -7683603982464447706L;
	public boolean isCheck=false;
	private String bankId;
	private String bankName;
	private String bankLimit;

	public String getBankLimit(){
		return bankLimit;
	}

	public void setBankLimit(String bankLimit){
		this.bankLimit=bankLimit;
	}

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

	
	
}
