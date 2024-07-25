package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/2 17:12
 */
public class PortraitCrossTable {
	private String labelId;//标签编码
	private String labelName;//标签名称
	private String categoryName;//场景一级分类名称
	private List<Map<String,String >> mapValue;//复合标签名称与值

	public PortraitCrossTable() {
	}

	public PortraitCrossTable(String labelId, String labelName, String categoryName, List<Map<String, String>> mapValue) {
		this.labelId = labelId;
		this.labelName = labelName;
		this.categoryName = categoryName;
		this.mapValue = mapValue;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<Map<String, String>> getMapValue() {
		return mapValue;
	}

	public void setMapValue(List<Map<String, String>> mapValue) {
		this.mapValue = mapValue;
	}
}
