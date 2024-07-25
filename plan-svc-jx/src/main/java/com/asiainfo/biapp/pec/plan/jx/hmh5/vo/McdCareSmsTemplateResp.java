package com.asiainfo.biapp.pec.plan.jx.hmh5.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 江西客户通关怀短信模板返回对象
 *
 * @author: lvchaochao
 * @date: 2023/3/14
 */
@ApiModel(value = "江西客户通关怀短信模板返回对象",description = "江西客户通关怀短信模板返回对象")
@Data
public class McdCareSmsTemplateResp {

    @ApiModelProperty(value = "模板编码")
    private String templateCode;

    @ApiModelProperty(value = "模板内容")
    private String templateContent;

    @ApiModelProperty(value = "模板状态：0-未引用 1-已引用 2-已弃用")
    private String statusStr;

    @ApiModelProperty(value = "模板状态：0-未引用 1-已引用 2-已弃用")
    private Integer status;

    @ApiModelProperty(value = "模板类型")
    private String templateType;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    private Date modifyTime;

    @ApiModelProperty(value = "创建人ID")
    private String createUserId;

    @ApiModelProperty(value = "审批状态：20-草稿 40-审批中 41-审批退回 42-审批终止 43-审批通过")
    private Integer approvalStatus;

    @ApiModelProperty(value = "审批状态：20-草稿 40-审批中 41-审批退回 42-审批终止 43-审批通过")
    private String approvalStatusStr;

    @ApiModelProperty(value = "地市ID,999-省公共模板")
    private String cityId;

    @ApiModelProperty(value = "消息类型：hmh5-客户通，daiwei-代维")
    private String msgSmsPlat;

    @ApiModelProperty(value = "有效截止日期")
    private String validDeadLine;

    @ApiModelProperty(value = "审批流实例")
    private String approveFlowId;

    @ApiModelProperty(value = "创建人名")
    private String userName;

    @ApiModelProperty(value = "审批节点名")
    private String nodeName;

    @ApiModelProperty(value = "审批日期")
    private String approveDate;

    @ApiModelProperty(value = "审批人")
    private String approvalerName;

    @ApiModelProperty(value = "审批结果")
    private String approveResult;
}
