package cn.ysgroup.ysdai.Beans.center;


import cn.ysgroup.ysdai.Beans.BaseBean;
import cn.ysgroup.ysdai.Beans.PageBean;

import java.util.List;

public class UserTenderList  extends BaseBean
{
	private static final long serialVersionUID = 5467303173842659039L;

	public UserTenderList() {
		setRcd("R0001");
	}

	private List<UserTender> userTenderList;

	private PageBean pageBean;

	public List<UserTender> getUserTenderList() {
		return userTenderList;
	}

	public void setUserTenderList(List<UserTender> userTenderList) {
		this.userTenderList = userTenderList;
	}

	public PageBean getPageBean() {
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

}
