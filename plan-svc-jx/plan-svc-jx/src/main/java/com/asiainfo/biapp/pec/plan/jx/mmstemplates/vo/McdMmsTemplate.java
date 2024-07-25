package com.asiainfo.biapp.pec.plan.jx.mmstemplates.vo;


import lombok.Data;

@Data
public class McdMmsTemplate implements java.io.Serializable{
    
    /**
	* @Fields serialVersionUID 
	*/
	/**
	 * 模板编号
	 */
    private String id;
	/**
	 * 模板名称
	 */
	private String templateName;
	/**
	 * 模板主题
	 */
	private String templateSubject;
	/**
	 * 彩信地址
	 */
	private String mmsAddress;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 时间
	 */
	private String time;
}
