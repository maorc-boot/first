package com.asiainfo.biapp.pec.approve.jx.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("mcd_dim_plan_type")
public class McdDimPlanTypeModel  {

	@TableId("TYPE_ID")
	private String typeId;
	@TableField("TYPE_NAME")
	private String typeName;
	@TableField("TYPE_PID")
	private String typePid;
	@TableField("CHANNEL_TYPE")
	private String channelType;
	@TableField("SORT_NUM")
	private String sortNum;
	@TableField("PRIORITY")
	private int priority;
	@TableField("SERVICE_TYPE_CODE")
	private int serviceTypeCode;


}