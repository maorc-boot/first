package com.asiainfo.biapp.pec.plan.jx.dna.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * description: dna待更新客群任务对象
 *
 * @author: lvchaochao
 * @date: 2023/12/20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("DNA_CUSTGROUP_UPDATE_TASK")
@ApiModel(value="dna待更新客群任务对象", description="dna待更新客群任务对象")
public class DNACustgroupUpdateTask {

    @ApiModelProperty(value = "任务ID")
    @TableId("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "子活动ID")
    @TableField("CAMPSEG_ID")
    private String campsegId;

    @ApiModelProperty(value = "客户群ID")
    @TableField("CUSTOM_GROUP_ID")
    private String customGroupId;

    @ApiModelProperty(value = "清单数据日期")
    @TableField("DATA_DATE")
    private Integer dataDate;

    @ApiModelProperty(value = "执行状态 待执行：50, 中间状态：505, 执行中：51, 异常：53, 完成：54")
    @TableField("EXEC_STATUS")
    private Integer execStatus;

    @ApiModelProperty(value = "客户群周期 1：一次性，2：月周期，3：日周期")
    @TableField("UPDATE_CYCLE")
    private Integer updateCycle;

    @ApiModelProperty(value = "客户群数量")
    @TableField("CUSTOM_NUM")
    private Integer customNum;

    @ApiModelProperty(value = "DNA接口获取的客群清单文件名")
    @TableField("DNA_FILE_NAME")
    private String dnaFileName;

    @ApiModelProperty(value = "替换后客群清单文件名")
    @TableField("REPLACED_FILE_NAME")
    private String replacedFileName;

    @ApiModelProperty(value = "异常信息")
    @TableField("EXCEPTION_MSG")
    private String exceptionMsg;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
