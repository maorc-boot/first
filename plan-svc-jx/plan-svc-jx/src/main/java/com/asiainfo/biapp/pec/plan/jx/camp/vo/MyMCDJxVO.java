package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author ranpf
 * @ClassName MyMCDVO
 * @date 2022/12/28 12:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel(value = "江西:首页我的营销策略返回对象", description = "")
public class MyMCDJxVO implements Serializable {

    @ApiModelProperty(value = "营销案ID")
    private String campsegId;

    @ApiModelProperty(value = "营销案名称")
    private String campsegName;

    @ApiModelProperty(value = "营销案状态")
    private String campsegStatId;

    @ApiModelProperty(value = "昨日成功数")
    private String yesterdaySuccess;

    @ApiModelProperty(value = "本月成功数")
    private String MonthlySuccess;
}
