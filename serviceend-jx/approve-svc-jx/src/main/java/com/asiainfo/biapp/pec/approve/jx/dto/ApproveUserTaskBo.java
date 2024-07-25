package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 待审批任务
 */

@Data
@ApiModel(value = "江西待审批任务")
@AllArgsConstructor
@NoArgsConstructor
public class ApproveUserTaskBo implements Cloneable {
	@ApiModelProperty(value = "业务标识")
	private String serviceType;
	@ApiModelProperty(value = "业务名称")
	private String serviceName;
	@ApiModelProperty(value = "系统类型（可当业务类型使用）。策略：IMCD；素材：MATERIAL，自定义标签：CUSTOM_LABEL，自定义预警：CUSTOM_ALARM")
	private String systemId;
	@ApiModelProperty(value = "系统名称")
	private String systemName;
	@ApiModelProperty(value = "流程模板id")
	private String processId;
	@ApiModelProperty(value = "流程实例id")
	private String instanceId;
	@ApiModelProperty(value = "提交人id")
	private String submitter;
	@ApiModelProperty(value = "审批人id")
	private String approvalUser;
	@ApiModelProperty(value = "审批人姓名")
	private String approvalUserName;
	@ApiModelProperty(value = "审批时间")
	private Date approvalTime;
	@ApiModelProperty(value = "节点id")
	private String nodeId;
	@ApiModelProperty(value = "审批轮次。第几轮审批")
	private Integer approvalTimes;
	@ApiModelProperty(value = "沟通意见")
	private String advice;
	@ApiModelProperty(value = "沟通状态 0 待审批,1审批完成，江西用")
	private Integer communicationState;

	@ApiModelProperty(value = "批量操作:流程实例id数组")
	private String[] instanceIds;
	@ApiModelProperty(value = "批量操作:业务标识数组")
	private String[] serviceTypes;
	@ApiModelProperty(value = "批量操作:业务名称数组")
	private String[] serviceNames;



	 
}
