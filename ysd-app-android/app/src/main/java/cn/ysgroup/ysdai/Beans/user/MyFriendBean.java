package cn.ysgroup.ysdai.Beans.user;

import cn.ysgroup.ysdai.Beans.BaseBean;

import java.util.List;



public class MyFriendBean extends BaseBean
{


	/**
	 * awardTotalMoneyTj : 0.00
	 * tgNo : https://www.hzjcb.com/fd/qBKObU
	 * spreadText : 注册即送580元大红包！最高年化收益率14.4%。
	 * spreadTextarea : 自2016年12月21日起，邀请好友，同享收益！
	 1.点击“立即邀请”按钮→分享/发送“邀请链接”至好友。

	 2.好友通过邀请人发送的“邀请链接”成功注册乐商贷，即视为邀请成功。

	 3.好友每次投资成功时，邀请人将获得好友该笔投资利息收益10%的现金奖励（在奖励记录中显示）。

	 4.每个好友都将有机会给您带来累计高达10000元的好友投资奖励。举例：邀请1位好友，最高可获10000元好友投资奖励； 邀请n位好友，最高可获n*10000元好友投资奖励。
	 * friendList : [{"id":94011,"username":"13777595762","createDate":1480388532000,"realStatus":0,"emailStatus":0,"bdSumMoney":null,"uadSumMoney":null},{"id":80679,"username":"18818018912","createDate":1475053618000,"realStatus":0,"emailStatus":0,"bdSumMoney":null,"uadSumMoney":null},{"id":80676,"username":"15907677515","createDate":1475053202000,"realStatus":0,"emailStatus":0,"bdSumMoney":null,"uadSumMoney":null},{"id":80621,"username":"188180189121","createDate":1475040657000,"realStatus":0,"emailStatus":0,"bdSumMoney":null,"uadSumMoney":null}]
	 * pageBean : {"pageNumber":1,"totalCount":4,"pageCount":1,"pageSize":10}
	 */

	private String awardTotalMoneyTj;
	private String tgNo;
	private String spreadText;
	private String spreadTextarea;
	/**
	 * pageNumber : 1
	 * totalCount : 4
	 * pageCount : 1
	 * pageSize : 10
	 */

	private PageBeanBean pageBean;
	/**
	 * id : 94011
	 * username : 13777595762
	 * createDate : 1480388532000
	 * realStatus : 0
	 * emailStatus : 0
	 * bdSumMoney : null
	 * uadSumMoney : null
	 */

	private List<FriendListBean> friendList;

	public String getAwardTotalMoneyTj() {
		return awardTotalMoneyTj;
	}

	public void setAwardTotalMoneyTj(String awardTotalMoneyTj) {
		this.awardTotalMoneyTj = awardTotalMoneyTj;
	}

	public String getTgNo() {
		return tgNo;
	}

	public void setTgNo(String tgNo) {
		this.tgNo = tgNo;
	}

	public String getSpreadText() {
		return spreadText;
	}

	public void setSpreadText(String spreadText) {
		this.spreadText = spreadText;
	}

	public String getSpreadTextarea() {
		return spreadTextarea;
	}

	public void setSpreadTextarea(String spreadTextarea) {
		this.spreadTextarea = spreadTextarea;
	}

	public PageBeanBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBeanBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<FriendListBean> getFriendList() {
		return friendList;
	}

	public void setFriendList(List<FriendListBean> friendList) {
		this.friendList = friendList;
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

	public static class FriendListBean {
		private int id;
		private String username;
		private long createDate;
		private int realStatus;
		private int emailStatus;
		private Object bdSumMoney;
		private Object uadSumMoney;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public long getCreateDate() {
			return createDate;
		}

		public void setCreateDate(long createDate) {
			this.createDate = createDate;
		}

		public int getRealStatus() {
			return realStatus;
		}

		public void setRealStatus(int realStatus) {
			this.realStatus = realStatus;
		}

		public int getEmailStatus() {
			return emailStatus;
		}

		public void setEmailStatus(int emailStatus) {
			this.emailStatus = emailStatus;
		}

		public Object getBdSumMoney() {
			return bdSumMoney;
		}

		public void setBdSumMoney(Object bdSumMoney) {
			this.bdSumMoney = bdSumMoney;
		}

		public Object getUadSumMoney() {
			return uadSumMoney;
		}

		public void setUadSumMoney(Object uadSumMoney) {
			this.uadSumMoney = uadSumMoney;
		}
	}
}
