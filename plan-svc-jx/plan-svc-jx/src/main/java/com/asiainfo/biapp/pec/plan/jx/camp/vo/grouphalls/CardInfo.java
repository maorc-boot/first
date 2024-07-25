package com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "江西:统一物料卡卷信息", description = "统一物料卡卷信息")
public class CardInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "卡卷类型 1卡卷,2卡包 3无")
	private String  cardType;
	@ApiModelProperty(value = "卡卷批次编码")
	private String  cardC;

	@ApiModelProperty(value = "卡包编码")
	private String  cardP;
	@ApiModelProperty(value = "卡卷活动ID")
	private String  cardActId;

}
