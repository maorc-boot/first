package com.asiainfo.biapp.pec.approve.jx.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author mamp
 * @date 2022/12/6
 */
@Data
public class ReadVo {
    @ApiModelProperty(value = "阅知待办ID(唯一)")
    private String id;
    @ApiModelProperty(value = "策略ID")
    private String campsegId;
    @ApiModelProperty(value = "阅知待办名称(策略名称相关)")
    private String campsegName;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "提交人ID")
    private String submiterId;
    @ApiModelProperty(value = "提交人姓名")
    private String submiterName;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "状态,0:待阅,1:已阅")
    private Integer status;
    @ApiModelProperty(value = "阅知处理时间")
    private Date dealTime;

}
