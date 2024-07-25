package com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 智推棱镜：场景表实体对象
 *
 * @author: lvchaochao
 * @date: 2024/4/16
 */
@TableName("MCD_PRISM_DIM_SCENE")
@ApiModel(value = "McdPrismDimScene对象",description = "智推棱镜场景表实体对象")
@Data
public class McdPrismDimScene {

    @ApiModelProperty("主键id")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "场景ID（DNA客户群ID或者有一对一关系）")
    @TableField("SCENE_ID")
    private String sceneId;

    @ApiModelProperty(value = "场景名称")
    @TableField("SCENE_NAME")
    private String sceneName;

    @ApiModelProperty(value = "场景层级")
    @TableField("SCENE_LEVEL")
    private Integer sceneLevel;

    @ApiModelProperty(value = "父场景ID")
    @TableField("SCENE_PID")
    private String scenePid;

    @ApiModelProperty(value = "场景(分裂的子客群)数量")
    @TableField("SCENE_COUNT")
    private Integer sceneCount;

    @ApiModelProperty(value = "场景裂变类型：0-标签 1-属性 2-客户群")
    @TableField("SCENE_FISSION_TYPE")
    private Integer sceneFissionType;

    @ApiModelProperty(value = "场景裂变类型对应具体的值")
    @TableField("SCENE_FISSION_VALUE")
    private String sceneFissionValue;

    @ApiModelProperty(value = "归属主题ID")
    @TableField("THEME_ID")
    private String themeId;

    // @ApiModelProperty(value = "归属模板ID")
    // @TableField("TEMPLATE_ID")
    // private String templateId;

    @ApiModelProperty(value = "场景条件")
    @TableField("SCENE_CONDITION")
    private String sceneCondition;

    @ApiModelProperty(value = "是否最后一级场景,1-是，0-否")
    @TableField("LEAF_SCENE_FLAG")
    private Integer leafSceneFlag;

    @ApiModelProperty(value = "创建人ID")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

    @ApiModelProperty(value = "状态 1-可用，0-不可用")
    @TableField("STATUS")
    private Integer status;

    @ApiModelProperty(value = "是否复制的场景, 1-是，0-否")
    @TableField("IS_COPY")
    private Integer isCopy;
}
