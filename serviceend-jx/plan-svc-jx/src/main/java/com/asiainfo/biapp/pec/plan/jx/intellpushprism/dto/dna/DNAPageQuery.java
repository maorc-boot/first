package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@ApiModel("DNA分页查询公共入参模型")
@Data
public class DNAPageQuery {
    /**
     * 当前页 默认第1页
     */
    private Integer pageIndex;
    /**
     * 每页显示记录数 默认10条
     */
    private Integer pageSize;
}
