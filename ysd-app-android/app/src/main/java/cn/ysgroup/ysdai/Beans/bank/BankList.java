package cn.ysgroup.ysdai.Beans.bank;

import cn.ysgroup.ysdai.Beans.BaseBean;

import java.io.Serializable;
import java.util.List;


public class BankList extends BaseBean implements Serializable
{

	private static final long serialVersionUID = -7683603982464447706L;

	public BankList() {
		setRcd("R0001");
	}

	private String id;
	private String bankId;
	private String realName;
	private String cardNo;
	private String branch;
	private String status;//0:签约中1：签约成功
	private String idNo;//身份证号
	private String registerTime;//注册时间
	private String phone;
	private List<BankCard> bankCardList;
	

	public List<BankCard> getBankCardList() {
		return bankCardList;
	}
	public void setBankCardList(List<BankCard> bankCardList) {
		this.bankCardList = bankCardList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getRegisterTime() {
		return registerTime;
	}
	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}
}
