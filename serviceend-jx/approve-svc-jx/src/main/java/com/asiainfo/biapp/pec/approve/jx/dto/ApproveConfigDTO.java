package com.asiainfo.biapp.pec.approve.jx.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author feify
 */
@Data
public class ApproveConfigDTO {

    /**
     * 每页大小
     */
    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;
    /**
     * 当前页码
     */
    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "部门ID")
    private String deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "地市ID")
    private String cityId;

    @ApiModelProperty(value = "地市名称")
    private String cityName;

    @ApiModelProperty(value = "区县ID")
    private String countyId;

    @ApiModelProperty(value = "区县名称")
    private String countyName;

    @ApiModelProperty(value = "网格ID")
    private String gridId;

    @ApiModelProperty(value = "网格名称")
    private String gridName;

    @ApiModelProperty(value = "系统ID")
    private String systemId;

    @ApiModelProperty(value = "流程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long processId;

    @ApiModelProperty(value = "关键字")
    private String keyWords;

    @ApiModelProperty(value = "流程名称")
    private String processName;

    @ApiModelProperty(value = "审批类型")
    private Integer processType;

}
