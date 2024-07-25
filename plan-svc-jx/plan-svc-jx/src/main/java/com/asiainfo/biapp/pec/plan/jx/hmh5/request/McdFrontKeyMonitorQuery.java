package com.asiainfo.biapp.pec.plan.jx.hmh5.request;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @date 2023-2-17
 * @description 客户通重点监控实体类
 */
@Data
@ApiModel("江西客户通重点指标监控查询入参")
public class McdFrontKeyMonitorQuery  extends McdPageQuery {

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

}
