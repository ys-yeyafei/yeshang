package cn.ysgroup.ysdai.Beans.user;


import cn.ysgroup.ysdai.Beans.BaseBean;

public class ChangeEmailBean extends BaseBean {

	private static final long serialVersionUID = -7683603982464447706L;

	public ChangeEmailBean() {
		setRcd("R0001");
	}

	private String id;
	private String emailStatus;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmailStatus() {
		return emailStatus;
	}
	public void setEmailStatus(String emailStatus) {
		this.emailStatus = emailStatus;
	}
	
	
}
