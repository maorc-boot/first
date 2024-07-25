package com.asiainfo.biapp.pec.plan.jx.hmh5.model;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author ranpf
 *
 */
@Data
@ApiModel("江西客户通工号查询对象")
@TableName("REPORT_CUS_MANAGER")
public class McdFrontReportCusManagerModel {
	@ApiModelProperty( "STAFF_ID")
	private String staffId;
	@ApiModelProperty( "STAFF_NAME")
	private String staffName;
	@ApiModelProperty( "CITY_ID")
	private String cityId;
	@ApiModelProperty( "CITY_NAME")
	private String cityName;
	@ApiModelProperty( "COUNTY_ID")
	private String countyId;
	@ApiModelProperty( "COUNTY_NAME")
	private String countyName;
	@ApiModelProperty( "CHANNEL_ID")
	private String channelId;
	@ApiModelProperty( "CHANNEL_NAME")
	private String channelName;
	@ApiModelProperty( "CHANNEL_TYPE")
	private String channelType;
	@ApiModelProperty( "CALLING_NUM")
	private String callingNum;
	@ApiModelProperty( "ISCALLING_NUM")
	private String iscallingNum;


}
