package cn.ysgroup.ysdai.Beans.Borrow;

import cn.ysgroup.ysdai.Beans.BaseBean;

import java.util.List;


public class BorrowRepaymentInfo extends BaseBean
{
	
	private static final long serialVersionUID = -1890190253914978420L;

	public BorrowRepaymentInfo() {
		setRcd("R0001");
	}

	private String debtMess;//债务人信息
	private String content;//项目描述
	private String realName;// 真实姓名
	private String useReason;// 借款用途
	private String paymentSource;// 还款来源
	private int orderNum;// 借款标分期顺序
	private String interest;// 本期所还利息
	private String capital;// 本期所还本金
	private String account;
	private Integer repaymentDateInt;// 还款时间
	private String isdayString;//天标OR月标

	private List<BorrowRepaymentDetailList> repaymentDetailList;

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getRepaymentDateInt() {
		return repaymentDateInt;
	}

	public void setRepaymentDateInt(Integer repaymentDateInt) {
		this.repaymentDateInt = repaymentDateInt;
	}

	public String getIsdayString() {
		return isdayString;
	}

	public void setIsdayString(String isdayString) {
		this.isdayString = isdayString;
	}

	public List<BorrowRepaymentDetailList> getRepaymentDetailList() {
		return repaymentDetailList;
	}

	public void setRepaymentDetailList(List<BorrowRepaymentDetailList> repaymentDetailList) {
		this.repaymentDetailList = repaymentDetailList;
	}

	public String getDebtMess()
	{
		return debtMess;
	}

	public void setDebtMess(String debtMess)
	{
		this.debtMess = debtMess;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getRealName()
	{
		return realName;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public String getUseReason()
	{
		return useReason;
	}

	public void setUseReason(String useReason)
	{
		this.useReason = useReason;
	}

	public String getPaymentSource()
	{
		return paymentSource;
	}

	public void setPaymentSource(String paymentSource)
	{
		this.paymentSource = paymentSource;
	}
}
