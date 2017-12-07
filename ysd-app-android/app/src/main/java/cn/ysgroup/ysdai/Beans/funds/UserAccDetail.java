package cn.ysgroup.ysdai.Beans.funds;

import java.io.Serializable;

/**
 * 资金记录
 *
 */
public class UserAccDetail implements Serializable  {

	private static final long serialVersionUID = -7188698619968241201L;

	private String type;//明细类型
	private String typeShow;//明细类型-显示
	private Long createDate;//发生时间
	private String sign;//1:收入 -1：支出
	private String tasteMoney;//体验金
	private String money;//操作金额
	private String ableMoney;//可用金额
//	private String total;//账户总额
//	private String unableMoney;//冻结金额
//	private String investorCollectionCapital;//投资者待收本金
//	private String investorCollectionInterest;//投资者待收利息
	private String remark;//备注
	
	
	
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeShow() {
		return typeShow;
	}
	public void setTypeShow(String typeShow) {
		this.typeShow = typeShow;
	}

	public Long getCreateDate()
	{
		return createDate;
	}

	public void setCreateDate(Long createDate)
	{
		this.createDate = createDate;
	}

	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAbleMoney() {
		return ableMoney;
	}
	public void setAbleMoney(String ableMoney) {
		this.ableMoney = ableMoney;
	}
//	public String getTotal() {
//		return total;
//	}
//	public void setTotal(String total) {
//		this.total = total;
//	}
//	public String getUnableMoney() {
//		return unableMoney;
//	}
//	public void setUnableMoney(String unableMoney) {
//		this.unableMoney = unableMoney;
//	}
//	public String getInvestorCollectionCapital() {
//		return investorCollectionCapital;
//	}
//	public void setInvestorCollectionCapital(String investorCollectionCapital) {
//		this.investorCollectionCapital = investorCollectionCapital;
//	}
//	public String getInvestorCollectionInterest() {
//		return investorCollectionInterest;
//	}
//	public void setInvestorCollectionInterest(String investorCollectionInterest) {
//		this.investorCollectionInterest = investorCollectionInterest;
//	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}


	public String getTasteMoney()
	{
		return tasteMoney;
	}

	public void setTasteMoney(String tasteMoney)
	{
		this.tasteMoney = tasteMoney;
	}
}
