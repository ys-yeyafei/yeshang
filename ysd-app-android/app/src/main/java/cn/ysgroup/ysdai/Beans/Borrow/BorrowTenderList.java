package cn.ysgroup.ysdai.Beans.Borrow;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.PageBean;

import java.util.List;


public class BorrowTenderList extends BaseBean
{

	private static final long serialVersionUID = -5564347644881208254L;

	public BorrowTenderList() {
		setRcd("R0001");
	}

	private List<BorrowTenderItem> borrowTenderItemList;

	private String apr;//年利率
	private PageBean pageBean;

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public List<BorrowTenderItem> getBorrowTenderItemList() {
		return borrowTenderItemList;
	}

	public void setBorrowTenderItemList(List<BorrowTenderItem> borrowTenderItemList) {
		this.borrowTenderItemList = borrowTenderItemList;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

}
