package com.asiainfo.biapp.pec.plan.jx.camp.req;

import com.asiainfo.biapp.pec.core.model.McdPageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: liuyp
 * @Date: 2024/4/19
 */
@Data
@ApiModel(value = "智推棱镜策略主题查询条件", description = "智推棱镜策略主题查询条件")
public class CampThemeQuery extends McdPageQuery {
	
	@ApiModelProperty(value = "用户ID", hidden = true)
	private String userId;
	
	@ApiModelProperty("主题创建时间")
	private String themeCreateTime;
	
	@ApiModelProperty("主题更新时间")
	private String themeUpdateTime;
	
	@ApiModelProperty("主题模糊查询关键字")
	private String themeKeywords;
	
	@ApiModelProperty("活动模糊查询关键字")
	private String campKeywords;
	
	@ApiModelProperty("场景分类, 0-套餐类, 1-流量包")
	private Integer sceneClass;
	
	@ApiModelProperty("场景类型, 0-主题营销类, 1-AI辅助营销类, 2-AI智能营销类")
	private Integer sceneType;
	
	@ApiModelProperty("场景裂变类型：0-标签, 1-属性, 2-客户群")
	private Integer sceneFissionType;
	
	@ApiModelProperty(value = "是否是我的主题/活动: 1-我的主题/活动, 0-全部主题/活动")
	private Integer isSelectMy = 1;
	
	@ApiModelProperty("主题ID")
	private String themeId;
	
	@ApiModelProperty(value = "活动开始时间")
	private String campStartDay;
	
	@ApiModelProperty(value = "活动结束时间")
	private String campEndDay;
	
	@ApiModelProperty(value = "活动状态")
	private Short campsegStatId;
	
	@ApiModelProperty(value = "渠道编码")
	private String channelId;
	
	@ApiModelProperty(value = "场景ID")
	private String sceneId;
	
	@ApiModelProperty(value = "场景名称")
	private String sceneName;
	
	@ApiModelProperty("排序字段,不传默认按照创建时间排序")
	private String orderColumn;
	
	@ApiModelProperty("排序规则, 1-降序; 其他数字-升序。不传默认是1")
	private Integer order = 1;
	
	@ApiModelProperty(value = "活动开始时间的开始", hidden = true)
	private String campStartDayStart;
	
	@ApiModelProperty(value = "活动开始时间的结束", hidden = true)
	private String campStartDayEnd;
	
	@ApiModelProperty(value = "活动结束时间的开始", hidden = true)
	private String campEndDayStart;
	
	@ApiModelProperty(value = "活动结束时间的结束", hidden = true)
	private String campEndDayEnd;
	
	@ApiModelProperty(value = "主题创建时间(开始)", hidden = true)
	private String themeCreateStartTime;
	
	@ApiModelProperty(value = "主题创建时间(结束)", hidden = true)
	private String themeCreateEndTime;
	
	@ApiModelProperty(value = "主题更新时间(开始)", hidden = true)
	private String themeUpdateStartTime;
	
	@ApiModelProperty(value = "主题更新时间(结束)", hidden = true)
	private String themeUpdateEndTime;

}
