package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
 

/**
 * 一线渠道维表
 * @author ranpf
 *
 */
@Data
@ApiModel("江西客户通看护一线营销渠道对象")
@TableName("MCD_FRONT_LINE_CHANNEL")
public class McdFrontLineChannelModel {
	@ApiModelProperty( "CHANNEL_ID")
	private String channelId;
	@ApiModelProperty( "CHANNEL_NAME")
	private String channelName;
	@ApiModelProperty( "CITY_ID")
	private String cityId;
	@ApiModelProperty( "COUNTY_ID")
	private String countyId;
	@ApiModelProperty( "AREA_ID")
	private String areaId;
	@ApiModelProperty( "AREA_NAME")
	private String areaName;

	@ApiModelProperty( "CHANNEL_STATUS")
	private String channelStatus;

	// @ApiModelProperty( "CITY_NAME")
	// private String cityName;
	// @ApiModelProperty( "COUNTY_NAME")
	// private String countyName;


}
