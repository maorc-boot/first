package com.asiainfo.biapp.pec.plan.jx.custgroup.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


@Data
@ApiModel(value = "客户群管理:客群信息", description = "客户群管理:客群信息")
public class McdCustgroupDefInfo implements java.io.Serializable {

	private static final long serialVersionUID = -540338883022707256L;
	@ApiModelProperty(value = "客户群ID")
	private String customGroupId;
	@ApiModelProperty(value = "客户群名称")
	private String customGroupName;
	@ApiModelProperty(value = "客户群描述")
	private String customGroupDesc;
	@ApiModelProperty(value = "客群创建人")
	private String createUserId;
	@ApiModelProperty(value = "客群创建时间")
	private Date createTime;
	@ApiModelProperty(value = "客群更新时间")
	private Date updateTime;
	@ApiModelProperty(value = "客群规则描述")
	private String ruleDesc;
	@ApiModelProperty(value = "客户群来源")
	private String customSourceId;
	@ApiModelProperty(value = "客群数量")
	private int customNum;
	@ApiModelProperty(value = "客群状态")
	private int customStatusId;
	@ApiModelProperty(value = "客群生效时间")
	private Date effectiveTime;
	@ApiModelProperty(value = "客群失效时间")
	private Date failTime;
	@ApiModelProperty(value = "客群更新周期")
	private int updateCycle;
	@ApiModelProperty(value = "客群数据日期")
	private String dataDate;
	@ApiModelProperty(value = "数据生成日期")
	private String dataTime;
	@ApiModelProperty(value = "推送成功客户数")
	private int actualCustomNum;
	@ApiModelProperty(value = "状态名称")
	private String statusName;
	@ApiModelProperty(value = "客群类型,政企 集团 个人")
	private String custType;
	@ApiModelProperty(value = "客群更新周期名称")
	private String updateCycleName;
	@ApiModelProperty(value = "是否可以删除标志,0是,1 否")
	private String delFlag;
	@ApiModelProperty(value = "客群更新状态，0/1：不更新/更新")
	private Integer updateStatus;


}
