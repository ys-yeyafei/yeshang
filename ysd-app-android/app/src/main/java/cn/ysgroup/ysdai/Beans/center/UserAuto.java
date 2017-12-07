package cn.ysgroup.ysdai.Beans.center;

import cn.ysgroup.ysdai.Beans.BaseBean;

import java.math.BigDecimal;


/**
 * 自动投资
 *
 */
public class UserAuto extends BaseBean
{


	private static final long serialVersionUID = 1362725099725030873L;


	public UserAuto() {
		setRcd("R0001");
	}
	
	
	private Integer autoTenderStatus;// 自投状态1启用0关闭
	private String ableMoney;//可用金额
	private String continueTotal; //续投金额
	private Integer autoTenderRule;// 自投规则0全投，1设置
	private BigDecimal autoTenderMoneyTop;// 自投上限金额
	private String autoTenderBorrowType;// 自投保留金额

	public Integer getAutoTenderStatus()
	{
		return autoTenderStatus;
	}

	public void setAutoTenderStatus(Integer autoTenderStatus)
	{
		this.autoTenderStatus = autoTenderStatus;
	}

	public String getAbleMoney()
	{
		return ableMoney;
	}

	public void setAbleMoney(String ableMoney)
	{
		this.ableMoney = ableMoney;
	}

	public String getContinueTotal()
	{
		return continueTotal;
	}

	public void setContinueTotal(String continueTotal)
	{
		this.continueTotal = continueTotal;
	}

	public Integer getAutoTenderRule()
	{
		return autoTenderRule;
	}

	public void setAutoTenderRule(Integer autoTenderRule)
	{
		this.autoTenderRule = autoTenderRule;
	}

	public BigDecimal getAutoTenderMoneyTop()
	{
		return autoTenderMoneyTop;
	}

	public void setAutoTenderMoneyTop(BigDecimal autoTenderMoneyTop)
	{
		this.autoTenderMoneyTop = autoTenderMoneyTop;
	}

	public String getAutoTenderBorrowType()
	{
		return autoTenderBorrowType;
	}

	public void setAutoTenderBorrowType(String autoTenderBorrowType)
	{
		this.autoTenderBorrowType = autoTenderBorrowType;
	}
}
