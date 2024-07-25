package com.asiainfo.biapp.pec.plan.jx.camp.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author mamp
 * @since 2023-04-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="批量导入任务", description="批量导入任务")
public class McdCampImportTask implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "ID", type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 任务名称
     */
    @ApiModelProperty(value = "任务名称")
    @TableField("TASK_NAME")
    private String taskName;

    /**
     * 任务编号 
     */
    @ApiModelProperty(value = "任务编号")
    @TableField("TASK_NO")
    private String taskNo;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    @TableField("CREATE_USER")
    private String createUser;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 任务状态
     */
    @ApiModelProperty(value = "任务状态,1-执行中，2-执行完成，3-执行失败")
    @TableField("TASK_STATUS")
    private String taskStatus;

    /**
     * 任务进度
     */
    @ApiModelProperty(value = "任务进度")
    @TableField("TASK_PROGRESS")
    private String taskProgress;

    /**
     * 剩余时间
     */
    @ApiModelProperty(value = "剩余时间")
    @TableField("REMAIN_TIME")
    private String remainTime;

    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
    @TableField("FAIL_REASON")
    private String failReason;

    /**
     * 活动总数量
     */
    @ApiModelProperty(value = "活动总数量")
    @TableField("CAMP_TOTAL")
    private Integer campTotal;

    /**
     * 任务描述
     */
    @ApiModelProperty(value = "任务描述")
    @TableField("TASK_DESCRIPTION")
    private String taskDescription;


}
