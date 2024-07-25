package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * description: 得分区间接口响应实体对象
 *
 * @author: lvchaochao
 * @date: 2024/6/7
 */
@NoArgsConstructor
@Data
@ApiModel("得分区间接口响应实体对象")
public class AIScoreRangeRespVO implements Serializable {


    /**
     * range : 0.9-1
     * custNum : 0
     * productId : 11002367
     * productName : 通用计划-jx
     * channelId : null
     * channelName : null
     */
    @ApiModelProperty("范围")
    private String range;

    @ApiModelProperty("人数")
    private int custNum;

    @ApiModelProperty("产品id")
    private String productId;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("渠道id")
    private Object channelId;

    @ApiModelProperty("渠道名称")
    private Object channelName;

    // 用于从JSONObject转换对象的构造函数
    public AIScoreRangeRespVO(JSONObject jsonObject) {
        BeanUtil.copyProperties(jsonObject, this);
    }

}
