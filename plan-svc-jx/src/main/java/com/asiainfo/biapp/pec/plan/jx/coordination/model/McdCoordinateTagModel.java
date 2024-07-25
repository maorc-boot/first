package com.asiainfo.biapp.pec.plan.jx.coordination.model;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * @author : shijl8
 * @date : 2023年7月31日11:14:30
 */
@ApiModel("标签列表返回实体")
@Data
public class McdCoordinateTagModel {

    @ApiModelProperty(value = "标签编码")
    private String tagId;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "客户类型ID")
    private long custTypeId;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "优先级")
    private long orderBy;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "状态： 1-可用，0-不可用")
    private long status;

    @ApiModelProperty(value = "客户类型")
    private String custTypeName;

    @ApiModelProperty("是否新增 true--为新增标签")
    private boolean isAdd;

}
