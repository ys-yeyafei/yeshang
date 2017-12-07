package cn.ysgroup.ysdai.Beans.funds;

import cn.ysgroup.ysdai.Beans.BaseBean;

public class UserHongbaoBean extends BaseBean
{

	private static final long serialVersionUID = 8505778562872027375L;

//	private String awardMoney;// 红包余额
	private Integer friendsCount;// 好友数
	// private String awardMoneyTj;//推荐注册红包
	// private String awardMoneyTz;//好友投资红包

	private String hbMoney;// 红包余额
	private String sumGetHbMoney;// 累计获得红包奖励
	private String sumUseHbMoney;// 累计使用红包奖励

	private String tgNo;// 邀请码
	private String sms;// 分享到短信的内容
	private String weixin;// 分享到微信好友的内容
	private String weixinFriend;// 分享到微信朋友圈的内容
	private String sina;// 分享到新浪微博的内容
	private String tencent;// 分享到腾讯微博的内容
	private String qq;// 分享到QQ的内容

	public UserHongbaoBean() {
		setRcd("R0001");
	}

	public Integer getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(Integer friendsCount) {
		this.friendsCount = friendsCount;
	}

	public String getTgNo() {
		return tgNo;
	}

	public void setTgNo(String tgNo) {
		this.tgNo = tgNo;
	}

	public String getSms() {
		return sms;
	}

	public void setSms(String sms) {
		this.sms = sms;
	}

	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	public String getWeixinFriend() {
		return weixinFriend;
	}

	public void setWeixinFriend(String weixinFriend) {
		this.weixinFriend = weixinFriend;
	}

	public String getSina() {
		return sina;
	}

	public void setSina(String sina) {
		this.sina = sina;
	}

	public String getTencent() {
		return tencent;
	}

	public void setTencent(String tencent) {
		this.tencent = tencent;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}
	/**
	public String getAwardMoney() {
		return awardMoney;
	}

	public void setAwardMoney(String awardMoney) {
		this.awardMoney = awardMoney;
	}

	
	 * public String getAwardMoneyTj() { return awardMoneyTj; }
	 * 
	 * 
	 * 
	 * public void setAwardMoneyTj(String awardMoneyTj) { this.awardMoneyTj =
	 * awardMoneyTj; }
	 * 
	 * 
	 * 
	 * public String getAwardMoneyTz() { return awardMoneyTz; }
	 * 
	 * 
	 * 
	 * public void setAwardMoneyTz(String awardMoneyTz) { this.awardMoneyTz =
	 * awardMoneyTz; }
	 **/

	public String getSumGetHbMoney() {
		return sumGetHbMoney;
	}

	public void setSumGetHbMoney(String sumGetHbMoney) {
		this.sumGetHbMoney = sumGetHbMoney;
	}

	public String getSumUseHbMoney() {
		return sumUseHbMoney;
	}

	public void setSumUseHbMoney(String sumUseHbMoney) {
		this.sumUseHbMoney = sumUseHbMoney;
	}

	public String getHbMoney() {
		return hbMoney;
	}

	public void setHbMoney(String hbMoney) {
		this.hbMoney = hbMoney;
	}

}
