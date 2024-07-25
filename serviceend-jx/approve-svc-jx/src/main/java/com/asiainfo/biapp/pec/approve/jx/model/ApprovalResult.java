package com.asiainfo.biapp.pec.approve.jx.model;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.Date;


@Data
public class ApprovalResult {

	private String approvalUserId; // 审批人ID
	private String approvalUser; // 审批人ID为了和数据库approval_User保持一致
	private String approvalUserName; // 审批人姓名
	private int result; // 审批结果，枚举值含义参考com.asiainfo.biapp.mcd.approve.constant.ApproveContant中的NODE_APPROVER_STATE_PASS、NODE_APPROVER_STATE_BACK、NODE_APPROVER_STATE_AWAIT
	private String advice; // 审批意见
	private String addInfo; // 附加信息
	private String instanceId; // 审批流程实例ID
	private String nodeId; // 审批节点ID
	private int approvalTimes; // 审批次数
	private Date approvalTime; // 审批时间
	private String serviceType; //业务标识
	private String campsegId; //子策略ID

	/*******wb add for 前台自己指定下一节点审批人******/
	private String nextNodesApprover;//前台指定的下一跳节点的审批人：[{"nodeId":"xx","approveId":"zhangSan"}]
	
	/********wb add for 二次开发，审批和提交时都通知外部系统---补充通知信息********/
	private String instanceName; // 审批流程实例ID
	private String createUserId; // 策划人id
	private String createUserName; // 策划人
	private String nodeName; // 策划人id


	/**
	 * 解析审批结果
	 * 
	 * @param approvalResult json格式的审批结果信息，格式如下：
	 *            {"approvalUserId":"admin","approvalUserName":"管理员","result":"1","advice":"通过","addInfo":"附加信息",
	 *             "instanceId":"2016111009552690","nodeId":"5","approvalTimes":"1","approvalTime":"2016-11-15 17:06:23"}
	 * @return 审批结果对象
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static ApprovalResult resolve(String approvalResult)throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		return mapper.readValue(approvalResult, ApprovalResult.class);
	}


}
