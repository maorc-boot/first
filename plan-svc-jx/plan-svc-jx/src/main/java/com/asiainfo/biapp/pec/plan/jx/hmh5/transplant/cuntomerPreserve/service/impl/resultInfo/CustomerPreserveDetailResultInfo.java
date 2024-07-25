package com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.impl.resultInfo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @projectName: serviceend-jx
 * @package: com.asiainfo.biapp.pec.plan.jx.hmh5.transplant.cuntomerPreserve.service.impl.resultInfo
 * @className: SelectCustomerPreserveDetailResultInfo
 * @author: chenlin
 * @description: 查询客户通维系详情的返回体
 * @date: 2023/6/13 20:16
 * @version: 1.0
 */
@Data
@ApiModel("客户通接触维系情况查询结果模型")
public class CustomerPreserveDetailResultInfo {

    /**
     * 城市名（最多16个字符）
     */
    @ApiModelProperty(value = "城市名")
    private String cityName;

    /**
     * 区县名（最多16个字符）
     */
    @ApiModelProperty(value = "区县名")
    private String countyName;

    /**
     * 渠道id（最多16个字符）
     */
    @ApiModelProperty(value = "渠道id")
    private String channelId;

    /**
     * 渠道名称（最多64个字符）
     */
    @ApiModelProperty(value = "渠道名称")
    private String channelName;

    /**
     * 网格名称（最多64个字符）
     */
    @ApiModelProperty(value = "网格名称")
    private String gridName;

    /**
     * 看护客户数
     */
    @ApiModelProperty(value = "看护客户数")
    private Integer khUserNum;

    /**
     * 当日短信发送量
     */
    @ApiModelProperty(value = "当日短信发送量")
    private Integer dxNumD;
}
