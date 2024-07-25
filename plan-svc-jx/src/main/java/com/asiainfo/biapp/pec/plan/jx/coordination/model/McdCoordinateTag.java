package com.asiainfo.biapp.pec.plan.jx.coordination.model;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author :  shijl8
 * @date : 2023-7-25
 */
@Data
@ApiModel("策略统筹标签配置表")
@TableName("mcd_coordinate_tag")
public class McdCoordinateTag {

    @ApiModelProperty(value = "标签编码")
    @TableId("TAG_ID")
    private String tagId;

    @ApiModelProperty(value = "标签名称")
    @TableField("TAG_NAME")
    private String tagName;

    @ApiModelProperty(value = "客户类型ID")
    @TableField("CUST_TYPE_ID")
    private long custTypeId;

    @ApiModelProperty(value = "优先级")
    @TableField("ORDER_BY")
    private long orderBy;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "状态： 1-可用，0-不可用")
    @TableField("STATUS")
    private long status;

    @ApiModelProperty(value = "操作人")
    @TableField("OPERATOR")
    private String operator;

}
