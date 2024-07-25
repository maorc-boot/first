package com.asiainfo.biapp.pec.plan.jx.hisdata.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 4.0策略审批记录
 * </p>
 *
 * @author mamp
 * @since 2023-05-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CmpApproveProcessRecordHis implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 流程ID
     */
    @TableField("PROCESS_ID")
    private String processId;

    /**
     * 实例ID
     */
    @TableField("INSTANCE_ID")
    private String instanceId;

    /**
     * 策略ID
     */
    @TableField("BUSINESS_ID")
    private String businessId;

    /**
     * 节点ID
     */
    @TableField("NODE_ID")
    private String nodeId;

    /**
     * 节点名称
     */
    @TableField("NODE_NAME")
    private String nodeName;

    /**
     * 审批人ID
     */
    @TableField("APPROVER")
    private String approver;

    /**
     * 审批人名称
     */
    @TableField("APPROVER_NAME")
    private String approverName;

    /**
     * 处理意见
     */
    @TableField("DEAL_OPINION")
    private String dealOpinion;

    /**
     * 审批状态
     */
    @TableField("SUBMIT_STATUS")
    private String submitStatus;

    /**
     * 审批人地市ID
     */
    @TableField("CITY_ID")
    private String cityId;

    /**
     * 审批人地市名称
     */
    @TableField("APPROVAL_USER_CITY")
    private String approvalUserCity;

    /**
     * 审批人部门ID
     */
    @TableField("DEPT_ID")
    private String deptId;

    /**
     * 审批人部门名称
     */
    @TableField("APPROVAL_USER_DEPT")
    private String approvalUserDept;

    /**
     * 审批时间
     */
    @TableField("CREATE_DATE")
    private String createDate;

    /**
     * 下级审批节点
     */
    @TableField("NEXT_NODES_APPROVER")
    private String nextNodesApprover;


}
