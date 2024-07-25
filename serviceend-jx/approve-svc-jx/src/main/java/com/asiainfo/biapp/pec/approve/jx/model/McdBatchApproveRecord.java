package com.asiainfo.biapp.pec.approve.jx.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 批量审批任务记录明细
 * </p>
 *
 * @author mamp
 * @since 2023-11-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="批量审批节点记录", description="批量审批节点记录")
public class McdBatchApproveRecord implements Serializable {

    private static final long serialVersionUID=1L;

    private String  batchId;

    /**
     * 系统ID
     */
    @ApiModelProperty(value = "系统ID")
    private String systemId;

    /**
     * 审批流程ID
     */
    @ApiModelProperty(value = "审批流程ID")
    private String processId;

    /**
     * 审批实例ID
     */
    @ApiModelProperty(value = "审批实例ID")
    private String instanceId;

    /**
     * 审批对象业务ID（策略ID,素材ID...）
     */
    @ApiModelProperty(value = "审批对象业务ID（策略ID,素材ID...）")
    private String businessId;

    /**
     * 审批记录ID
     */
    @ApiModelProperty(value = "审批记录ID")
    private String recordId;

    /**
     * 审批节点ID
     */
    @ApiModelProperty(value = "审批节点ID")
    private String nodeId;

    @ApiModelProperty(value = "提交人ID")
    private String createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 审批状态 0-待审批 1-审批通过 2-审批失败
     */
    private Integer status;

    /**
     * 审批意见
     */
    private String dealOpinion ;


}
