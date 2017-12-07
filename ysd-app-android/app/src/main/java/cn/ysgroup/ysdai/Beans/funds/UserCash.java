package cn.ysgroup.ysdai.Beans.funds;

import java.io.Serializable;

public class UserCash implements Serializable   {

	private static final long serialVersionUID = -7199442360772895028L;

	private Integer id;//提现记录ID
	private Integer status;//状态【0-审核中；1-审核成功；2-审核失败；3-用户取消；4-处理中】
	private String statusShow;
	
	private Long createDate;//申请时间
	private Long rechargeDate;//处理时间
	private String money;//金额
	
	private String fee;//手续费-金额
	
	private String cardNo;//银行卡号
	private String bankName;//银行卡银行
	private String branch;//开户行
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusShow() {
		String str="";
		if(status.compareTo(2) == 0 ||status.compareTo(3) == 0){
			str = "提现失败";
		}else if(status.compareTo(0) == 0 ||status.compareTo(4) == 0){
			str = "处理中";
		}else if(status.compareTo(1) == 0){
			str = "提现成功";
		}
		
		return str;
	}

	public Long getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Long createDate)
	{
		this.createDate = createDate;
	}

	public Long getRechargeDate()
	{
		return rechargeDate;
	}

	public void setRechargeDate(Long rechargeDate)
	{
		this.rechargeDate = rechargeDate;
	}

	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public void setStatusShow(String statusShow) {
		this.statusShow = statusShow;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getMoney() {
		return money;
	}
	public String getFee() {
		return fee;
	}
	
	
	
	
}
