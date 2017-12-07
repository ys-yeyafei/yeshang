package cn.ysgroup.ysdai.Beans.index;

import cn.ysgroup.ysdai.Beans.BaseBean;

import java.util.List;


public class IndexBean extends BaseBean {

	private static final long serialVersionUID = -7683603982464447706L;


	/**
	 * id : 1379
	 * name : 新手标第103期
	 * type : 16
	 * businessType : 1
	 * businessCode : xs00103
	 * timeLimit : 7
	 * account : 100000
	 * apr : 14.4
	 * baseApr : 5.4
	 * awardApr : 9.0
	 * award : 0
	 * funds : 0
	 * status : 3
	 * showBorrowStatus : 收益中
	 * schedule : 100
	 * tenderSize : 0
	 * tenderSubject : 0
	 * verifyTime : 1482319500000
	 * showBorrowType : 16
	 * lowestAccount : 100
	 * balance : 0
	 */

	private IndexBorrowBean indexBorrow;
	/**
	 * indexBorrow : {"id":1379,"name":"新手标第103期","type":"16","businessType":"1","businessCode":"xs00103","timeLimit":"7","account":"100000","apr":14.4,"baseApr":5.4,"awardApr":9,"award":"0","funds":"0","status":3,"showBorrowStatus":"收益中","schedule":"100","tenderSize":0,"tenderSubject":0,"verifyTime":1482319500000,"showBorrowType":"16","lowestAccount":"100","balance":"0"}
	 * indexTypeItemList : [{"type":"14","typeImage":"","showBorrowType":"天标","typeDescribe":"描述"},{"type":"15","typeImage":"","showBorrowType":"月标","typeDescribe":"描述"},{"type":"16","typeImage":"","showBorrowType":"新手标","typeDescribe":"描述"}]
	 * indexImageItemList : [{"rcd":"R0001","rmg":null,"imageUrl":"/data/upfiles/images/201612/bcd466f313a04dd6ac1ae087b9de2dc2.jpg","type":1,"typeTarget":"https://www.hzjcb.com/article/content/677.htm"},{"rcd":"R0001","rmg":null,"imageUrl":"/data/upfiles/images/201612/fc7f64f48f804f1a948f8a3b55573414.jpg","type":1,"typeTarget":"https://www.hzjcb.com/activity/detail.do?id=26"},{"rcd":"R0001","rmg":null,"imageUrl":"/data/upfiles/images/201612/27ed82199ff7449f8f09c9ad22b245ce.jpg","type":1,"typeTarget":"https://www.hzjcb.com/article/content/682.htm"},{"rcd":"R0001","rmg":null,"imageUrl":"/data/upfiles/images/201612/6e25daddae524f2287aea0eb3bc598a2.jpg","type":1,"typeTarget":"https://www.hzjcb.com/activity/detail.do?id=27"},{"rcd":"R0001","rmg":null,"imageUrl":"/data/upfiles/images/201612/a457cec4e0154a5c94836fd0b9f44d88.jpg","type":1,"typeTarget":"https://www.hzjcb.com/activity/detail.do?id=25"},{"rcd":"R0001","rmg":null,"imageUrl":"/data/upfiles/images/201612/f6bb83a196b6408bbe2df338b58fd7ed.jpg","type":1,"typeTarget":"https://www.hzjcb.com/activity/detail.do?id=20"},{"rcd":"R0001","rmg":null,"imageUrl":"/data/upfiles/images/201611/9d75b89d95fb4fd9bcb39879a0dcea35.png","type":1,"typeTarget":"https://www.hzjcb.com/activity/detail.do?id=5"}]
	 * totalUserNum : 96794
	 * totalTenderMoney : 381109210
	 * notReadNum : 26
	 * userFlg : 0
	 * activity : {"id":27,"createDate":1482478008000,"modifyDate":1482718643000,"title":"让我们荡起\u201c双奖\u201d","startTime":1482422400000,"endTime":1483545600000,"imgWeb":"/data/upfiles/images/201612/d7c880d4c9e24657a4b10c7c3529286d.jpg","imgApp":"/data/upfiles/images/201612/151a9bae0ba14cdc82b524de2a6a33fc.jpg","content":"https://www.hzjcb.com/web/data/upfiles/images/201612/ef9dd6e038404413863b4caaec211b32.jpg","status":1,"orderList":1,"array":null}
	 */

