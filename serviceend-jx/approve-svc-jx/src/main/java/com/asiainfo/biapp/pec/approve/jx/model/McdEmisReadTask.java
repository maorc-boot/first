package com.asiainfo.biapp.pec.approve.jx.model;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 阅知待办任务表
 * </p>
 *
 * @author mamp
 * @since 2022-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class McdEmisReadTask implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableField("ID")
    private String id;

    /**
     * 阅知名称
     */
    @ApiModelProperty(value = "阅知名称")
    @TableField("NAME")
    private String name;

    /**
     * 业务ID(策略ID等)
     */
    @ApiModelProperty(value = "主键ID")
    @TableField("BUSINESS_ID")
    private String businessId;

    /**
     * 业务类型,1-策略
     */
    @ApiModelProperty(value = "业务类型,1-策略")
    @TableField("BUSINESS_TYPE")
    private Integer businessType;

    /**
     * 阅知提交人id
     */
    @ApiModelProperty(value = "阅知提交人id")
    @TableField("SUBMIT_USER")
    private String submitUser;

    /**
     * 阅知提交人id
     */
    @ApiModelProperty(value = "阅知提交人id")
    @TableField("SUBMIT_USER_NAME")
    private String submitUserName;

    /**
     * 触发阅知的审批节点ID
     */
    @ApiModelProperty(value = "触发阅知的审批节点ID")
    @TableField("NODE_ID")
    private String nodeId;

    /**
     * 0:待阅,1:已阅
     */
    @ApiModelProperty(value = "0:待阅,1:已阅")
    @TableField("STATUS")
    private Integer status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 阅知处理时间
     */
    @ApiModelProperty(value = "阅知处理时间")
    @TableField("DEAL_TIME")
    private Date dealTime;

    /**
     * 阅知处理人
     */
    @ApiModelProperty(value = "阅知处理人ID")
    @TableField("READ_USER")
    private String readUser;

    @ApiModelProperty(value = "阅知处理意见")
    @TableField("ADVICE")
    private String advice;
}
