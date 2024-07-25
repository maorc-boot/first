package com.asiainfo.biapp.pec.element.jx.custLabel.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class LabelModel {
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

}
