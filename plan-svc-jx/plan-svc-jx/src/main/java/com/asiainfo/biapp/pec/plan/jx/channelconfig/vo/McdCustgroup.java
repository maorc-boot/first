package com.asiainfo.biapp.pec.plan.jx.channelconfig.vo;

import lombok.Data;

import java.util.Date;


@Data
public class McdCustgroup implements java.io.Serializable {
	private static final long serialVersionUID = -540338883022707256L;
	private String customGroupId;
	private String customGroupName;
	private String customGroupDesc;
	private String createUserId;
	private Date createTime;
	private String ruleDesc;
	private String customSourceId;
	private int customNum;
	private int customStatusId;
	private Date effectiveTime;
	private Date failTime;
	private int updateCycle;
	private String custGroupUpdateCycle;
	
	private String dataDate;
	
	private String dataTime;  //数据生成日期
	
//	IMCD_ZJ为了前台统一使用模板   别名
	private String typeId;
	private String typeName;
	
	private String campsegCustGroupId;  //客户群和策略关联关系表信息主键
	
	private String createUserName;  //创建客户群用户名称

	private int actualCustomNum; //推送成功客户数

	private String isIntelRec;//类型

	private String statusName;

}
