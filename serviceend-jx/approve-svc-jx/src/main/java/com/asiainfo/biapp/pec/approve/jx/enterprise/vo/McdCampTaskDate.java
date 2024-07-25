package com.asiainfo.biapp.pec.approve.jx.enterprise.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 任务执行计划表
 * </p>
 *
 * @author imcd
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_camp_task_date")
@ApiModel(value="McdCampTaskDate对象", description="任务执行计划表")
public class McdCampTaskDate extends Model<McdCampTaskDate> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    @TableId("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "清单数据日期")
    @TableField("DATA_DATE")
    private Integer dataDate;

    @ApiModelProperty(value = "任务最终派单客户群清单表")
    @TableField("TASK_SENDODD_TAB_NAME")
    private String taskSendoddTabName;

    @ApiModelProperty(value = "执行状态")
    @TableField("EXEC_STATUS")
    private Integer execStatus;

    @ApiModelProperty(value = "清单数据量")
    @TableField("CUST_LIST_COUNT")
    private Integer custListCount;

    @ApiModelProperty(value = "计划执行时间")
    @TableField("PLAN_EXEC_TIME")
    private Date planExecTime;

    @ApiModelProperty(value = "子活动ID")
    @TableField("CAMPSEG_ID")
    private String campsegId;


    @Override
    protected Serializable pkVal() {
        return this.taskId;
    }

}
