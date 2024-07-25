package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 智推棱镜：展示的模板信息
 *
 * @author: lvchaochao
 * @date: 2024/4/22
 */
@Data
@ApiModel("展示的模板信息")
public class TemplateInfoRespondVO {

    @ApiModelProperty(value = "策划主题名称")
    private String themeName;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "创建人ID")
    private String createUser;

    @ApiModelProperty(value = "适用场景说明")
    private String sceneDesc;

    @ApiModelProperty(value = "引用次数")
    private Integer refCount;
}
