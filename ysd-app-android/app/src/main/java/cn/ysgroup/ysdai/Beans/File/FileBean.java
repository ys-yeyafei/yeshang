package cn.ysgroup.ysdai.Beans.File;


import cn.ysgroup.ysdai.Beans.BaseBean;

public class FileBean extends BaseBean
{

	private static final long serialVersionUID = -7683603982464447706L;

	public FileBean() {
		setRcd("R0001");
	}

	private String filename ;
	private String url;
	private String fullUrl;

	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFullUrl() {
		return fullUrl;
	}
	public void setFullUrl(String fullUrl) {
		this.fullUrl = fullUrl;
	}
	
	
}
