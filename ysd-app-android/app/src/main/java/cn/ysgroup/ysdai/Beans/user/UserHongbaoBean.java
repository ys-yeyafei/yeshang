package cn.ysgroup.ysdai.Beans.user;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.PageBean;

import java.util.List;



public class UserHongbaoBean extends BaseBean
{

	private static final long serialVersionUID = -4685847152264946796L;

	public UserHongbaoBean() {
		setRcd("R0001");
	}


	private String tgNo;// 邀请码
	private Integer friendsCount;// 好友数
	private String hbMoney;// 红包余额
	private String sumGetHbMoney;// 累计获得红包奖励
	private String sumUseHbMoney;// 累计使用红包奖励
	
	private List<UserHongbaoViews> userHongbaoViews;//红包列表
	private PageBean pageBean;
	
	public List<UserHongbaoViews> getUserHongbaoViews() {
		return userHongbaoViews;
	}

	public void setUserHongbaoViews(List<UserHongbaoViews> userHongbaoViews) {
		this.userHongbaoViews = userHongbaoViews;
	}

	public String getHbMoney() {
		return hbMoney;
	}

	public void setHbMoney(String hbMoney) {
		this.hbMoney = hbMoney;
	}

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

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
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
	
}
