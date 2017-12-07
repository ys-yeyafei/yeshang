package cn.ysgroup.ysdai.Beans.funds;


import cn.ysgroup.ysdai.Beans.BaseBean;

public class UserCashBean extends BaseBean
{


	private static final long serialVersionUID = 2063096271291788714L;
	private String realname;//会员姓名
	private String bankId;//银行ID
	private String cardNo;//银行卡号
	private String bankName;//银行卡银行
	private String branch;//开户行
	private String cashMoney;//可提现额度
	private String cashFeeMoney;//可免费提现额度
	private String ableMoney;//可用额度
	
	private String feeScale;//提现手续费比例- 0.003
	private String feeFixed;//提现手续费固定 -5
	private String userCashChargeTimes;//当月提现次数
	private String cashChargeTimes;//总免费提现次数



	public UserCashBean() {
		setRcd("R0001");
	}
	
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
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

	public String getCashMoney() {
		return cashMoney;
	}

	public void setCashMoney(String cashMoney) {
		this.cashMoney = cashMoney;
	}

	public String getCashFeeMoney() {
		return cashFeeMoney;
	}

	public void setCashFeeMoney(String cashFeeMoney) {
		this.cashFeeMoney = cashFeeMoney;
	}

	public String getAbleMoney() {
		return ableMoney;
	}

	public void setAbleMoney(String ableMoney) {
		this.ableMoney = ableMoney;
	}


	public String getUserCashChargeTimes() {
		return userCashChargeTimes;
	}

	public void setUserCashChargeTimes(String userCashChargeTimes) {
		this.userCashChargeTimes = userCashChargeTimes;
	}

	public String getCashChargeTimes() {
		return cashChargeTimes;
	}

	public void setCashChargeTimes(String cashChargeTimes) {
		this.cashChargeTimes = cashChargeTimes;
	}

	public String getFeeScale() {
		return feeScale;
	}

	public void setFeeScale(String feeScale) {
		this.feeScale = feeScale;
	}

	public String getFeeFixed() {
		return feeFixed;
	}

	public void setFeeFixed(String feeFixed) {
		this.feeFixed = feeFixed;
	}

	
	
	
}
