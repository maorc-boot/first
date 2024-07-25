package com.asiainfo.biapp.pec.approve.jx.vo;

import lombok.Data;

import java.util.Date;

/**
 * 待审批任务
 * @author ranpf
 * @date 2023年5月25日 上午11:05:19
 */

@Data
public class ApproveUserTaskH5Bo {
	private String serviceType;//业务标识
	private String serviceName;//业务名称
	private String systemId;//系统类型（可当业务类型使用）。策略：IMCD；素材：MATERIAL
	private String systemName;//系统名称
	private String processId;//流程模板id
	private String instanceId;//流程实例id
	private String submitter; //提交人id
	private String approvalUser;//审批人id
	private String approvalUserName;//审批人姓名
	private Date approvalTime; //审批时间
	
	private String nodeId;//节点id
	private Integer approvalTimes;//审批轮次。第几轮审批
	private Long resultCount; //分页查询的总记录条数
	
	private String channelName;//渠道名称
	private String channelId;//渠道名称
	private String cepEventId;//实时短信判断字段(不为空为实时短信)
	
	private Integer testSmsStatus;//测试短信状态0：未发送，1：发送成功，2：发送失败。

	private String expireDate;//	活动有效期

	private String channelScanseValue; //场景名称

	private String addInfo;

	private String scoreChannelId;

	private String leader;

	private String approvalParams;

	private String littleType;

	private String campsegId;
	private String advice;//沟通意见
	private Integer communicationState;//沟通状态，江西用


}
