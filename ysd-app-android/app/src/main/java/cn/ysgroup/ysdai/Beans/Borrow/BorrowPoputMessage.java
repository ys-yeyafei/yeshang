package cn.ysgroup.ysdai.Beans.Borrow;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.user.UserHongbaoViews;

import java.util.List;


public class BorrowPoputMessage extends BaseBean
{

	private static final long serialVersionUID = -360158109477419687L;

	public BorrowPoputMessage() {
		setRcd("R0001");
	}
	
	private Integer id;//项目ID
	private String tenderMoney;//投资金额
	private String tasteMoney;//体验金
	
	private String awardScale;//使用红包比例

	private List<UserHongbaoViews> userHongbaoItem;//红包列表

	private String ableMoney;//账户可用金额
	private String awardMoney;//账户可用红包
	private String isDxb;// 是否是定向标
	
	public String getAwardMoney() {
		return awardMoney;
	}
	public void setAwardMoney(String awardMoney) {
		this.awardMoney = awardMoney;
	}

	public String getTasteMoney()
	{
		return tasteMoney;
	}

	public void setTasteMoney(String tasteMoney)
	{
		this.tasteMoney = tasteMoney;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTenderMoney() {
		return tenderMoney;
	}
	public void setTenderMoney(String tenderMoney) {
		this.tenderMoney = tenderMoney;
	}
	public String getAwardScale() {
		return awardScale;
	}
	public void setAwardScale(String awardScale) {
		this.awardScale = awardScale;
	}
	public List<UserHongbaoViews> getUserHongbaoItem() {
		return userHongbaoItem;
	}
	public void setUserHongbaoItem(List<UserHongbaoViews> userHongbaoItem) {
		this.userHongbaoItem = userHongbaoItem;
	}
	public String getAbleMoney() {
		return ableMoney;
	}
	public void setAbleMoney(String ableMoney) {
		this.ableMoney = ableMoney;
	}
	public String getIsDxb() {
		return isDxb;
	}
	public void setIsDxb(String isDxb) {
		this.isDxb = isDxb;
	}
	
	
	
	
}
