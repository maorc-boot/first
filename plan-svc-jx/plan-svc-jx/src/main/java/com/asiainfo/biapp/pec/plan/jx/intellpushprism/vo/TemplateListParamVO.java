package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description: 智推棱镜：模板列表查询入参对象
 *
 * @author: lvchaochao
 * @date: 2024/4/25
 */
@ApiModel("模板列表查询入参对象")
@Data
public class TemplateListParamVO extends McdPageQuery {

    @ApiModelProperty("场景分类 与枚举值activity_type下的一致")
    private Integer sceneClass;

    @ApiModelProperty(value = "是否是我的模板: 1-我的模板, 0-全部模板")
    private Integer isSelectMy = 1;

}
