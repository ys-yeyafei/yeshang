package cn.ysgroup.ysdai.Beans.index;

import java.io.Serializable;

public class IndexTypeItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9192956948380633923L;

	private String type;// 类型
	private String typeImage;//类型图片
	private String showBorrowType;//类型名称
	private String typeDescribe;//类型描述

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeImage() {
		return typeImage;
	}

	public void setTypeImage(String typeImage) {
		this.typeImage = typeImage;
	}

	public String getShowBorrowType() {
		return showBorrowType;
	}

	public void setShowBorrowType(String showBorrowType) {
		this.showBorrowType = showBorrowType;
	}

	public String getTypeDescribe() {
		return typeDescribe;
	}

	public void setTypeDescribe(String typeDescribe) {
		this.typeDescribe = typeDescribe;
	}

}
