package cn.ysgroup.ysdai.Beans.Borrow;

import java.io.Serializable;
import java.util.Date;

public class BorrowTenderItem implements Serializable {


	 /**
	 * 
	 */
	private static final long serialVersionUID = -2719301430404446021L;
	private Integer id;// 标ID
	 private Integer tenderId;//投资记录ID
	 private String username;//投资人用户名
	 private Date createDate;//投资时间
	 private String money;//预计投标金额
	 private String account;//实际成功金额
	 private String autoTenderStatus; // 1自动，2手动
	 private String status;//投标状态【1：全部通过;2：部分通过】
	 
	 private String arp;//年利率
	 private String loginStatus;//登录状态
	 private String ableMoney;//可用余额
	 private Integer clientType;//客户端类型
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTenderId() {
		return tenderId;
	}
	public void setTenderId(Integer tenderId) {
		this.tenderId = tenderId;
	}
	public String getUsername() {
		
		if(username !=null && username.length()==11) {
			return username.substring(0, 3)+"****"+username.substring(7);
		}
		
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
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAutoTenderStatus() {
		return autoTenderStatus;
	}
	public void setAutoTenderStatus(String autoTenderStatus) {
		this.autoTenderStatus = autoTenderStatus;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getAbleMoney() {
		return ableMoney;
	}
	public void setAbleMoney(String ableMoney) {
		this.ableMoney = ableMoney;
	}
	public String getArp() {
		return arp;
	}
	public void setArp(String arp) {
		this.arp = arp;
	}
	public Integer getClientType() {
		return clientType;
	}
	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}
	 
	 
}
