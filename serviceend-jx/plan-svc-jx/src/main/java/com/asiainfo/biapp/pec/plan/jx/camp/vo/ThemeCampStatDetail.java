package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: liuyp
 * @Date: 2024/5/9
 */
@Data
@ApiModel("主题下活动详情信息")
public class ThemeCampStatDetail {
	
	@ApiModelProperty("根活动id")
	private String campsegRootId;
	
	@ApiModelProperty("活动状态")
	private int campsegStatId;
	
	@ApiModelProperty("是否是预演活动 0-不预演 1-预演")
	private int previewCamp;

	@ApiModelProperty("主题id")
	private String themeId;

	@ApiModelProperty("渠道id")
	private String channelId;
	
}