	private int totalUserNum;
	private int totalTenderMoney;
	private int notReadNum;
	private int userFlg;
	/**
	 * id : 27
	 * createDate : 1482478008000
	 * modifyDate : 1482718643000
	 * title : 让我们荡起“双奖”
	 * startTime : 1482422400000
	 * endTime : 1483545600000
	 * imgWeb : /data/upfiles/images/201612/d7c880d4c9e24657a4b10c7c3529286d.jpg
	 * imgApp : /data/upfiles/images/201612/151a9bae0ba14cdc82b524de2a6a33fc.jpg
	 * content : https://www.hzjcb.com/web/data/upfiles/images/201612/ef9dd6e038404413863b4caaec211b32.jpg
	 * status : 1
	 * orderList : 1
	 * array : null
	 */

	private ActivityBean activity;
	/**
	 * type : 14
	 * typeImage :
	 * showBorrowType : 天标
	 * typeDescribe : 描述
	 */

	private List<IndexTypeItemListBean> indexTypeItemList;
	/**
	 * rcd : R0001
	 * rmg : null
	 * imageUrl : /data/upfiles/images/201612/bcd466f313a04dd6ac1ae087b9de2dc2.jpg
	 * type : 1
	 * typeTarget : https://www.hzjcb.com/article/content/677.htm
	 */

	private List<IndexImageItemListBean> indexImageItemList;

	public IndexBorrowBean getIndexBorrow() {
		return indexBorrow;
	}

	public void setIndexBorrow(IndexBorrowBean indexBorrow) {
		this.indexBorrow = indexBorrow;
	}

	public int getTotalUserNum() {
		return totalUserNum;
	}

	public void setTotalUserNum(int totalUserNum) {
		this.totalUserNum = totalUserNum;
	}

	public int getTotalTenderMoney() {
		return totalTenderMoney;
	}

	public void setTotalTenderMoney(int totalTenderMoney) {
		this.totalTenderMoney = totalTenderMoney;
	}

	public int getNotReadNum() {
		return notReadNum;
	}

	public void setNotReadNum(int notReadNum) {
		this.notReadNum = notReadNum;
	}

	public int getUserFlg() {
		return userFlg;
	}

	public void setUserFlg(int userFlg) {
		this.userFlg = userFlg;
	}

	public ActivityBean getActivity() {
		return activity;
	}

	public void setActivity(ActivityBean activity) {
		this.activity = activity;
	}

	public List<IndexTypeItemListBean> getIndexTypeItemList() {
		return indexTypeItemList;
	}

	public void setIndexTypeItemList(List<IndexTypeItemListBean> indexTypeItemList) {
		this.indexTypeItemList = indexTypeItemList;
	}

	public List<IndexImageItemListBean> getIndexImageItemList() {
		return indexImageItemList;
	}

	public void setIndexImageItemList(List<IndexImageItemListBean> indexImageItemList) {
		this.indexImageItemList = indexImageItemList;
	}

	public static class IndexBorrowBean {
		private int id;
		private String name;
		private String type;
		private String businessType;
		private String businessCode;
		private String timeLimit;
		private String account;
		private double apr;
		private double baseApr;
		private double awardApr;
		private String award;
		private String funds;
		private int status;
		private String showBorrowStatus;
		private String schedule;
		private int tenderSize;
		private int tenderSubject;
		private long verifyTime;
		private String showBorrowType;
		private String lowestAccount;
		private String balance;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getBusinessType() {
			return businessType;
		}

		public void setBusinessType(String businessType) {
			this.businessType = businessType;
		}

		public String getBusinessCode() {
			return businessCode;
		}

		public void setBusinessCode(String businessCode) {
			this.businessCode = businessCode;
		}

		public String getTimeLimit() {
			return timeLimit;
		}

