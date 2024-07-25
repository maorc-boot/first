package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * description: 江西客户通关怀短信模板对象
 *
 * @author: lvchaochao
 * @date: 2023/3/14McdSmsTemplate.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "MCD_CARE_SMS_TEMPALTE")
@ApiModel(value = "江西客户通关怀短信模板对象",description = "江西客户通关怀短信模板对象")
public class McdCareSmsTemplate extends Model<McdCareSmsTemplate> {

    @ApiModelProperty(value = "模板编码")
    @TableId(value = "TEMPLATE_CODE")
    private String templateCode;

    @ApiModelProperty(value = "模板内容")
    @TableField("TEMPLATE_CONTENT")
    private String templateContent;

    @ApiModelProperty(value = "模板状态：0-未引用 1-已引用 2-已弃用")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "模板类型")
    @TableField("TEMPLATE_TYPE")
    private String templateType;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    @ApiModelProperty(value = "创建人ID")
    @TableField("CREATE_USERID")
    private String createUserId;

    @ApiModelProperty(value = "审批状态：20-草稿 40-审批中 41-审批退回 42-审批终止 43-审批通过")
    @TableField("APPROVAL_STATUS")
    private Integer approvalStatus;

    @ApiModelProperty(value = "地市ID,999-省公共模板")
    @TableField("CITY_ID")
    private String cityId;

    @ApiModelProperty(value = "消息类型：hmh5-客户通，daiwei-代维")
    @TableField("MSG_SMS_PLAT")
    private String msgSmsPlat;

    @ApiModelProperty(value = "有效截止日期")
    @TableField("VALID_DEADLINE")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date validDeadLine;

    @ApiModelProperty(value = "审批流实例")
    @TableField("APPROVE_FLOW_ID")
    private String approveFlowId;

}
