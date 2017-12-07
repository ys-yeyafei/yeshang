package cn.ysgroup.ysdai.Beans.user;


import cn.ysgroup.ysdai.Beans.BaseBean;

public class ChangePwdBean extends BaseBean {

	private static final long serialVersionUID = -7683603982464447706L;

	public ChangePwdBean() {
		setRcd("R0001");
	}

	private String id;
	private String codeState;
	private String pwdState;

	public String getCodeState() {
		return codeState;
	}
	public void setCodeState(String codeState) {
		this.codeState = codeState;
	}
	public String getPwdState() {
		return pwdState;
	}
	public void setPwdState(String pwdState) {
		this.pwdState = pwdState;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
