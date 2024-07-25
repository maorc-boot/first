package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import com.asiainfo.biapp.pec.plan.model.McdCustgroupDef;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 批量导入客群响应实体对象
 *
 * @author: lvchaochao
 * @date: 2024/6/4
 */
@Data
@ApiModel("批量导入客群响应实体对象")
public class BatchImportCustRespVO extends McdCustgroupDef {

    @ApiModelProperty("dna的表达式")
    private String finalExpression;
}
