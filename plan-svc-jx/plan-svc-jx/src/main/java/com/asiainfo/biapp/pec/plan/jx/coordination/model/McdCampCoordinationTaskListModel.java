package com.asiainfo.biapp.pec.plan.jx.coordination.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author : ranpf
 * @date : 2023-5-9 19:54:10
 */
@Data
@ApiModel("策略统筹融合计算任务与策略关系表")
@TableName("mcd_camp_coordinate_task_list")
public class McdCampCoordinationTaskListModel {

    @ApiModelProperty(value = "子策略ID")
    @TableField("CAMPSEG_ID")
    private String campsegId;

    @ApiModelProperty(value = "任务ID")
    @TableField("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "父策略ID")
    @TableField("CAMPSEG_PID")
    private String campsegPId;

    @ApiModelProperty(value = "策略名称")
    @TableField("CAMPSEG_NAME")
    private String campsegName;

    @ApiModelProperty("渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty("运营位ID")
    @TableField("ADIV_ID")
    private String adivId;

    @ApiModelProperty(value = "产品ID")
    @TableField("PLAN_ID")
    private String planId;

    @ApiModelProperty("客群ID")
    @TableField("CUSTOM_GROUP_ID")
    private String customGroupId;

    @ApiModelProperty("客群清单文件名称")
    @TableField("FILE_NAME")
    private String fileName;

    @ApiModelProperty(value = "客户数")
    @TableField("CUSTOM_NUM")
    private int customNum;

    @ApiModelProperty(value = "策划人ID")
    @TableField("CREATE_USER_ID")
    private String createUserId;

    @ApiModelProperty(value = "推送方式 1全量,0偏好")
    @TableField("PUSH_TYPE")
    private String pushType;


    @ApiModelProperty(value = "策略预演, 预演1,不预演0")
    @TableField("PREVIEW_CAMP")
    private String previewCamp;

}
