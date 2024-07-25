package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.warnReport.controller.reqParam
 * @className: SelectWarnDetailParam
 * @author: chenlin
 * @description: 查询预警详情参数
 * @date: 2023/6/20 14:55
 * @version: 1.0
 */
@Data
@ApiModel("查询预警详情的请求参数")
public class SelectWarnDetailParam {

    @ApiModelProperty(value = "查询当前页，默认1", example = "1")
    private Integer pageNum;
    @ApiModelProperty(value = "查询页大小，默认10", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "地市编号，默认null")
    private String cityId;

    @ApiModelProperty(value = "区县编号，默认null")
    private String countyId;

    @ApiModelProperty(value = "员工编号，默认null")
    private String staffId;

    @ApiModelProperty(value = "预警类型，默认null")
    private String warnType;

    @ApiModelProperty(value = "导出日期，默认null，没有选择日期则导出所有")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "不可选择将来的日期！")
    private Date statDate;

    @ApiModelProperty(value = "格式化的时间字符传 后端使用", hidden = true)
    private String statDateStr;

    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }

}
