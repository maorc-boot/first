package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * @projectName: customer
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.mcdAppAlarmInfo.controller.reqParam
 * @className: SelectAlarmParam
 * @author: chenlin
 * @description: 自定义预警页面查询请求模型
 * @date: 2023/6/28 15:25
 * @version: 1.0
 */
@Data
@ApiModel("自定义预警页面查询请求模型")
public class SelectAlarmParam {

    @ApiModelProperty(value = "查询当前页，默认1", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "查询页大小，默认10", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "模糊查询关键字，可以为预警ID或者预警名称，默认null查全部")
    @Pattern(regexp = "\\S+",message = "关键字不能含有空字符！")
    private String keywords;


    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }

}
