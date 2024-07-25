package com.asiainfo.biapp.pec.plan.jx.link.vo;

public enum RespondStatus {
	SUCESS("成功", "200"), FAILD("失败", "201"), EXISTED("已存在","202"),POP("弹窗","1"),NO_POP("不弹窗","0"),NO_PERMISSIONS("权限不足","2001");
	
	private String name;
	
	private String value;

	private RespondStatus(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String toString(){
		return String.valueOf(this.getValue());
	}
}
