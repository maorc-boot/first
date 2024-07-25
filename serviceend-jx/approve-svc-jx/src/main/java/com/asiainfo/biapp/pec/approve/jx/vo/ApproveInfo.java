package com.asiainfo.biapp.pec.approve.jx.vo;

import lombok.Data;

/**
 * Created by ranpf on 2023/5/22.
 */

@Data
public class ApproveInfo {
    private String instanceId; // 审批实例id
    private String approvalUserId; // 审批人id
    private String nodeId;        // 审批节点id
    private String approvalTimes; // 审批次数
    private String result;        // 审批结果
    private String advice;        // 审批建议


}
