package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CampChannel {
	@ApiModelProperty("渠道编码")
	private String channelId;
	@ApiModelProperty("渠道名称")
	private String channelName;


}
