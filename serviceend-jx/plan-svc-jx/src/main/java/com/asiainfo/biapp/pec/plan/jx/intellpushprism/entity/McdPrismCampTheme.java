package com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * description: 智推棱镜策略主题表实体对象
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@TableName("MCD_PRISM_CAMP_THEME")
@ApiModel(value = "McdPrismCampTheme对象",description = "智推棱镜策略主题表实体对象")
@Data
@Accessors(chain = true)
public class McdPrismCampTheme {

    @ApiModelProperty("主键id")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "策划主题编号")
    @TableField("THEME_ID")
    private String themeId;

    @ApiModelProperty(value = "策划主题名称")
    @TableField("THEME_NAME")
    private String themeName;

    @ApiModelProperty(value = "策划主题内容 存储所有配置信息回显使用")
    @TableField("THEME_CONTENT")
    private String themeContent;

    @ApiModelProperty(value = "场景分类 与枚举值activity_type下的一致")
    @TableField("SCENE_CLASS")
    private Integer sceneClass;

    @ApiModelProperty(value = "场景类型 0-主题营销类 1-AI辅助营销类 2-AI智能营销类")
    @TableField("SCENE_TYPE")
    private Integer sceneType;

    @ApiModelProperty(value = "适用场景说明")
    @TableField("SCENE_DESC")
    private String sceneDesc;

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
}
