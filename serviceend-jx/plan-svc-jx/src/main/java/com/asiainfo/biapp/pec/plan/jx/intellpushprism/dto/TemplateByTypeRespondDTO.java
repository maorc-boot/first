package com.asiainfo.biapp.pec.plan.jx.intellpushprism.dto;

import com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo.TemplateInfoRespondVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description: 智推棱镜：查询各类型下的模板的响应对象
 *
 * @author: lvchaochao
 * @date: 2024/4/22
 */
@Data
@ApiModel("查询各类型下的模板的响应对象")
public class TemplateByTypeRespondDTO {

    @ApiModelProperty("场景类型")
    private Integer sceneType;

    @ApiModelProperty("展示的模板信息")
    private List<TemplateInfoRespondVO> templateInfoRespondVOList;
}
