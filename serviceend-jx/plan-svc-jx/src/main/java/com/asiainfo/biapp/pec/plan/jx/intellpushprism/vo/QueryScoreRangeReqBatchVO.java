package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * description: 客群打分区间数据批量查询入参对象
 *
 * @author: lvchaochao
 * @date: 2024/6/3
 */
@NoArgsConstructor
@Data
@ApiModel("客群打分区间数据批量查询入参对象")
public class QueryScoreRangeReqBatchVO implements Serializable {

    @ApiModelProperty("客群打分区间数据批量查询入参")
    private List<QueryScoreRangeReqVO> data;

}
