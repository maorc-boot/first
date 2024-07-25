package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import com.asiainfo.biapp.pec.plan.jx.camp.req.TacticsInfoJx;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description: 智推棱镜: 场景蓝图信息保存对象
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@Data
@ApiModel(value = "智推棱镜：场景蓝图信息保存对象", description = "智推棱镜：场景蓝图信息保存对象")
public class IntellPushPrismReqVO {

    @NotNull(message = "主题基本信息不能为空")
    @ApiModelProperty(value = "主题基本信息", required = true)
    private ThemeBaseInfo themeBaseInfo;

    @NotNull(message = "场景基本信息不能为空")
    @ApiModelProperty(value = "场景基本信息", required = true, notes = "二维数组结构，一条完整线的场景数据按照顺序传递为其中一个数组对象，非完整线的数据不保存")
    private List<List<SceneBaseInfo>> sceneBaseInfo;

    @ApiModelProperty(value = "活动信息", required = true)
    private List<TacticsInfoJx> campInfo;

    @NotNull(message = "是否创建模板信息不能为空")
    @ApiModelProperty(value = "是否创建模板 0-不是 1-是", required = true)
    private Integer isCreateTemplate;

    @NotNull(message = "是否一键引用不能为空")
    @ApiModelProperty(value = "是否一键引用 0-不是 1-是", required = true)
    private Integer isOneClickRef;

    @ApiModelProperty(value = "模板id 一键引用时使用", required = true)
    private String templateId;

    @ApiModelProperty(value = "是否AI流程 0-不是 1-是", required = true)
    private Integer isAI;
}
