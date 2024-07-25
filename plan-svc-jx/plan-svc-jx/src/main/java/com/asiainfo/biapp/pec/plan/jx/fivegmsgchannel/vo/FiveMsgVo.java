package com.asiainfo.biapp.pec.plan.jx.fivegmsgchannel.vo;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 5g消息关键词列表查询入参对象
 *
 * @author lvcc
 * @date 2023/02/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("5g消息关键词列表查询入参对象")
public class FiveMsgVo extends McdPageQuery {

    @ApiModelProperty("应用号名  根据应用号过滤查询时使用")
    private String applicationNum;

    @ApiModelProperty("关键词id 删除时使用")
    private String keywordsId;
}