		public void setTimeLimit(String timeLimit) {
			this.timeLimit = timeLimit;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public double getApr() {
			return apr;
		}

		public void setApr(double apr) {
			this.apr = apr;
		}

		public double getBaseApr() {
			return baseApr;
		}

		public void setBaseApr(double baseApr) {
			this.baseApr = baseApr;
		}

		public double getAwardApr() {
			return awardApr;
		}

		public void setAwardApr(double awardApr) {
			this.awardApr = awardApr;
		}

		public String getAward() {
			return award;
		}

		public void setAward(String award) {
			this.award = award;
		}

		public String getFunds() {
			return funds;
		}

		public void setFunds(String funds) {
			this.funds = funds;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getShowBorrowStatus() {
			return showBorrowStatus;
		}

		public void setShowBorrowStatus(String showBorrowStatus) {
			this.showBorrowStatus = showBorrowStatus;
		}

		public String getSchedule() {
			return schedule;
		}

		public void setSchedule(String schedule) {
			this.schedule = schedule;
		}

		public int getTenderSize() {
			return tenderSize;
		}

		public void setTenderSize(int tenderSize) {
			this.tenderSize = tenderSize;
		}

		public int getTenderSubject() {
			return tenderSubject;
		}

		public void setTenderSubject(int tenderSubject) {
			this.tenderSubject = tenderSubject;
		}

		public long getVerifyTime() {
			return verifyTime;
		}

		public void setVerifyTime(long verifyTime) {
			this.verifyTime = verifyTime;
		}

		public String getShowBorrowType() {
			return showBorrowType;
		}

		public void setShowBorrowType(String showBorrowType) {
			this.showBorrowType = showBorrowType;
		}

		public String getLowestAccount() {
			return lowestAccount;
		}

		public void setLowestAccount(String lowestAccount) {
			this.lowestAccount = lowestAccount;
		}

		public String getBalance() {
			return balance;
		}

		public void setBalance(String balance) {
			this.balance = balance;
		}
	}

	public static class ActivityBean {
		private int id;
		private long createDate;
		private long modifyDate;
		private String title;
		private long startTime;
		private long endTime;
		private String imgWeb;
		private String imgApp;
		private String content;
		private int status;
		private int orderList;
		private Object array;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
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

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public long getStartTime() {
			return startTime;
		}

		public void setStartTime(long startTime) {
			this.startTime = startTime;
		}

		public long getEndTime() {
			return endTime;
		}

		public void setEndTime(long endTime) {
			this.endTime = endTime;
		}

		public String getImgWeb() {
			return imgWeb;
		}

		public void setImgWeb(String imgWeb) {
			this.imgWeb = imgWeb;
		}

		public String getImgApp() {
			return imgApp;
		}

		public void setImgApp(String imgApp) {
			this.imgApp = imgApp;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public int getOrderList() {
			return orderList;
		}

		public void setOrderList(int orderList) {
			this.orderList = orderList;
		}

		public Object getArray() {
			return array;
		}

		public void setArray(Object array) {
			this.array = array;
		}
	}

	public static class IndexTypeItemListBean {
		private String type;
		private String typeImage;
		private String showBorrowType;
		private String typeDescribe;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTypeImage() {
			return typeImage;
		}

		public void setTypeImage(String typeImage) {
			this.typeImage = typeImage;
		}

		public String getShowBorrowType() {
			return showBorrowType;
		}

		public void setShowBorrowType(String showBorrowType) {
			this.showBorrowType = showBorrowType;
		}

		public String getTypeDescribe() {
			return typeDescribe;
		}

		public void setTypeDescribe(String typeDescribe) {
			this.typeDescribe = typeDescribe;
		}
	}

	public static class IndexImageItemListBean {
		private String rcd;
		private Object rmg;
		private String imageUrl;
		private int type;
		private String typeTarget;

		public String getRcd() {
			return rcd;
		}

		public void setRcd(String rcd) {
			this.rcd = rcd;
		}

		public Object getRmg() {
			return rmg;
		}

		public void setRmg(Object rmg) {
			this.rmg = rmg;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		public String getTypeTarget() {
			return typeTarget;
		}

		public void setTypeTarget(String typeTarget) {
			this.typeTarget = typeTarget;
		}
	}
}
