package com.asiainfo.biapp.pec.element.jx.custLabel.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签信息
 *
 * @author admin
 * @version 1.0
 * @date 2023-03-16 下午 16:41:24
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LabelInfo {
	@ApiModelProperty(value = "标签Id")
	private String labelId;
	@ApiModelProperty(value = "标签名称")
	private String labelName;

}
