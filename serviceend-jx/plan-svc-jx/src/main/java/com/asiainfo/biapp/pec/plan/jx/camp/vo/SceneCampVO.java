package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: liuyp
 * @Date: 2024/4/19
 */
@Data
@ApiModel(value = "智推棱镜策略列表返回对象", description = "智推棱镜策略列表返回对象")
public class SceneCampVO {
	
	@ApiModelProperty("创建时间")
	private Date createTime;
	
	@ApiModelProperty("策略名称")
	private String campsegName;
	
	@ApiModelProperty("策划id")
	private String campsegId;
	
	@ApiModelProperty("开始时间")
	private Date startDate;
	
	@ApiModelProperty("结束时间")
	private Date endDate;
	
	@ApiModelProperty("创建人")
	private String createUsername;
	
	@ApiModelProperty("策略状态")
	private Integer campsegStatId;
	
	@ApiModelProperty("策略状态名称")
	private String campsegStatName;
	
	@ApiModelProperty("归属地市")
	private String cityId;
	
	@ApiModelProperty("地市名称")
	private String cityName;
	
	@ApiModelProperty("预演策略 1是0否")
	private String previewCamp;
	
	@ApiModelProperty("渠道ID列表")
	private List<String> channelIds;
	
	@ApiModelProperty("场景ID")
	private String sceneId;
	
	@ApiModelProperty("场景名称")
	private String sceneName;

	@ApiModelProperty("主题id")
	private String themeId;
	
}
