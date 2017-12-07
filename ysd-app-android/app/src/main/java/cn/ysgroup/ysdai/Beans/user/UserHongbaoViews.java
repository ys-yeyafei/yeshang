package cn.ysgroup.ysdai.Beans.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserHongbaoViews implements Serializable{

	private Integer id;//红包ID
	private Integer userId;// 用户编号-红包所有者
	private String hbNo;// 编号
	private BigDecimal money;// 原始面额
	private Integer status;// 状态 0未使用1使用2已过期
	private Date startTime;// 领取时间
	private Date endTime;// 到期时间
	private Integer source;// 1注册2投资奖励3首次投资奖励4实名、邮箱认证通过奖励
	private String name;// 来源说明0
	private int overDays;//过期时间
	private Integer expDate;//使用有效期
	private Integer limitStart;// 项目期限起始
	private Integer limitEnd;// 项目期限结束
	private Integer isPc;// PC【0：不可用，1：可用】
	private Integer isApp;// APP【0：不可用，1：可用】
	private Integer isHfive;// H5【0：不可用，1：可用】
	private String investFullMomey;

	public String getInvestFullMomey(){
		return investFullMomey;
	}

	public void setInvestFullMomey(String investFullMomey){
		this.investFullMomey=investFullMomey;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getHbNo() {
		return hbNo;
	}
	public void setHbNo(String hbNo) {
		this.hbNo = hbNo;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public int getOverDays()
	{
		return overDays;
	}

	public void setOverDays(int overDays)
	{
		this.overDays = overDays;
	}

	public Integer getExpDate()
	{
		return expDate;
	}

	public void setExpDate(Integer expDate)
	{
		this.expDate = expDate;
	}

	public Integer getLimitStart()
	{
		return limitStart;
	}

	public void setLimitStart(Integer limitStart)
	{
		this.limitStart = limitStart;
	}

	public Integer getLimitEnd()
	{
		return limitEnd;
	}

	public void setLimitEnd(Integer limitEnd)
	{
		this.limitEnd = limitEnd;
	}

	public Integer getIsPc()
	{
		return isPc;
	}

	public void setIsPc(Integer isPc)
	{
		this.isPc = isPc;
	}

	public Integer getIsApp()
	{
		return isApp;
	}

	public void setIsApp(Integer isApp)
	{
		this.isApp = isApp;
	}

	public Integer getIsHfive()
	{
		return isHfive;
	}

	public void setIsHfive(Integer isHfive)
	{
		this.isHfive = isHfive;
	}
}
