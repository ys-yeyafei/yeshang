package cn.ysgroup.ysdai.Beans.userInfo;


import cn.ysgroup.ysdai.Beans.BaseBean;

public class UserSafePwdBean extends BaseBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1044762609839146720L;

	// 安全密码是否设置
	private Integer safePwdStatus;

	public Integer getSafePwdStatus() {
		return safePwdStatus;
	}

	public void setSafePwdStatus(Integer safePwdStatus) {
		this.safePwdStatus = safePwdStatus;
	}

}
