package com.asiainfo.biapp.pec.plan.jx.link.vo;


import lombok.Data;

/**
 * McdDimCampStatus generated by MyEclipse - Hibernate Tools
 */

@Data
public class McdDimCampStatus implements java.io.Serializable {

	// Fields    

	/**
	 * 
	 */
	private static final long serialVersionUID = -2608192226656313301L;

	private Short campsegStatId;

	private String campsegStatName;

	private String campsegStatDesc;
	/** 0：可见;1：程序控制;2：暂不使用或等待开发' */
	private short campsegStatVisible;


}
