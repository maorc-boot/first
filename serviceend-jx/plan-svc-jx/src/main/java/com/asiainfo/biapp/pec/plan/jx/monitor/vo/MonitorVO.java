package com.asiainfo.biapp.pec.plan.jx.monitor.vo;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 主机监控实体
 *
 * @author lvcc
 * @date 2023/01/10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MCD_HOST_MONITOR_INFO")
@ApiModel(value="MonitorVO对象", description="主机监控实体")
public class MonitorVO extends Model<MonitorVO> {

    @ApiModelProperty(value = "主机IP")
    @TableField("HOST_IP")
    private String ip;

    @ApiModelProperty(value = "CPU使用率")
    @TableField("CPU_USAGE")
    @JsonFormat(shape = JsonFormat.Shape.STRING) // 转换String类型返回前端，确保精度不丢失
    private double cpu;

    @ApiModelProperty(value = "内存使用率")
    @TableField("RAM_USAGE")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private double ram;

    @ApiModelProperty(value = "磁盘使用率")
    @TableField("DISK_USAGE")
    private Integer disk;

    @ApiModelProperty(value = "数据采集时间")
    @TableField("DATA_TIME")
    private Date data;

}
