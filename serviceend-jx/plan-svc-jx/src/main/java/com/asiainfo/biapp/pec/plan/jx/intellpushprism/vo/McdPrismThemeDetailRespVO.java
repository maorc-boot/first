package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * description: 主题详情响应实体对象
 *
 * @author: lvchaochao
 * @date: 2024/4/29
 */
@Data
@ApiModel("主题详情响应实体对象")
public class McdPrismThemeDetailRespVO {

    @ApiModelProperty(value = "策划主题内容 存储所有配置信息回显使用")
    private String themeOrTemplateContent;

    @ApiModelProperty(value = "主题或模板下活动信息")
    private List<Map<String, Object>> allCampByThemeIdOrTemplateId;

    @ApiModelProperty(value = "是否复制的场景, 1-是，0-否")
    private Integer isCopy;
}
