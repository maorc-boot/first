package com.asiainfo.biapp.pec.plan.jx.intellpushprism.vo;

import com.asiainfo.biapp.pec.plan.vo.CustgroupDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * description: 智推棱镜：场景基本信息对象
 *
 * @author: lvchaochao
 * @date: 2024/4/15
 */
@Data
@ApiModel(value = "智推棱镜：场景基本信息对象", description = "智推棱镜：场景基本信息对象")
public class SceneBaseInfo {

    @ApiModelProperty(value = "场景ID（DNA客户群ID或者有一对一关系）")
    private String sceneId;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "场景层级")
    private Integer sceneLevel;

    @ApiModelProperty(value = "父场景ID")
    private String scenePid;

    @ApiModelProperty(value = "场景(分裂的子客群)数量")
    private Integer sceneCount;

    @ApiModelProperty(value = "场景裂变类型：0-标签 1-客户群 2-客群&标签")
    private Integer sceneFissionType;

    @ApiModelProperty(value = "裂变的客群对象信息", required = true, notes = "选择客群或者客群&标签裂变时，此对象不能为空")
    private CustgroupDetailVO customerInfo;

    @ApiModelProperty(value = "场景裂变类型对应具体的值")
    private String sceneFissionValue;

    @ApiModelProperty(value = "归属主题ID")
    private String themeId;

    @ApiModelProperty(value = "场景条件", notes = "场景条件TreeDetails对象json串")
    private String sceneCondition;

    @ApiModelProperty(value = "是否最后一级场景,1-是，0-否")
    private Integer leafSceneFlag;

    @ApiModelProperty(value = "创建人ID")
    private String createUser;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "状态 1-可用，0-不可用")
    private Integer status;
}
