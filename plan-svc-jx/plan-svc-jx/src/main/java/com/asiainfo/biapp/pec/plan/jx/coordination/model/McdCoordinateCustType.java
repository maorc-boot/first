package com.asiainfo.biapp.pec.plan.jx.coordination.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author :  shijl8
 * @date : 2023-7-25
 */
@Data
@ApiModel("策略统筹-客户类型")
@TableName("mcd_coordinate_cust_type")
public class McdCoordinateCustType {

    @ApiModelProperty(value = "客户类型ID")
    @TableId("CUST_TYPE_ID")
    private long custTypeId;

    @ApiModelProperty(value = "客户类型名称")
    @TableField("CUST_TYPE_NAME")
    private String custTypeName;

    @ApiModelProperty(value = "排序字段")
    @TableField("ORDER_BY")
    private long orderBy;

    @ApiModelProperty(value = "状态：1-可用 0-不可用")
    @TableField("STATUS")
    private long status;

}
