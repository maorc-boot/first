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
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author liwang
 * @since 2021-12-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("mcd_camp_task")
@ApiModel(value="McdCampTask对象", description="")
public class McdCampTask extends Model<McdCampTask> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务ID")
    @TableId("TASK_ID")
    private String taskId;

    @ApiModelProperty(value = "策略ID")
    @TableField("CAMPSEG_ID")
    private String campsegId;

    @ApiModelProperty(value = "渠道ID")
    @TableField("CHANNEL_ID")
    private String channelId;

    @ApiModelProperty(value = "运营位ID")
    @TableField("CHANNEL_ADIV_ID")
    private String channelAdivId;

    @ApiModelProperty(value = "执行状态")
    @TableField("EXEC_STATUS")
    private Integer execStatus;

    @ApiModelProperty(value = "控制状态 0: 运行 1: 暂停")
    @TableField("CTRL_STATUS")
    private BigDecimal ctrlStatus;

    @ApiModelProperty(value = "周期类型: 1: 次性 2: 月周期 3: 日周期")
    @TableField("CYCLE_TYPE")
    private BigDecimal cycleType;

    @ApiModelProperty(value = "任务开始时间")
    @TableField("TASK_START_TIME")
    private Date taskStartTime;

    @ApiModelProperty(value = "任务结束时间")
    @TableField("TASK_END_TIME")
    private Date taskEndTime;

    // @Transient
    @TableField(exist = false)
    private int intGroupNum;//客户群数目

    // @Transient
    @TableField(exist = false)
    private int botherAvoidNum;//免打扰控制数目

    // @Transient
    @TableField(exist = false)
    private int contactControlNum;//频次控制数目

    @Override
    protected Serializable pkVal() {
        return null;
    }

}
