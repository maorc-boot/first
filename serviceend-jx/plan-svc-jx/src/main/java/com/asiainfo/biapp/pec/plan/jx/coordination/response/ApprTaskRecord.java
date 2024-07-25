package com.asiainfo.biapp.pec.plan.jx.coordination.response;

import com.asiainfo.biapp.pec.core.enums.TransPropEnum;
import com.asiainfo.biapp.pec.plan.jx.camp.vo.CmpApproveProcessRecordJx;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @author : ranpf
 * @date : 2023-4-18 20:45:54
 *
 */
@ApiModel(value = "江西审批列表实体", description = "江西审批列表实体")
@Data
public class ApprTaskRecord extends CmpApproveProcessRecordJx {


    @ApiModelProperty(value = "任务ID")
    private String taskId;

    @ApiModelProperty(value = "任务父ID")
    private String taskPId;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty("执行状态 10待执行,20计算中,25计算失败, 30待审批,40审批中,41审批驳回,54审批通过,90已推送")
    private int execStatus;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty(value = "产品总数")
    private int planTotalNum;

    @ApiModelProperty(value = "客户总数")
    private int customTotalNum;

    @ApiModelProperty(value = "策划人ID")
    private String createUserId;

    @ApiModelProperty(value = "策划人")
    private String createUserName;

    @ApiModelProperty(value = "审批流ID")
    private String approveFlowId;

    @ApiModelProperty(value = "标签编码")
    private String tagId;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "客户类型名称")
    private String custTypeName;

    @ApiModelProperty(value = "优先级")
    private String orderBy;

    @ApiModelProperty(value = "是否有子任务信息 true--有  false--没有")
    private boolean hasChildTasks;

    @ApiModelProperty(value = "任务导出样例文件名称前缀")
    private String exportFileName;

    @ApiModelProperty(value = "任务推送文件名称前缀")
    private String pushFileName;
}
