package com.asiainfo.biapp.pec.plan.jx.camp.vo.grouphalls;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel(value = "江西:集团统一物料实体", description = "集团统一物料实体")
public class GroupHallsJxMaterialVo implements Serializable{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value = "conversationId")
	private String conversationId;
	@ApiModelProperty(value = "返回编码")
	private String responseCode;
	@ApiModelProperty(value = "总条数")
	private String totalRecords;
	@ApiModelProperty(value = "物料列表")
	private List<MaterialJxDataVo> recordList;


	

}
