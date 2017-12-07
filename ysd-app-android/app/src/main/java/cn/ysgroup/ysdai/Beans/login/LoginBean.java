package cn.ysgroup.ysdai.Beans.login;


import cn.ysgroup.ysdai.Beans.BaseBean;

public class LoginBean extends BaseBean {

	private static final long serialVersionUID = -7683603982464447706L;

	public LoginBean() {
		setRcd("R0001");
	}

	private Integer userId;
	private String username;
	private String realName;
	private String sid;
	private String token;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}
}
