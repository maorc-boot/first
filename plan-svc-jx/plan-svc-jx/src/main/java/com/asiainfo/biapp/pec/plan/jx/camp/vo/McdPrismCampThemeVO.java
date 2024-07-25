package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: liuyp
 * @Date: 2024/4/19
 */
@Data
@ApiModel(value = "智推棱镜策略主题对象", description = "智推棱镜策略主题对象")
public class McdPrismCampThemeVO {
	
	@ApiModelProperty("策划主题ID")
	private String id;

	@ApiModelProperty("策划主题编号")
	private String themeId;
	
	@ApiModelProperty("策划主题名称")
	private String themeName;
	
	@ApiModelProperty("场景分类 与枚举值activity_type下的一致")
	private int sceneClass;
	
	@ApiModelProperty("场景类型 0-主题营销类 1-AI辅助营销类 2-AI智能营销类")
	private int sceneType;
	
	@ApiModelProperty("适用场景说明")
	private String sceneDesc;
	
	@ApiModelProperty("创建人ID")
	private String createUser;
	
	@ApiModelProperty("创建人名称")
	private String createUserName;
	
	@ApiModelProperty("创建时间")
	private String createTime;
	
	@ApiModelProperty("更新时间")
	private String updateTime;
	
	@ApiModelProperty("主题状态 1-可用，0-不可用")
	private int status;
	
	@ApiModelProperty("主题下的活动是否可以批量提交")
	private boolean submittable;
	
	@ApiModelProperty("主题下的活动是否可以批量修改")
	private boolean modifiable;
	
	@ApiModelProperty("主题下的活动是否可以批量删除")
	private boolean deletable;
	
	@ApiModelProperty("渠道ID列表")
	private List<String> channelIds;
	
	@ApiModelProperty("活动列表")
	private List<SceneCampVO> campList;
	
}
