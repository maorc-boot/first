package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto.dna;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description: 标签裂变获取客群编号接口响应实体
 *
 * @author: lvchaochao
 * @date: 2024/4/16
 */
@Data
@NoArgsConstructor
@ApiModel("标签裂变获取客群编号接口响应实体")
public class LabelFissionGetCustIdRespDTO implements Serializable {


    /**
     * customerId : 210
     * customerName : 2727832裂变客群1
     * userCount : 82374
     */
    @ApiModelProperty("客群编号")
    private int customerId;

    @ApiModelProperty("客群名称")
    private String customerName;

    @ApiModelProperty("客群数量")
    private int userCount;


}
