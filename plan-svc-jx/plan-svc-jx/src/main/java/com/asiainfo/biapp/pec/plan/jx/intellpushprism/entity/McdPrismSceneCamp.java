package com.asiainfo.biapp.pec.plan.jx.intellpushprism.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description:智推棱镜：场景与策略关系实体对象
 *
 * @author: lvchaochao
 * @date: 2024/4/17
 */
@TableName("MCD_PRISM_SCENE_CAMP")
@ApiModel(value = "McdPrismSceneCamp对象",description = "智推棱镜场景与策略关系实体对象")
@Data
public class McdPrismSceneCamp {

    @ApiModelProperty(value = "场景ID")
    @TableField("SCENE_ID")
    private String sceneId;

    @ApiModelProperty(value = "活动rootId")
    @TableField("CAMPSEG_ROOT_ID")
    private String campsegRootId;
}
