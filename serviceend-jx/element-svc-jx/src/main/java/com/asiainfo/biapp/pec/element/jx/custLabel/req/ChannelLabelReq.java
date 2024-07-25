package com.asiainfo.biapp.pec.element.jx.custLabel.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客群标签配置页面搜索
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-16 上午 11:19:20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChannelLabelReq {
	@ApiModelProperty(value = "活动搜索关键字")
	private String keyWords;
	@ApiModelProperty(value = "每页大小，默认值10")
	private Integer size = 10;
	@ApiModelProperty(value = "当前页码，默认1")
	private Integer current = 1;

}
