package com.asiainfo.biapp.pec.plan.jx.enterprise.vo;

/**
 * 标签属性
 *
 * @author admin
 * @version 1.0
 * @date 2022/8/3 14:34
 */
public class PortraitLebelData {
	//枚举名称
	private String dataName;
	//枚举值
	private String dateValue;
	//标签单位
	private String dataUnit;

	public PortraitLebelData() {
	}

	public PortraitLebelData(String dataName, String dateValue, String dataUnit) {
		this.dataName = dataName;
		this.dateValue = dateValue;
		this.dataUnit = dataUnit;
	}

	public String getDataName() {
		return dataName;
	}

	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public String getDataUnit() {
		return dataUnit;
	}

	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}
}
