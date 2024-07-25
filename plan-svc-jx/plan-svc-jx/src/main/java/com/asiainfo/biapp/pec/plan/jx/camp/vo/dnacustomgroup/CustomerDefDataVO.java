package com.asiainfo.biapp.pec.plan.jx.camp.vo.dnacustomgroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * DNA方返回客群信息data数据实体
 *
 * @author lvcc
 * @date 2023/12/12
 */
@Data
@ApiModel(value = "DNA方返回客群信息data数据实体", description = "DNA方返回客群信息data数据实体")
public class CustomerDefDataVO {

    @ApiModelProperty(value = "客户群列表")
    private List<CustomerDefVO> customerList;

    @ApiModelProperty(value = "总客群数")
    private int total;

    @ApiModelProperty(value = "页数")
    private int totalPages;
}
