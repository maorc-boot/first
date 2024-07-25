package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * description: 根据主题或者模版查询下面的活动入参对象
 *
 * @author: lvchaochao
 * @date: 2024/4/29
 */
@ApiModel("根据主题或者模版查询下面的活动入参对象")
@Data
@Accessors(chain = true)
public class QueryCampByThemeIdOrTepIdReqVO {

    @ApiModelProperty("主题id")
    private String themeId;

    @ApiModelProperty("模板id")
    private String templateId;
}
