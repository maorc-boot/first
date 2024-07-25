package com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "江西:统一物料通用文本信息", description = "统一物料通用文本信息")
public class GeneralInfo implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "通用文本名称")
	private String  generalTextName;
	@ApiModelProperty(value = "通用文本值")
	private String  generalTextValue;
	@ApiModelProperty(value = "通用图片地址URL")
	private String  generalImageSrc;
	@ApiModelProperty(value = "通用图片链接地址")
	private String  generalImageLink;
	@ApiModelProperty(value = "通用图片是否广告 0 是,1不是")
	private String  generalImageAdFlag;

}
