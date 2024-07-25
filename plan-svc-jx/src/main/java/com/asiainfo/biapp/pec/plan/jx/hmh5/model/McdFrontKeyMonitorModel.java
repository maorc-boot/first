package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * @author ranpf
 * @date 2023-2-17
 * @description 客户通重点监控实体类
 */
@Data
@TableName("mcd_front_key_monitor")
@ApiModel("江西客户通重点指标监控对象")
public class McdFrontKeyMonitorModel {
    // 监控编码
    @ApiModelProperty("监控编码")
    @TableField("MONITOR_CODE")
    private String monitorCode;

    // 监控指标名称
    @ApiModelProperty("监控指标名称")
    @TableField("MONITOR_NAME")
    private String monitorName;

    // 监控指标值
    @ApiModelProperty("监控指标值")
    @TableField("MONITOR_VALUE")
    private Double monitorValue;

    // 监控指标收集时间
    @ApiModelProperty("监控指标收集时间")
    @TableField("MONITOR_TIME")
    private Date monitorTime;

    // 拓展字段
    @ApiModelProperty("拓展字段")
    @TableField("EXT1")
    private String ext1;

    // 拓展字段
    @TableField("EXT2")
    private String ext2;

    // 拓展字段
    @TableField("EXT3")
    private String ext3;


}
