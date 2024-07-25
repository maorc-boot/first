package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.controller.reqParam;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Past;
import java.util.Date;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.controller.reqParam
 * @className: SelectCustomerPreserveDetailParam
 * @author: chenlin
 * @description: 查询客户通维系详情的参数
 * @date: 2023/6/13 18:36
 * @version: 1.0
 */
@Data
@ApiModel("查询客户通维系详情的参数")
public class CustomerPreserveDetailParam {

    @ApiModelProperty(value = "查询当前页，默认1", example = "1")
    private Integer pageNum;

    @ApiModelProperty(value = "查询页大小，默认10", example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "查询客户接触维系日期，格式yyyy-MM-dd，默认null查全部", example = "2020-07-20")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "不可选择将来的日期！")
    private Date opDate;

    /**
     * 城市id（最多4个字符）
     */
    @ApiModelProperty(value = "要查询的城市id（最多4个字符），默认null查全部", example = "791")
    private String cityId;

    /**
     * 区县id（最多8个字符）
     */
    @ApiModelProperty(value = "要查询的区县id（最多8个字符），默认null查全部", example = "918")
    private String countyId;

    /**
     * 网格id（最多16个字符）
     */
    @ApiModelProperty(value = "要查询的网格id（最多16个字符），默认null查全部", example = "79112915")
    private String gridId;


    public Integer getPageNum() {
        return pageNum != null && pageNum > 0 ? pageNum : 1;
    }

    public Integer getPageSize() {
        return pageSize != null && pageSize > 0 ? pageSize : 10;
    }
}
