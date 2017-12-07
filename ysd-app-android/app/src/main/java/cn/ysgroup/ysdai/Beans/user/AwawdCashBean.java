package cn.ysgroup.ysdai.Beans.user;

import cn.ysgroup.ysdai.Beans.BaseBean;

import java.util.List;



public class AwawdCashBean extends BaseBean
{


	/**
	 * sumCashMoney : null
	 * sumFriendsTzMoney : null
	 * sumTzMoney : null
	 * cashList : [{"id":669393,"userId":75905,"type":"tui_detail_award_ht","money":0,"signFlg":1,"awardMoney":680,"remark":"测试","userAccountDetailId":null,"relateTo":null,"relateKey":null,"createDate":1483931138000,"modifyDate":1483931138000,"reserve1":null,"typeName":null,"username":null,"isLook":1},{"id":665050,"userId":75905,"type":"tui_detail_award_ht","money":0,"signFlg":1,"awardMoney":580,"remark":"测试","userAccountDetailId":null,"relateTo":null,"relateKey":null,"createDate":1479979133000,"modifyDate":1479979133000,"reserve1":null,"typeName":null,"username":null,"isLook":1}]
	 * pageBean : {"pageNumber":1,"totalCount":2,"pageCount":1,"pageSize":10}
	 */

	private Object sumCashMoney;
	private Object sumFriendsTzMoney;
	private Object sumTzMoney;
	/**
	 * pageNumber : 1
	 * totalCount : 2
	 * pageCount : 1
	 * pageSize : 10
	 */

	private PageBeanBean pageBean;
	/**
	 * id : 669393
	 * userId : 75905
	 * type : tui_detail_award_ht
	 * money : 0
	 * signFlg : 1
	 * awardMoney : 680
	 * remark : 测试
	 * userAccountDetailId : null
	 * relateTo : null
	 * relateKey : null
	 * createDate : 1483931138000
	 * modifyDate : 1483931138000
	 * reserve1 : null
	 * typeName : null
	 * username : null
	 * isLook : 1
	 */

	private List<CashListBean> cashList;

	public Object getSumCashMoney() {
		return sumCashMoney;
	}

	public void setSumCashMoney(Object sumCashMoney) {
		this.sumCashMoney = sumCashMoney;
	}

	public Object getSumFriendsTzMoney() {
		return sumFriendsTzMoney;
	}

	public void setSumFriendsTzMoney(Object sumFriendsTzMoney) {
		this.sumFriendsTzMoney = sumFriendsTzMoney;
	}

	public Object getSumTzMoney() {
		return sumTzMoney;
	}

	public void setSumTzMoney(Object sumTzMoney) {
		this.sumTzMoney = sumTzMoney;
	}

	public PageBeanBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBeanBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<CashListBean> getCashList() {
		return cashList;
	}

	public void setCashList(List<CashListBean> cashList) {
		this.cashList = cashList;
	}

	public static class PageBeanBean {
		private int pageNumber;
		private int totalCount;
		private int pageCount;
		private int pageSize;

		public int getPageNumber() {
			return pageNumber;
		}

		public void setPageNumber(int pageNumber) {
			this.pageNumber = pageNumber;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}

		public int getPageCount() {
			return pageCount;
		}

		public void setPageCount(int pageCount) {
			this.pageCount = pageCount;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}
	}

	public static class CashListBean {
		private int id;
		private int userId;
		private String type;
		private int money;
		private int signFlg;
		private int awardMoney;
		private String remark;
		private Object userAccountDetailId;
		private Object relateTo;
		private Object relateKey;
		private long createDate;
		private long modifyDate;
		private Object reserve1;
		private Object typeName;
		private Object username;
		private int isLook;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getMoney() {
			return money;
		}

		public void setMoney(int money) {
			this.money = money;
		}

		public int getSignFlg() {
			return signFlg;
		}

		public void setSignFlg(int signFlg) {
			this.signFlg = signFlg;
		}

		public int getAwardMoney() {
			return awardMoney;
		}

		public void setAwardMoney(int awardMoney) {
			this.awardMoney = awardMoney;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public Object getUserAccountDetailId() {
			return userAccountDetailId;
		}

		public void setUserAccountDetailId(Object userAccountDetailId) {
			this.userAccountDetailId = userAccountDetailId;
		}

		public Object getRelateTo() {
			return relateTo;
		}

		public void setRelateTo(Object relateTo) {
			this.relateTo = relateTo;
		}

		public Object getRelateKey() {
			return relateKey;
		}

		public void setRelateKey(Object relateKey) {
			this.relateKey = relateKey;
		}

		public long getCreateDate() {
			return createDate;
		}

		public void setCreateDate(long createDate) {
			this.createDate = createDate;
		}

		public long getModifyDate() {
			return modifyDate;
		}

		public void setModifyDate(long modifyDate) {
			this.modifyDate = modifyDate;
		}

		public Object getReserve1() {
			return reserve1;
		}

		public void setReserve1(Object reserve1) {
			this.reserve1 = reserve1;
		}

		public Object getTypeName() {
			return typeName;
		}

		public void setTypeName(Object typeName) {
			this.typeName = typeName;
		}

		public Object getUsername() {
			return username;
		}

		public void setUsername(Object username) {
			this.username = username;
		}

		public int getIsLook() {
			return isLook;
		}

		public void setIsLook(int isLook) {
			this.isLook = isLook;
		}
	}
}
