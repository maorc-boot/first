package com.asiainfo.biapp.pec.element.jx.custLabel.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 客群标签配置model
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-16 上午 11:19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelLabel {
	@ApiModelProperty(value = "渠道编码")
	private String channelId;
	@ApiModelProperty(value = "渠道名称")
	private String channelName;
	@ApiModelProperty(value = "推送客群所选标签ids")
	private String custLabelIds;
	@ApiModelProperty(value = "推送客群所选标签名称")
	private String custLabelNames;
	@ApiModelProperty(value = "创建人")
	private String createUserId;
	@ApiModelProperty(value = "创建时间")
	private Timestamp createTime;
	@ApiModelProperty(value = "更新时间")
	private Timestamp updateTime;

}
