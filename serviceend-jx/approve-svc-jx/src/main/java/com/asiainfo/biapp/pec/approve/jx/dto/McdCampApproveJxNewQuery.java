package com.asiainfo.biapp.pec.approve.jx.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : ranpf
 * @date : Created in 2023-1-9
 */

@Data
@ApiModel(value = "江西策略审批查询接口入参",description = "江西策略审批查询接口入参")
public class McdCampApproveJxNewQuery {


    @ApiModelProperty(value = "每页大小")
    private Integer size = 10;

    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @ApiModelProperty(value = "业务对象名称")
    private String campsegName;

    @ApiModelProperty(value = "渠道")
    private String channelId;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty(value = "当前登录人")
    private String userId;

    /**
     *  审批类型的枚举值，根据不同的枚举值查询不同的审批类型
     */
    // 2023-07-05 17:24:20
    @ApiModelProperty("审批类型的枚举值，根据不同的枚举值查询不同的审批类型")
    private String enumKey;

    /**
     *  地市id值，当查询类型为自定义标签时赋值
     */
    // 2023-07-05 17:24:20
    @ApiModelProperty("地市id值，当查询类型为自定义标签时赋值")
    private String cityId;

    /**
     *  枚举值，根据不同的枚举值查询不同的审批类型
     */
    // 2023-07-05 17:24:20
    @ApiModelProperty("预警类型的key值，当查询类型为自定义预警时赋值")
    private String dicKey;

    @ApiModelProperty("审批节点ID")
    private String nodeId;

    @ApiModelProperty("审批流程ID")
    private String processId;

    @ApiModelProperty("所属场景名称或id")
    private String sceneKey;


}
