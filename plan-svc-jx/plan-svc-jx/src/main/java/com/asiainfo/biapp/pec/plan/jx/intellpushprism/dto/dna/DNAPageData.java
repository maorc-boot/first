package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DNA接口返回的分页数据实体
 */
@Data
public class DNAPageData {
    @ApiModelProperty("总条数")
    private Integer total;
    @ApiModelProperty("总页数")
    private Integer totalPage;
}
