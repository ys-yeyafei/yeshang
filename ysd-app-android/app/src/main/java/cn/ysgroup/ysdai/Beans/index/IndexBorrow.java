package cn.ysgroup.ysdai.Beans.index;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class IndexBorrow implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3647035841484545161L;

	private Integer id;// 标ID
	private String name;// 标题
	private String type;// 类型【0-秒标，1-质押标，2-流转标，3-信用标，4-月标,5 流转标】新标类型【11天标 12 月标
	// 13流转标】
	private String businessType;
	private String businessCode;// 项目编号
	private String timeLimit;// 借款天数
	private String account;// 借款总金额
	private BigDecimal apr;// 年利率
	private BigDecimal baseApr;//基本利息
	private BigDecimal awardApr;//奖励利息
	private String award;// 投标奖励方式
	private String funds;// 投标金额比例奖励

	private int status;// 状态码0-发表未审核；1-审核通过；2-审核未通过；3-满标审核通过；4-满标审核未通过；5-等待满标审核；6-过期或撤回；7-已还完;8-删除状态
	private String showBorrowStatus;// 状态名称
	private String schedule; // 投标的百分比
	private String balance;
	private int tenderSize;// 已投次数

	private int tenderSubject;// 新客标记

	private Date verifyTime;// 审核时间

	private String showBorrowType;// 类型名称

	private String lowestAccount;// 最低起投金额

	public void setBalance(String balance){
		this.balance=balance;
	}

	public String getBlance(){
		return balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public BigDecimal getApr() {
		return apr;
	}

	public void setApr(BigDecimal apr) {
		this.apr = apr;
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

	public Date getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}

	public String getShowBorrowType() {
		return showBorrowType;
	}

	public void setShowBorrowType(String showBorrowType) {
		this.showBorrowType = showBorrowType;
	}


	public String getLowestAccount()
	{
		return lowestAccount;
	}

	public void setLowestAccount(String lowestAccount)
	{
		this.lowestAccount = lowestAccount;
	}

	public BigDecimal getBaseApr()
	{
		return baseApr;
	}

	public void setBaseApr(BigDecimal baseApr)
	{
		this.baseApr = baseApr;
	}

	public BigDecimal getAwardApr()
	{
		return awardApr;
	}

	public void setAwardApr(BigDecimal awardApr)
	{
		this.awardApr = awardApr;
	}
}
