package com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "江西:统一物料客群信息", description = "统一物料客群信息")
public class UGInfo implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "IOP客群 1 iop客群,2iop客群取反")
	private String  ugType;
	@ApiModelProperty(value = "客户端目标客群 根据运营位对应设置，取值为客群编号 ")
	private String  targetUserGroup;
	@ApiModelProperty(value = "客群筛选规则 0 或 1和")
	private String  ugRule;

}
