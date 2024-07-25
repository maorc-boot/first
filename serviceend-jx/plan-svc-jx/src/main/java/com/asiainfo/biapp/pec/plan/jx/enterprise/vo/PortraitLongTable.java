package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

/**
 * TODO
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/2 17:12
 */
public class PortraitLongTable {
	private String labelId;//标签编码
	private String labelName;//标签名称
	private String labelValue;//标签值
	private String categoryName;//场景一级分类名称

	public PortraitLongTable() {
	}

	public PortraitLongTable(String labelId, String labelName, String labelValue, String categoryName) {
		this.labelId = labelId;
		this.labelName = labelName;
		this.labelValue = labelValue;
		this.categoryName = categoryName;
	}

	public String getLabelId() {
		return labelId;
	}

	public void setLabelId(String labelId) {
		this.labelId = labelId;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public String getLabelValue() {
		return labelValue;
	}

	public void setLabelValue(String labelValue) {
		this.labelValue = labelValue;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
