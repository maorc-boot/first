package com.asiainfo.biapp.pec.plan.jx.coordination.vo;

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
 * description: 策略统筹产品表实体
 *
 * @author: lvchaochao
 * @date: 2023/7/24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(value = "mcd_coordinate_plan")
@ApiModel(value = "策略统筹产品表实体对象",description = "策略统筹产品表实体")
public class McdCoordinatePlanVo {

    @ApiModelProperty(value = "产品编码")
    @TableId("PLAN_ID")
    private String planId;

    @ApiModelProperty(value = "产品名称")
    @TableField("PLAN_NAME")
    private String planName;

    @ApiModelProperty(value = "分类编码")
    @TableField("TYPE_ID")
    private String typeId;

    @ApiModelProperty(value = "优先级")
    @TableField("PRIORITY")
    private Integer priority;

    @ApiModelProperty(value = "状态 1-可用,0-不可用")
    @TableField("status")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "操作人")
    @TableField("operator")
    private String operator;

}
