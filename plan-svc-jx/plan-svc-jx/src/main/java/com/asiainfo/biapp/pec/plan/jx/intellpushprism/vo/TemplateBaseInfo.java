package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 智推棱镜：模板基本信息对象
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@Data
@ApiModel(value = "智推棱镜：模板基本信息对象", description = "智推棱镜：模板基本信息对象")
public class TemplateBaseInfo {

    @ApiModelProperty(value = "策划模板编号")
    private String templateId;

    @ApiModelProperty(value = "策划模板名称")
    private String templateName;

    @ApiModelProperty(value = "策划模板内容 存储所有配置信息回显使用")
    private String templateContent;

    @ApiModelProperty(value = "模板被引用次数")
    private Integer refCount;

    @ApiModelProperty(value = "场景分类 与枚举值activity_type下的一致")
    private Integer sceneClass;

    @ApiModelProperty(value = "场景类型 0-主题营销类 1-AI辅助营销类 2-AI智能营销类")
    private Integer sceneType;

    @ApiModelProperty(value = "适用场景说明")
    private String sceneDesc;

    @ApiModelProperty(value = "创建人ID")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "状态 1-可用，0-不可用")
    private Integer status;
}
