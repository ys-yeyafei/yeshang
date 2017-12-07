package cn.ysgroup.ysdai.Beans.index;

import java.io.Serializable;

public class IndexImageItem implements Serializable {

	private static final long serialVersionUID = -203604197991113337L;

	private String imageUrl; // 滚动图url
	private Integer type; // 类型：0无1网页2项目类型3文章id
	private String typeTarget;// 类型目标：网页url（类型1时启用）类型2时，为项目类型。类型3时，为文章id

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTypeTarget() {
		return typeTarget;
	}

	public void setTypeTarget(String typeTarget) {
		this.typeTarget = typeTarget;
	}

}
