package cn.ysgroup.ysdai.Beans.user;

import java.math.BigDecimal;
import java.util.Date;

public class UserAward {

	private Integer id;
	private String username;
	private Date createDate;
	private Integer realStatus;
	private Integer emailStatus;
	private BigDecimal bdSumMoney;
	private BigDecimal uadSumMoney;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getRealStatus() {
		return realStatus;
	}

	public void setRealStatus(Integer realStatus) {
		this.realStatus = realStatus;
	}

	public Integer getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Integer emailStatus) {
		this.emailStatus = emailStatus;
	}

	public BigDecimal getBdSumMoney() {
		return bdSumMoney;
	}

	public void setBdSumMoney(BigDecimal bdSumMoney) {
		this.bdSumMoney = bdSumMoney;
	}

	public BigDecimal getUadSumMoney() {
		return uadSumMoney;
	}

	public void setUadSumMoney(BigDecimal uadSumMoney) {
		this.uadSumMoney = uadSumMoney;
	}

}
