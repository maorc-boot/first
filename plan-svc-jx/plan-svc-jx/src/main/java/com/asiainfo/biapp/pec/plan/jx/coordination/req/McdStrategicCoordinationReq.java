package com.asiainfo.biapp.pec.plan.jx.coordination.req;

import com.asiainfo.biapp.pec.plan.vo.CampBaseInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 * @date 2023/6/9
 */

@Data
@ApiModel(value = "策略池策略查询入参", description = "策略池策略查询入参")
public class McdStrategicCoordinationReq  {


    @ApiModelProperty(value = "策略编码或名称")
    private String campsegIdOrName;

    @ApiModelProperty(value = "客群类型 0 全部, 1 一次性,2周期性")
    private int updateCycle;

    @ApiModelProperty(value = "开始时间 格式: yyyy-mm-dd")
    private String startDate;

    @ApiModelProperty(value = "结束时间 格式: yyyy-mm-dd")
    private String  endDate;

    @ApiModelProperty(value = "策划人")
    private String createUserName;

    @ApiModelProperty(value = "营销类型 1策略类")
    private int campTypeId =1;

    @ApiModelProperty(value = "产品类型 1 实物产品")
    private int planType = 1;

    @ApiModelProperty(value = "渠道ID 默认为85外呼802")
    private String channelId = "802";

    @ApiModelProperty(value = "每页大小，默认值10")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码，默认1")
    private Integer current = 1;


}
