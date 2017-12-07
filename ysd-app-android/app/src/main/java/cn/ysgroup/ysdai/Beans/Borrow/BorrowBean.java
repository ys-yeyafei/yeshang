package cn.ysgroup.ysdai.Beans.Borrow;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.user.UserHongbaoViews;

import java.math.BigDecimal;
import java.util.List;


public class BorrowBean extends BaseBean {

	private static final long serialVersionUID = -7683603982464447706L;

	public BorrowBean() {
		setRcd("R0001");
	}

	private Integer id;// 标ID
	private String name;// 标题
	private String type;// 类型【0-秒标，1-质押标，2-流转标，3-信用标，4-月标,5 流转标】新标类型【11天标 12
						// 月标13流转标】
	private String businessType;// 业务类型 1，2，3
	private String businessCode;// 项目编号
	private String timeLimit;// 借款天数
	private String account;// 借款总金额
	private BigDecimal yearApr;// 年利率
	private BigDecimal baseApr;//基本利息
	private BigDecimal awardApr;//奖励利息

	private String funds;// 投标金额比例奖励
	private String style;// 还款方式 【1分期付息，到期本息 2到期付本息，3等额本金】
	private String lowestAccount;// 最低投标额
	private String mostAccount;// 最高投标额
	private int pwdFlg;// 定向标密码标记
	private Long verifyTime;// 审核时间
	private int status;// 状态码0-发表未审核；1-审核通过；2-审核未通过；3-满标审核通过；4-满标审核未通过；5-等待满标审核；6-过期或撤回；7-已还完;8-删除状态
	private String showBorrowStatus;
	private int showSchedule;
	private String content;// 标详细描述
	private String borImage; // 标的图片
	private String balance; // 剩余金额
	private int userFlg;
	private BigDecimal userAbleMoney;
	private BigDecimal tasteMoney;
	
	private Long currentDate;//当前时间
	private Long overDate;//过期时间
	private String styleName; //还款方式
	private String borrowVerifyJson;

	private BigDecimal awardScale;//使用红包比例

	private List<UserHongbaoViews> userHongbaoItem;//红包列表
	private String capitalEnsure;//资金保障
	private Integer realStatus;//认证庄涛
	private String expireDate;
	private String isNewbor;

	public String getExpireDate(){
		return expireDate;
	}

	public String getIsNewbor(){
		return isNewbor;
	}

	public void setExpireDate(String expireDate){
		this.expireDate=expireDate;
	}

	public void setIsNewbor(String isNewbor){
		this.isNewbor=isNewbor;
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

	public BigDecimal getYearApr() {
		return yearApr;
	}

	public void setYearApr(BigDecimal yearApr) {
		this.yearApr = yearApr;
	}

	public String getFunds() {
		return funds;
	}

	public void setFunds(String funds) {
		this.funds = funds;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getLowestAccount() {
		return lowestAccount;
	}

	public void setLowestAccount(String lowestAccount) {
		this.lowestAccount = lowestAccount;
	}

	public String getMostAccount() {
		return mostAccount;
	}

	public void setMostAccount(String mostAccount) {
		this.mostAccount = mostAccount;
	}

	public int getPwdFlg() {
		return pwdFlg;
	}

	public void setPwdFlg(int pwdFlg) {
		this.pwdFlg = pwdFlg;
	}

	public Long getVerifyTime() {
		return verifyTime;
	}

	public void setVerifyTime(Long verifyTime) {
		this.verifyTime = verifyTime;
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

	public int getShowSchedule() {
		return showSchedule;
	}

	public void setShowSchedule(int showSchedule) {
		this.showSchedule = showSchedule;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBorImage() {
		return borImage;
	}

	public void setBorImage(String borImage) {
		this.borImage = borImage;
	}

	public int getUserFlg() {
		return userFlg;
	}

	public void setUserFlg(int userFlg) {
		this.userFlg = userFlg;
	}

	public BigDecimal getUserAbleMoney() {
		return userAbleMoney;
	}

	public void setUserAbleMoney(BigDecimal userAbleMoney) {
		this.userAbleMoney = userAbleMoney;
	}

	public Long getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Long currentDate) {
		this.currentDate = currentDate;
	}

	public Long getOverDate() {
		return overDate;
	}

	public void setOverDate(Long overDate) {
		this.overDate = overDate;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

	public String getBorrowVerifyJson() {
		return borrowVerifyJson;
	}

	public void setBorrowVerifyJson(String borrowVerifyJson) {
		this.borrowVerifyJson = borrowVerifyJson;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public BigDecimal getAwardScale() {
		return awardScale;
	}

	public void setAwardScale(BigDecimal awardScale) {
		this.awardScale = awardScale;
	}

	public List<UserHongbaoViews> getUserHongbaoItem() {
		return userHongbaoItem;
	}

	public void setUserHongbaoItem(List<UserHongbaoViews> userHongbaoItem) {
		this.userHongbaoItem = userHongbaoItem;
	}

	public BigDecimal getTasteMoney()
	{
		return tasteMoney;
	}

	public void setTasteMoney(BigDecimal tasteMoney)
	{
		this.tasteMoney = tasteMoney;
	}

	public String getCapitalEnsure()
	{
		return capitalEnsure;
	}

	public void setCapitalEnsure(String capitalEnsure)
	{
		this.capitalEnsure = capitalEnsure;
	}

	public Integer getRealStatus()
	{
		return realStatus;
	}

	public void setRealStatus(Integer realStatus)
	{
		this.realStatus = realStatus;
	}

	public BigDecimal getAwardApr()
	{
		return awardApr;
	}

	public void setAwardApr(BigDecimal awardApr)
	{
		this.awardApr = awardApr;
	}

	public BigDecimal getBaseApr()
	{
		return baseApr;
	}

	public void setBaseApr(BigDecimal baseApr)
	{
		this.baseApr = baseApr;
	}
}
