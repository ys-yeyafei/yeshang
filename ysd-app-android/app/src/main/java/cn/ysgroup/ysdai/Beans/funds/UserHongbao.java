package cn.ysgroup.ysdai.Beans.funds;

import java.io.Serializable;
import java.util.Date;

public class UserHongbao implements Serializable  {

	private static final long serialVersionUID = 8505778562872027375L;

	private String type;//类型
	private String typeShow;//显示文字
	private Date createDate;//时间
	private Integer signFlg;//(1:收入; -1:支出)
	private String money;//操作金额
	private String ableAwardMoney;//可用红包余额
	
	private String hbNo;//红包编号
	private int overDays;//过期时间
	
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
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Integer getSignFlg() {
		return signFlg;
	}
	public void setSignFlg(Integer signFlg) {
		this.signFlg = signFlg;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAbleAwardMoney() {
		return ableAwardMoney;
	}
	public void setAbleAwardMoney(String ableAwardMoney) {
		this.ableAwardMoney = ableAwardMoney;
	}
	public String getHbNo() {
		return hbNo;
	}
	public void setHbNo(String hbNo) {
		this.hbNo = hbNo;
	}


	public int getOverDays()
	{
		return overDays;
	}

	public void setOverDays(int overDays)
	{
		this.overDays = overDays;
	}
}
