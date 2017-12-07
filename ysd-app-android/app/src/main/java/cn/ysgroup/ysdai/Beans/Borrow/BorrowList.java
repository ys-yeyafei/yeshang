package cn.ysgroup.ysdai.Beans.Borrow;

import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.PageBean;

import java.util.List;



public class BorrowList extends BaseBean {

	private static final long serialVersionUID = -7683603982464447706L;

	public BorrowList() {
		setRcd("R0001");
	}

	private List<BorrowItem> borrowItemList;

	private PageBean pageBean;

	public List<BorrowItem> getBorrowItemList() {
		return borrowItemList;
	}

	public void setBorrowItemList(List<BorrowItem> borrowItemList) {
		this.borrowItemList = borrowItemList;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

}
