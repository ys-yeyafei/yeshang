package cn.ysgroup.ysdai.Beans.funds;

import java.io.Serializable;
import java.util.Date;

public class UserFriend implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4816438520897954671L;

	private String friendName;//好友用户名
	private Date createDate;//注册时间
	private String awardMoneyTj;//推荐注册红包
	private String awardMoneyTz;//好友投资红包
	
	private int realStatus;//实名状态
	private int emailStatus;//邮箱状态
	private String phoneStatus;//手机状态

	
	
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getEmailStatus()
	{
		return emailStatus;
	}

	public void setEmailStatus(int emailStatus)
	{
		this.emailStatus = emailStatus;
	}

	public int getRealStatus()
	{
		return realStatus;
	}

	public void setRealStatus(int realStatus)
	{
		this.realStatus = realStatus;
	}

	public String getPhoneStatus()
	{
		return phoneStatus;
	}

	public void setPhoneStatus(String phoneStatus) {
		this.phoneStatus = phoneStatus;
	}
	public String getAwardMoneyTj() {
		return awardMoneyTj;
	}
	public void setAwardMoneyTj(String awardMoneyTj) {
		this.awardMoneyTj = awardMoneyTj;
	}
	public String getAwardMoneyTz() {
		return awardMoneyTz;
	}
	public void setAwardMoneyTz(String awardMoneyTz) {
		this.awardMoneyTz = awardMoneyTz;
	}
	
	
	
	
	
	
	
	
	
	
}
