package com.asiainfo.biapp.pec.plan.jx.camp.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class McdExcellentRecommendCamp {
	private String statDate;//月
	private String campsegId;
	private String campsegName;
	private String cityId;
	@ApiModelProperty("营销用户数")
	private long campUserNum;//营销用户数
	@ApiModelProperty("营销成功用户数")
	private long campSuccNum;//营销成功用户数
	@ApiModelProperty("成功率")
	private double campSuccRate;//成功率
	@ApiModelProperty("转化率")
	private double pvConvertRate;//转化率
	@ApiModelProperty("得分")
	private double orderScore;//排序分值：=覆盖率*0.25+成功率*0.75。覆盖率=成功用户/所有订购的用户
	private List<CampChannel> channels;

}
